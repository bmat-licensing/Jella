package com.bmat.ella;

public class Util {
	public static String joinArray(String[] array, String separator){
		String result = "";
		String sep = "";
		for(String value: array){
			result += sep + value;
			sep = separator;
		}
		return result;
	}
}
