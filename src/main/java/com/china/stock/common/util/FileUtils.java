package com.china.stock.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {

	public static void main(String[] args) {

	}

	public static void writeToTxt(Map map, String filename) {
		System.out.println(map);
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			List list = (List) entry.getValue();
			File file = new File(filename);
			FileWriter fw = null;
			BufferedWriter writer = null;
			Iterator iterator = list.iterator();
			try {
				fw = new FileWriter(file);
				writer = new BufferedWriter(fw);
				while (iterator.hasNext()) {
					writer.write(iterator.next().toString());
				}
				writer.newLine();// 换行
				writer.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeListToTxt(List list, String filename) {
		System.out.println(list);
		File file = new File(filename);
		FileWriter fw = null;
		BufferedWriter writer = null;
		Iterator iterator = list.iterator();
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			while (iterator.hasNext()) {
				writer.write(iterator.next().toString());
				writer.newLine();// 换行
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
