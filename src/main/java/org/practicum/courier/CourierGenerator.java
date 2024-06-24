package ru.practicum.courier;

import ru.practicum.model.Courier;
import ru.practicum.utils.Utils;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin(Utils.randomLogin())
                .withPassword(Utils.randomPassword())
                .withFirstName(Utils.randomFirstName());
    }

    public static Courier withoutLoginCourier() {
        return new Courier()
                .withPassword(Utils.randomPassword())
                .withFirstName(Utils.randomFirstName());
    }

    public static Courier withoutPasswordCourier() {
        return new Courier()
                .withLogin(Utils.randomLogin())
                .withFirstName(Utils.randomFirstName());
    }

    public static Courier withoutFirstNameCourier() {
        return new Courier()
                .withLogin(Utils.randomLogin())
                .withPassword(Utils.randomPassword());

    }
}
