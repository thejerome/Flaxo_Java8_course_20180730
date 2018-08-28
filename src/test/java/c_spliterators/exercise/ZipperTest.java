package c_spliterators.exercise;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

public class ZipperTest {
    @Test
    public void zip() throws Exception {
        assertEquals(ImmutableList.of("1-4", "2-5", "3-6"),
                Zipper.zip(
                        ImmutableList.of("1", "2", "3"),
                        ImmutableList.of("4", "5", "6"),
                        (l, r) -> String.join("-", l, r)
                ).collect(toList()));
    }

    @Test
    public void zipFirstLonger() throws Exception {
        assertEquals(ImmutableList.of("1-4", "2-5"),
                Zipper.zip(
                        ImmutableList.of("1", "2", "3"),
                        ImmutableList.of("4", "5"),
                        (l, r) -> String.join("-", l, r)
                ).collect(toList()));
    }

    @Test
    public void zipSecondLongerLonger() throws Exception {
        assertEquals(ImmutableList.of("1-4", "2-5", "3-6"),
                Zipper.zip(
                        ImmutableList.of("1", "2", "3"),
                        ImmutableList.of("4", "5", "6", "7"),
                        (l, r) -> String.join("-", l, r)
                ).collect(toList()));
    }

    @Test
    public void zipIntegers() throws Exception {
        assertEquals(ImmutableList.of(5, 7, 9),
                Zipper.zip(
                        ImmutableList.of(1, 2, 3),
                        ImmutableList.of(4, 5, 6, 7),
                        (l, r) -> l + r
                ).collect(toList()));
    }

    @Test
    public void zipperSpliterator() throws Exception {
        ListZipSpliterator<String, String, String> spliterator = new ListZipSpliterator<>(
                ImmutableList.of("1", "2", "3", "4"),
                ImmutableList.of("5", "6", "7", "8", "9"),
                (l, r) -> String.join("-", l, r)
        );

        assertEquals(4L, spliterator.estimateSize());

        Spliterator<String> subSpliterator = spliterator.trySplit();
        assertNotNull(subSpliterator);
        assertEquals(2L, subSpliterator.estimateSize());

        List<String> collected = Stream.concat(
                StreamSupport.stream(subSpliterator, false),
                StreamSupport.stream(spliterator, false)
        ).collect(toList());

        assertEquals(ImmutableList.of("1-5", "2-6", "3-7", "4-8"), collected);

    }

}