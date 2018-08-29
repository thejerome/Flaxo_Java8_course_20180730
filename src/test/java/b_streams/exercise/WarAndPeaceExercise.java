package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import b_streams.data.WAPResult;

public class WarAndPeaceExercise {

    @Test
    public void warAndPeace() throws IOException {
       StringBuilder builder = Stream.of(
                Paths.get("src", "test", "resources", "WAP12.txt"),
                Paths.get("src", "test", "resources", "WAP34.txt"))
                .flatMap(path -> {
                    try {
                        return Files.lines(path, Charset.forName("windows-1251"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).map(String::toLowerCase)
                .map(s -> s.replaceAll("[^\\sа-яА-Яa-zA-Z]", " "))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(w -> w.length() > 3)
                .collect(
                        (Supplier<TreeMap<String, Integer>>) TreeMap::new,
                        (map, word) -> map.put(word, map.getOrDefault(word, 0) + 1),
                        TreeMap::putAll
                ).entrySet().stream()
                .filter(entry -> entry.getValue() > 9)
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .collect(() -> new StringBuilder(),
                       (sb, entry) -> sb.append(entry.getKey() + " - " + entry.getValue() + "\n"),
                       (sb1, sb2) -> sb1.append(sb2)
               );
        builder.setLength(builder.length() - 1);
        String result = builder.toString();
        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WAPResult().result, result);
    }

}
