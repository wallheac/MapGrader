package main.java.MapFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.*;

public class FileGetData implements GetData {

	public Path getDir(){
		return findMostRecentDir();
	}
	
//methods
	public void getData(Assignment assignArr){
		
		//find the most recently modified dir and unzip most recent zip file
		Path assignDir = this.zipHandler(this.findMostRecentDir());
		
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(assignDir, "*.{txt,json}"); 
		    
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
	
	private Path findMostRecentDir(){
		String dir = "C:\\Users\\amy\\Documents\\W CIV\\Lindenwood";
		int i;
		String tmp=null;
		long time=0;
		File tempDir, tempFile;
		ArrayList fileQ=new ArrayList();
		fileQ.add(dir);
		
		while(!fileQ.isEmpty()) {
		  tempDir=new File((String)fileQ.remove(0)); //tempDir gets the first thing in fileQ
		  if(tempDir.list()!=null){//if tempDir contains directories (list returns an array of strings of files and directories
		  for(i=0; i<tempDir.list().length; i++) {
		    tempFile=new File(tempDir.getPath()+File.separatorChar+tempDir.list()[i]);
		    if(tempFile.isDirectory()) {
		    	System.out.println(tempFile.getPath()+" "+fileQ.size());
		    	if(tempFile.lastModified()>time) { 
		    		time=tempFile.lastModified();     
		    		tmp=tempFile.getPath(); 
		    		}
	    		fileQ.add(tempFile.getPath());
		      }
		    }
		  }	
		}
		System.out.println("most recently modified "+tmp);
		
		return Paths.get(tmp);
		
	}
	//takes the last modified directory, finds last modified zip file and unzips
	//returns path to the new directory
	private Path zipHandler(Path dir){
		//find last modified zip file
		Path mostRecentZip = null;
		long time = 0;
		Date date = null;
		
		try 
		{
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{zip}");
			
			for(Path entry: stream){
				File tempFile=new File(entry.toString());
				if(tempFile.lastModified() > time){
					time = tempFile.lastModified();
					date = new Date(time);
					mostRecentZip = entry;					
				};
			};
		}catch (IOException e) {
			System.out.println(e + ": did not find zip file in the last modified directory");			
		     }
		
		//handles case where there is no zip file in the last modified directory
			if(mostRecentZip == null){
				return dir;
			}
			//replace print statement with test
			System.out.println(mostRecentZip.toString());
		
		//unzip to new directory
			UnzipUtility unzipper = new UnzipUtility();			
			String destDir = dir.toString() + File.separatorChar + "submissions" + (new SimpleDateFormat("MM-dd-yy").format(date));
			try {
				unzipper.unzip(mostRecentZip.toString(), destDir.toString());
			} catch (IOException e) {
				System.out.println("Could not unzip files");
				e.printStackTrace();
			}
			
		return Paths.get(destDir);
	}
	

}
