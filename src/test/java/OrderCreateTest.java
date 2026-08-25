import clients.OrderClient;
import models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest {

    private OrderClient orderClient;

    @BeforeEach
    public void setUp() {
        orderClient = new OrderClient();
    }

    @ParameterizedTest
    @MethodSource("colorData")
    public void orderCanBeCreatedWithDifferentColors(String[] color) {
        Order order = new Order(
                "Иван", "Иванов", "Москва, Тверская 1", "Сокольники",
                "+79001112233", 5, "2026-09-15", "Позвоните заранее", color);

        orderClient.create(order)
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue());
    }

    static Stream<Arguments> colorData() {
        return Stream.of(
                Arguments.of((Object) new String[]{"BLACK"}),
                Arguments.of((Object) new String[]{"GREY"}),
                Arguments.of((Object) new String[]{"BLACK", "GREY"}),
                Arguments.of((Object) new String[]{})
        );
    }
}