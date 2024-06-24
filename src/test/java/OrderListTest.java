import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.practicum.courier.OrderClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class OrderListTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Получение списка всех заказов")
    @Description("Получение списка всех заказов, проверка кода ответа и что в ответе есть список заказов, который больше 0")
    public void getOrderListExpects200() {

        OrderClient orderClient = new OrderClient();

        ValidatableResponse response = orderClient.getOrders();

        response.assertThat().statusCode(200)
                .assertThat().body("orders", hasSize(greaterThan(0)));
    }
}
