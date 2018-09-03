package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private final List<L> lList;
    private final List<R> rList;
    private final BiFunction<L, R, T> combiner;

    private int startIn;
    private final int endEx;

    public ListZipSpliterator(List<L> lList, List<R> rList, BiFunction<L, R, T> combiner) {
        int sizeLList = lList.size();
        int sizeRList = rList.size();

        if (sizeLList > sizeRList) {
            this.lList = lList.subList(0, sizeRList);
            this.rList = rList;
        } else {
            this.rList = rList.subList(0, sizeLList);
            this.lList = lList;
        }

        this.combiner = combiner;

        this.startIn = 0;
        this.endEx = this.lList.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO

        if (startIn < endEx) {
            action.accept(combiner.apply(lList.get(startIn), rList.get(startIn)));
            startIn += 1;
            return true;
        }


        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        int length = endEx - startIn;

        if (length < 2) {
            return null;
        }

        int mid = startIn + length / 2;
        final ListZipSpliterator res = new ListZipSpliterator(lList.subList(0, mid), rList.subList(0, mid), combiner);
        startIn = mid;

        return res;
    }

    @Override
    public long estimateSize() {
        //TODO
        return endEx - startIn;
    }

    @Override
    public int characteristics() {
        //TODO
        return 0;
    }
}
