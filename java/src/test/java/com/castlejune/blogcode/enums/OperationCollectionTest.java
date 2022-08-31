package com.castlejune.blogcode.enums;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OperationCollectionTest {

    @Test
    @DisplayName("람다 테스트")
    void test1() {
        // given
        double a = 20;
        double b = 10;

        // when && then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(Operation.PLUS.apply(a, b)).isEqualTo(30);
            softAssertions.assertThat(Operation.MINUS.apply(a, b)).isEqualTo(10);
            softAssertions.assertThat(Operation.TIMES.apply(a, b)).isEqualTo(200);
            softAssertions.assertThat(Operation.DIVIDE.apply(a, b)).isEqualTo(2);
        });
    }
}