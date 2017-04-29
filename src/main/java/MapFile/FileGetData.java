package main.java.MapFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileGetData implements GetData {

//methods
	public void getData(Assignment assignArr){
		
		//Path dir = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\bbc canvas\\submissions04-06");
		Path dir = this.getDir();	
		try 
				{
					DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{txt,json}"); 
				    
					for (Path entry: stream)
				    {
				    	Geography curr = new Geography(entry);
				    	assignArr.addToAssignment(curr);
				    }
				}
				catch (IOException e) {
				     System.err.println(e);
				     }
			
			
	}
	
	public Path getDir(){
		String dir = "C:\\Users\\amy\\Documents\\W CIV\\Lindenwood";
		int i;
		String tmp=null;
		long time=0;
		File tempDir, tempFile;
		ArrayList fileQ=new ArrayList();
		fileQ.add(dir);
		
		while(!fileQ.isEmpty()) {
		  tempDir=new File((String)fileQ.remove(0)); //tempDir gets the first thing in fileQ
		  if(tempDir.list()!=null)//if tempDir contains directories (list returns an array of strings of files and directories
		  for(i=0; i<tempDir.list().length; i++) {
		    tempFile=new File(tempDir.getPath()+File.separatorChar+tempDir.list()[i]);
		    if(tempFile.isDirectory()) {
		    	System.out.println(tempFile.getPath()+" "+fileQ.size());
		    	if(tempFile.lastModified()>time) { 
		    		time=tempFile.lastModified();     
		    		tmp=tempFile.getPath(); }
	    		fileQ.add(tempFile.getPath());
		      }
		    }
		  }
		System.out.println("most recently modified "+tmp);
		
		return Paths.get(tmp);
		
	}

}
