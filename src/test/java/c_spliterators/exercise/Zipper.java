package c_spliterators.exercise;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Zipper {
    public static <L, R, T> Stream<T> zip(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        return StreamSupport.stream(new ListZipSpliterator<>(list1, list2, combiner), false);
    }
}
