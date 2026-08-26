import clients.OrderClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.greaterThan;

public class OrderListTest {

    private OrderClient orderClient;

    @BeforeEach
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Возвращается непустой список заказов")
    @Description("Запрос списка заказов возвращает код 200 и непустой список orders")
    public void ordersListIsNotEmpty() {
        orderClient.getOrdersList()
                .assertThat()
                .statusCode(200)
                .body("orders.size()", greaterThan(0));
    }
}