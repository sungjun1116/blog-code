package com.castlejune.blogcode.lambda;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean useColor) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {

            if ((useColor && apple.getColor().equals(color))
                    || (!useColor && apple.getWeight() > weight)) {

                result.add(apple);
            }
        }

        return result;
    }
}
