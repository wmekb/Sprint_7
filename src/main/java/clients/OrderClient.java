package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создать заказ")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}