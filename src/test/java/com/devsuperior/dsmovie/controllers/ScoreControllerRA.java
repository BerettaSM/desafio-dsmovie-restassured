package com.devsuperior.dsmovie.controllers;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.devsuperior.dsmovie.tests.TokenUtil;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ScoreControllerRA {
	
    private static final String SCORES_ENDPOINT = "/scores";

    private RequestSpecification scoresRequestSpec;
    private RequestSpecification moviesAsAdminRequestSpec;
    private RequestSpecification moviesAsAdminWithInvalidTokenRequestSpec;
    private RequestSpecification moviesAsClientRequestSpec;
    
    private long existingMovieId, nonExistingMovieId;

    private static String adminUsername;
    private static String adminPassword;
    private static String adminToken;

    @BeforeAll
    public static void init() throws JSONException {
        baseURI = "http://localhost:8080";

        adminUsername = "maria@gmail.com";
        adminPassword = "123456";
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
    }

    @BeforeEach
    public void setUp() {
        scoresRequestSpec = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + adminToken);

        existingMovieId = 1L;
        nonExistingMovieId = 99L;
    }

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
        scoresRequestSpec
            .when()
                .body(
                    createJsonBody(
                        "movieId", nonExistingMovieId,
                        "score", 0))
                .put(SCORES_ENDPOINT)
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}

    private JSONObject createJsonBody(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new RuntimeException("Key without a value");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(String.valueOf(keyValuePairs[i]), keyValuePairs[i + 1]);
        }
        return new JSONObject(map);
    }
}
