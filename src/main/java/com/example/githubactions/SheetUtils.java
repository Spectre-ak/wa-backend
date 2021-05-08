package com.example.githubactions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * this class is responsible for account creation, login, update profile and all
 * sort of work at user level
 * 
 * @author upadh
 *
 */
@RestController
public class SheetUtils {

	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "credentialsSheets.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	@PostMapping("/signInGoogle")
	@CrossOrigin({ "http://localhost:3000", "friendly-doodle.azurewebsites.net","https://woay.azurewebsites.net/" })
	public synchronized String signInGoogleAndAccCreate(
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "profilePicLink", defaultValue = "") String profilePicLink,
			@RequestParam(name = "type", defaultValue = "") String type,
			@RequestParam(name = "pass", defaultValue = "") String pass,

			HttpServletRequest request, HttpServletResponse response) {

		System.out.println(email);
		System.out.println(name);
		System.out.println(profilePicLink);
		System.out.println(type);
		System.out.println(pass);
		
		//if(true)
		//	return "testing";
		
		if (isCookiePresent(request))
			return "cookie_present";

		if (email.equals(""))
			return "empty email";
		else if (type.equals(""))
			return "type not found";

		String result[]=new String[2];
		if (type.equals("gsign"))
			result = InsertNewUser(email, name, profilePicLink, pass, type);
		else if (type.equals("accCreate")) {
			result = InsertNewUser(email, name, profilePicLink, pass, type);
		} else if (type.equals("defLogin")) {
			result = InsertNewUser(email, name, profilePicLink, pass, type);
		}
		
		System.out.println("result is "+Arrays.toString(result));

		if (result[0].equals("email_present_gsign") || 
				result[0].equals("new_user_added")
				|| result[0].equals("user_valid")) {
			// email already present so log in the user and return id
			
			return result[1];
		}
		else
			return result[0];
	}

	public synchronized String[] InsertNewUser(String email, String name, String profilePicLink, String password,
			String type) {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			final String spreadsheetId = "1HeohQ3OBDlKtEzop1lV34BVX3WM--tLl_oCuf5Md3hQ";
			final String range = "!A:J";
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();
			ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
			List<List<Object>> values = response.getValues();

			if (type.equals("gsign")) {
				if (values == null || values.isEmpty()) {
					System.out.println("No data found.");
				} else {
					for (List row : values) {
						System.out.println(row);
						if (row.get(1).equals(email)) {
							//return "email_present_gsign,"+row.get(0).toString();
							return new String[] {"email_present_gsign",row.get(0).toString()};
						}
					}
				}
			} 
			else if(type.equals("accCreate")) {
				if (values == null || values.isEmpty()) {
					System.out.println("No data found.");
				} else {
					for (List row : values) {
						System.out.println(row);
						if (row.get(1).equals(email)) {
							return new String[] {"email_in_use","-"};
						}
					}
				}
			}
			else {
				if (values == null || values.isEmpty()) {
					System.out.println("No data found.");
				} else {
					for (List row : values) {
						System.out.println(row);
						if (row.get(1).equals(email) && row.get(2).equals(password)) {
							return new String[] {"user_valid",row.get(0).toString()};
						}
					}
				}
				return new String[] {"user_invalid","-"};
			}

			// new email ==> inserting new row

			String userId=UUID.randomUUID().toString();
			List<List<Object>> userDetails = new ArrayList<>();
			ArrayList<Object> arrayList = new ArrayList<>();
			
			arrayList.add(userId);
			arrayList.add(email);
			arrayList.add(password);// -- no password for google sign in

			arrayList.add(name);
			arrayList.add(profilePicLink);
			JSONArray jsonArray = new JSONArray();
			jsonArray.add("html");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("skills", jsonArray);
			arrayList.add(jsonObject.toJSONString().toString());
			arrayList.add("-");
			arrayList.add("-");
			arrayList.add("-");
			arrayList.add("-");
			arrayList.add("-");
			

			userDetails.add(arrayList);

			// TODO: Assign values to desired fields of `requestBody`:
			ValueRange requestBody = new ValueRange();
			requestBody.setValues(userDetails);

			Sheets.Spreadsheets.Values.Append request = service.spreadsheets().values().append(spreadsheetId, range,
					requestBody);
			request.setValueInputOption("RAW");
			request.setInsertDataOption("INSERT_ROWS");

			AppendValuesResponse appendValuesResponse = request.execute();
			System.out.println(appendValuesResponse.getUpdates());
			return new String[] {"new_user_added",userId};

		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { e.getMessage()};
		}

	}

	boolean isCookiePresent(HttpServletRequest request) {
		Cookie[] ar = request.getCookies();
		System.out.println("cookie received " + Arrays.toString(ar));
		if (ar != null)
			for (Cookie cookie : ar) {
				// checks if user is logged in or not
				if (cookie.getName().equals("userID"))
					return true;
			}
		return false;
	}
	
	@GetMapping("/testCookie")
	@CrossOrigin(origins = {"http://localhost:3000","http://localhost:44"
		,"https://friendly-doodle.azurewebsites.net"},allowCredentials = "true")
	public String setCookie(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] ar = request.getCookies();
		System.out.println("cookie received " + Arrays.toString(ar));
		if (ar != null)
			for (Cookie cookie : ar) {
				System.out.println(cookie.getName());
			}
		
		Cookie jwtTokenCookie = new Cookie("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t");

	      jwtTokenCookie.setMaxAge(1123424);
	     // jwtTokenCookie.setSecure(true);
	     // jwtTokenCookie.setHttpOnly(true);
	      jwtTokenCookie.setPath("/");
	      //jwtTokenCookie.setDomain("friendly-doodle.azurewebsites.net");
	      response.addCookie(jwtTokenCookie);
	      
	      response.addHeader("Access-Control-Allow-Credentials", "true");
	     // response.addHeader("Access-Control-Allow-Origin", "http://localhost");
	      
		return "added";
	}
	
	@GetMapping("/test22")
	public String testSheet() {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			final String spreadsheetId = "1HeohQ3OBDlKtEzop1lV34BVX3WM--tLl_oCuf5Md3hQ";
			final String range = "!A:J";
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();
			ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
			List<List<Object>> values = response.getValues();
			String reString="";
			for (List row : values) {
				reString+=row.toString();
				System.out.println(row);
			}
			
			return reString;
		}
		catch (Exception e) {
			return e.getMessage();
		}

	}
	
}
