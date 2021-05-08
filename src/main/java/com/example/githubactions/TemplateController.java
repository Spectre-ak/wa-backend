package com.example.githubactions;
import java.io.File;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin({ "http://localhost:3000/", "friendly-doodle.azurewebsites.net","https://woay.azurewebsites.net/"
		,"https://woay.azurewebsites.net/" , "woay.azurewebsites.net" })
@RestController
public class TemplateController {

	private DataService dataService;

	@Autowired
	public TemplateController(DataService dataService){
		this.dataService = dataService;
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

	@GetMapping("/projects/{id}")
	public List getProjectsById(@PathVariable int id) throws Exception {
		return this.dataService.getProjectsById(id);
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
