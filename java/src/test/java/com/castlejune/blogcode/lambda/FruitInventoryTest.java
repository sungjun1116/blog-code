package com.castlejune.blogcode.lambda;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FruitInventoryTest {

    List<Apple> fruitList = new ArrayList<>();

    @BeforeEach
    void createFruitInventory() {
        fruitList.add(Apple.builder()
                .color("red")
                .weight(150)
                .build());
        fruitList.add(Apple.builder()
                .color("green")
                .weight(100)
                .build());
        fruitList.add(Apple.builder()
                .color("green")
                .weight(150)
                .build());
    }

    @Test
    @DisplayName("filterGreenApples 테스트")
    void test1() {
        // when
        List<Apple> greenApples = FruitInventory.filterGreenApples(fruitList);

        // then
        Assertions.assertThat(greenApples.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("filterApplesByColor 테스트")
    void test2() {
        // given
        String color = "red";

        // when
        List<Apple> apples = FruitInventory.filterApplesByColor(fruitList, color);

        // then
        Assertions.assertThat(apples.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("filterApples 테스트")
    void test3() {
        // given
        String color = "red";
        int weight = 120;
        boolean useColor = true;
        boolean unusedColor = false;

        // when
        List<Apple> useColorAppleList = FruitInventory.filterApples(fruitList, color, weight, useColor);
        List<Apple> unusedColorAppleList = FruitInventory.filterApples(fruitList, color, weight, unusedColor);

        // then
        Assertions.assertThat(useColorAppleList.size()).isEqualTo(1);
        Assertions.assertThat(unusedColorAppleList.size()).isEqualTo(2);
    }
}