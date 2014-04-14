package com.quesity.general;

import android.os.Handler;

public class Utils {

	public static double roundToHalf(double num) {
		return (double)Math.round(num*2)/2;
	}
	
	
	/**
	 * Displays a num either as x.5 or x without the trailing zero.
	 * @param num
	 * @return
	 */
	public static String displayRoundToHalf(double num) {
		double fixed = roundToHalf(num);
		if ( fixed == Math.floor(fixed) ) {
			Integer int_num = (int) fixed;
			return int_num.toString();
		}
		return Double.toString(fixed);
	}
	

}
