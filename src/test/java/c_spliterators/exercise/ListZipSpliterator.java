package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {


    private int startWith;
    private int endWithout;
    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;


    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.combiner = combiner;
        if (list1.size() > list2.size()) {
            this.list1 = list1.subList(0, list2.size());
            this.list2 = list2;
        } else {
            this.list2 = list2.subList(0, list1.size());
            this.list1 = list1;
        }
        startWith = 0;
        endWithout = this.list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if (startWith < endWithout) {
            action.accept(combiner.apply(list1.get(startWith), list2.get(startWith)));
            startWith++;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        if (estimateSize() < 2) {
            return null;
        }
        int middle = (int) (startWith + ((estimateSize() / 2)));
        ListZipSpliterator<L, R, T> spliterator = new ListZipSpliterator<>(list1.subList(0, middle), list2.subList(0, middle), combiner);
        startWith = middle;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return endWithout - startWith;
    }

    @Override
    public int characteristics() {
        //TODO
        return Spliterator.IMMUTABLE |
                Spliterator.ORDERED |
                Spliterator.SIZED |
                Spliterator.SUBSIZED |
                Spliterator.NONNULL;
    }
}