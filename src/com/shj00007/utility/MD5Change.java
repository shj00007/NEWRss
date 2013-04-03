package com.shj00007.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Change {

	public static String getMD5(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			return toHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private static String toHex(byte buffer[]) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 15, 16));
		}

		return sb.toString();
	}
	
	
}
