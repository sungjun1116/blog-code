package com.castlejune.blogcode.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class FruitInventory {

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, BiPredicate<Apple, Apple> applePredicate, Apple compare) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {

            if (applePredicate.test(apple, compare)) {
                result.add(apple);
            }
        }

        return result;
    }

}
