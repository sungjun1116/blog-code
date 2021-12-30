package com.castlejune.blogcode.lambda;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class Apple {
    private String color;
    private int weight;

    @Builder
    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }
}