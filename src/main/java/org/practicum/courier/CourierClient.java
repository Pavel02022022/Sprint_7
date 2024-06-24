package ru.practicum.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.practicum.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Create courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("api/v1/courier")
                .then();
    }

    @Step("Login courier")
    public ValidatableResponse login(String login, String password) {

        CourierCreds creds = new CourierCreds(login, password);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post("api/v1/courier/login")
                .then();
    }

    @Step("Delete courier")
    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .pathParam("id", id)
                .and()
                .when()
                .delete("/api/v1/courier/{id}");
    }

    @Step("Extract courier id")
    public int extractId(Courier courier){
        ValidatableResponse response = login(courier.getLogin(), courier.getPassword());
        return response.extract().path("id");
    }

}
