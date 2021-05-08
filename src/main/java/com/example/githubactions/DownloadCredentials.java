package com.example.githubactions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadCredentials {

	@GetMapping("/downloadRDAMVMYJK9ehfnoXU")
	public String download(@RequestParam(name = "credURL") String credURL,
			@RequestParam(name="tokenURL")String tokenURL) {
		if(checkCred() || checkToken())
			return "alreadySet";
		downloadCred(credURL);
		downloadToken(tokenURL);


		return "done";

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

	void downloadCred(String credURL) {
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
	void downloadToken(String tokenURL) {
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
}
