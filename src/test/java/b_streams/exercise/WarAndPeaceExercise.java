package b_streams.exercise;

import b_streams.data.WAPResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarAndPeaceExercise {

    /**
     * Map lowercased words to its amount in text and concatenate its entries.
     * If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
     * Entries in final String should be also sorted by amount and then in alphabetical order if needed.
     * Also omit any word with lengths less than 4 and frequency less than 10
     */
    @Test
    public void warAndPeace() throws IOException {

        String result = Stream.of(Paths.get("src", "test", "resources", "WAP12.txt"),
                Paths.get("src", "test", "resources", "WAP34.txt"))
                .flatMap(this::readFromPath) // to stream of lines
                .map(String::toLowerCase) // to lower case
                .flatMap(s -> Arrays.stream(s.split("[^a-zа-яё]"))) // to stream of words
                .filter(s -> s.length() >= 4) // remove short words
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // count words' frequencies
                .entrySet()
                .stream()
                .filter(stringLongEntry -> stringLongEntry.getValue() >= 10) // remove rare words
                .sorted(Comparator.comparing(Map.Entry::getKey)) // lexicographical sort
                .sorted((stringLongEntry1, stringLongEntry2) -> // sort by frequency
                        Long.compare(
                                stringLongEntry2.getValue(),
                                stringLongEntry1.getValue()
                        ))
                .map(stringLongEntry -> // build result strings "word - frequency\n"
                        stringLongEntry.getKey() +
                                " - " +
                                stringLongEntry.getValue()
                )
                .collect(joining("\n"));

        assertEquals(new WAPResult().result, result);
    }

    private Stream<String> readFromPath(Path path) {
        try {
            return Files.lines(path, Charset.forName("windows-1251"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private class UncheckedIOException extends RuntimeException {
        UncheckedIOException(Throwable var1) {
            super(var1);
        }
    }
}
