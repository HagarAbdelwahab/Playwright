package services;

import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class TransferService {


    public static Response transferMoney(String fromAccountId, String toAccountId, Double amount , Map<String, String> cookie, SessionFilter sessionFilter){
        Response response = given()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9,ar;q=0.8")
                .header("cache-control", "max-age=0")
                .header("content-type", "application/x-www-form-urlencoded")
                .cookies(cookie)
                .queryParam("fromAccountId", fromAccountId)
                .queryParam("toAccountId", toAccountId)
                .queryParam("amount", amount)
                .filter(sessionFilter)
                . log().all()
                .when()
                .post("https://parabank.parasoft.com/parabank/services_proxy/bank/transfer");
        return response;

    }
}
