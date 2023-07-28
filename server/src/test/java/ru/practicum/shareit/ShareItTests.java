package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

public class ShareItTests {
    @Test
    public void mainMethodShouldStartApplicationWithoutErrors() {
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }

}

