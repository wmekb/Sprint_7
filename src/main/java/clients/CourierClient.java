package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Создать курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Залогинить курьера")
    public ValidatableResponse login(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH + "/login")
                .then();
    }

    @Step("Удалить курьера по id")
    public ValidatableResponse delete(int courierId) {
        return given()
                .spec(getSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }
}