package com.api.webhook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

/*
 * @Author Rohan_Sharma
*/

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class WebhookController {

	@Autowired
	private Gson gson = new Gson();

	@Value("${auth.token}")
	private String authToken;

	/*------------------------ Default ------------------------ */
	@GetMapping("/")
	public ResponseEntity<String> test() {
		return ResponseEntity.status(HttpStatus.OK).body("API working fine");
	}

	/*------------------------ GET ------------------------ */
	@GetMapping("/webhooks")
	public ResponseEntity<Integer> getRequest(@RequestParam(name = "hub.mode") String hub_mode,
			@RequestParam(name = "hub.challenge") int hub_challenge,
			@RequestParam(name = "hub.verify_token") String hub_verify_token) {
		String mode = hub_mode;
		int challange = hub_challenge;
		String token = hub_verify_token;

		System.out.println("hub_mode: " + mode);
		System.out.println("hub_challenge: " + challange);
		System.out.println("hub_verify_token: " + token);

		if (mode != null && token != null) {
			if (mode.equals("subscribe") && token.equals(authToken)) {
				return ResponseEntity.status(HttpStatus.OK).body(challange);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	/*------------------------ POST ------------------------ */
	@PostMapping("/webhooks")
	public ResponseEntity<String> postRequest(@RequestBody String jsonStringReq) {
		System.out.println("body:"+gson.toJson(jsonStringReq));
		String api= "http://34.214.61.86:6006/webhooks";
		HttpEntity<String> entity = new HttpEntity<String>(jsonStringReq);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(api, HttpMethod.POST, entity,Object.class);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
