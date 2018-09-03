package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private int startOfRow;
    private int endOfRow;
    private int startOfCol;
    private int current;
    private int endOfCol;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, 0,  array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startOfRow, int endOfRow, int current, int startOfCol, int endOfCol) {
        super((endOfCol - startOfCol) + (endOfRow - startOfRow - 1) * endOfCol,
            IMMUTABLE | SIZED | SUBSIZED | ORDERED | NONNULL);

        this.array = array;
        this.startOfRow = startOfRow;
        this.endOfRow = endOfRow;
        this.startOfCol = startOfCol;
        this.current = current;
        this.endOfCol = endOfCol;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        RectangleSpliterator rectangleSpliterator;
        int rowLength = endOfRow - startOfRow;
        int colLength = endOfCol - startOfCol;
        int middle;

        if (rowLength < 2 && colLength < 2) {
            return null;
        }

        if (rowLength >= colLength) {
            middle = startOfRow + rowLength/2;
            rectangleSpliterator = new RectangleSpliterator(array, startOfRow, middle, startOfCol, current,
                endOfCol);
            current = startOfCol;
            startOfRow = middle;
        }else{
            middle = startOfCol + colLength/2;
            if (current >= middle) {
                startOfCol++;
                rectangleSpliterator = new RectangleSpliterator(array, startOfCol, endOfRow,
                    startOfCol, startOfCol, middle);
            } else {
                rectangleSpliterator = new RectangleSpliterator(array, startOfRow, endOfRow,
                    startOfCol, current, middle);
                current = middle;
            }
            startOfCol = middle;
        }
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (endOfCol - current) + (endOfRow - startOfRow - 1) * endOfCol;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startOfRow < endOfRow) {
            int value = array[startOfRow][current++];
            action.accept(value);
            if (current == endOfCol) {
                current = startOfCol;
                startOfRow++;
            }
            return true;
        }
        return false;
    }
}
