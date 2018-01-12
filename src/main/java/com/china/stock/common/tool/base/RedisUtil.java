package com.china.stock.common.tool.base;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.china.stock.common.tool.conf.Conf;

public class RedisUtil {

	protected static Logger log = Logger.getLogger(RedisUtil.class);

	private static JedisPool pool = null;

	private static ShardedJedisPool pools = null;

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return ShardedJedisPool
	 */
	public static ShardedJedisPool getPools() {
		if (pools == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			// config.setMaxActive(500);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			// config.setMaxWait(1000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			String ip="127.0.0.1";
			JedisShardInfo jedisShardInfo1 = new JedisShardInfo(ip, "6379");
			JedisShardInfo jedisShardInfo2 = new JedisShardInfo(ip, "6379");
			List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
			jedisShardInfo1.setTimeout(2000);
			jedisShardInfo2.setTimeout(2000);
			list.add(jedisShardInfo1);
			list.add(jedisShardInfo2);

			pools = new ShardedJedisPool(config, list);
		}
		return pools;
	}

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static JedisPool getPool() throws NumberFormatException, IOException {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			// config.setMaxActive(500);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			// config.setMaxWait(1000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			pool = new JedisPool(config, Conf.getValue("redisIp"),
					Integer.valueOf(Conf.getValue("redisPort")),
					Integer.valueOf(ObjUtil.ifNull(Conf.getValue("rTimeOut"),
							"5000").toString()), Conf.getValue("rRequirepass"));
		}
		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResources(ShardedJedisPool pools,
			ShardedJedis redis) {
		if (redis != null) {
			pools.returnResource(redis);
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param key
	 *            关键字
	 * @param val
	 *            值
	 * @param expSecond
	 *            过期时间,秒
	 * @return
	 */
	public static void set(String key, String val, int expSecond) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.setex(key, expSecond, val);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param key
	 * @return
	 */
	private static void setKeyVal(String key, String val) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.setex(key, ObjUtil.ifNull(
					Integer.parseInt(Conf.getValue("rSecond")), 86400), val);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param key
	 * @return
	 */
	private static void delKeyVal(String key) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	private static String getKeyVal(String key) {
		String value = null;

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	private static boolean exists(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			// e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public static void set(String key, String val) {
		try {
			setKeyVal(key, val);
		} catch (Exception e) {
			log.error(e);
		}
	}


	public static void setNotExists(String key, String val) {
		try {
			if (!exists(key))
				setKeyVal(key, val);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static String get(String key) {
		try {
			return getKeyVal(key);
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
	
	public static void del(String key) {
		try {
			delKeyVal(key);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static boolean contain(String key) {
		try {
			return exists(key);
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param key
	 * @return
	 */
	private static void setKeyVals(String key, String val) {

		ShardedJedisPool pools = null;
		ShardedJedis jedis = null;
		try {
			pools = getPools();
			jedis = pools.getResource();
			jedis.set(key, val);
			jedis.expire(key, ObjUtil.ifNull(
					Integer.parseInt(Conf.getValue("rSecond")), 86400));
		} catch (Exception e) {
			// 释放redis对象
			pools.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResources(pools, jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	private static String getKeyVals(String key) {
		String value = null;

		ShardedJedisPool pools = null;
		ShardedJedis jedis = null;
		try {
			pools = getPools();
			jedis = pools.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pools.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResources(pools, jedis);
		}

		return value;
	}

	/**
	 * 保存数据
	 * 
	 * @param key
	 *            关键字
	 * @param val
	 *            值
	 * @param expSecond
	 *            过期时间,秒
	 * @return
	 */
	public static void testList(String key, String val, int expSecond) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 列表数据key是否存在,如果返回true,说明有缓存,则可以根据member用为key,到缓存中获取
	 * 
	 * @param key
	 *            关键字
	 * @param member
	 *            条件组合key,如(cid:1-order:tt)
	 * @return
	 */
	public static boolean listKeyExists(String key, String member) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			
			Long index = jedis.zrank(key, member);
			if (index != null) {
				
				jedis.zincrby(key, 1, member);// 如果在名称为key的zset中已经存在元素member，则该元素的score增加increment
				return true;
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return false;
	}

	/**
	 * 列表数据member如果在缓存中不存在,则根据情况存储到缓存中,如果返回true,说明已存储,则可以根据member作为key,将值存入缓存中
	 * 
	 * @param key
	 *            关键字
	 * @param member
	 *            条件组合key,如(cid:1-order:tt)
	 * @param count
	 *            允许存放最大数量
	 * @param seconds
	 *            过期时间
	 * @return
	 */
	public static boolean listSetKey(String key, String member, Long count,int seconds) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			Long index = jedis.zcard(key); // 获取当前存储值数量
			
			if (index < count) { // 当前最大存储空间足够时,存入新的内容
				jedis.zadd(key, 0, member);
				if (index == 0) { //设置过期时间
					jedis.expire(key, seconds);
				}
				return true;
			}else{
				if(jedis.zremrangeByScore(key, 0, 1)>0){//删除当前访问频率少的记录
					jedis.zadd(key, 0, member);
					return true;
				}else{//未删除
					return false;
				}
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池 
			returnResource(pool, jedis);
		}
		return false;
	}
	
	/**
	 * 列表数据member如果在缓存中不存在,则根据情况存储到缓存中,如果返回true,说明已存储,则可以根据member作为key,将值存入缓存中
	 * 
	 * @param key
	 *            关键字
	 * @param member
	 *            条件组合key,如(cid:1-order:tt)
	 * @return
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static boolean listSetKey(String key, String member) throws NumberFormatException, IOException {
		JedisPool pool = null;
		Jedis jedis = null;
		Long count=(Long) ObjUtil.ifNull(
				Long.parseLong(Conf.getValue("rCount")), 86400);
		Integer seconds=(Integer) ObjUtil.ifNull(
				Integer.parseInt(Conf.getValue("rSecond")), 86400);
		
		try {
			pool = getPool();
			jedis = pool.getResource();

			Long index = jedis.zcard(key); // 获取当前存储值数量
			if (index < count) { // 当前最大存储空间足够时,存入新的内容
				jedis.zadd(key, 0, member);
				if (index == 0) { //设置过期时间
					jedis.expire(key, seconds);
				}
				return true;
			}else{
				if(jedis.zremrangeByScore(key, 0, 1)>0){//删除当前访问频率少的记录
					jedis.zadd(key, 0, member);
					return true;
				}else{//未删除
					return false;
				}
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池 
			returnResource(pool, jedis);
		}
		return false;
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RedisUtil.set("qaa", "111111");
	}
}
