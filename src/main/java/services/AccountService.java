package services;

import io.restassured.filter.session.SessionFilter;

import io.restassured.response.Response;
import utils.JSONExtractor;

import static io.restassured.RestAssured.given;


import java.util.Map;
public class AccountService {

    public static String openAccount(String customerId, String fromAccountId,  Map<String, String> cookie, SessionFilter sessionFilter){
        Response re = given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9,ar;q=0.8")
                .header("cache-control", "max-age=0")
                .header("content-type", "application/x-www-form-urlencoded")
                .cookies(cookie)
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", 0)
                .queryParam("fromAccountId", fromAccountId)
                 .filter(sessionFilter)
                . log().all()
                .when()
                .post("https://parabank.parasoft.com/parabank/services_proxy/bank/createAccount");

        String newAccountID= JSONExtractor.getStringFromJson(re,"id");

        return newAccountID;

    }


    public static Response getAccounts(String customerId, Map<String, String> cookie, SessionFilter sessionFilter){
        Response response = given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9,ar;q=0.8")
                .header("cache-control", "max-age=0")
                .header("content-type", "application/x-www-form-urlencoded")
                .cookies(cookie)
                .filter(sessionFilter)
                .log().all()
                .when()
                .get("https://parabank.parasoft.com/parabank/services_proxy/bank/customers/"+customerId+"/accounts");
        System.out.println("adadada"+response.getBody().prettyPrint());
        return response;

    }



}
