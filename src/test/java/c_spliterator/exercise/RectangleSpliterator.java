package c_spliterator.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int[][] array;
    private int rowStart;
    private int rowEnd;
    private int columnStart;
    private int columnEnd;
    private int columnCurrent;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    private RectangleSpliterator(int[][] array, int rowStart, int rowEnd, int columnStart, int columnCurrent, int columnEnd){
        super((rowEnd - rowStart) * (columnEnd - columnStart), Spliterator.IMMUTABLE
            |Spliterator.SIZED
            |Spliterator.SUBSIZED
            |Spliterator.ORDERED
            |Spliterator.NONNULL);

        this.array = array;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.columnCurrent = columnCurrent;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final RectangleSpliterator spliterator;
        int columns = columnEnd - columnStart;
        int rows = rowEnd - rowStart;
        int mid;

        if (rows <= 1 && columns <= 1) spliterator = null;
        else if (rows < columns){
            mid = columnStart + columns/2;
            if(columnCurrent < mid){
                spliterator = new RectangleSpliterator(array, rowStart, rowEnd, columnStart, columnCurrent,  mid);
                columnCurrent = mid;
            } else spliterator = new RectangleSpliterator(array, ++columnStart, rowEnd, columnStart, columnStart, mid);

            columnStart = mid;
        } else {
            mid = rowStart + rows/2;
            spliterator = new RectangleSpliterator(array, rowStart, mid, columnStart, columnCurrent, columnEnd);
            rowStart = mid;
            columnCurrent = columnStart;
        }

        return spliterator;

    }

    @Override
    public long estimateSize() {
        //TODO
        return (rowEnd - rowStart - 1) * columnEnd + (columnEnd - columnCurrent);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (rowStart < rowEnd) {
            action.accept(array[rowStart][columnCurrent++]);
            if (columnCurrent == columnEnd){
                columnCurrent = columnStart;
                ++rowStart;
            }
            return true;
        } else
            return false;
    }
}
