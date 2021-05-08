package com.example.githubactions;
import java.io.File;

import com.example.githubactions.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin({ "http://localhost:3000/", "friendly-doodle.azurewebsites.net","https://woay.azurewebsites.net"
		,"https://woay.azurewebsites.net/" , "woay.azurewebsites.net" ,"http://localhost:3000"})
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
	public Set<Object> getAllProjects(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate){
		if(startDate != null && endDate != null)
		{
			return this.dataService.getAllProjectsBetweenDates(startDate, endDate);
		}
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

	@GetMapping("/resources/{id}")
	public Map<String, String> getResourcesById(@PathVariable int id) {
		Map<String, String> foundResource = this.dataService.getResourceById(id);
		if(foundResource.isEmpty()) {
			foundResource.put("Resource " + id, "Does Not Exists");
		}
		return foundResource;
	}

	@PostMapping("recommendations")
	public Map getRecommendations(
			@RequestBody Recommendation recommendation)
			throws Exception{
		return this.dataService.getRecommendations(recommendation);
	}

	@GetMapping("senior")
	public List<Map<String, String>> getSeniorRole() {
		return this.dataService.getResourcesByProperty("Role Level",
				"Senior");
	}

	@GetMapping("mid")
	public List<Map<String, String>> getMidRole() {
		return this.dataService.getResourcesByProperty("Role Level",
				"Mid");
	}

	@GetMapping("junior")
	public List<Map<String, String>> getJuniorRole() {
		return this.dataService.getResourcesByProperty("Role Level",
				"Junior");
	}

	@GetMapping("engr")
	public List<Map<String, String>> getEngineer() {
		return this.dataService.getResourcesByProperty("Role",
				"Engr");
	}

	@GetMapping("ux")
	public List<Map<String, String>> getUX() {
		return this.dataService.getResourcesByProperty("Role",
				"UX");
	}

	@GetMapping("pm")
	public List<Map<String, String>> getPM() {
		return this.dataService.getResourcesByProperty("Role",
				"PM");
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
