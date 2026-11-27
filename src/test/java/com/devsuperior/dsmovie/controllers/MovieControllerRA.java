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

public class MovieControllerRA {
    
    private long existingMovieId;

    @BeforeEach
    public void setUp() {
        existingMovieId = 1L;
    }
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/movies")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.findAll.size()", greaterThan(0));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .param("title", "star")
            .get("/movies")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.findAll.size()", equalTo(3));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/movies/{id}", existingMovieId)
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
