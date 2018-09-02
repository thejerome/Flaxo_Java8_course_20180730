package c_spliterator.exercise;

import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int[][] array;

    private int startInclusiveForRow;
    private int endExclusiveForRow;
    private int startInclusiveForColumn;
    private int currentForColumn;
    private int endExclusiveForColumn;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    public RectangleSpliterator(int[][] array,
                                int startInclusiveForRow,
                                int endExclusiveForRow,
                                int startInclusiveForColumn,
                                int currentForColumn,
                                int endExclusiveForColumn) {

        super((endExclusiveForRow - startInclusiveForRow) * (endExclusiveForColumn - startInclusiveForColumn),
                IMMUTABLE
                        | ORDERED
                        | SIZED
                        | SUBSIZED
                        | NONNULL);

        this.array = array;
        this.startInclusiveForRow = startInclusiveForRow;
        this.endExclusiveForRow = endExclusiveForRow;
        this.startInclusiveForColumn = startInclusiveForColumn;
        this.currentForColumn = currentForColumn;
        this.endExclusiveForColumn = endExclusiveForColumn;
    }

    @Override
    public RectangleSpliterator trySplit() {

        int diffForRow = endExclusiveForRow - startInclusiveForRow;
        int diffForColumn = endExclusiveForColumn - startInclusiveForColumn;

        if (diffForRow < 2 && diffForColumn < 2) {
            return null;
        }

        final RectangleSpliterator result;

        if (diffForRow >= diffForColumn) {

            final int middleForRow = startInclusiveForRow + diffForRow / 2;

            result = new RectangleSpliterator(
                    array,
                    startInclusiveForRow,
                    middleForRow,
                    startInclusiveForColumn,
                    currentForColumn,
                    endExclusiveForColumn);

            startInclusiveForRow = middleForRow;
            currentForColumn = startInclusiveForColumn;

        } else {

            final int middleForColumn = startInclusiveForColumn + diffForColumn / 2;

            if (middleForColumn > currentForColumn) {

                result = new RectangleSpliterator(
                        array,
                        startInclusiveForRow,
                        endExclusiveForRow,
                        startInclusiveForColumn,
                        currentForColumn,
                        middleForColumn);

                currentForColumn = middleForColumn;

            } else {

                result = new RectangleSpliterator(
                        array,
                        ++startInclusiveForRow,
                        endExclusiveForRow,
                        startInclusiveForColumn,
                        startInclusiveForColumn,
                        middleForColumn);
            }

            startInclusiveForColumn = middleForColumn;

        }

        return result;
    }

    @Override
    public long estimateSize() {
        return (endExclusiveForRow - startInclusiveForRow) * endExclusiveForColumn - currentForColumn;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {

        if (startInclusiveForRow >= endExclusiveForRow) {
            return false;
        }

        action.accept(array[startInclusiveForRow][currentForColumn]);
        if (++currentForColumn == endExclusiveForColumn) {
            startInclusiveForRow++;
            currentForColumn = startInclusiveForColumn;
        }

        return true;
    }
}
