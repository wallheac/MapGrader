package main.java.MapFile;


import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CanvasGetData implements GetData {

	private final Path DATA_STORE_DIR = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\BBC keys\\canvasToken.txt");
	//global instance of the HTTP Transport
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	/** Global instance of the JSON factory. */
	static final JsonFactory JSON_FACTORY = new GsonFactory();
	
//methods
	public void getData(Assignment assign){
		String token = this.getTestToken();
		//url is for testing. Will need to enter at least part of this as an arg from console eventually
		GenericUrl url = new GenericUrl("https://lindenwood.instructure.com/api/v1/courses/21517/assignments/234979/submissions?per_page=50");
		HttpResponse response;
		String body = "";
		try{
			response = this.executeGet(HTTP_TRANSPORT, JSON_FACTORY, token, url);
			
			//response toString
			Scanner s = new Scanner(response.getContent());
			s.useDelimiter("\\A");
			body = s.hasNext() ? s.next() : "";
			s.close();
			//check content of response
		    System.out.println(body);   
			
		}catch(IOException e){
			System.out.println(e + ": invalid HTTP response");
			}
		
		ArrayList<Students> students = this.createStudentList(body);
		Iterator<Students> studentsItr = students.iterator();
		while(studentsItr.hasNext()){
			Students student = studentsItr.next();
			Iterator<Document> docsItr = student.attachments.iterator();
			GenericUrl docUrl = null;
			while(docsItr.hasNext()){
				Document document = docsItr.next();
				if (document.mime_class == "text"){
					docUrl = new GenericUrl(document.mime_class);
				}
			}
		}
		
	  }
	
				
		//request assignments from the Canvas API ,<lindenwood.instructure.com> - this will return a JSON object
		
		//get user_id - this is not in Geography class currently
		//get filename - this is Geography.name
		//get late status (boolean)
		//get URL for text file	
		//download text file - pass Path to Geography constructor
		//add Geography to assignmentArr
		//assign.addToAssignment(/*Geography*/);
		
	private String getTestToken(){
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
			return token;
	}
	//modified (VERY slightly) from: https://developers.google.com/api-client-library/java/google-oauth-java-client/oauth2
	private HttpResponse executeGet(
		      HttpTransport transport, JsonFactory jsonFactory, String token, GenericUrl url)
		      throws IOException {
		    Credential credential =
		        new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(token);
		    HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
		    return requestFactory.buildGetRequest(url).execute();
		  }
	
			//parse JSON - modified from https://github.com/google/gson/blob/master/extras/src/main/java/com/google/gson/extras/examples/rawcollections/RawCollectionsExample.java
			//also used: https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects
	private ArrayList<Students> createStudentList(String body){
		Gson gson = new Gson();
		Type studentListType = new TypeToken<ArrayList<Students>>(){}.getType();
		ArrayList<Students> students = gson.fromJson(body, studentListType);
		return students;
	}
	
	
}
