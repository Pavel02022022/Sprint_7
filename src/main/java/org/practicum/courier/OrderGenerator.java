package ru.practicum.courier;

import ru.practicum.model.Order;

import static ru.practicum.utils.Utils.*;

public class OrderGenerator {

    public static Order randomOrder(String[] color) {
        return new Order()
                .withFirstName(randomFirstName())
                .withLastName(randomLastName())
                .withAddress(randomAddress())
                .withMetroStation(randomMetroStation())
                .withPhone(randomPhone())
                .withRentTime(randomRentTime())
                .withDeliveryDate(randomDeliveryDate())
                .withComment(randomComment())
                .withColor(color);
    }

}
