package com.china.stock.common.tool.base;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKAnalyzer {
	
	/**
	 * 分词处理
	 * @throws IOException 
	 * 
	 */
	public static String getParticiple(String content) throws IOException{
		if (content==null) 
			return "";
		StringReader sr=new StringReader(content);
		IKSegmenter ik=new IKSegmenter(sr, true);
		Lexeme lex=null;
		String curFc = "";
		while((lex=ik.next())!=null){
			curFc += lex.getLexemeText();
			curFc+=",";
			
		}
		return curFc.endsWith(",")?curFc.substring(0,curFc.length()-1):curFc;
	}
	
	public static void main(String[] args) throws Exception
	{
		String text="我的�?";

		System.out.println(getParticiple(text));		
	}
}
