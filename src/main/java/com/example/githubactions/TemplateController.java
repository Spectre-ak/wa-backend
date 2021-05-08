package com.example.githubactions;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TemplateController {

	private DataService dataService;

	@Autowired
	public TemplateController(DataService dataService){
		this.dataService = dataService;
	}

	@RequestMapping("/")
	public String index() {
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
	public Map<String, String> getProjectsById(@PathVariable int id) throws Exception {
		return this.dataService.getProjectsById(id);
	}





}
