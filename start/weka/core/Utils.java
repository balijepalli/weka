/*
 *    Utils.java
 *    Copyright (C) 1999 Eibe Frank,Len Trigg,Yong Wang
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package weka.core;

import java.lang.Math;
import java.util.StringTokenizer;

/**
 * Class implementing some simple utility methods.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author Yong Wang (yongwang@cs.waikato.ac.nz)
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version 1.1 - Jan 1999 - Added partitionOptions, stop option parsing at --
 * (Len) <br>
 *          1.0 - Initial version (Yong and Eibe)
 */
public final class Utils {

  // =================
  // Public variables.
  // =================

  /** The natural logarithm of 2. */

  public static double log2 = Math.log(2);

  // ===============
  // Public methods.
  // ===============

  /**
   * Checks if the given array contains any non-empty options.
   *
   * @param strings an array of strings
   * @exception Exception if there are any non-empty options
   */

  public static void checkForRemainingOptions(String [] options) 
    throws Exception {
    
    int illegalOptionsFound = 0;
    StringBuffer text = new StringBuffer();

    if (options == null)
      return;
    for (int i = 0; i < options.length; i++) 
      if (options[i].length() > 0) {
	illegalOptionsFound++;
	text.append(options[i]+' ');
      }
    if (illegalOptionsFound > 0)
      throw new Exception("Illegal options: "+text);
  }
  
  /**
   * Returns the correlation coefficient of two double vectors
   *
   * @param y1 double vector 1
   * @param y2 double vector 2
   * @param n the length of two double vectors
   * @return the correlation coefficient
   */

  public final static double correlation(double y1[],double y2[],int n) {

    int i;
    double av1=0.0,av2=0.0,y11=0.0,y22=0.0,y12=0.0,c;
    
    if(n<=1)return 1.0;
    for(i=0;i<n;i++){
      av1 += y1[i];
      av2 += y2[i];
    }
    av1 /= (double)n;
    av2 /= (double)n;
    for(i=0;i<n;i++){
      y11 += (y1[i] - av1) * (y1[i] - av1);
      y22 += (y2[i] - av2) * (y2[i] - av2);
      y12 += (y1[i] - av1) * (y2[i] - av2);
    }
    if(y11*y22 == 0.0) c=1.0;
    else c = y12 / Math.sqrt(Math.abs(y11*y22));
    
    return c;
  }

  /**
   * Rounds a double and converts it into String.
   *
   * @param value the double value
   * @param afterDecimalPoint the number of digits after the decimal point
   * @return the double as a formatted string
   */
  
  public static String doubleToString(double value, int afterDecimalPoint) {
    
    StringBuffer stringBuffer;
    double temp;
    int i,dotPosition;
    long precisionValue;
    
    temp = value * Math.pow(10.0, afterDecimalPoint);
    if (Math.abs(temp) < Long.MAX_VALUE) {
      precisionValue = 	(temp > 0) ? (long)(temp + 0.5) 
                                   : -(long)(Math.abs(temp) + 0.5);
      if (precisionValue == 0) {
	stringBuffer = new StringBuffer(String.valueOf(0));
      } else {
	stringBuffer = new StringBuffer(String.valueOf(precisionValue));
      }
      if (afterDecimalPoint == 0)
	return stringBuffer.toString();
      dotPosition = stringBuffer.length() - afterDecimalPoint;
      while (((precisionValue < 0) && (dotPosition < 1)) ||
	     (dotPosition < 0)) {
	if (precisionValue < 0) {
	  stringBuffer.insert(1, 0);
	} else {
	  stringBuffer.insert(0, 0);
	}
	dotPosition++;
      }
      stringBuffer.insert(dotPosition, '.');
      if ((precisionValue < 0) && (stringBuffer.charAt(1) == '.')) {
	stringBuffer.insert(1, 0);
      } else if (stringBuffer.charAt(0) == '.') {
	stringBuffer.insert(0, 0);
      }
      int currentPos = stringBuffer.length() - 1;
      while ((currentPos > dotPosition) &&
	     (stringBuffer.charAt(currentPos) == '0')) {
	stringBuffer.setCharAt(currentPos--, ' ');
      }
      if (stringBuffer.charAt(currentPos) == '.') {
	stringBuffer.setCharAt(currentPos, ' ');
      }
      
      return stringBuffer.toString().trim();
    } 
    return new String("NaN");
  }

  /**
   * Rounds a double and converts it into a formatted decimal-justified String.
   *
   * @param value the double value
   * @param width the width of the string
   * @param afterDecimalPoint the number of digits after the decimal point
   * @return the double as a formatted string
   */
  
  public static String doubleToString(double value, int width,
				      int afterDecimalPoint) {
    
    String tempString = doubleToString(value, afterDecimalPoint);
    char[] result;
    int dotPosition;

    // Number to large for given width
    if (tempString.length() >= width) {
      return tempString;
    }

    // Initialize result
    result = new char[width];
    for (int i = 0; i < result.length; i++) {
      result[i] = ' ';
    }

    if (afterDecimalPoint > 0) {
      // Get position of decimal point and insert decimal point
      dotPosition = tempString.indexOf('.');
      if (dotPosition == -1) {
	dotPosition = tempString.length();
      } else {
	result[width - afterDecimalPoint - 1] = '.';
      }
    } else {
      dotPosition = tempString.length();
    }

    // Copy characters before decimal point
    for (int i = 0; i < dotPosition; i++) {
      result[width - afterDecimalPoint - dotPosition - 1 + i] = 
	  tempString.charAt(i);
    }

    // Copy characters after decimal point
    for (int i = dotPosition + 1; i < tempString.length(); i++) {
      result[width - afterDecimalPoint - 1 + i - dotPosition] = 
	  tempString.charAt(i);
    }

    return new String(result);
  }

  /**
   * Tests if a is equal to b.
   *
   * @param a a double
   * @param b a double
   */

  public static boolean eq(double a,double b){
    return (a-b < 1e-6) && (b-a < 1e-6); 
  }

  /**
   * Checks if the given array contains the flag "-Char". Stops
   * searching at the first marker "--". If the flag is found,
   * it is replaced with the empty string.
   *
   * @param flag the character indicating the flag.
   * @param strings the array of strings containing all the options.
   * @return true if the flag was found
   * @exception Exception if an illegal option was found
   */

  public static boolean getFlag(char flag, String [] options) 
  throws Exception {

    if (options == null) {
      return false;
    }
    for (int i = 0; i < options.length; i++) {
      if ((options[i].length() > 1) && (options[i].charAt(0) == '-')) {
	try {
	  Double dummy = Double.valueOf(options[i]);
	} catch (NumberFormatException e) {
	  if (options[i].length() > 2) {
	    throw new Exception("Illegal option: " + options[i]);
	  }
	  if (options[i].charAt(1) == flag) {
	    options[i] = "";
	    return true;
	  }
	  if (options[i].charAt(1) == '-') {
	    return false;
	  }
	}
      }
    }
    return false;
  }

  /**
   * Gets an option indicated by a flag "-Char" from the given array
   * of strings. Stops searching at the first marker "--". Replaces 
   * flag and option with empty strings.
   *
   * @param flag the character indicating the option.
   * @param options the array of strings containing all the options.
   * @return the indicated option or an empty string
   * @exception Exception if the option indicated by the flag can't be found
   */

  public static String getOption(char flag, String [] options) 
       throws Exception {

    String newString;

    if (options == null)
      return "";
    for (int i = 0; i < options.length; i++) {
      if ((options[i].length() > 0) && (options[i].charAt(0) == '-')) {
	
	// Check if it is a negative number
	
	try {
	  Double dummy = Double.valueOf(options[i]);
	} catch (NumberFormatException e) {
	  if (options[i].length() > 2) {
	    throw new Exception("Illegal option: " + options[i]);
	  }
	  if (options[i].charAt(1) == flag) {
	    if (i + 1 == options.length) {
	      throw new Exception("No value given for -" + flag + " option.");
	    }
	    options[i] = "";
	    newString = new String(options[i + 1]);
	    options[i + 1] = "";
	    return newString;
	  }
	  if (options[i].charAt(1) == '-') {
	    return "";
	  }
	}
      }
    }
    return "";
  }

  /**
   * Returns the secondary set of options (if any) contained in
   * the supplied options array. The secondary set is defined to
   * be any options after the first "--". These options are removed from
   * the original options array.
   *
   * @param options the input array of options
   * @return the array of secondary options
   */
  public static String [] partitionOptions(String [] options) {

    for (int i = 0; i < options.length; i++) {
      if (options[i].equals("--")) {
	options[i++] = "";
	String [] result = new String [options.length - i];
	for (int j = i; j < options.length; j++) {
	  result[j - i] = options[j];
	  options[j] = "";
	}
	return result;
      }
    }
    return new String [0];
  }

  /**
   * Split up a string containing options into an array of strings,
   * one for each option.
   *
   * @param optionString the string containing the options
   * @return the array of options
   */
  public static String [] splitOptions(String optionString) {

    FastVector optionsVec = new FastVector();
    StringTokenizer st = new StringTokenizer(optionString);
    while (st.hasMoreTokens())
      optionsVec.addElement(st.nextToken());

    String [] options = new String[optionsVec.size()];
    for (int i = 0; i < optionsVec.size(); i++) {
      options[i] = (String)optionsVec.elementAt(i);
    }
    return options;
  }

  /**
   * Computes entropy for an array of integers.
   *
   * @param counts array of counts
   * @returns - a log2 a - b log2 b - c log2 c + (a+b+c) log2 (a+b+c)
   * when given array [a b c]
   */
  public static double info(int counts[]) {
    int total = 0; int c;
    double x = 0;
    for (int j = 0; j < counts.length; j++) {
      x -= xlogx(counts[j]); total += counts[j];
    }
    return x + xlogx(total);
  }

  /**
   * Tests if a is smaller or equal to b.
   *
   * @param a a double
   * @param b a double
   */
  public static boolean smOrEq(double a,double b){
    return (a-b < 1e-6);
  }

  /**
   * Tests if a is greater or equal to b.
   *
   * @param a a double
   * @param b a double
   */
  public static boolean grOrEq(double a,double b){
    return (b-a < 1e-6);
  }
  
  /**
   * Tests if a is smaller than b.
   *
   * @param a a double
   * @param b a double
   */
  public static boolean sm(double a,double b){
    return (b-a > 1e-6);
  }

  /**
   * Tests if a is smaller than b.
   *
   * @param a a double
   * @param b a double 
   */
  public static boolean gr(double a,double b){
    return (a-b > 1e-6);
  }

  /**
   * Returns the logarithm of a for base 2.
   *
   * @param a a double
   */
  public static double log2(double a){
    return Math.log(a)/log2;
  }

  /**
   * Returns index of maximum element in a given
   * array of doubles.
   *
   * @param doubles the array of doubles
   * @return the index of the maximum element
   */
  public static int maxIndex(double [] doubles){

    double maximum = 0;
    int maxIndex = 0;

    for (int i=0;i<doubles.length;i++)
      if ((i == 0) || (doubles[i] > maximum)){
	maxIndex = i;
	maximum = doubles[i];
      }

    return maxIndex;
  }

  /**
   * Returns index of maximum element in a given
   * array of integers.
   *
   * @param ints the array of integers
   * @return the index of the maximum element
   */
  public static int maxIndex(int [] ints){

    int maximum = 0;
    int maxIndex = 0;

    for (int i=0;i<ints.length;i++)
      if ((i == 0) || (ints[i] > maximum)){
	maxIndex = i;
	maximum = ints[i];
      }

    return maxIndex;
  }

  /**
   * Computes the mean for an array of doubles.
   *
   * @param vector the array
   * @return the mean
   */
  public static double mean(double[] vector) {
  
    double sum = 0;

    if (vector.length == 0)
      return 0;
    for (int i = 0; i < vector.length; i++) 
      sum += vector[i];
    return sum / (double) vector.length;
  }

  /**
   * Returns index of minimum element in a given
   * array of integers.
   *
   * @param ints the array of integers
   * @return the index of the minimum element
   */
  public static int minIndex(int [] ints){

    int minimum = 0;
    int minIndex = 0;

    for (int i=0;i<ints.length;i++)
      if ((i == 0) || (ints[i] < minimum)){
	minIndex = i;
	minimum = ints[i];
      }

    return minIndex;
  }

  /**
   * Returns index of minimum element in a given
   * array of doubles.
   *
   * @param doubles the array of doubles
   * @return the index of the minimum element
   */
  public static int minIndex(double [] doubles){

    double minimum = 0;
    int minIndex = 0;

    for (int i=0;i<doubles.length;i++)
      if ((i == 0) || (doubles[i] < minimum)){
	minIndex = i;
	minimum = doubles[i];
      }

    return minIndex;
  }

  /**
   * Normalizes the doubles in the array by their sum.
   *
   * @param doubles the array of double
   * @exception Exception if sum is NaN
   */
  public static void normalize(double[] doubles) throws Exception {

    double sum = 0;

    for (int i = 0; i < doubles.length; i++)
      sum += doubles[i];
    if (Double.isNaN(sum)) {
      throw new Exception("Array contains NaN - can't normalize");
    }
    if (sum != 0)
      for (int i = 0; i < doubles.length; i++)
	doubles[i] /= sum;
  }

  /**
   * Normalizes the doubles in the array using the given value.
   *
   * @param doubles the array of double
   * @param sum the value by which the doubles are to be normalized
   * @exception Exception if sum is zero
   */
  public static void normalize(double[] doubles, double sum) 
       throws Exception {

    if (sum == 0)
      throw new Exception("Can't normalize array. Sum is zero.");
    for (int i = 0; i < doubles.length; i++)
      doubles[i] /= sum;
  }

  /**
   * Rounds a double to the next nearest integer value. The JDK version
   * of it doesn't work properly.
   *
   * @param value the double value
   * @return the resulting integer value
   */
  public static int round(double value){

    int roundedValue;
    
    roundedValue = value>0? (int)(value+0.5) : -(int)(Math.abs(value)+0.5);
    
    return roundedValue;
  }

  /**
   * Rounds a double to the given number of decimal places.
   *
   * @param value the double value
   * @param afterDecimalPoint the number of digits after the decimal point
   * @return the double rounded to the given precision
   */
  public static double roundDouble(double value,int afterDecimalPoint){

    double mask;

    mask = Math.pow(10.0,(double)afterDecimalPoint);

    return (double)(Math.round(value*mask))/mask;
  }

  /**
   * Sorts a given array of doubles in ascending order and returns an 
   * array of integers with the positions of the elements of the original 
   * array in the sorted array. The sort is stable (Equal elements remain
   * in their original order.)
   *
   * @param array this array is not changed by the method!
   * @return an array of integers with the positions in the sorted
   * array.
   */
  public static int[] sort(double [] array){

    int [] index = new int[array.length];
    int [] newIndex = new int[array.length];
    int [] helpIndex;
    int numEqual;
    
    for (int i = 0; i < index.length; i++)
      index[i] = i;
    quickSort(array,index,0,array.length-1);

    // Make sort stable

    int i = 0;
    while (i < index.length) {
      numEqual = 1;
      for (int j = i+1; ((j < index.length) && Utils.eq(array[index[i]],
							array[index[j]])); j++)
	numEqual++;
      if (numEqual > 1) {
	helpIndex = new int[numEqual];
	for (int j = 0; j < numEqual; j++)
	  helpIndex[j] = i+j;
	quickSort(index, helpIndex, 0, numEqual-1);
	for (int j = 0; j < numEqual; j++) 
	  newIndex[i+j] = index[helpIndex[j]];
	i += numEqual;
      } else {
	newIndex[i] = index[i];
	i++;
      }
    }

    return newIndex;
  }

  /**
   * Computes the variance for an array of doubles.
   *
   * @param vector the array
   * @return the variance
   */
  public static double variance(double[] vector) {
  
    double sum = 0, sumSquared = 0;

    if (vector.length <= 1)
      return 0;
    for (int i = 0; i < vector.length; i++) {
      sum += vector[i];
      sumSquared += (vector[i] * vector[i]);
    }
    return (sumSquared - (sum * sum / (double) vector.length)) / 
      (double) (vector.length - 1);
  }

  /**
   * Computes the sum of the elements of an array of doubles.
   *
   * @param doubles the array of double
   * @returns the sum of the elements
   */
  public static double sum(double[] doubles) {

    double sum = 0;

    for (int i = 0; i < doubles.length; i++)
      sum += doubles[i];
    return sum;
  }

  /**
   * Computes the sum of the elements of an array of integers.
   *
   * @param ints the array of integers
   * @returns the sum of the elements
   */
  public static int sum(int[] ints) {

    int sum = 0;

    for (int i = 0; i < ints.length; i++)
      sum += ints[i];
    return sum;
  }

  /**
   * Returns c*log2(c) for a given integer value c.
   *
   * @param c an integer value
   * @returns c*log2(c) (but is careful to return 0 if c is 0)
   */
  public static double xlogx(int c) {
    if (c == 0) return 0.0;
    return c * Utils.log2((double) c);
  }
 
  /**
   * Implements quicksort for an array of indices.
   *
   * @param array the array of doubles to be sorted
   * @param index the index which should contain the positions in the
   * sorted array
   * @param lo0 the first index of the subset to be sorted
   * @param hi0 the last index of the subset to be sorted
   */
  private static void quickSort(double [] array,int [] index,int lo0,int hi0){

    int lo = lo0;
    int hi = hi0;
    double mid;
    double midPlus;
    double midMinus;
    int help;
    
    if (hi0 > lo0){
      
      // Arbitrarily establishing partition element as the midpoint of
      // the array.
            
      mid = array[index[(lo0+hi0)/2]];
      midPlus = mid+1e-6;
      midMinus = mid-1e-6;

      // loop through the array until indices cross
      
      while(lo <= hi){
	
	// find the first element that is greater than or equal to  
	// the partition element starting from the left Index.
		
	while ((array[index[lo]] < midMinus) && (lo < hi0))
	  ++lo;
	
	// find an element that is smaller than or equal to 
	// the partition element starting from the right Index.
	
	while ((array[index[hi]] > midPlus) && (hi > lo0))
	  --hi;
	
	// if the indexes have not crossed, swap
	
	if(lo <= hi) {
	  help = index[lo];
	  index[lo] = index[hi];
	  index[hi] = help;
	  ++lo;
	  --hi;
	}
      }
      
      // If the right index has not reached the left side of array
      // must now sort the left partition.
      
      if(lo0 < hi)
	quickSort(array,index,lo0,hi);
      
      // If the left index has not reached the right side of array
      // must now sort the right partition.
      
      if(lo < hi0)
	quickSort(array,index,lo,hi0);
    }
  }

  // ===============
  // Private methods
  // ===============

  /**
   * Implements quicksort for an array of indices.
   *
   * @param array the array of integers to be sorted
   * @param index the index which should contain the positions in the
   * sorted array
   * @param lo0 the first index of the subset to be sorted
   * @param hi0 the last index of the subset to be sorted
   */
  private static void quickSort(int [] array,int [] index,int lo0,int hi0){

    int lo = lo0;
    int hi = hi0;
    int mid;
    int help;
    
    if (hi0 > lo0){
      
      // Arbitrarily establishing partition element as the midpoint of
      // the array.
            
      mid = array[index[(lo0+hi0)/2]];

      // loop through the array until indices cross
      
      while(lo <= hi){
	
	// find the first element that is greater than or equal to  
	// the partition element starting from the left Index.
		
	while ((array[index[lo]] < mid) && (lo < hi0))
	  ++lo;
	
	// find an element that is smaller than or equal to 
	// the partition element starting from the right Index.
	
	while ((array[index[hi]] > mid) && (hi > lo0))
	  --hi;
	
	// if the indexes have not crossed, swap
	
	if(lo <= hi) {
	  help = index[lo];
	  index[lo] = index[hi];
	  index[hi] = help;
	  ++lo;
	  --hi;
	}
      }
      
      // If the right index has not reached the left side of array
      // must now sort the left partition.
      
      if(lo0 < hi)
	quickSort(array,index,lo0,hi);
      
      // If the left index has not reached the right side of array
      // must now sort the right partition.
      
      if(lo < hi0)
	quickSort(array,index,lo,hi0);
    }
  }
}
