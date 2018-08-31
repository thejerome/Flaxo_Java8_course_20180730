package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import b_streams.data.WAPResult;

public class WarAndPeaceExercise {

    @Test
    public void warAndPeace() throws IOException {

        String result = Stream.of(
            Paths.get("src", "test", "resources", "WAP12.txt"),
            Paths.get("src", "test", "resources", "WAP34.txt"))
            .flatMap(WarAndPeaceExercise::read)
            .map(String::toLowerCase)
            .map(string -> string.replaceAll("[^a-zA-Zа-яА-Я]", " "))
            .flatMap(string -> Arrays.stream(string.split("[\\s,.!]+")))
            .filter(string -> string.length() >= 4)
            .collect(Collectors.groupingBy(String::toString,
                TreeMap::new,
                Collectors.counting())) //TreeMap for alphabetical order
            .entrySet().stream()
            .filter(entry -> entry.getValue() >= 10)
            .sorted((entry, nextEntry) -> Long.compare(nextEntry.getValue(), entry.getValue()))
            .map(entry -> entry.getKey() + " - " + entry.getValue() + "\n")
            .collect(Collectors.joining())
            .trim();
        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WAPResult().result, result);
    }

    private static Stream<String> read(Path path) {
        try {
            return Files.lines(path, Charset.forName("windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
