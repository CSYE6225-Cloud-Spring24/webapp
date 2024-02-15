package com.webapplication.Webapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.webapplication.Webapp.entity.User;
import com.webapplication.Webapp.repository.UserRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Base64;
import java.util.Base64.Encoder;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebappApplicationTests {

	@Test
	void contextLoads() {
	}

	private static ConfigurableApplicationContext appContext;

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	public static void setUp() {
		RestAssured.baseURI = "http://localhost:8080";
		appContext = SpringApplication.run(WebappApplication.class);
	}

	@AfterAll
	public static void tearDown() {
		appContext.close();
	}

	@Test
	@Order(1)
	void CreateAndValidateAccount() throws Exception {

		User newUser = new User();
		newUser.setUsername("keerthana@gmail.com");
		newUser.setPassword("Keerthana@123");
		newUser.setFirst_name("Keerthana");
		newUser.setLast_name("Mikkili");


		User existingUser = userRepository.findByUsername("keerthana@gmail.com");
		if (existingUser != null) {
			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());

			return;
		}
        
		given()
		.get("/healthz")
		.then()
		.statusCode(HttpStatus.OK.value());
		
		given()
				.contentType(ContentType.JSON)
				.body(newUser)
				.when()
				.post("/v1/user")
				.then()
				.assertThat()
				.statusCode(HttpStatus.CREATED.value());


		given()
				.header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials("keerthana@gmail.com", "Keerthana@123"))
				.when()
				.get("/v1/user/self")
				.then()
				.assertThat()
				.statusCode(HttpStatus.OK.value())
				.body("username", equalTo("keerthana@gmail.com"))
				.body("first_name", equalTo("Keerthana"))
				.body("last_name", equalTo("Mikkili"));


		given()
				.contentType(ContentType.JSON)
				.body(newUser)
				.when()
				.post("/v1/user")
				.then()
				.assertThat()
				.statusCode(HttpStatus.BAD_REQUEST.value());
	}


	@Test
	void IncorrectEmailwhileCreatingUser() {
			User newUser = new User();
			newUser.setUsername("InvalidEmail");
			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());

	}


	@Test
	void IncorrectPasswordwhileCreatingUser() {
			User newUser = new User();
			newUser.setUsername("meghana@gmail.com");
			newUser.setPassword("weak");

			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void IncorrectFirstNamewhileCreatingUser() {
			User newUser = new User();
			newUser.setUsername("meghana@gmail.com");
			newUser.setPassword("Meghana@123");
			newUser.setFirst_name("");
			newUser.setLast_name("Meg");

			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void IncorrectLastNamewhileCreatingUser() {
			User newUser = new User();
			newUser.setUsername("meghana@gmail.com");
			newUser.setPassword("Meghana@123");
			newUser.setFirst_name("Meghana");
			newUser.setLast_name("");

			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void UserCreationWithEmptyFields() {
		User newUser = new User();
			given()
					.contentType(ContentType.JSON)
					.body(newUser)
					.when()
					.post("/v1/user")
					.then()
					.assertThat()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	@Order(2)
	void UpdateAndValidateUser() {
		String username = "keerthana@gmail.com";
		String password = "Keerthana@123";
		String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

		User existingUser = userRepository.findByUsername(username);

		if (existingUser != null) {

			User updatedUser = new User();
			updatedUser.setUsername(existingUser.getUsername()); // Preserve username
			updatedUser.setFirst_name("UpdatedFirstName");
			updatedUser.setLast_name("UpdatedLastName");
			updatedUser.setPassword("NewKeerthana@123");
			System.out.println("Request Body: " + updatedUser.toString());


			given()
					.header("Authorization", "Basic " + credentials)
					.contentType(ContentType.JSON)
					.body(updatedUser)
					.when()
					.put("/v1/user/self")
					.then()
					.log().all()
					.assertThat()
					.statusCode(HttpStatus.NO_CONTENT.value());


			User fetchedUser = userRepository.findByUsername(username);


			assertEquals("UpdatedFirstName", fetchedUser.getFirst_name());
			assertEquals("UpdatedLastName", fetchedUser.getLast_name());
			assertEquals(existingUser.getUsername(), fetchedUser.getUsername());
			assertEquals(existingUser.getAccount_created(), fetchedUser.getAccount_created());
			assertNotEquals(existingUser.getAccount_updated(), fetchedUser.getAccount_updated());

		} else {
			fail("User does not exist for username: " + username);
		}
	}

	@Test
	@Order(3)
	void UpdateUserWithoutFirstName() {
		String username = "keerthana@gmail.com";
		String password = "NewKeerthana@123";
		String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		
		String requestBody = "{\"username\":\"keerthana@gmail.com\", \"first_name\":\"\", \"last_name\":\"UpdatedLastName\", \"password\":\"Updatedpassword\"}";

		given()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.body(requestBody)
				.when()
				.put("/v1/user/self")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("error",equalTo("First Name is mandatory"));
	}

	@Test
	@Order(3)
	void UpdateUserWithoutLastName() {
		String username = "keerthana@gmail.com";
		String password = "NewKeerthana@123";
		String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

		String requestBody = "{\"username\":\"keerthana@gmail.com\",\"first_name\":\"UpdatedFirstName\", \"last_name\":\"\", \"password\":\"Updatedpassword\"}";

		given()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.body(requestBody)
				.when()
				.put("/v1/user/self")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("error",equalTo("Last Name is mandatory"));
	}

	private String getBase64Credentials(String username, String password) {
		String credentials = username + ":" + password;
		byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
		return Base64.getEncoder().encodeToString(credentialsBytes);
	}
}
