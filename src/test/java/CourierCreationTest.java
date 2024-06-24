import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.courier.CourierClient;
import ru.practicum.model.Courier;

import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.courier.CourierGenerator.*;

public class CourierCreationTest {
    private static  final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private ValidatableResponse response;
    Courier courier;

    @Before
    public void setUP(){
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера со случайными данными, проверка ответа и его кода")
    public void createCourierExpects201(){
        courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        response.assertThat().statusCode(201)
                .assertThat().body("ok", equalTo(true));

        courierClient.extractId(courier);
    }


    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать двух одинаковых курьеров, проверка ответа и его кода")
    public void createDoubleCourierExpects409(){
        courier = randomCourier();

        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        ValidatableResponse response2 = courierClient.create(courier);

        response2.assertThat().statusCode(409)
                .assertThat().body("code", equalTo(409))
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что нельзя создать курьера без логина, проверка ответа и его кода")
    public void createCourierWithoutLoginExpects400(){
        Courier courier = withoutLoginCourier();

        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        response.assertThat().statusCode(400)
                .assertThat().body("code", equalTo(400))
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что нельзя создать курьера без пароля, проверка ответа и его кода")
    public void createCourierWithoutPasswordExpects400(){
        Courier courier = withoutPasswordCourier();

        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        response.assertThat().statusCode(400)
                .assertThat().body("code", equalTo(400))
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без имени") // Этот тест упадёт, т.к. без имени можно создать курьера
    @Description("Проверка, что нельзя создать курьера без имени, проверка ответа и его кода")
    public void createCourierWithoutFirstNameExpects400(){
        courier = withoutFirstNameCourier();

        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        response.assertThat().statusCode(400)
                .assertThat().body("code", equalTo(400))
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера c одинаковым логином")
    @Description("Проверка, что нельзя создать курьера c одинаковым логином, проверка ответа и его кода")
    public void createCourierWithSameLoginExpects400(){
        courier = randomCourier();

        CourierClient courierClient = new CourierClient();
        response = courierClient.create(courier);

        Courier courier2 = new Courier().withLogin(courier.getLogin()).withPassword("123").withFirstName("dsf");

        ValidatableResponse response2 = courierClient.create(courier2);

        response2.assertThat().statusCode(409)
                .assertThat().body("code", equalTo(409))
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    public void tearDown() {
        // Удаляем курьера, если он был успешно создан
        if (response.extract().statusCode()==201) {
            CourierClient courierClient = new CourierClient();
            System.out.println("Удаляем курьера № " + courierClient.extractId(courier));
            courierClient.delete(courierClient.extractId(courier));
        }
    }
}
