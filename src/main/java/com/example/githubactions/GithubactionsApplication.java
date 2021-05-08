package com.example.githubactions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GithubactionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubactionsApplication.class, args);
		if(checkCred() || checkToken()) {
			return;
		}
		downloadCred("https://drive.google.com/uc?id=1AJLOiIPVvnty7SVmlIgL5H86955SFv-c&export=download");
		downloadToken("https://drive.google.com/uc?id=1tKgBesShmyq0Y4z8C7-W-Gb3xZ0VDBVA&export=download");
	}
	

	static boolean checkCred() {
		File f=new File("credentialsSheets.json");
		if(f.exists()) {
			return true;
		}
		else return false;
	}
	
	static boolean checkToken() {
		File f=new File("tokens");
		if(f.exists()) {
			return true;
		}
		return false;
	}
	
	
	
	
	static void downloadCred(String credURL) {
		try {

			try (BufferedInputStream inputStream = new BufferedInputStream(
					new URL(credURL).openStream());
					FileOutputStream fileOS = new FileOutputStream("credentialsSheets.json")) {
				byte data[] = new byte[1024];
				int byteContent;
				while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
					fileOS.write(data, 0, byteContent);
				}
			} catch (IOException e) {
				// handles IO exceptions
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		}
		
	}
	static void downloadToken(String tokenURL) {
		try {

			File f=new File("tokens");
			f.mkdir();
			try (BufferedInputStream inputStream = new BufferedInputStream(
					new URL(tokenURL).openStream());
				
					FileOutputStream fileOS = new FileOutputStream("tokens/StoredCredential")) {
				byte data[] = new byte[1024];
				int byteContent;
				while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
					fileOS.write(data, 0, byteContent);
				}
			} catch (IOException e) {
				// handles IO exceptions
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		}
	}
	
	
	
	
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
		return (factory) -> factory.addContextCustomizers(
				(context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
	}

}

/*
 * //
 * https://bug-free-octo-happiness.azurewebsites.net/downloadRDAMVMYJK9ehfnoXU?
 * credURL= tokenURL=
 */



