package main.java.MapFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CanvasGetData implements GetData {
//variables
	private final Path DATA_STORE_DIR = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\BBC keys\\canvasToken.txt");
//methods
	public void getData(Assignment assign){
		//OAuth2 - get Canvas test token for my account
		String token = "";
		try{
			String tmp;
			BufferedReader buf = Files.newBufferedReader(DATA_STORE_DIR);
			while ((tmp = buf.readLine()) != null){
				token += tmp;
			}
		}catch(IOException e){
			System.out.println(e + ": unable to read token file");
		}
		
		//request assignments from the Canvas API ,<lindenwood.instructure.com> - this will return a JSON object
		
		//parse returned JSON - probably create a POJO out of it
		
		//get user_id - this is not in Geography class currently
		//get filename - this is Geography.name
		//get late status (boolean)
		//get URL for text file	
		//download text file - pass Path to Geography constructor
		//add Geography to assignmentArr
		//assign.addToAssignment(/*Geography*/);
		
		
	}
}
