package main.java.MapFile;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
	//Canvas course Id for testing
	private int courseId = 21517;
	
//methods
	public void getData(Assignment assign){
		String token = this.getTestToken();
		//url is for testing. Will need to enter at least part of this as an arg from console eventually
		//get list of submissions for an assignment from Canvas
		GenericUrl url = new GenericUrl("https://lindenwood.instructure.com/api/v1/courses/"+ courseId +"/assignments/234979/submissions?per_page=50");
		HttpResponse response;
		String body = "";
		try{
			response = this.executeGet(HTTP_TRANSPORT, JSON_FACTORY, token, url);
			body = this.InputStreamToString(response.getContent());			
		}catch(IOException e){
			System.out.println(e + ": invalid HTTP response for list of submissions");
			}
		//use gson to create an array of student objects
		ArrayList<Students> students = this.createStudentList(body);
		//iterate over student objects
		Iterator<Students> studentsItr = students.iterator();
		while(studentsItr.hasNext()){
			Students student = studentsItr.next();
			
			//iterate over attachments and file text file
			if(student.attachments != null){
				Iterator<Document> docsItr = student.attachments.iterator();
				GenericUrl docUrl = null;
				
				while(docsItr.hasNext()){
					Document document = docsItr.next();
					
					if (document.mime_class.equals("text")){
						docUrl = new GenericUrl(document.url);
						HttpResponse docResponse;
						String docContent = null;
						try {
								HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
							    docResponse = requestFactory.buildGetRequest(docUrl).execute();
							    docContent = this.InputStreamToString(docResponse.getContent());
							} catch (IOException e) {
								System.out.println(e + ": invalid HTTP response to request for student file at" + student.user_id);
							}
							Geography curr = new Geography(docContent, document.filename, student.user_id, student.assignment_id, courseId);
							assign.addToAssignment(curr);
						}
					}
				}
			}
		}

	//OAuth2 - get Canvas test token for my account	
	private String getTestToken(){
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
	
	private String InputStreamToString(InputStream stream){
		Scanner s = new Scanner(stream);
		s.useDelimiter("\\A");
		String string = s.hasNext() ? s.next() : "";
		s.close();
		//check content of response
	    System.out.println(string); 
	    return string;
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
