import clients.OrderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {

    private OrderClient orderClient;

    @BeforeEach
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void ordersListIsReturned() {
        orderClient.getOrdersList()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}