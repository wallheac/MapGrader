package main.java.MapFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

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
		GenericUrl url = new GenericUrl("https://lindenwood.instructure.com/api/v1/courses/21517/assignments/234978/submissions");
		try{
		HttpResponse response = this.executeGet(HTTP_TRANSPORT, JSON_FACTORY, token, url);
		}catch(IOException e){
			System.out.println(e + ": invalid HTTP response");
			}
	  }
				
		//request assignments from the Canvas API ,<lindenwood.instructure.com> - this will return a JSON object
		
		//parse returned JSON - probably create a POJO out of it
		
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
	private HttpResponse executeGet(
		      HttpTransport transport, JsonFactory jsonFactory, String token, GenericUrl url)
		      throws IOException {
		    Credential credential =
		        new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(token);
		    HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
		    return requestFactory.buildGetRequest(url).execute();
		  }
	
	
}
