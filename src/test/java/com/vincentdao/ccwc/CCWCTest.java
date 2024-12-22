package com.vincentdao.ccwc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class CCWCTest {

    private static final String TEST_INPUT_PATH = "test.txt";

    @Test
    void givenTestInput_whenCountNumberOfBytes_thenCorrectCountIsReturned() throws IOException {
        try (InputStream is = CCWCTest.class.getClassLoader().getResourceAsStream(TEST_INPUT_PATH)) {
            assertThat(CCWC.INSTANCE.countBytes(is)).isEqualTo(342190);
        }
    }

    @Test
    void givenTestInput_whenCountNumberOfLines_thenCorrectCountIsReturned() throws IOException {
        try (InputStream is = CCWCTest.class.getClassLoader().getResourceAsStream(TEST_INPUT_PATH)) {
            assertThat(CCWC.INSTANCE.countLines(is)).isEqualTo(7145);
        }
    }

    @Test
    void givenTestInput_whenCountNumberOfWords_thenCorrectCountIsReturned() throws IOException {
        try (InputStream is = CCWCTest.class.getClassLoader().getResourceAsStream(TEST_INPUT_PATH)) {
            assertThat(CCWC.INSTANCE.countWords(is)).isEqualTo(58164);
        }
    }

    @Test
    void givenTestInput_whenCountNumberOfCharacters_thenCorrectCountIsReturned() throws IOException {
        try (InputStream is = CCWCTest.class.getClassLoader().getResourceAsStream(TEST_INPUT_PATH)) {
            assertThat(CCWC.INSTANCE.countCharacters(is)).isEqualTo(339292);
        }
    }
}
