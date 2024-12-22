package com.vincentdao.ccwc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CCWCApplication {

    private static final String OPTION_BYTES = "-c";
    private static final String OPTION_LINES = "-l";
    private static final String OPTION_WORDS = "-w";
    private static final String OPTION_CHARACTERS = "-m";

    private static final String SINGLE_OPTION_RESULT_FORMAT = "\t%d\t%s";
    private static final String MULTIPLE_OPTION_RESULT_FORMAT = "\t%d\t%d\t%d\t%s";

    public static void main(String[] args) throws IOException {
        if (args.length != 1 && args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        final Option option = switch (args[0]) {
            case OPTION_BYTES -> Option.COUNT_BYTES;
            case OPTION_LINES -> Option.COUNT_LINES;
            case OPTION_WORDS -> Option.COUNT_WORDS;
            case OPTION_CHARACTERS -> Option.COUNT_CHARACTERS;
            default -> null;
        };

        final Path inputPath;

        if (option == null) {
            if (args.length == 2) {
                throw new IllegalArgumentException("Unknown option: " + args[0]);
            } else {
                inputPath = Path.of(args[0]);
            }
        } else {
            if (args.length == 1) {
                inputPath = null;
            } else {
                inputPath = Path.of(args[1]);
            }
        }

        if (inputPath != null && Files.notExists(inputPath)) {
            throw new IllegalArgumentException("Input file not found: " + inputPath);
        }

        final Long result = switch (option) {
            case COUNT_BYTES -> CCWC.INSTANCE.countBytes(openFile(inputPath));
            case COUNT_LINES -> CCWC.INSTANCE.countLines(openFile(inputPath));
            case COUNT_WORDS -> CCWC.INSTANCE.countWords(openFile(inputPath));
            case COUNT_CHARACTERS -> CCWC.INSTANCE.countCharacters(openFile(inputPath));
            case null -> null;
        };

        if (result != null) {
            System.out.printf(SINGLE_OPTION_RESULT_FORMAT, result, inputPath);
            return;
        }

        System.out.printf(MULTIPLE_OPTION_RESULT_FORMAT,
                CCWC.INSTANCE.countBytes(openFile(inputPath)),
                CCWC.INSTANCE.countLines(openFile(inputPath)),
                CCWC.INSTANCE.countWords(openFile(inputPath)),
                inputPath);
    }

    private static InputStream openFile(Path filePath) throws IOException {
        return new FileInputStream(filePath.toFile());
    }
}
