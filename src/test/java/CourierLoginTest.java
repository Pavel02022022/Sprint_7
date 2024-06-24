import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.practicum.courier.CourierClient;
import ru.practicum.model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static ru.practicum.courier.CourierGenerator.randomCourier;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private ValidatableResponse response;

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Логин курьера, проверка кода ответа и что в ответе есть id")
    public void loginCourierExpects200(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

        response = courierClient.login(courier.getLogin(), courier.getPassword());

        response.assertThat().statusCode(200)
                .assertThat().body("id", is(notNullValue()));
    }

    @Test
    @DisplayName("Логинация курьера без логина")
    @Description("Логин курьера без логина, проверка ответа и его кода")
    public void loginCourierWithoutLoginExpects400(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

        response = courierClient.login(null, courier.getPassword());

        response.assertThat().statusCode(400);
        response.body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логинация курьера без пароля") //Этот тест точно упадёт, т.к. возвращает код 504
    @Description("Логинация курьера без пароля, проверка ответа и его кода")
    public void loginCourierWithoutPasswordExpects504(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

        response = courierClient.login(courier.getLogin(), null);

        response.assertThat().statusCode(400);
        response.body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логинация курьера с неверным логином")
    @Description("Логинация курьера с неверным логином, проверка ответа и его кода")
    public void loginCourierWithWrongLoginExpects404(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

        response = courierClient.login("fakeLogin", courier.getPassword());

        response.assertThat().statusCode(404);
        response.body("code", equalTo(404))
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логинация курьера с неверным паролем")
    @Description("Логинация курьера с неверным паролем, проверка ответа и его кода")
    public void loginCourierWithWrongPasswordExpects404(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        courierClient.create(courier);

        response = courierClient.login(courier.getLogin(), "fakepassword");

        response.assertThat().statusCode(404);
        response.body("code", equalTo(404))
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Логинация несуществующего курьера, проверка ответа и его кода")
    public void loginFakeCourierExpects404(){
        CourierClient courierClient = new CourierClient();
        response = courierClient.login("fakeCourier1", "fakepassword1");

        response.assertThat().statusCode(404);
        response.body("code", equalTo(404))
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        // Удаляем курьера, если он был успешно создан при запуске теста
        if (response.extract().statusCode()==200) {
            int id = response.extract().path("id");
            System.out.println("Удаляем курьера № "+  id);
            CourierClient courierClient = new CourierClient();
            courierClient.delete(id);
        }
    }
}
