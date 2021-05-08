package com.example.githubactions;
import java.io.File;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin({ "http://localhost:3000/", "friendly-doodle.azurewebsites.net","https://woay.azurewebsites.net/"
		,"https://woay.azurewebsites.net/" , "woay.azurewebsites.net" })
@RestController
public class TemplateController {

	private DataService dataService;

	private SheetUtils sheetUtils;

	@Autowired
	public TemplateController(DataService dataService, SheetUtils sheetUtils){
		this.dataService = dataService;
		this.sheetUtils = sheetUtils;
	}

	@RequestMapping("/")
	public String index() {
		if(checkCred() || checkToken()) {
			return "downloaded";
		}
		return "application satarted";
	}

	@GetMapping("/all")
	public List<Map<String, String>> getAll() {
		return this.dataService.getAll();
	}

	@GetMapping("/projects")
	public Set<Object> getAllProjects(){
		return this.dataService.getAllProjects();
	}

	@GetMapping("/vendors")
	public Set<String> getVendors() {
		 return this.dataService.getVendors();
	}

	@GetMapping("/vendors/{id}")
	public List<Map<String, String>> getVendorsById(@PathVariable int id) {
		return this.dataService.getVendorsById(id);
	}

	@GetMapping("/projects/{id}")
	public List getProjectsById(@PathVariable int id) throws Exception {
		return this.dataService.getProjectsById(id);
	}

	@PostMapping("getRecommendations")
	public String getRecommendations(
			@RequestParam(value = "skills",defaultValue = "")String[] skills,
			@RequestParam(value = "resourcesDemand",defaultValue = "")String resourcesDemand,
			@RequestParam(value = "date",defaultValue ="")String date){
			this.dataService.getRecommendations(skills, resourcesDemand, date);
		return null;
	}



	static HashMap<String,List<List<Object>>> getEngr(List<List<Object>> values){
		HashMap<String,List<List<Object>>> hm=new HashMap<>();
		hm.put("engr",new ArrayList<>());
		hm.put("ux",new ArrayList<>());
		hm.put("pm",new ArrayList<>());
		hm.put("junior",new ArrayList<>());
		hm.put("mid",new ArrayList<>());
		hm.put("senior",new ArrayList<>());

		for(List<Object> row:values) {
			//System.out.println(row);
			try {
				if(row.get(2).toString().toLowerCase().equals("engr")) {
					List<List<Object>> list=hm.get("engr");
					list.add(row);
					hm.put("engr",list);
				}
				else if(row.get(2).toString().toLowerCase().equals("ux")) {
					List<List<Object>> list=hm.get("ux");
					list.add(row);
					hm.put("ux",list);
				}
				else if(row.get(2).toString().toLowerCase().equals("pm")) {
					List<List<Object>> list=hm.get("pm");
					list.add(row);
					hm.put("pm",list);
				}

				if(row.get(3).toString().toLowerCase().equals("junior")) {
					List<List<Object>> list=hm.get("junior");
					list.add(row);
					hm.put("junior",list);
				}else if(row.get(3).toString().toLowerCase().equals("mid")) {
					List<List<Object>> list=hm.get("mid");
					list.add(row);
					hm.put("mid",list);
				}else if(row.get(3).toString().toLowerCase().equals("senior")) {
					List<List<Object>> list=hm.get("senior");
					list.add(row);
					hm.put("senior",list);
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return hm;
	}

	@GetMapping("/resources/{id}")
	public Map<String, String> getResourcesById(@PathVariable int id) {
		Map<String, String> foundResource = this.dataService.getResourceById(id);
		if(foundResource.isEmpty()) {
			foundResource.put("Resource " + id, "Does Not Exists");
		}
			return foundResource;
	}

	boolean checkCred() {
		File f=new File("credentialsSheets.json");
		if(f.exists()) {
			return true;
		}
		else return false;
	}

	boolean checkToken() {
		File f=new File("tokens");
		if(f.exists()) {
			return true;
		}
		else return false;
	}

}
