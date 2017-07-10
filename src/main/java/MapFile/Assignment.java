package main.java.MapFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Assignment {
	//variables
	private ArrayList<Geography> assignArr = new ArrayList<Geography>();
	private Geography key; 
	private String keydir = "";
	
	//METHODS
	//getters
	public ArrayList<Geography> getAssignment(){
		return this.assignArr;
	}
	public Geography getKey(){
		return this.key;
	}
	
	//setters
	public void addToAssignment(Geography student){
		this.assignArr.add(student);
	}
	public void setAssignmentArr(ArrayList<Geography> assignArr){
		this.assignArr = assignArr;
	}
	//constructor
	public Assignment(){
		this.keydir = "";
		this.key = this.makeKey(this.keydir);
	}
	public Assignment(String keydir){
		this.keydir = keydir;
		this.key = makeKey(keydir);
	}
		
	//Makes a key for an assignment
	
	public Geography makeKey(String keydir){
	//if there is no path provided, use default
		Path keypath = null;
		if (keydir.equals("")){
		FileGetData data = new FileGetData();
		keypath = Paths.get((data.getDir().getParent().getParent().toString()+ File.separatorChar + "BBC keys"));
		//this try block finds the last modified file in the dir 
		try 
		{
			DirectoryStream<Path> stream = Files.newDirectoryStream(keypath, "*.{txt}");
			long time = 0;
						
			for(Path entry: stream){
				File tempFile=new File(entry.toString());
				if(tempFile.lastModified() > time){
					time = tempFile.lastModified();
					keypath = entry;					
				};
			};
		}catch (IOException e) {
			System.out.println(e + ": did not find key in the last modified directory");			
		     }
		}
		else{
			keypath = Paths.get(keydir);
		}
		return new Geography(keypath);
	}
	
		
		//grading logic 
	public void grade(){
		Iterator<Geography> itr = assignArr.iterator();
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
			for (int i = 0; i < key.getAnswers().size(); i++){ //loop over key.answers
				if(temp.getAnswers().contains(key.getAnswers().get(i))){ //count correct answers for map
						count ++;
					}
			}
			temp.setGrade((pointsPossible - (int)((key.getAnswers().size() - count)/factor)));
					
			//this is console output. Eventually should be on other side of output interface
			System.out.println(temp.getFileName() + " - " + "count: " + count + "... grade: " + temp.getGrade());
		}
}
/*Thinking I should have users choosing Canvas or file input via CLI
 * may require varargs if I decide to have them put in the filepath*/

public static void main (String[] args){
	//create assignment
	
	//get information from user
	//canvas or file?
	Scanner in = new Scanner(System.in);
	System.out.println("Do you want to get assignment from Canvas or from files?");
	System.out.println("1. All Canvas");
	System.out.println("2. All Files");
	System.out.println("3. Canvas in, file out");
	
	String filepathIn = "";
	String filepathOut = "";
	String keypath = "";
	int choice = 0;
	do{
		choice = in.nextInt();
		
		switch(choice){
			case 1: choice = 1;
				Assignment assign = new Assignment();
				System.out.println("courseID?: ");
				int courseId = in.nextInt();
				System.out.println("assignmentID?: ");
				int assignmentId = in.nextInt();
				System.out.println("key filepath: ");
				String trashyTrash = in.nextLine();
				keypath = in.nextLine();
				while (!keypath.contains(".txt")){
					System.out.println("key filepath must point to a .txt file");
					System.out.println("key filepath: ");
					keypath = in.nextLine();
				}
				CanvasGetData canvasData = new CanvasGetData();
				if(courseId != 0 && assignmentId != 0){ // enter zeros if you want to keep your test values
					canvasData.setCourseId(courseId);
					canvasData.setAssignmentId(assignmentId);
				}
				canvasData.getData(assign);//pass in an assignment
				assign.grade();
				CanvasOutputData canvasOutput = new CanvasOutputData();
				if(courseId != 0 && assignmentId != 0){//enter zeros if you want to keep test values
					canvasOutput.setCourseId(courseId);
				}
				canvasOutput.OutputData(assign);
				break;
				
			case 2: choice = 2;
			int fileChoice = 0;
			do{
				System.out.println("1. use default file settings");
				System.out.println("2. let me designate an assignment filepath, an output filepath, and a key filepath");
				 fileChoice = in.nextInt();
				if(fileChoice < 1 && fileChoice > 2)
				{
					System.out.println("Invalid choice, please enter a valid menu option");
				}
			}while(fileChoice < 0 && fileChoice > 2);
			
				if (fileChoice == 2){
					System.out.println("Filepaths must be entered with slashes (\\) between folders"); 
					System.out.println("assignment filepath: ");
					String handleNewLineChar = in.nextLine();
					filepathIn = in.nextLine();					
					System.out.println(filepathIn);
					System.out.println("output filepath: ");
					filepathOut = in.nextLine();
					System.out.println("key filepath: ");
					keypath = in.nextLine();
					while (!keypath.contains(".txt")){
						System.out.println("key filepath must point to a .txt file");
						System.out.println("key filepath: ");
						keypath = in.nextLine();
					}
				}
				Assignment assign2;
				if(!keypath.equals("")){
					assign2 = new Assignment(keypath);
				}
				else{
					assign2 = new Assignment();
				}
				FileGetData fileData;
				if (!filepathIn.equals("")){
					fileData = new FileGetData(filepathIn);
				}
				else{
				fileData = new FileGetData();
				}
				fileData.getData(assign2);
				assign2.grade();
				FileOutputData fileOutput;
				if(!filepathOut.equals("")){
					fileOutput = new FileOutputData(filepathOut);
				}
				else{
				fileOutput = new FileOutputData();
				}
				fileOutput.OutputData(assign2);				
				break;
			case 3: choice = 3;
				System.out.println("output filepath: ");
				String handleTrashNewLine = in.nextLine();
				filepathOut = in.nextLine();
				System.out.println("key filepath: ");
				keypath = in.nextLine();
				while (!keypath.contains(".txt")){
					System.out.println("key filepath must point to a .txt file");
					System.out.println("key filepath: ");
					keypath = in.nextLine();
				}
				Assignment assign3;
				if(!keypath.equals("")){
					assign3 = new Assignment(keypath);
				}
				else{
					assign3 = new Assignment();
				}
				CanvasGetData mixData = new CanvasGetData();
				mixData.getData(assign3);//pass in an assignment
				assign3.grade();
				FileOutputData mixOutput;
				if(!filepathOut.equals("")){
					mixOutput = new FileOutputData(filepathOut);
				}
				else{
					mixOutput = new FileOutputData();
				}
				mixOutput.OutputData(assign3);
			break;
			default: choice = -1;
			break;
		}
	}while(choice < 1 || choice >  4);
	
}
}