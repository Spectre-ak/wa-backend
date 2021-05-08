package com.example.githubactions;

import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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

@CrossOrigin({ "http://localhost:3000", "friendly-doodle.azurewebsites.net",
	"https://woay.azurewebsites.net/"
	,"https://woay.azurewebsites.net/" , "woay.azurewebsites.net" })
@RestController
public class RecommendationsUtil {

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


//	//@PostMapping("getRecommendations")
//	public String getRecommendations(
//			@RequestParam(value = "skills",defaultValue = "")String skills,
//			@RequestParam(value = "resourcesDemand",defaultValue = "")String resourcesDemand,
//			@RequestParam(value = "date",defaultValue ="")String date){
//
//		System.out.println(skills);
//		System.out.println(resourcesDemand);System.out.println(date);
//
//		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
//		long time=0;
//		try {
//			Date date2=simpleDateFormat.parse(date);
//			System.out.println(date2.getTime());
//			time=date2.getTime();
//
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//JSONObject jsonObject = new JSONObject(resourcesDemands);
//
//		JSONArray jsonArray=new JSONArray(skills);
//
//		ArrayList skillsList=(ArrayList) jsonArray.toList();
//		System.out.println(skillsList);
//
//
//		JSONArray roleDemandsArray=new JSONArray(resourcesDemand);
//
//		for(Object obj:roleDemandsArray) {
//			JSONObject jsonObject=(JSONObject) obj;
//			System.out.println(jsonObject.get("junior"));
//		}
//
//
//		try {
//			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			//
//			//final String spreadsheetId = "1-FQd-uYzRgZTYovEjUa9VbxubPK6S-xILGFMnXTAIT4";
//			final String spreadsheetId = "1PkATem4Il_rVosE35tpwTPgydf_Ni1wtqfPFN-Zg6x0";//--> deployed accnt sheet id
//			final String range = "!A:AB";
//			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//					.setApplicationName(APPLICATION_NAME).build();
//			ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
//			List<List<Object>> values = response.getValues();
//
//			HashMap<String,List<List<Object>>> hm=getEngr(values);
//
//			List<String> recommendations=new ArrayList<>();
//			for(Object obj:roleDemandsArray) {
//				//each iteration is a resource
//				JSONObject jsonObject=(JSONObject) obj;
//				boolean junior=false,senior=false,mid=false,engr=false
//						,ux=false,pm=false;
//				//engr = IL
//				//ux = TX
//				//pm = AZ
//				if(jsonObject.getBoolean("junior")) {
//					if(jsonObject.getBoolean("engr")) {
//
//						List<List<Object>> locationsList=new ArrayList<>();
//
//						for(List<Object> loc:hm.get("junior")) {
//							if(loc.get(18).equals("IL"))
//								locationsList.add(loc);
//						}
//						if(time==0) {
//							for(List<Object> loc:locationsList) {
//								if(recommendations.contains(loc.get(0).toString()))
//									continue;
//								recommendations.add(loc.get(0).toString());
//								break;
//							}
//						}
//						else {
//							long diffTillNow=Long.MAX_VALUE;
//							String closestTimeResource="";
//							for(List<Object> loc:locationsList) {
//								try {
//									if(recommendations.contains(loc.get(0).toString()))
//										continue;
//									String productEndDate1=loc.get(7).toString();
//									String ar2[]=productEndDate1.split("/");
//
//									System.out.println(productEndDate1);
//
//									ar2[2]="2021";
//									String productEndDat=ar2[0]+"-"+ar2[1]+"-"+ar2[2];
//
//									System.out.println(productEndDat);
//									System.out.println(loc.get(0).toString());
//									SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("mm-dd-yyyy");
//									long currTime=0;
//									try {
//										Date date2=simpleDateFormat2.parse(productEndDat);
//										currTime=date2.getTime();
//										System.out.println(currTime);
//										System.out.println(time);
//
//										long diff=0;
//										if(currTime>=time)
//											diff=currTime-time;
//										else
//											diff=time-currTime;
//
//
//
//										System.out.println(diff);
//
//										if(diffTillNow>diff) {
//											diffTillNow=diff;
//											closestTimeResource=loc.get(0).toString();
//										}
//
//										System.out.println();
//
//									} catch (ParseException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//								} catch (Exception e) {
//									System.out.println("OuterDivErr");
//									//e.printStackTrace();
//									continue;
//								}
//							}
//							if(closestTimeResource!="")
//								recommendations.add(closestTimeResource);
//
//							System.out.println(recommendations);
//						}
//
//					}
//					if(jsonObject.getBoolean("ux")) {
//
//					}
//					if(jsonObject.getBoolean("pm")) {
//
//					}
//				}
//				else if(jsonObject.getBoolean("mid")) {
//					if(jsonObject.getBoolean("engr")) {
//
//					}
//					if(jsonObject.getBoolean("ux")) {
//
//					}
//					if(jsonObject.getBoolean("pm")) {
//
//					}
//				}
//				else if(jsonObject.getBoolean("senior")) {
//					if(jsonObject.getBoolean("engr")) {
//
//					}
//					if(jsonObject.getBoolean("ux")) {
//
//					}
//					if(jsonObject.getBoolean("pm")) {
//
//					}
//				}
//
//
//			}
//
//
//			for(String key:hm.keySet()) {
//				//System.out.println((key));
//				for(List list:hm.get(key)) {
//					//System.out.println(list);
//				}
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//
//
//
//		return null;
//	}
//
//
//
//	static HashMap<String,List<List<Object>>> getEngr(List<List<Object>> values){
//		HashMap<String,List<List<Object>>> hm=new HashMap<>();
//		hm.put("engr",new ArrayList<>());
//		hm.put("ux",new ArrayList<>());
//		hm.put("pm",new ArrayList<>());
//		hm.put("junior",new ArrayList<>());
//		hm.put("mid",new ArrayList<>());
//		hm.put("senior",new ArrayList<>());
//
//		for(List<Object> row:values) {
//			//System.out.println(row);
//			try {
//				if(row.get(2).toString().toLowerCase().equals("engr")) {
//					List<List<Object>> list=hm.get("engr");
//					list.add(row);
//					hm.put("engr",list);
//				}
//				else if(row.get(2).toString().toLowerCase().equals("ux")) {
//					List<List<Object>> list=hm.get("ux");
//					list.add(row);
//					hm.put("ux",list);
//				}
//				else if(row.get(2).toString().toLowerCase().equals("pm")) {
//					List<List<Object>> list=hm.get("pm");
//					list.add(row);
//					hm.put("pm",list);
//				}
//
//				if(row.get(3).toString().toLowerCase().equals("junior")) {
//					List<List<Object>> list=hm.get("junior");
//					list.add(row);
//					hm.put("junior",list);
//				}else if(row.get(3).toString().toLowerCase().equals("mid")) {
//					List<List<Object>> list=hm.get("mid");
//					list.add(row);
//					hm.put("mid",list);
//				}else if(row.get(3).toString().toLowerCase().equals("senior")) {
//					List<List<Object>> list=hm.get("senior");
//					list.add(row);
//					hm.put("senior",list);
//				}
//
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		return hm;
//	}

}
