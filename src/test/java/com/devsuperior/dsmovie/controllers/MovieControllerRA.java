package com.devsuperior.dsmovie.controllers;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class MovieControllerRA {

    private static final String MOVIES_ENDPOINT = "/movies";

    private RequestSpecification moviesRequestSpec;
    
    private long existingMovieId;

    @BeforeEach
    public void setUp() {
        moviesRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);

        existingMovieId = 1L;
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
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
