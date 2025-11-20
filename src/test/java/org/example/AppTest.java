package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AppTest {

    @Test
    void loopMethod() {
        assertEquals(List.of(1, 2, 3), App.loopMethod(3));
    }
}