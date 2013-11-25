package com.quesity.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader {

	public static String readFromStream(InputStream s) throws IOException {
		String output  = "";
		InputStreamReader reader =new InputStreamReader(s);
		BufferedReader r = new BufferedReader(reader);
		String l="";
		while ((l = r.readLine()) != null) {
			output = output + l;
		}
		
		return output;
	}
}
