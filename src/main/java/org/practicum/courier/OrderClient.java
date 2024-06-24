package ru.practicum.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Create order")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Get orders")
    public ValidatableResponse getOrders() {
        return  given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders")
                .then();
    }
}
