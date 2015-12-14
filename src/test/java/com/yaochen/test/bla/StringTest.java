package com.yaochen.test.bla;

import java.net.URLDecoder;

import org.junit.Test;

public class StringTest {
	
	@Test
	public void testUrlEncode() throws Exception {
		String encoded = "%E9%9D%9E%E8%8B%B1%E6%96%87%E5%AD%97%E7%AC%A6";
		String decode = URLDecoder.decode(encoded );
		System.err.println(decode);
		String reDecode = URLDecoder.decode(decode);
		System.err.println(reDecode);
		
	}
	
}
