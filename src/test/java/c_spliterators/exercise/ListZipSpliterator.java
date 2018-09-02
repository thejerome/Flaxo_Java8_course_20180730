package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;

    private int startInc;
    private int endExc;


    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int startInc, int endExc) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInc = startInc;
        this.endExc = endExc;
    }
    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        if(list1.size() > list2.size()){
            list1 = list1.subList(0, list2.size());
        } else {
            list2 = list2.subList(0, list1.size());
        }

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInc = 0;
        this.endExc = list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
       if(startInc >= endExc){
           return false;
       }
       action.accept(combiner.apply(list1.get(startInc), list2.get(startInc)));
       startInc++;
       return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        int length = endExc - startInc;
        if (length < 2){
            return null;
        }
        int newStartInc = startInc;
        startInc = startInc + length / 2;

        return new ListZipSpliterator<L, R, T>(list1, list2, combiner, newStartInc, startInc);
    }

    @Override
    public long estimateSize() {
        return endExc - startInc;
    }

    @Override
    public int characteristics() {
        return Spliterator.IMMUTABLE
                | Spliterator.ORDERED
                | Spliterator.SIZED
                | Spliterator.SUBSIZED
                | Spliterator.NONNULL;
    }
}
