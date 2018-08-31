package c_spliterators.exercise;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private final List<L> list1;
    private final List<R> list2;
    private final Iterator<L> list1Iterator;
    private final Iterator<R> list2Iterator;
    private final BiFunction<L, R, T> combiner;


    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.list1Iterator = list1.iterator();
        this.list2Iterator = list2.iterator();
        this.combiner = combiner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if (list1Iterator.hasNext() && list2Iterator.hasNext()) {
            final L elem1 = list1Iterator.next();
            final R elem2 = list2Iterator.next();
            final T combined = combiner.apply(elem1, elem2);
            action.accept(combined);
            return true;
        }
        return false;
//        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        if (list1.size() <= 1 || list2.size() <= 1) {
            return null;
        }
        final List<L> resultList1 = new ArrayList<>();
        final List<R> resultList2 = new ArrayList<>();
        final long workSize = estimateSize() / 2;

        for (int i = 0; i < workSize; i++) {
            resultList1.add(list1.get(i));
            resultList2.add(list2.get(i));
        }
        return new ListZipSpliterator<L, R, T>(resultList1, resultList2, combiner);
//        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        //TODO
        return list1.size() > list2.size() ? list2.size() : list1.size();
//        throw new UnsupportedOperationException();
    }

    @Override
    public int characteristics() {
        //TODO
        return IMMUTABLE
                | SIZED
                | SUBSIZED
                | NONNULL
                | IMMUTABLE;
//        throw new UnsupportedOperationException();
    }
}
