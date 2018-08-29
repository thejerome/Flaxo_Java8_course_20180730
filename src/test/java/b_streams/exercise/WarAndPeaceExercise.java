package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import b_streams.data.WAPResult;

public class WarAndPeaceExercise {

    @Test
    public void warAndPeace() throws IOException {
        Stream<Path> pathStream = Stream.of(
                Paths.get("src", "test", "resources", "WAP12.txt"),
                Paths.get("src", "test", "resources", "WAP34.txt"));


        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        String result = pathStream
            .flatMap(path -> {
                try {
                    return Files.lines(path, Charset.forName("windows-1251"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .map(String::toLowerCase)
            .map(string -> string.replaceAll("[^a-zA-Zа-яА-Я]", " "))
            .flatMap(string -> Arrays.stream(string.split("[\\s,.!]+")))
            .filter(string -> string.length() > 3)
            .collect(Collectors.groupingBy(String::toString, Collectors.counting()))
            .entrySet()
            .stream()
            .sorted((o1, o2) ->
                o1.getValue().equals(o2.getValue())
                ? o1.getKey().compareTo(o2.getKey())
                : o2.getValue().compareTo(o1.getValue()))
            .filter(entry -> entry.getValue() > 9)
            .map(entry -> entry.getKey() + " - " + entry.getValue() + "\n")
            .collect(Collectors.joining())
            .trim();
        assertEquals(new WAPResult().result, result);
    }

}
