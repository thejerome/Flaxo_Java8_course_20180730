package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int RowStartInclusive;
    private int RowEndExclusive;
    private int ColumnStartInclusive;
    private int ColumnEndExclusive;
    private int cursor;

    public RectangleSpliterator(int[][] array) {
        //TODO
        super(0, 0);
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
