package com.yaochen.test;

import java.io.FileInputStream;

import org.junit.Test;

public class NewTryCatch {
	
	@Test
	public void testName() throws Exception {
		String file = null;
		try(FileInputStream fis = new FileInputStream(file);){
			
		}catch (Exception e) {
		}finally{
//			fis.close();
		}
	}
	
}
