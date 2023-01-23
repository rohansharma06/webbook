package com.api.webhook.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class WebhookController {
	
	@Value("${auth.token}")
	private String authToken;
	
	/*------------------------ Default ------------------------ */
	@GetMapping("/")
	public ResponseEntity<String> test(){
		return ResponseEntity.status(HttpStatus.OK).body("API working fine");
	}
	
	/*------------------------ GET ------------------------ */
	@GetMapping("/webhooks")
	public ResponseEntity<String> getRequest(@RequestParam String hub_mode,@RequestParam String hub_challenge,@RequestParam String hub_verify_token) {
		String mode=hub_mode;
		String challange=hub_challenge;
		String token=hub_verify_token;
		
		System.out.println("hub_mode: "+mode);
		System.out.println("hub_challenge: "+challange);
		System.out.println("hub_verify_token: "+token);
		
		if(mode != null && token != null){
	        if(mode.equals("subscribe") && token.equals(authToken)){
	        	return ResponseEntity.status(HttpStatus.OK).body(challange);
	        }else{
	        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	        }

	    }else {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}
	
}
