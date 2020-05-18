Feature: Validating Place API's
@AddPlace @Regression
Scenario Outline: Verify if place is being successfully added using AddPlaceAPI
	Given Add Place payload with "<name>" "<language>" "<address>"
	When  User calls "AddPlaceAPI" with "Post" http request
	Then The API call got success with status code 200
	And "status" in response body is "OK"
	And "scope" in response body is "APP"
	And verify place_ID created maps to "<name>" using "getPlaceAPI"
	
Examples:
	|name | language | address|
	|AAA  |	English  | USA	  |
#	|BBB  | Hindi    | UK	  |

@DeletePlace @regression
Scenario: verify if Delete Place functionality is working
	Given DeletePlace Payload
	When User calls "deletePlaceAPI" with "Post" http request
	Then The API call got success with status code 200
	And "status" in response body is "OK"