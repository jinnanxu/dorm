package com.dorm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NumberUtils {
	private static int sequence = 0;
	private static int length = 2;

	public static synchronized String getLocalTrmSeqNum() {
		sequence = sequence >= 999999 ? 1 : sequence + 1;
		String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
		String s = Integer.toString(sequence);
		return datetime + addLeftZero(s, length);
	}

	/**
	 * 左填0
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String addLeftZero(String s, int length) {
		// StringBuilder sb=new StringBuilder();
		int old = s.length();
		if (length > old) {
			char[] c = new char[length];
			char[] x = s.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: " + s + " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		return s.substring(0, length);
	}

	public static String genServiceCode(){
		Random random = new Random();
		int randomNumber = random.nextInt(900000) + 100000;
		return randomNumber+"";
	}

	public static String genBillNo(){
		sequence = sequence >= 999999 ? 1 : sequence + 1;
		String datetime = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		String s = Integer.toString(sequence);
		return datetime + addLeftZero(s, 3);
	}

	public static String genCardNo(){
		sequence = sequence >= 999999 ? 1 : sequence + 1;
		String datetime = new SimpleDateFormat("yyMMdd").format(new Date());
		String s = Integer.toString(sequence);
		return datetime + addLeftZero(s, 3);
	}
}
