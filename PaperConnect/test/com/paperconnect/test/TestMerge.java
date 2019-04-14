package com.paperconnect.test;

import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.util.Merge;

/**
 * Tests cases for Merge sort class using project objects.
 */
public class TestMerge {
	private ArrayList<Paper> paperObjects_10;
	private ArrayList<Paper> paperObjects_100;
	private ArrayList<Paper> paperObjects_1000;
	
	private ArrayList<LookupTableLine> luTableLineObjects_5;
	private ArrayList<LookupTableLine> luTableLineObjects_10;
	private ArrayList<LookupTableLine> luTableLineObjects_15;
	
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
		
		luTableLineObjects_5 = new ArrayList<LookupTableLine>();
		luTableLineObjects_10 = new ArrayList<LookupTableLine>();
		luTableLineObjects_15 = new ArrayList<LookupTableLine>();
		
		luTableLineObjects_5.add(new LookupTableLine("hello", "1"));
		luTableLineObjects_5.add(new LookupTableLine("this", "1"));
		luTableLineObjects_5.add(new LookupTableLine("is", "1"));
		luTableLineObjects_5.add(new LookupTableLine("a", "1"));
		luTableLineObjects_5.add(new LookupTableLine("test", "1"));
		
		luTableLineObjects_10.add(new LookupTableLine("/***", "1"));
		luTableLineObjects_10.add(new LookupTableLine("this", "1"));
		luTableLineObjects_10.add(new LookupTableLine("not", "1"));
		luTableLineObjects_10.add(new LookupTableLine("Hello", "1"));
		luTableLineObjects_10.add(new LookupTableLine("case", "1"));
		luTableLineObjects_10.add(new LookupTableLine("1234", "1"));
		luTableLineObjects_10.add(new LookupTableLine("hey now", "1"));
		luTableLineObjects_10.add(new LookupTableLine("youre an", "1"));
		luTableLineObjects_10.add(new LookupTableLine("allstar", "1"));
		luTableLineObjects_10.add(new LookupTableLine("test", "1"));
		
		luTableLineObjects_15.add(new LookupTableLine("big boy", "1"));
		luTableLineObjects_15.add(new LookupTableLine("get", "1"));
		luTableLineObjects_15.add(new LookupTableLine("your", "1"));
		luTableLineObjects_15.add(new LookupTableLine("game", "1"));
		luTableLineObjects_15.add(new LookupTableLine("on", "1"));
		luTableLineObjects_15.add(new LookupTableLine("nanananana", "1"));
		luTableLineObjects_15.add(new LookupTableLine("BATMAN", "1"));
		luTableLineObjects_15.add(new LookupTableLine("????", "1"));
		luTableLineObjects_15.add(new LookupTableLine("", "1"));
		luTableLineObjects_15.add(new LookupTableLine("something", "1"));
		luTableLineObjects_15.add(new LookupTableLine("just", "1"));
		luTableLineObjects_15.add(new LookupTableLine("like", "1"));
		luTableLineObjects_15.add(new LookupTableLine("this", "1"));
		luTableLineObjects_15.add(new LookupTableLine("tununnunu", "1"));
		luTableLineObjects_15.add(new LookupTableLine("55524", "1"));
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
	
	@Test
	public void testLookupTableLine() {
		
		Merge.sortMergeTD(luTableLineObjects_5, 5);
		Merge.sortMergeTD(luTableLineObjects_10, 10);
		Merge.sortMergeTD(luTableLineObjects_15, 15);
		
		assert(Merge.isSorted(luTableLineObjects_5, 5));
		assert(Merge.isSorted(luTableLineObjects_10, 10));
		assert(Merge.isSorted(luTableLineObjects_15, 15));
	}
}
