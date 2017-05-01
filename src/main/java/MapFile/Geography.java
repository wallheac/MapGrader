package main.java.MapFile;
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

public class Geography {
	
	//elements
	private String name;
	private ArrayList<String>answers;
	private int grade;
	
	//methods
	//setters
	public void setGrade(int grade){
		this.grade = grade;
	}
	public void setName(String name){
		this.name = name;
	}
	
	//getters
	public ArrayList<String> getAnswers(){
		return this.answers;		
	}
	public String getName(){
		return this.name;
	}
	public int getGrade(){
		return this.grade;
	}
	
	//constructor - load name and answers	
	public Geography(Path entry)
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
        
        //String joined = String.join(", ", answers);
        //System.out.println(joined);     
      
	}
	
}

