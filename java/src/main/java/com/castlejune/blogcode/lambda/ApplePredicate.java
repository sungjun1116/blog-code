package com.castlejune.blogcode.lambda;

@FunctionalInterface
public interface ApplePredicate {
    boolean test(Apple apple, Apple compare);
}

