package main.java.MapFile;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


//TODO
//MAIN
//import my key as a MapFile instance named "key"
//create an arraylist to hold student submissions
	//name of file as index to arraylist
//create an instance of a MapFile for each student
	//name of file as name of instance?
//check array of student answers against array in key

/*TODO
 * MapFile
 * 
 * open file
 * load contents as string
 * parse string for answers 
*/
	

public class MapFile {
	
	//elements
	private String name;
	private ArrayList<String>answers;
	
	//methods
	//constructor - load name and answers	
	public MapFile(Path entry)
	{
		//extract filename
		Path fileName = entry.getFileName();
        this.name = fileName.toString();
        
        System.out.println(name);
        
		String fullFile = null;
		try {
			fullFile = new String(Files.readAllBytes(entry));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//map JSON within string
		
		try{
	    	Map<String, Object> stuff = (new Gson()).fromJson(fullFile, Map.class);
		    Map<String, Object> groups = (Map<String,Object>) stuff.get("groups");
		    Collection<Object> colors = groups.values();
		    
		    //colors is a collection that may contain more than one object
		    //accounting for students who use more than one color
		    ArrayList<String> allAnswers = new ArrayList<String>();
		    Iterator<Object> itr = colors.iterator();
		    
	    	while(itr.hasNext()){
		    	Map<String, Object> oneColor = (Map<String, Object>) itr.next();
		    	ArrayList tempAnswers =(ArrayList<String>) oneColor.get("paths");
		    	allAnswers.addAll(tempAnswers);
	    	}
	    	this.answers = allAnswers;
	    		    
		} catch (NoSuchElementException e){
			e.printStackTrace();
		}
        
        String joined = String.join(", ", answers);
        System.out.println(joined);     
      
	}
	
	
	
public static void main (String[] args){
	
	ArrayList<MapFile> maps = new ArrayList<MapFile>();
		
	//filename should eventually be variable
	Path keydir = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\BBC keys\\Awallhermfechtel03-09.txt");
	MapFile key =  new MapFile(keydir);
	//eventually, the last two folders in the path should be entered by the user
	Path dir = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\bbc canvas\\submissions03-09");
			try 
			{
				DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{txt,json}"); 
			    for (Path entry: stream)
			    {
			    	MapFile curr = new MapFile(entry);
			    	maps.add(curr);
			    }
			}
			catch (IOException e) {
			     System.err.println(e);
			     }
		Iterator<MapFile> itr = maps.iterator();
		int count = 0;
		int pointsPossible = 10;
		//calculate how many items are worth one point
		int factor = 0;
		factor = (int) Math.rint((float)key.answers.size()/5);
		
		System.out.println(factor);
		
		while (itr.hasNext())//iterates through list of maps
		{
			MapFile temp = itr.next(); //get next map
			count = 0; // reset counter
			for (int i = 0; i < key.answers.size(); i++) //loop over key.answers
			{
				
				if(temp.answers.contains(key.answers.get(i))) //count correct answers for map
						{
							count ++;
						}
				
			}
			int grade = pointsPossible - (int)((key.answers.size() - count)/factor);
			System.out.println(temp.name + " - " + "count: " + count + "... grade: " + grade);
			
		}
		
}
}

