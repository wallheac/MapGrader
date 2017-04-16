package main.java.MapFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileGetData implements GetData {

//methods
	public void getData(Assignment assignment){
		
		Path dir = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\bbc canvas\\submissions04-06");
			try 
				{
					DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{txt,json}"); 
				    
					for (Path entry: stream)
				    {
				    	Geography curr = new Geography(entry);
				    	assignment.addToAssignment(curr);
				    }
				}
				catch (IOException e) {
				     System.err.println(e);
				     }
			
			
	}

}
