package com.paperconnect.util;

import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;

/**
 * Merge sort algorithms for arrays 
 * @author Purv Patel
 */
public class Merge {
	private static ArrayList<Object> temp;
	/**
	 * merge function using Comparable
	 * @param x - the input array list containing products that need to be sorted.
	 * @param low - the least index of the input array list 
	 * @param mid - the middle index of the input array list
	 * @param high - the biggest index of the input array list 
	 */
	private static <T extends Comparable<? super T>> void merge( ArrayList<T> x, int low, int mid, int high) {
		//Declaring book-keeping variables and copying contents of x into array temp for sorting later on
		int i,j, k;
		for(i = low; i <= high; i++)
			temp.set(i, x.get(i));
		
		//Assigning values to varibles, splitting the array x into two sorted sub arrays
		i = low;
		j = mid + 1;
		k = low;
		
		//merging two sorted sub arrays into one sorted array using the temp copy of x
		while(i <= mid && j <= high) {
			if (((Comparable) temp.get(i)).compareTo(temp.get(j)) < 0) 
				x.set(k, (T) temp.get(i++));
			else  
				x.set(k, (T) temp.get(j++));
			k++;
		}
		
		//Assigning the remaining elements from one sub array onto the end of x
		if(i > mid)
			for(; k <= high; k++)
				x.set(k, (T) temp.get(j++));
		else
			for(; k <= high; k++)
				x.set(k, (T) temp.get(i++));
	}
	
	/**
	 * Top-Down merge sort (recursively)
	 * @param x - input array list to be sorted
	 * @param low - index of first element of array list 
	 * @param high - index of last element in array list
	 */
	private static <T extends Comparable<? super T>> void sortTD(ArrayList<T> x, int low, int high) {
		//terminating condition
		if(low >= high)
			return;
		//splitting array into smaller sub arrays to be sorted
		int mid = low + (high - low)/2;
		sortTD(x, low, mid);
		sortTD(x, mid + 1, high);
		merge(x, low, mid, high);
	}
	/**
	 * top-down merge sort using Comparable
	 * @param x - the input array list containing products that need to be sorted.
	 * @param n - the size of the input array list
	 */
	public static <T extends Comparable<? super T>> void sortMergeTD ( ArrayList<T> x, int n ) {
		//Declaring temp array used for the merge function and calling top-down sort
		temp = new ArrayList<Object>();
		for(int i = 0; i < n; i++) {
			temp.add(new Object());
		}
		sortTD(x, 0, n - 1);
	}
	
	/**
	 * Test if a array list of comparable objects is sorted
	 * @param x ArrayList of comparable objects 
	 * @param n size of ArrayList
	 * @return A boolean value indicating whether array list is sorted
	 */
	public static <T extends Comparable<? super T>> boolean isSorted(ArrayList<T> x, int n) {
		for(int i = 0; i < n - 1; i++)
			if(x.get(i).compareTo(x.get(i + 1)) > 0)
				return false;
		return true;
	}
}
