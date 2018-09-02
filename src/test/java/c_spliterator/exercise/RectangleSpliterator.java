package c_spliterator.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;

    private int startRow;
    private int endRow;
    private int startColumn;
    private int endColumn;
    private int currentColumn;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startRow, int endRow, int startColumn,
                                 int currentColumn, int endColumn) {

        super((endRow - startRow) * (endColumn - startColumn),
                Spliterator.IMMUTABLE
                        | Spliterator.NONNULL
                        | Spliterator.ORDERED
                        | Spliterator.SIZED
                        | Spliterator.SUBSIZED
        );

        this.array = array;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startColumn = startColumn;
        this.currentColumn = currentColumn;
        this.endColumn = endColumn;
    }

    @Override
    public RectangleSpliterator trySplit() {
        final RectangleSpliterator rectangleSpliterator;
        int rows = endRow - startRow;
        int columns = endColumn - startColumn;

        if (rows < 2 && columns < 2) {
            return null;
        }

        int middle;
        if (rows >= columns) {
            middle = startRow + rows / 2;
            rectangleSpliterator = new RectangleSpliterator(array, startRow, middle, startColumn,
                    currentColumn, endColumn);
            startRow = middle;
            currentColumn = startColumn;
        } else {
            middle = startColumn + columns / 2;
            if (currentColumn >= middle) {
                rectangleSpliterator = new RectangleSpliterator(array, ++startRow, endRow, startColumn, startColumn, middle);
            } else {
                rectangleSpliterator = new RectangleSpliterator(
                        array, startRow, endRow, startColumn, currentColumn, middle);
                currentColumn = middle;
            }
            startColumn = middle;
        }
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        return (endRow - startRow - 1) * endColumn + (endColumn - currentColumn);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startRow < endRow) {
            action.accept(array[startRow][currentColumn]);
            if (++currentColumn == endColumn) {
                startRow++;
                currentColumn = startColumn;
            }
            return true;
        } else {
            return false;
        }
    }
}