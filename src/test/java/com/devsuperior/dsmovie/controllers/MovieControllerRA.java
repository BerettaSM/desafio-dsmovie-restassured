package com.devsuperior.dsmovie.controllers;

import static org.hamcrest.Matchers.greaterThan;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class MovieControllerRA {
	
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
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {		
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
