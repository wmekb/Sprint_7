import clients.CourierClient;
import io.qameta.allure.Description;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    private CourierClient courierClient;
    private int courierId = -1;

    private final String login = "ninja_" + System.currentTimeMillis();
    private final String password = "1234";
    private final String firstName = "saske";

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
        courierClient.create(new Courier(login, password, firstName));
    }

    @Test
    @DisplayName("Курьер может авторизоваться и получить id")
    @Description("Успешная авторизация возвращает код 200 и тело с id курьера")
    public void courierCanLoginAndGetId() {
        courierId = courierClient.login(new Courier(login, password))
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Ошибка при неверном пароле")
    @Description("Авторизация с неверным паролем возвращает ошибку 404")
    public void loginWithWrongPasswordReturnsError() {
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.login(new Courier(login, "wrongPassword"))
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при отсутствии обязательного поля")
    @Description("Авторизация без пароля возвращает ошибку 400")
    public void loginWithoutPasswordReturnsError() {
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.login(new Courier(login, ""))
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации несуществующего пользователя")
    @Description("Авторизация под несуществующим логином возвращает ошибку 404")
    public void loginWithNonexistentUserReturnsError() {
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.login(new Courier("nonexistent_" + System.currentTimeMillis(), password))
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterEach
    public void tearDown() {
        if (courierId != -1) {
            courierClient.delete(courierId);
        }
    }
}