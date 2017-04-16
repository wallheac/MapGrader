package main.java.MapFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class Assignment {
	//variables
	private ArrayList<Geography> assignment = new ArrayList<Geography>();
	private Geography key; 
	
	//METHODS
	//getters
	public ArrayList<Geography> getAssignment(){
		return this.assignment;
	}
	public Geography getKey(){
		return this.key;
	}
	
	//setters
	public void addToAssignment(Geography student){
		this.assignment.add(student);
	}
	public void setAssignmentArr(ArrayList assignment){
		this.assignment = assignment;
	}
		
	//Makes a key for an assignment
	//filename should eventually be variable
	public void makeKey(){
	Path keydir = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\BBC keys\\Awallhermfechtel04-06.txt");
	this.key = new Geography(keydir);
	}
	
		
		//grading logic 
	public void grade(){
		this.makeKey();
		Iterator<Geography> itr = assignment.iterator();
		int count = 0;
		int pointsPossible = 10;
		//calculate how many items are worth one point
		int factor = 0;
		factor = (int) Math.rint((float)key.getAnswers().size()/5);
		
		System.out.println(factor);
		
		while (itr.hasNext())//iterates through list of maps
		{
			Geography temp = itr.next(); //get next map
			count = 0; // reset counter
			for (int i = 0; i < key.getAnswers().size(); i++) //loop over key.answers
			{
				if(temp.getAnswers().contains(key.getAnswers().get(i))) //count correct answers for map
					{
						count ++;
					}
			}
			temp.setGrade((pointsPossible - (int)((key.getAnswers().size() - count)/factor)));
			
		
			//this is console output. Eventually should be on other side of output interface
			System.out.println(temp.getName() + " - " + "count: " + count + "... grade: " + temp.getGrade());
		}
}
/*Thinking I should have users choosing Canvas or file input via CLI
 * may require varargs if I decide to have them put in the filepath*/

public static void main (String[] args){
	//create assignment
	Assignment assignment = new Assignment();
	//get data
	FileGetData data = new FileGetData();
	data.getData(assignment);//pass in an assignment
	//grade it
	assignment.grade();
}
}