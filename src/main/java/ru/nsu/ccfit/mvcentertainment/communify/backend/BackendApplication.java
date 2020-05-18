package ru.nsu.ccfit.mvcentertainment.communify.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.CustomUserDetails;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.UserDetailsServiceImpl;

@RestController
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@GetMapping("/users/signin")
	public void test() {
		UserDetails user = userDetailsService.loadUserByUsername("test");
		CustomUserDetails userDetails = (CustomUserDetails) user;

	}

}
