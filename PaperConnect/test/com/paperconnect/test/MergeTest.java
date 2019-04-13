package com.paperconnect.test;

import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paperconnect.client.Paper;
import com.paperconnect.util.Merge;

public class MergeTest {
	ArrayList<Paper> paperObjects_10;
	ArrayList<Paper> paperObjects_100;
	ArrayList<Paper> paperObjects_1000;
	
	@Before
	public void setUp() throws Exception {
		paperObjects_10 = new ArrayList<Paper>();
		paperObjects_100 = new ArrayList<Paper>();
		paperObjects_1000 = new ArrayList<Paper>();
		
		int max = 10000;
		int min = 0;
		Random r = new Random();
		String p = "";
		
		for(int i = 0; i < 10; i++) {
			p = String.valueOf(i);
			paperObjects_10.add(new Paper(p, p, p, p, r.nextInt((max - min) + 1) + min));
		}
		
		for(int i = 0; i < 100; i++) {
			p = String.valueOf(i);
			paperObjects_100.add(new Paper(p, p, p, p, r.nextInt((max - min) + 1) + min));
		}
		
		for(int i = 0; i < 1000; i++) {
			p = String.valueOf(i);
			paperObjects_1000.add(new Paper(p, p, p, p, r.nextInt((max - min) + 1) + min));
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPaperObjectsSort() {
		
		Merge.sortMergeTD(paperObjects_10, 10);
		Merge.sortMergeTD(paperObjects_100, 100);
		Merge.sortMergeTD(paperObjects_1000, 1000);
		
		assert(Merge.isSorted(paperObjects_10, 10));
		assert(Merge.isSorted(paperObjects_100, 100));
		assert(Merge.isSorted(paperObjects_1000, 1000));
		
	}

}
