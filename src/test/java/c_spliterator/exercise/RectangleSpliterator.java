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
        this(array, 0, array.length, 0, 0, array[0].length);
    }
    private RectangleSpliterator(int[][] array, int rowStartInclusive, int rowEndExclusive, int columnStartInclusive,
        int cursor, int columnEndExclusive) {
        super((rowEndExclusive - rowStartInclusive) * (columnEndExclusive - columnStartInclusive),
            IMMUTABLE
                | ORDERED
                | SIZED
                | SUBSIZED
                | NONNULL);
        this.array = array;
        this.rowStartInclusive = rowStartInclusive;
        this.rowEndExclusive = rowEndExclusive;
        this.columnStartInclusive = columnStartInclusive;
        this.cursor = cursor;
        this.columnEndExclusive = columnEndExclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
            int rows = rowEndExclusive - rowStartInclusive;
            int columns = columnEndExclusive - columnStartInclusive;
            if (rows < 2 && columns < 2) {
                return null;
            }
            final RectangleSpliterator resultNow;
            int mid;
            if (rows >= columns) {
                mid = rowStartInclusive + rows / 2;
                resultNow = new RectangleSpliterator(array, rowStartInclusive, mid, columnStartInclusive,
                    cursor, columnEndExclusive);
                rowStartInclusive = mid;
                cursor = columnStartInclusive;
            } else {
                mid = columnStartInclusive + columns / 2;
                if (cursor >= mid) {
                    resultNow = new RectangleSpliterator(array, ++rowStartInclusive, rowEndExclusive,
                        columnStartInclusive, columnStartInclusive, mid);
                } else {
                    resultNow = new RectangleSpliterator(array, rowStartInclusive, rowEndExclusive, columnStartInclusive,
                        cursor, mid);
                    cursor = mid;
                }
                columnStartInclusive = mid;
            }
            return resultNow;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (rowEndExclusive - rowStartInclusive - 1) * columnEndExclusive + (columnEndExclusive - cursor);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (rowStartInclusive < rowEndExclusive) {
            action.accept(array[rowStartInclusive][cursor]);
            cursor++;
            if (cursor == columnEndExclusive) {
                rowStartInclusive++;
                cursor = columnStartInclusive;
            }
            return true;
        } else {
            return false;
        }
    }
}
