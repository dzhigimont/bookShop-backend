package com.zhigimont.bookstore;

import com.zhigimont.bookstore.config.SecurityUtility;
import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.domain.security.Role;
import com.zhigimont.bookstore.domain.security.UserRole;
import com.zhigimont.bookstore.service.UserSecurityService;
import com.zhigimont.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookstoreAngularApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreAngularApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		User user1 = new User();
		user1.setFirstName("Kristina");
		user1.setLastName("Karbovskaya");
		user1.setUsername("karbon");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("user"));
		user1.setEmail("karbovskay@mail.ru");
		Set<UserRole> userRole = new HashSet<>();
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		userRole.add(new UserRole(user1,role));

		userService.createUser(user1,userRole);
		userRole.clear();


		User user2 = new User();
		user2.setFirstName("Dima");
		user2.setLastName("Zhigimont");
		user2.setUsername("admin");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user2.setEmail("12345@mail.ru");
		Role role2 = new Role();
		role2.setRoleId(2);
		role2.setName("ROLE_ADMIN");
		userRole.add(new UserRole(user2,role2));

		userService.createUser(user2,userRole);




	}
}
