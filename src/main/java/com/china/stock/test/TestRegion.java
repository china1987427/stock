package com.china.stock.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.china.stock.common.thread.HtmlUtil;


public class TestRegion {
	public static void main(String[] args) {
		try{
			Document doc = HtmlUtil.parserHtml("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
			Elements ele = doc.getElementsByClass("MsoNormal").eq(1).select("span");
			String city = ele.eq(3).text();
			 Pattern p_space = Pattern.compile("\\s*|\t|\r|\n", Pattern.CASE_INSENSITIVE);  
		     Matcher m_space = p_space.matcher(city);  
		     city = m_space.replaceAll(""); // 过滤空格回车标签  
			System.out.println(city);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void savePrivince() {
		Document doc = HtmlUtil.parserHtml("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
		Elements ele = doc.getElementsByClass("MsoNormal").select("b");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("regionCode", "中国");
		m.put("regionName", "中国");
		m.put("fid", "0");
		m.put("level", "0");
		list.add(m);
		for (int i = 0; i < ele.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (i % 2 != 0) {
				continue;
			}
			if (ele.eq(i) == null) {
				break;
			}
			String regionCode = ele.eq(i).text().trim();
			String regionName = ele.eq(i + 1).text().trim();
			if ("海南省".equals(regionCode)) {
				regionName = regionCode;
				regionCode = ele.eq(i).get(0).siblingElements().text();
				map.put("regionCode", regionCode);
				map.put("regionName", regionName);
				map.put("fid", "1");
				map.put("level", "1");
				list.add(map);
				regionName = ele.eq(i + 1).text().trim();
			}
			if ("重庆市".equals(regionName)) {
				map = new HashMap<String, Object>();
				regionCode = ele.eq(i + 1).get(0).siblingElements().text();
				map.put("regionCode", regionCode);
				map.put("regionName", regionName);
				map.put("fid", "1");
				map.put("level", "1");
				list.add(map);
				continue;
			}
			map.put("regionCode", regionCode);
			map.put("regionName", regionName);
			map.put("fid", "1");
			map.put("level", "1");
			// list.add(map);
		}
		System.out.println(list);
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^\\d{1,}$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static String toHanyuPinyin(String ChineseLanguage) {
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			for (int i = 0; i < cl_chars.length; i++) {
				if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
				} else {// 如果字符不是中文,则不转换
					hanyupinyin += cl_chars[i];
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			System.out.println("字符不能转成汉语拼音");
		}
		return hanyupinyin;
	}

	public static String getFirstLetter(String ChineseLanguage) {
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		try {
			String str = String.valueOf(cl_chars[0]);
			if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
				hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
				;
			} else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
				hanyupinyin += cl_chars[0];
			} else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

				hanyupinyin += cl_chars[0];
			} else {// 否则不转换

			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			System.out.println("字符不能转成汉语拼音");
		}
		return hanyupinyin;
	}
}
