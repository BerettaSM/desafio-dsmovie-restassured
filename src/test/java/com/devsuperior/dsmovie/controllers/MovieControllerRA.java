package com.devsuperior.dsmovie.controllers;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.devsuperior.dsmovie.tests.JsonUtil.createJsonBody;
import com.devsuperior.dsmovie.tests.TokenUtil;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class MovieControllerRA {

    private static final String MOVIES_ENDPOINT = "/movies";

    private RequestSpecification moviesRequestSpec;
    private RequestSpecification moviesAsAdminRequestSpec;
    private RequestSpecification moviesAsAdminWithInvalidTokenRequestSpec;
    private RequestSpecification moviesAsClientRequestSpec;
    
    private long existingMovieId, nonExistingMovieId;

    private static String adminUsername;
    private static String adminPassword;
    private static String adminToken;
    private static String invalidAdminToken;

    private static String clientUsername;
    private static String clientPassword;
    private static String clientToken;

    @BeforeAll
    public static void init() throws JSONException {
        baseURI = "http://localhost:8080";

        adminUsername = "maria@gmail.com";
        adminPassword = "123456";
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidAdminToken = adminToken + "abc123";

        clientUsername = "alex@gmail.com";
        clientPassword = "123456";
        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
    }

    @BeforeEach
    public void setUp() {
        moviesRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);
        
        moviesAsAdminRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + adminToken);

        moviesAsClientRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + clientToken);

        moviesAsAdminWithInvalidTokenRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + invalidAdminToken);

        existingMovieId = 1L;
        nonExistingMovieId = 99L;
    }
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        moviesRequestSpec
            .when()
                .get(MOVIES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.findAll.size()", greaterThan(0));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        moviesRequestSpec
            .when()
                .param("title", "star")
                .get(MOVIES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.findAll.size()", equalTo(3));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
        moviesRequestSpec
            .when()
                .get(MOVIES_ENDPOINT + "/{id}", existingMovieId)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", not(emptyArray()));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
        moviesRequestSpec
            .when()
                .get(MOVIES_ENDPOINT + "/{id}", nonExistingMovieId)
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
        moviesAsAdminRequestSpec
            .when()
                .body(
                    createJsonBody(
                        "title", "",
                        "score", 0.0,
                        "count", 0,
                        "image", "https://bogusurl.com/"))
                .post(MOVIES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value());
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
        moviesAsClientRequestSpec
            .when()
                .body(
                    createJsonBody(
                        "title", "Some bogus title",
                        "score", 0.0,
                        "count", 0,
                        "image", "https://bogusurl.com/"))
                .post(MOVIES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        moviesAsAdminWithInvalidTokenRequestSpec
            .when()
                .body(
                    createJsonBody(
                        "title", "Some bogus title",
                        "score", 0.0,
                        "count", 0,
                        "image", "https://bogusurl.com/"))
                .post(MOVIES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
	}

}
