package services;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;

import java.util.Map;
import java.util.Random;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;


public class LoginService {

    public static String login(String username, String password, SessionFilter sessionFilter){
        Response re = given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9,ar;q=0.8")
                .header("cache-control", "max-age=0")
                .header("content-type", "application/x-www-form-urlencoded")
              //  .filter(sessionFilter)
               .formParam("username", username)
               .formParam("password", password)
                .when()
                .post("https://parabank.parasoft.com/parabank/login.htm")
                .then().log().all().extract().response();

        return re.getCookie("Cookie");
    }


    public static    Map<String, String> register(String firstName, String lastName, String street, String city, String state, int zipCode
            , int phoneNumber,int  ssn , String username , String password , String confirmedPass , SessionFilter sessionFilter){


        // Make a GET request to retrieve initial cookies
        Response initialResponse = RestAssured.get("https://parabank.parasoft.com/parabank/register.htm");
        Map<String, String> cookies =  initialResponse.getCookies();

        given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("upgrade-insecure-requests", "1")
                .cookies(cookies)
                .formParam("customer.firstName", firstName)
                .formParam("customer.lastName", lastName)
                .formParam("customer.address.street", street)
                .formParam("customer.address.city", city)
                .formParam("customer.address.state", state)
                .formParam("customer.address.zipCode", zipCode)
                .formParam("customer.phoneNumber", phoneNumber)
                .formParam("customer.ssn", ssn)
                .formParam("customer.username", username)
                .formParam("customer.password", password)
                .formParam("repeatedPassword", confirmedPass)
                .filter(sessionFilter)
                .when()
                .post("https://parabank.parasoft.com/parabank/register.htm")
                .then()
                 .log().all()
                .statusCode(200)
                .extract()
                .response();

        return cookies;
    }


    public static String getCustomerId(  Map<String, String> cookie,SessionFilter sessionFilter) {

        String bodyString = given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9,ar;q=0.8")
                .header("cache-control", "max-age=0")
                .header("content-type", "application/x-www-form-urlencoded")
                .cookies(cookie)
                .filter(sessionFilter)
                .log().all()
                .when()
                .get("https://parabank.parasoft.com/parabank/overview.htm")
                .then().log().all().extract().response().body().asString();
        

        String regex = "services_proxy/bank/customers/\\\"\\s\\+\\s(\\d+)\\s\\+\\s\\\"/accounts";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(bodyString);

        // Find and extract the customer ID if the pattern matches
        String customerId = null;
        if (matcher.find()) {
             customerId = matcher.group(1);
            System.out.println("Customer ID: " + customerId);
        } else {
            System.out.println("Customer ID not found.");
        }
        return customerId;
    
    }




    public static String generateSessionId() {
        Random random = new Random();
        StringBuilder sessionIdBuilder = new StringBuilder();

        while (sessionIdBuilder.length() < 32) {
            int randomInt = random.nextInt(16);
            sessionIdBuilder.append(Integer.toHexString(randomInt));
        }

        return sessionIdBuilder.toString().toUpperCase();
    }
}
