import clients.CourierClient;
import io.qameta.allure.Description;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {

    private CourierClient courierClient;
    private int courierId = -1;

    private final String login = "ninja_" + System.currentTimeMillis();
    private final String password = "1234";
    private final String firstName = "saske";

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Курьер может быть создан")
    @Description("Проверка успешного создания курьера: код 201 и тело ok:true")
    public void courierCanBeCreated() {
        Courier courier = new Courier(login, password, firstName);
        courierClient.create(courier)
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Повторное создание курьера с тем же логином возвращает ошибку 409")
    public void createDuplicateCourierReturnsError() {
        Courier courier = new Courier(login, password, firstName);
        courierClient.create(courier).assertThat().statusCode(201);
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.create(courier)
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без обязательного поля")
    @Description("Создание курьера без пароля возвращает ошибку 400")
    public void createCourierWithoutRequiredFieldReturnsError() {
        Courier courier = new Courier(login, "", firstName);
        courierClient.create(courier)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    public void tearDown() {
        if (courierId != -1) {
            courierClient.delete(courierId);
        }
    }
}