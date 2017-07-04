package main.java.MapFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FileOutputData implements OutputData{

	public void OutputData(Assignment assignment) {
		ArrayList<Geography> arr = assignment.getAssignment();
		GregorianCalendar calendar = new GregorianCalendar();
		String date = String.valueOf(calendar.get(Calendar.MONTH)) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String filename = "C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\" + date + ".txt";
		String joinBy = ",";
		String[]headers = {"student", "grade"};
		
		try{
			FileWriter writer = new FileWriter(filename);
			for(String item: headers){
				writer.write(item);
				writer.write(joinBy);
			}
			writer.write("\n");
			for(Geography geography : arr){
				writer.write(geography.getFileName());
				writer.write(joinBy);
				writer.write(String.valueOf(geography.getGrade()));
				writer.write("\n");
			}
			writer.close();
		}catch (IOException e){
			System.out.println("Unable to create file" + e);
		}
	}
	

}
