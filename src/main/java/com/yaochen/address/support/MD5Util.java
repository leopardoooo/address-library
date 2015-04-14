package com.yaochen.address.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static String byte2hex(byte[] b) {
		if (b == null)
			return "";
		String hs = "";
		String stmp = "";
		for (byte element : b) {
			stmp = Integer.toHexString(element & 0XFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toLowerCase();
	}
	/**
	 * loginPassword=form传入的密码
	 * loginKey=AUserData保存的loginKey
	 * @param loginPassword
	 * @param loginKey
	 * @return
	 */
	public static String EncodePasswordByLoginKey(String loginPassword,String loginKey){
		if(loginPassword==null||loginKey==null){
			return null;
		}
		return EncodePassword(loginPassword+loginKey);
	}

	public static String EncodePassword(String input) {
		if (input == null)
			return null;
		byte[] digesta = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(input.getBytes());
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return byte2hex(digesta);
	}

	
	public static void main(String[] args) {
		System.err.println(EncodePassword("123"));
	}

}