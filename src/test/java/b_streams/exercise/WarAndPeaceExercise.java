package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
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


        String result = pathStream.flatMap(path -> {
            try {
                return Files.lines(path, Charset.forName("Windows-1251"));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        })
                .flatMap(s -> Arrays.stream(s.trim().replaceAll("[^\\p{L}]+", " ").split("\\s")))
                .map(String::toLowerCase)
                .filter(s -> s.length() >= 4)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(se -> se.getValue() > 9)
                .sorted((o1, o2) -> (o1.getValue().equals(o2.getValue())) ?
                        o1.getKey().compareTo(o2.getKey()) :
                        Long.compare(o2.getValue(), o1.getValue()))
                .collect(StringBuilder::new, (sb, sle) -> sb.append(sle.getKey()).append(" - ").append(sle.getValue()).append("\n"), StringBuilder::append)
                .toString().trim();






        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WAPResult().result, result);
    }

}
