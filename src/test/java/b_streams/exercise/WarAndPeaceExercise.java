package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
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

import org.junit.jupiter.api.Test;

import b_streams.data.WAPResult;

public class WarAndPeaceExercise {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> wapStream = Stream.of(
                Paths.get("src", "test", "resources", "WAP12.txt"),
                Paths.get("src", "test", "resources", "WAP34.txt"));


        String result = wapStream
                .flatMap(WarAndPeaceExercise::toWordStream)
                .filter(s -> s.length() >= 4)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting())).entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .filter(entry -> entry.getValue() >= 10)
                .map(entry -> String.format("%s - %d", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WAPResult().result, result);
    }

    public static Stream<String> toWordStream(Path path) {
        try {
            return Files.lines(path, Charset.forName("windows-1251"))
                    .flatMap(line -> Arrays.stream(line.split("[^a-zA-Zа-яА-Я]")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
