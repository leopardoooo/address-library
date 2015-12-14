package com.yaochen.test.bla;

import org.junit.Test;

public class ExpCalcTest {
	int init = 20;
	int round = 130;
	// 790606
	
	@Test
	public void testCalc() throws Exception {
		int total = 20;
		for(int index = round;index< 300 ;index+=10){
			init += 1; 
			total += init;
		}
		
		System.err.println(total);
	}
}
