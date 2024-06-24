import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.practicum.courier.OrderClient;
import ru.practicum.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ru.practicum.courier.OrderGenerator.randomOrder;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private String[] color;

    public OrderCreationTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @Parameterized.Parameters
    public static Object[][] order() {
        return new Object[][]{
                {null}, // цвет не выбран
                {null}, // цвет не выбран
                {new String[]{"BLACK"}}, // выбран чёрный цвет
                {new String[]{"GRAY"}}, // выбран серый цвет
                {new String[]{"BLACK, GRAY"}} // Выбраны оба цвета

        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Параметризованное создание заказа, проверка кода ответа и что в ответе есть track номер")
    public void orderCreationExpects201(){

        Order order = randomOrder(color);
        OrderClient orderClient = new OrderClient();

        ValidatableResponse response = orderClient.createOrder(order);

        response.statusCode(201)
                .assertThat().body("track", notNullValue());
    }

}
