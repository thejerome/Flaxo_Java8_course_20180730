package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private int rowStartInclusive;
    private int rowEndExclusive;
    private int columnStartInclusive;
    private int columnEndExclusive;
    private int cursor;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, array[0].length, 0);
    }
    private RectangleSpliterator(int[][] array, int rowStartInclusive, int rowEndExclusive,
        int columnStartInclusive, int columnEndExclusive, int cursor) {
        super((rowEndExclusive - rowStartInclusive) * (columnEndExclusive - columnStartInclusive),
            IMMUTABLE | ORDERED | SIZED | SUBSIZED | NONNULL);
        this.array = array;
        this.rowStartInclusive = rowStartInclusive;
        this.rowEndExclusive = rowEndExclusive;
        this.columnStartInclusive = columnStartInclusive;
        this.columnEndExclusive = columnEndExclusive;
        this.cursor = cursor;
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
