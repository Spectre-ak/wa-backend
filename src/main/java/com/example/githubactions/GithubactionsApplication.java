package com.example.githubactions;

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
	}
	
	@Bean
	WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomi1zer() {
	    return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {

	        @Override
			public
	        void customize(TomcatServletWebServerFactory tomcatServletWebServerFactory) {
	            tomcatServletWebServerFactory.addContextCustomizers(new TomcatContextCustomizer() {
	                @Override
	                public void customize(Context context) {
	                    context.setCookieProcessor(new LegacyCookieProcessor());
	                }

					
	            });
	        }
	    };
	}
	
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
		return (factory) -> factory.addContextCustomizers(
				(context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
	}

}

//  https://bug-free-octo-happiness.azurewebsites.net/downloadRDAMVMYJK9ehfnoXU?credURL=https://drive.google.com/uc?id=1AJLOiIPVvnty7SVmlIgL5H86955SFv-c&export=download&tokenURL=https://drive.google.com/uc?id=1tKgBesShmyq0Y4z8C7-W-Gb3xZ0VDBVA&export=download



