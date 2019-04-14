package com.paperconnect.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;
import com.paperconnect.util.Search;
/**
 * Test cases for binary search on look up table and paper data set
 *
 */
public class TestSearch {
	private ArrayList<Paper> paperObjects_15;
	private ArrayList<LookupTableLine> luTableLineObjects_15;
	
	@Before
	public void setUp() throws Exception {
		
		//lexicographically sorted (id) Paper list 
		String p = "1";
		paperObjects_15 = new ArrayList<Paper>();
		paperObjects_15.add(new Paper("a", p, p, p, 15));
		paperObjects_15.add(new Paper("b", p, p, p, 14));
		paperObjects_15.add(new Paper("c", p, p, p, 13));
		paperObjects_15.add(new Paper("d", p, p, p, 12));
		paperObjects_15.add(new Paper("e", p, p, p, 11));
		paperObjects_15.add(new Paper("f", p, p, p, 10));
		paperObjects_15.add(new Paper("h", p, p, p, 9));
		paperObjects_15.add(new Paper("i", p, p, p, 8));
		paperObjects_15.add(new Paper("j", p, p, p, 7));
		paperObjects_15.add(new Paper("k", p, p, p, 6));
		paperObjects_15.add(new Paper("l", p, p, p, 5));
		paperObjects_15.add(new Paper("m", p, p, p, 4));
		paperObjects_15.add(new Paper("n", p, p, p, 3));
		paperObjects_15.add(new Paper("o", p, p, p, 2));
		paperObjects_15.add(new Paper("p", p, p, p, 1));
		
		//lexicographically sorted (keyword) LookupTableLine list
		luTableLineObjects_15 = new ArrayList<LookupTableLine>();
		luTableLineObjects_15.add(new LookupTableLine("abig boy", "1"));
		luTableLineObjects_15.add(new LookupTableLine("bget", "2"));
		luTableLineObjects_15.add(new LookupTableLine("cyour", "3"));
		luTableLineObjects_15.add(new LookupTableLine("dgame", "4"));
		luTableLineObjects_15.add(new LookupTableLine("eon", "5"));
		luTableLineObjects_15.add(new LookupTableLine("fnanananana", "6"));
		luTableLineObjects_15.add(new LookupTableLine("gBATMAN", "7"));
		luTableLineObjects_15.add(new LookupTableLine("i????", "8"));
		luTableLineObjects_15.add(new LookupTableLine("j", "9"));
		luTableLineObjects_15.add(new LookupTableLine("ksomething", "10"));
		luTableLineObjects_15.add(new LookupTableLine("ljust", "11"));
		luTableLineObjects_15.add(new LookupTableLine("mlike", "12"));
		luTableLineObjects_15.add(new LookupTableLine("nthis", "13"));
		luTableLineObjects_15.add(new LookupTableLine("otununnunu", "14"));
		luTableLineObjects_15.add(new LookupTableLine("p55524", "15"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testbinarySearchID() {
		assert(Search.binarySearchID(paperObjects_15, "c").getField(PaperFields.ID) == "c");
		assert(Search.binarySearchID(paperObjects_15, "o").getField(PaperFields.ID) == "o");
		assert(Search.binarySearchID(paperObjects_15, "a").getField(PaperFields.ID) == "a");
		assert(Search.binarySearchID(paperObjects_15, "z") == null);
	}
	
	@Test
	public void testbinarySearchKeyword() {
		
		assert(Search.binarySearchKeyword(luTableLineObjects_15, "nthis").getRightHalf() == "13");
		assert(Search.binarySearchKeyword(luTableLineObjects_15, "p55524").getRightHalf() == "15");
		assert(Search.binarySearchKeyword(luTableLineObjects_15, "abig boy").getRightHalf() == "1");
		assert(Search.binarySearchKeyword(luTableLineObjects_15, "not in there") == null);
	}

}
