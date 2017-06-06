package main.java.MapFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


import com.google.gson.Gson;

public class Geography {
	
	//elements
	private String fileName = null;
	private String fullFile = null;
	private ArrayList<String>answers;
	private int grade;
	private int userId;
	private int assignmentId;
	private int courseId;
	
	//methods
	//setters
	public void setGrade(int grade){
		this.grade = grade;
	}
	public void setFileName(String name){
		this.fileName = name;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}
	public void setAssignmentId(int assignmentId){
		this.assignmentId = assignmentId;
	}
	public void setCourseId(int courseId){
		this.courseId = courseId;
	}
	
	//getters
	public ArrayList<String> getAnswers(){
		return this.answers;		
	}
	public String getFileName(){
		return this.fileName;
	}
	public int getGrade(){
		return this.grade;
	}
	public int getUserId(){
		return this.userId;
	}
	public int getAssignmentId(){
		return this.assignmentId;
	}
	public int getCourseId(){
		return this.courseId;
	}
	
	//constructor - load fileName and answers	
	public Geography(Path entry)
	{
		//extract filename
		this.fileName = entry.getFileName().toString();
        this.fullFile = this.fileToString(entry); 
        this.answers = this.mapJson(fullFile);
        
	}
	public Geography(String fileContent, String filename, int userId, int assignmentId, int courseId){
		this.fullFile = fileContent; 
		this.fileName = filename;
        this.answers = this.mapJson(fullFile);
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.courseId = courseId;
	}
	
	private String fileToString(Path entry){
	//this should be a method fileToString - setter?
		String file = null;
			try {
				file = new String(Files.readAllBytes(entry));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
	}
	
	//map JSON within string
	@SuppressWarnings("unchecked")
	private ArrayList<String> mapJson(String fullFile){	
		ArrayList<String>allAnswers = new ArrayList<String>();
			try{
		    	Map<String, Object> stuff = (new Gson()).fromJson(fullFile, Map.class);
			    Map<String, Object> groups = (Map<String,Object>) stuff.get("groups");
			    Collection<Object> colors = groups.values();
			    //colors is a collection that may contain more than one object
			    //accounting for students who use more than one color
			    ArrayList<String> tempAnswers = new ArrayList<String>();
			    Iterator<Object> itr = colors.iterator();
			    
		    	while(itr.hasNext()){
			    	Map<String, Object> oneColor = (Map<String, Object>) itr.next();
			    	tempAnswers =(ArrayList<String>) oneColor.get("paths");
			    	allAnswers.addAll(tempAnswers);
		    	}
		    			    		    
			} catch (NoSuchElementException e){
				e.printStackTrace();
			}
			return allAnswers;
	}
}

