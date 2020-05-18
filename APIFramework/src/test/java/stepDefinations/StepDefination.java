package stepDefinations;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

public class StepDefination extends Utils{

	RequestSpecification res;
	ResponseSpecification rs;
	Response response;
	JsonPath js;
	static String place_id;
	
	TestDataBuild data = new TestDataBuild();
	
	

	@Given("Add Place payload with {string} {string} {string}")
	public void add_Place_payload_with(String name, String language, String address) throws IOException {
		
		res = given().spec(requestSpecification())
		.body(data.addPlacePayload(name,language,address));
	
	}

	@When("User calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method) {
		
		APIResources resouceAPI = APIResources.valueOf(resource);			//value of: invoke the constructor in APIResouce class
		System.out.println(resouceAPI.getResource());
		rs = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		if(method.equalsIgnoreCase("POST")) {
		response= res.when().post(resouceAPI.getResource());
		}
		else if(method.equalsIgnoreCase("GET")){
			response= res.when().get(resouceAPI.getResource());
		}
		
	}

	@Then("The API call got success with status code {int}")
	public void the_API_call_got_success_with_status_code(Integer int1) {
	   assertEquals(response.getStatusCode(),200);
	}

	 @Then("{string} in response body is {string}")
	    public void in_response_body_is(String keyValue, String expectedValue) throws Throwable {
	       
	        assertEquals(getJsonPath(response, keyValue).toString(),expectedValue);
	    }
	 
	 @Then("verify place_ID created maps to {string} using {string}")
	 public void verify_place_ID_created_maps_to_using(String expectedName, String resource) throws IOException {
	   //requestSpec
		place_id = getJsonPath(response, "place_id");
		 res = given().spec(requestSpecification()).queryParam("place_id", place_id);
		 
		 user_calls_with_http_request(resource,"GET");
		 String actualName = getJsonPath(response, "name");
		 assertEquals(actualName,expectedName);
				 			 
	 }

	 @Given("DeletePlace Payload")
	 public void deleteplace_Payload() throws IOException {
		res = given().spec(requestSpecification())
		 .body(data.deletePlacePayload(place_id));
	 }

	
}
