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
        int rows = rowEndExclusive - rowStartInclusive;
        int cols = columnEndExclusive - columnStartInclusive;
        if (rows < 2 && cols < 2) {
            return null;
        }
        final RectangleSpliterator currentResult;
        int mid;
        if (rows >= cols) {
            mid = rowStartInclusive + rows / 2;
            currentResult = new RectangleSpliterator(array, rowStartInclusive, mid, columnStartInclusive,
                cursor, columnEndExclusive);
            rowStartInclusive = mid;
            cursor = columnStartInclusive;
        } else {
            mid = columnStartInclusive + cols / 2;
            if (cursor >= mid) {
                currentResult = new RectangleSpliterator(array, ++rowStartInclusive, rowEndExclusive,
                    columnStartInclusive, columnStartInclusive, mid);
            } else {
                currentResult = new RectangleSpliterator(array, rowStartInclusive, rowEndExclusive, columnStartInclusive,
                    cursor, mid);
                cursor = mid;
            }
            columnStartInclusive = mid;
        }
        return currentResult;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (rowEndExclusive - rowStartInclusive - 1) * columnEndExclusive + (columnEndExclusive - cursor);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (rowStartInclusive >= rowEndExclusive || columnStartInclusive >= columnEndExclusive) {
            return false;
        }
        final int value = array[rowStartInclusive][columnStartInclusive];
        cursor++;
        action.accept(value);
        return true;
    }
}
