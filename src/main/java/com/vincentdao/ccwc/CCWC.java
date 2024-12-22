package com.vincentdao.ccwc;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public enum CCWC {

    INSTANCE;

    private static final int BUFFER_SIZE = 2048;
    private static final int EOF = -1;

    private static final int NEW_LINE = '\n';
    private static final int SPACE = ' ';
    private static final int TAB = '\t';
    private static final int CARRIAGE_RETURN = '\r';

    private static final int UTF_8_1ST_BYTE_MASK = 0b10000000;
    private static final int UTF_8_1ST_BYTE_INDICATOR = 0;

    private static final int UTF_8_2ND_BYTE_MASK = 0b11100000;
    private static final int UTF_8_2ND_BYTE_INDICATOR = 0b11000000;

    private static final int UTF_8_3RD_BYTE_MASK = 0b11110000;
    private static final int UTF_8_3RD_BYTE_INDICATOR = 0b11100000;

    private static final int UTF_8_4TH_BYTE_MASK = 0b11111000;
    private static final int UTF_8_4TH_BYTE_INDICATOR = 0b11110000;

    public long countBytes(InputStream inputStream) throws IOException {
        try (inputStream) {
            Objects.requireNonNull(inputStream, "InputStream must not be null");

            long bytesCounted = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != EOF) {
                bytesCounted += bytesRead;
            }

            return bytesCounted;
        }
    }

    public long countLines(InputStream inputStream) throws IOException {
        try (inputStream) {
            Objects.requireNonNull(inputStream, "InputStream must not be null");

            long linesCounted = 0;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while (reader.readLine() != null) {
                    linesCounted++;
                }
            }

            return linesCounted;
        }
    }

    public long countWords(InputStream inputStream) throws IOException {
        try (inputStream) {
            Objects.requireNonNull(inputStream, "InputStream must not be null");

            long wordsCounted = 0;
            boolean isReadingWord = false;
            int byteRead;

            while ((byteRead = inputStream.read()) != EOF) {
                switch (byteRead) {
                    case NEW_LINE, SPACE, TAB, CARRIAGE_RETURN -> {
                        if (isReadingWord) {
                            wordsCounted++;
                            isReadingWord = false;
                        }
                    }
                    default -> isReadingWord = true;
                }
            }

            if (isReadingWord) {
                wordsCounted++;
            }

            return wordsCounted;
        }
    }

    public long countCharacters(InputStream inputStream) throws IOException {
        try (inputStream) {
            Objects.requireNonNull(inputStream, "InputStream must not be null");

            long charactersCounted = 0;
            int codePointBytesLeft;
            int byteRead;

            while ((byteRead = inputStream.read()) != EOF) {
                if ((byteRead & UTF_8_1ST_BYTE_MASK) == UTF_8_1ST_BYTE_INDICATOR) {
                    codePointBytesLeft = 0;
                } else if ((byteRead & UTF_8_2ND_BYTE_MASK) == UTF_8_2ND_BYTE_INDICATOR) {
                    codePointBytesLeft = 1;
                } else if ((byteRead & UTF_8_3RD_BYTE_MASK) == UTF_8_3RD_BYTE_INDICATOR) {
                    codePointBytesLeft = 2;
                } else if ((byteRead & UTF_8_4TH_BYTE_MASK) == UTF_8_4TH_BYTE_INDICATOR) {
                    codePointBytesLeft = 3;
                } else {
                    throw new IllegalArgumentException("Invalid character code point: " + byteRead);
                }

                while (codePointBytesLeft-- > 0) {
                    if (inputStream.read() == EOF) {
                        throw new EOFException("Unexpected EOF while reading character code point");
                    }
                }

                charactersCounted++;
            }

            return charactersCounted;
        }
    }
}
