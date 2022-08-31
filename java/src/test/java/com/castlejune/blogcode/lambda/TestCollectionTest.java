package com.castlejune.blogcode.lambda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestCollectionTest {
    
    @Test
    @DisplayName("test")
    void test() {
        String a = "54321";
        String b = new String("12345");

        System.out.println(a == b);
        System.out.println(a.equals(b));
    }

}