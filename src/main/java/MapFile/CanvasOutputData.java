package main.java.MapFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class CanvasOutputData implements OutputData{

	private final Path DATA_STORE_DIR = Paths.get("C:\\Users\\amy\\Documents\\W CIV\\Lindenwood\\Sp 17\\BBC keys\\canvasToken.txt");
	//global instance of the HTTP Transport
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	/** Global instance of the JSON factory. */
	static final JsonFactory JSON_FACTORY = new GsonFactory();
	//Canvas course Id for testing
	private int courseId = 21517;
	
	public void OutputData(Assignment assignment) {
		String token = this.getTestToken();
		//url is for testing. Will need to enter at least part of this as an arg from console eventually
		GenericUrl url = new GenericUrl("https://lindenwood.instructure.com/api/v1/courses/"+ courseId +"/assignments/234979/submissions/update_grades");
		HttpResponse response;
		String body = "";
		try{
			response = this.executePost(assignment, HTTP_TRANSPORT, JSON_FACTORY, token, url);
					
		}catch(IOException e){
			System.out.println(e + ": invalid HTTP POST request - failed to post grades");
			}
		
	}
	
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
	
	private HttpResponse executePost(Assignment assignment, HttpTransport transport, JsonFactory jsonFactory, String token, GenericUrl url)
		      throws IOException {
		    Credential credential =
		        new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(token);
		    HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
		    //from https://stackoverflow.com/questions/23001661/post-multipart-form-with-google-http-java-client
		    MultipartContent content = new MultipartContent().setMediaType(
		            new HttpMediaType("multipart/form-data")
		                    .setParameter("boundary", "__END_OF_PART__"));
		    for (Geography geography : assignment.getAssignment()){
		    //request params - 'grade_data[3][posted_grade]=88'
		    String idAndGrade = ("grade_data[" + geography.getUserId() + "][posted_grade]=" + geography.getGrade());
		        MultipartContent.Part part = new MultipartContent.Part(
		                new ByteArrayContent(null, idAndGrade.getBytes()));
		        
		        content.addPart(part);
		    }
		    
		    return requestFactory.buildPostRequest(url, content).execute();
		  }

	public void setCourseId(int courseId) {
		this.courseId = courseId;		
	}

}
