package b_streams.data;

import static java.nio.file.Files.readAllLines;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class WAPResult {
    public final String result;

    public WAPResult() throws IOException {
        result = readAllLines(Paths.get("src", "test","resources", "WAPResult.txt")).stream().collect(Collectors.joining("\n"));
    }
}
