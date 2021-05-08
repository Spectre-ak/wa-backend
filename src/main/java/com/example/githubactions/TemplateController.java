package com.example.githubactions;
import java.io.File;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

	@PostMapping("recommendations")
	public Map getRecommendations(
			@RequestBody Recommendation recommendation)
			throws Exception{
		return this.dataService.getRecommendations(recommendation);
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
