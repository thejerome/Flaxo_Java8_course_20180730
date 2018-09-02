package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private BiFunction<L, R, T> combiner;
    private int start;
    private int end;
    private List<L> list1;
    private List<R> list2;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        this.combiner = combiner;
        start = 0;

        if (list1.size() > list2.size()) {
            this.list1 = list1.subList(0, list2.size());
            this.list2 = list2;
        } else {
            this.list2 = list2.subList(0, list1.size());
            this.list1 = list1;
        }

        end = this.list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (start < end) {
            action.accept(combiner.apply(list1.get(start), list2.get(start)));
            start++;
            return true;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        if (estimateSize() < 2) {
            return null;
        }

        int middle = start + (int) ((estimateSize() / 2));

        ListZipSpliterator<L, R, T> spliterator = new ListZipSpliterator<>(list1.subList(0, middle), list2.subList(0, middle), combiner);

        start = middle;

        return spliterator;
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | ORDERED | SIZED | SUBSIZED | NONNULL;
    }
}
