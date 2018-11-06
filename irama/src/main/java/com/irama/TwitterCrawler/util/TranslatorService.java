package com.irama.TwitterCrawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TranslatorService {
	private String executeCommand(String command, String lang, String text) {
		
		//Translate Shell's abbr. of hindi - hi. 
		if(lang.equals("hi_IN")) {
			lang = "hi"; 
		}
		
		String finalCommand = "C:\\cygwin64\\bin\\bash.exe --login -c \"" + command + lang + " '" + text + "'\"";
		//System.out.println(finalCommand);
		
		StringBuffer output = new StringBuffer();
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(finalCommand);
			p.waitFor();
			BufferedReader reader = 
                           new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + " ");
			}
			//System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	/**
	 * Accepts question in English and returns a JSONObject having its translation in target languages
	 * @param question
	 * @return
	 * @throws FileNotFoundException
	 */
	public String translateSentence(String sentence) throws FileNotFoundException {
		String result;
        
        result = executeCommand("trans -b :", "en", sentence);
        
        return result;
	}
}

