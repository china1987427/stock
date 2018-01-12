package com.china.stock.common.database.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionImpl;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.china.stock.common.util.StringUtil;


public final class JDBCAgent {

	public JDBCAgent(Connection conn) {
		raw = false;
		batchCount = 0;
		batchExcuted = 0;
		this.conn = conn;
		raw = false;
		init();
	}

	public JDBCAgent() {
		this(false);
	}

	public JDBCAgent(boolean raw) {
		this.raw = false;
		batchCount = 0;
		batchExcuted = 0;
		this.raw = raw;
		if (raw)
			try {
				conn = getRawConnection();
			} catch (SQLException e) {
				log.error(e);
			}
		else
			conn = getConnection();
		init();
	}

	public JDBCAgent(String dsName) {
		raw = false;
		batchCount = 0;
		batchExcuted = 0;
		raw = true;
		try {
			conn = getRawConnection(dsName);
		} catch (SQLException e) {
			log.error(e);
		}
		init();
	}

	protected void init() {
		// stSet = new HashSet();
		// List dbaList = (List)
		// AppContext.getThreadContext("USER_CONTEXT_DBAGENTS_KEY");
		// if (dbaList == null) {
		// dbaList = new ArrayList();
		// AppContext.putThreadContext("USER_CONTEXT_DBAGENTS_KEY", dbaList);
		// }
		// dbaList.add(this);
	}

	public int execute(String sqlString) throws Exception {
		return execute(sqlString, ((List) (new ArrayList())));
	}

	public int execute(String sqlString, Object param) throws Exception {
		List params = new ArrayList();
		params.add(param);
		return execute(sqlString, params);
	}

	public int execute(String sqlString, List params) throws Exception {
		if (sqlString == null)
			throw new Exception("There is not a executable sql.");
		String sqlOpt = sqlString.trim().substring(0, 6).toLowerCase();
		if (log.isDebugEnabled()) {
			String tmpStr = sqlString;
			for (int i = 0; i < params.size(); i++) {
				Object param = params.get(i);
				String newString = param != null ? (new StringBuilder("'")).append(param.toString()).append("'").toString() : "null";
				tmpStr = StringUtil.replace(tmpStr, "?", newString, 1);
			}

			log.debug(tmpStr);
		}
		int status;
		if (sqlOpt.startsWith("select"))
			status = query(sqlString, params);
		else if (sqlOpt.startsWith("update") || sqlOpt.startsWith("delete") || sqlOpt.startsWith("insert"))
			status = update(sqlString, params);
		else if (executeOther(sqlString, params))
			status = 1;
		else
			status = 0;
		return status;
	}

	private void setPreparedStatementObject(PreparedStatement pst, int index, Object obj) throws SQLException {
		Object paramObj = obj;
		if (obj != null && (obj instanceof Date) && !(obj instanceof java.sql.Date))
			paramObj = new Timestamp(((Date) obj).getTime());
		pst.setObject(index, paramObj);
	}

	private int query(String sql, List params) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(sql, 1004, 1007);
		Iterator inputParamsIte = params.iterator();
		for (int index = 1; inputParamsIte.hasNext(); index++)
			setPreparedStatementObject(pst, index, inputParamsIte.next());

		query_result = pst.executeQuery();
		int rsRowCount = 0;
		query_result.last();
		rsRowCount = query_result.getRow();
		query_result.beforeFirst();
		return rsRowCount;
	}

	private int update(String sql, List params) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(sql);
		Iterator inputParamsIte = params.iterator();
		for (int index = 1; inputParamsIte.hasNext(); index++)
			setPreparedStatementObject(pst, index, inputParamsIte.next());

		int ret = pst.executeUpdate();
		pst.close();
		return ret;
	}

	private boolean executeOther(String SQLString, List params) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(SQLString);
		Iterator inputParamsIte = params.iterator();
		for (int index = 1; inputParamsIte.hasNext(); index++)
			setPreparedStatementObject(pst, index, inputParamsIte.next());

		boolean ret = pst.execute();
		pst.close();
		return ret;
	}

	public ResultSet getQueryResult() {
		return query_result;
	}

	public int executeBatch(List batchedSql) throws Exception {
		Statement stm;
		stm = null;
		if (batchedSql == null || batchedSql.size() == 0)
			throw new Exception("No batched sql found.");
		int j;
		stm = conn.createStatement();
		Iterator batchedSqlIte = batchedSql.iterator();
		int i = 0;
		int f = 0;
		while (batchedSqlIte.hasNext()) {
			stm.addBatch((String) batchedSqlIte.next());
			if (++i == 1000) {
				f = stm.executeBatch().length;
				i = 0;
			}
		}
		j = stm.executeBatch().length + f;
		stm.close();
		return j;
	}

	public void batch1Prepare(String sql) throws Exception, SQLException {
		if (sql == null || sql.length() == 0)
			throw new Exception("No batched sql.");
		batchCount = 0;
		batchExcuted = 0;
		if (pstBatch != null) {
			pstBatch.close();
			pstBatch = null;
		}
		pstBatch = conn.prepareStatement(sql);
	}

	public void batch2Add(List params) throws SQLException {
		batchCount++;
		if (batchCount == 1000) {
			batch3Execute();
			batchCount = 0;
		}
		for (int i = 0; i < params.size(); i++)
			setPreparedStatementObject(pstBatch, i + 1, params.get(i));

		pstBatch.addBatch();
	}

	public int batch3Execute() throws SQLException {
		batchExcuted += pstBatch.executeBatch().length;
		return batchExcuted;
	}

	public FlipInfo findByPaging(String sql, FlipInfo fpi) throws Exception, SQLException {
		return findByPaging(sql, ((List) (new ArrayList())), fpi);
	}

	public FlipInfo findByPaging(String sql, List params, FlipInfo fpi) throws Exception, SQLException {
		int idx = sql.toLowerCase().indexOf(" from ");
		if (idx == -1)
			throw new Exception("Illegal sql that not contain from keyword.");
		String rsql = sql;
		if (fpi.isNeedTotal()) {
			String lowerCase = rsql.toLowerCase();
			String countSql;
			if (lowerCase.indexOf("group by") != -1 || lowerCase.indexOf("distinct") != -1) {
				int orderIdx = lowerCase.indexOf("order by");
				String tempSql = rsql;
				if (orderIdx != -1)
					tempSql = tempSql.substring(0, orderIdx);
				countSql = (new StringBuilder("select count(1) from (")).append(tempSql).append(") a").toString();
			} else {
				countSql = (new StringBuilder("select count(*) ")).append(rsql.substring(idx)).toString();
				int orderIdx = countSql.toLowerCase().indexOf("order by");
				if (orderIdx != -1)
					countSql = countSql.substring(0, orderIdx);
			}
			execute(countSql, params);
			if (query_result.next())
				fpi.setTotal(query_result.getInt(1));
			else
				throw new Exception("Failed to query total record count.");
		}
		if (fpi.isNeedTotal() && fpi.getTotal() == 0)
			fpi.setPage((new Integer(0)).intValue());
		else if (fpi.getPage() < 1)
			fpi.setPage((new Integer(1)).intValue());
		else if (fpi.getPage() > fpi.getPages().intValue())
			fpi.setPage(fpi.getPages().intValue());
		if (fpi.getTotal() > 0 || !fpi.isNeedTotal()) {
			if (fpi.getSize() == -1) {
				fpi.setPage((new Integer(1)).intValue());
				fpi.setSize(fpi.getTotal());
			}
			int startRow = fpi.getStartAt().intValue();
			fpi.getSize();
			String sortField = fpi.getSortField();
			String sortOrder = fpi.getSortOrder();
			if (sortField != null && !"".equals(sortField)) {
				StringBuilder sqlb = new StringBuilder(rsql);
				if (rsql.indexOf("order by") == -1)
					sqlb.append(" order by ");
				else
					sqlb.append(",");
				sqlb.append(sortField);
				if (sortOrder != null)
					sqlb.append(" ").append(sortOrder);
				rsql = sqlb.toString();
			}
			Dialect dialect = getDialect();
			boolean useLimit = dialect.supportsLimit();
			boolean useOffset = useLimit && dialect.supportsLimitOffset();
			String pagingSql = null;
			if (useLimit)
				pagingSql = dialect.getLimitString(sql, useOffset ? startRow : 0, getMaxOrLimit(fpi, dialect));
			else
				throw new Exception((new StringBuilder("Limit not supported by current database type:")).append(dialect.getClass()).toString());
			List pagingParams = new ArrayList(10);
			int col = 0;
			if (useLimit && dialect.bindLimitParametersFirst())
				col += bindLimitParameters(dialect, pagingParams, col, fpi);
			col += params.size();
			pagingParams.addAll(params);
			if (useLimit && !dialect.bindLimitParametersFirst())
				col += bindLimitParameters(dialect, pagingParams, col, fpi);
			execute(pagingSql, pagingParams);
			fpi.setData(resultSetToList(fpi, !useOffset && dialect.useMaxForLimit()));
		}
		return fpi;
	}

	private int bindLimitParameters(Dialect dialect, List pagingParams, int index, FlipInfo fi) throws SQLException {
		if (!dialect.supportsVariableLimit())
			return 0;
		int firstRow = dialect.convertToFirstRowValue(fi.getStartAt().intValue());
		int lastRow = getMaxOrLimit(fi, dialect);
		boolean hasFirstRow = dialect.supportsLimitOffset() && (firstRow > 0 || dialect.forceLimitUsage());
		boolean reverse = dialect.bindLimitParametersInReverseOrder();
		if (hasFirstRow && !reverse)
			pagingParams.add(index, Integer.valueOf(firstRow));
		pagingParams.add(index + (!reverse && hasFirstRow ? 1 : 0), Integer.valueOf(lastRow));
		if (hasFirstRow && reverse)
			pagingParams.add(index + 1, Integer.valueOf(firstRow));
		return hasFirstRow ? 2 : 1;
	}

	private static int getMaxOrLimit(FlipInfo fi, Dialect dialect) {
		int firstRow = dialect.convertToFirstRowValue(fi.getStartAt().intValue());
		int lastRow = fi.getSize();
		if (dialect.useMaxForLimit())
			return lastRow + firstRow;
		else
			return lastRow;
	}

	public List resultSetToList() throws Exception {
		return resultSetToList(true);
	}

	public List resultSetToList(boolean lowercaseKey) throws Exception {
		if (query_result == null)
			throw new Exception("未进行过任何查询操作！");
		else
			return resultSetToList(query_result, lowercaseKey);
	}

	private List resultSetToList(FlipInfo fi, boolean useMaxForLimit) throws Exception {
		ResultSet rs = query_result;
		if (rs == null)
			throw new Exception("未进行过任何查询操作！");
		if (useMaxForLimit) {
			rs.setFetchSize(fi.getSize());
			if (fi.getStartAt().intValue() != 0) {
				rs.absolute(fi.getStartAt().intValue());
			} else {
				rs.first();
				rs.previous();
			}
		}
		return resultSetToList(rs, true);
	}

	private List resultSetToList(ResultSet rs, boolean lowercaseKey) throws Exception {
		if (rs == null)
			throw new Exception("未进行过任何查询操作！");
		ResultSetMetaData rsmd = rs.getMetaData();
		List dataList = new ArrayList();
		int columns = rsmd.getColumnCount();
		Map map;
		for (; rs.next(); dataList.add(map)) {
			map = new LinkedHashMap();
			for (int j = 1; j <= columns; j++) {
				String columnName = lowercaseKey ? rsmd.getColumnLabel(j).toLowerCase() : rsmd.getColumnLabel(j);
				Object value;
				if (rsmd.getColumnType(j) == 93)
					value = rs.getTimestamp(columnName);
				else if (rsmd.getColumnType(j) == 2005)
					value = extractClobString(rs, columnName);
				else
					value = rs.getObject(columnName);
				map.put(columnName, value);
			}

		}

		rs.getStatement().close();
		return dataList;
	}

	public Map resultSetToMap() throws Exception, SQLException {
		if (query_result == null)
			throw new Exception("\u672A\u8FDB\u884C\u8FC7\u4EFB\u4F55\u67E5\u8BE2\u64CD\u4F5C\uFF01");
		else
			return resultSetToMap(query_result);
	}

	public Map resultSetToMap(ResultSet rs) throws Exception, SQLException {
		if (rs == null)
			throw new Exception("未进行过任何查询操作！");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		if (rs.isAfterLast())
			throw new Exception("end of resultset!");
		Map map = new LinkedMap();
		if (rs.isBeforeFirst()) {
			rs.next();
			for (int j = 1; j <= columns; j++) {
				String columnName = rsmd.getColumnLabel(j).toLowerCase();
				Object value;
				if (rsmd.getColumnType(j) == 93)
					value = rs.getTimestamp(columnName);
				else if (rsmd.getColumnType(j) == 2005)
					value = extractClobString(rs, columnName);
				else
					value = rs.getObject(columnName);
				map.put(columnName, value);
			}

		}
		return map;
	}

	public void close() {
		if (stSet.size() > 0)
			try {
				for (Iterator ite = stSet.iterator(); ite.hasNext(); ((Statement) ite.next()).close())
					;
				stSet.clear();
			} catch (Exception _ex) {
			}
		if (pstBatch != null)
			try {
				pstBatch.close();
				pstBatch = null;
				batchCount = 0;
				batchExcuted = 0;
			} catch (SQLException _ex) {
			}
		if (raw && conn != null)
			try {
				conn.close();
				conn = null;
			} catch (SQLException _ex) {
			}
	}

	public String getDBUserName() throws SQLException {
		return conn.getMetaData().getUserName();
	}

	public Dialect getDialect() {
		return ((SessionImpl) currentSession()).getFactory().getDialect();
	}

	public static Object executeProcedure(String callString, CallableStatementCallback action) {
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(getDataSource());
		log.debug(callString);
		return jt.execute(callString, action);
	}

	public static Object executeProcedure(String dsName, String callString, CallableStatementCallback action) {
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(getDataSource(dsName));
		log.debug(callString);
		return jt.execute(callString, action);
	}

	public static Object executePreparedStatement(String preparedString, PreparedStatementCallback action) {
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(getDataSource());
		log.debug(preparedString);
		return jt.execute(preparedString, action);
	}

	public static Object executePreparedStatement(String dsName, String preparedString, PreparedStatementCallback action) {
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(getDataSource(dsName));
		log.debug(preparedString);
		return jt.execute(preparedString, action);
	}

	public static Connection getConnection() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		StockHibernateDaoSupport daoSupport = (StockHibernateDaoSupport) wac.getBean("hibernateDaoSupport");
		return daoSupport.currentConnection();
	}

	public static Connection getRawConnection() throws SQLException {
		return getRawConnection("dataSource");
	}

	public static Connection getRawConnection(String dsName) throws SQLException {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		DataSource ds = (DataSource) wac.getBean("dataSource");
		return ds.getConnection();
	}

	public static DataSource getDataSource() {
		return getDataSource("dataSource");
	}

	public static DataSource getDataSource(String dsName) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return (DataSource) wac.getBean("dataSource");
	}

	private static Session currentSession() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		StockHibernateDaoSupport daoSupport = (StockHibernateDaoSupport) wac.getBean("hibernateDaoSupport");
		return daoSupport.currentSession();
	}

	public static String getDBType() {
		String dbName = "stock";
		if (dbName != null)
			dbName = dbName.toLowerCase();
		return dbName;
	}

	public void executeNamedSql(String namedSql, Map params) throws Exception, IOException, SQLException {
		String exeSql = namedSql;
		Map indexedParams = new TreeMap(new Comparator() {

			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}

			public int compare(Object obj, Object obj1) {
				return compare((Integer) obj, (Integer) obj1);
			}

			final JDBCAgent this$0;

			{
				this$0 = JDBCAgent.this;
			}
		});
		StringBuilder sb = new StringBuilder(exeSql);
		for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			int fromIndex = 0;
			do
				fromIndex = replaceNamedSql(namedSql, fromIndex, indexedParams, params, key, sb);
			while (fromIndex != -1);
		}

		List exeParam = new ArrayList();
		Collection paramColl = indexedParams.values();
		for (Iterator iterator1 = paramColl.iterator(); iterator1.hasNext();) {
			Object o = iterator1.next();
			if (o instanceof Collection)
				exeParam.addAll((Collection) o);
			else
				exeParam.add(o);
		}

		execute(sb.toString(), exeParam);
	}

	private int replaceNamedSql(String sql, int fromIndex, Map indexedParams, Map params, String key, StringBuilder exeSql) {
		String paramKey = (new StringBuilder(":")).append(key).toString();
		int idx = sql.indexOf(paramKey, fromIndex);
		if (idx != -1) {
			Object val = params.get(key);
			StringBuilder replaceStr = new StringBuilder("?");
			if (val instanceof Collection) {
				Collection valColl = (Collection) val;
				for (int i = 1; i < valColl.size(); i++)
					replaceStr.append(",?");

			}
			indexedParams.put(Integer.valueOf(idx), val);
			int exeIdx = exeSql.indexOf(paramKey);
			exeSql.replace(exeIdx, exeIdx + paramKey.length(), replaceStr.toString());
			return idx + paramKey.length();
		} else {
			return -1;
		}
	}

	private static String extractClobString(ResultSet rs, String columnName) throws SQLException {
		return clobToString(rs.getClob(columnName));
	}

	public static String clobToString(Clob clob) throws SQLException {
		Reader is;
		BufferedReader br;
		if (clob == null)
			return null;
		is = clob.getCharacterStream();
		br = new BufferedReader(is);
		String s1;
		try {
			String s = br.readLine();
			StringBuilder sb = new StringBuilder();
			while (s != null) {
				sb.append(s);
				s = br.readLine();
				if (s != null)
					sb.append("\n");
			}
			s1 = sb.toString();
		} catch (IOException e) {
			throw new SQLException(e.getMessage(), e);
		}
		try {
			br.close();
			is.close();
		} catch (Exception _ex) {
		}
		return s1;
	}

	private static Logger log = Logger.getLogger("org.hibernate.SQL.DBA");
	private Connection conn;
	private ResultSet query_result;
	private HashSet stSet;
	private boolean raw;
	private int batchCount;
	private int batchExcuted;
	private PreparedStatement pstBatch;

}
