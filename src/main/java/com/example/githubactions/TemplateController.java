package com.example.githubactions;
import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateController {

		@RequestMapping("/")
	public String index() {
		if(checkCred() || checkToken()) {
			return "downloaded";
		}
		
		return "application satarted";
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
