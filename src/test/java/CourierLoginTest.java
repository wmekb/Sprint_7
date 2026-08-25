import clients.CourierClient;
import models.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
        // создаём курьера для тестов логина
        courierClient.create(new Courier(login, password, firstName));
    }

    @Test
    public void courierCanLogin() {
        courierId = courierClient.login(new Courier(login, password))
                .assertThat()
                .statusCode(200)
                .extract().path("id");
    }

    @Test
    public void loginSuccessReturnsId() {
        courierClient.login(new Courier(login, password))
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.login(new Courier(login, "wrongPassword"))
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginWithoutPasswordReturnsError() {
        courierId = courierClient.login(new Courier(login, password)).extract().path("id");
        courierClient.login(new Courier(login, ""))
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
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