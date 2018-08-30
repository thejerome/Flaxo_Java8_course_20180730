package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private  int startInclusiveRaw;
    private final int endExclusiveRaw;
    private  int startInclusiveCol;
    private final int endExclusiveCol;
    private  int rawOffset;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, array[0].length);
        //super(0, 0);
    }

    private RectangleSpliterator(int[][] array, int startInclusiveRaw, int endExclusiveRaw, int startInclusiveCol, int endExclusiveCol) {
        super((endExclusiveRaw - startInclusiveRaw) * (endExclusiveCol - startInclusiveCol),
                IMMUTABLE
                        | ORDERED
                        | SIZED
                        | SUBSIZED
                        | NONNULL
                        | CONCURRENT);
        this.array = array;
        this.startInclusiveRaw = startInclusiveRaw;
        this.endExclusiveRaw = endExclusiveRaw;
        this.startInclusiveCol = startInclusiveCol;
        this.endExclusiveCol = endExclusiveCol;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final int size = (endExclusiveRaw - startInclusiveRaw) * (endExclusiveCol - startInclusiveCol);

        if (size <= 1) return null;

        final int mid;
        final RectangleSpliterator currentResult;
        if (startInclusiveRaw < endExclusiveRaw) {
            mid = startInclusiveRaw + (endExclusiveRaw - startInclusiveRaw) / 2;
            currentResult = new RectangleSpliterator(array, mid, endExclusiveRaw, startInclusiveCol, endExclusiveCol);
            startInclusiveRaw = mid;
        } else {
            mid = startInclusiveCol + (endExclusiveCol - startInclusiveCol) / 2;
            currentResult = new RectangleSpliterator(array, startInclusiveRaw, endExclusiveRaw, mid, endExclusiveCol);
            startInclusiveCol = mid;
        }
        return currentResult;
//        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        if (startInclusiveRaw >= endExclusiveRaw) {
            return endExclusiveCol - startInclusiveCol;
        }

        return (endExclusiveRaw - startInclusiveRaw) * endExclusiveCol - rawOffset;
//        return (endExclusiveRaw - startInclusiveRaw) * (endExclusiveCol - startInclusiveCol);
//        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startInclusiveRaw >= endExclusiveRaw) {
            return false;
        }

        final int value = array[startInclusiveRaw][startInclusiveCol];

        if (startInclusiveCol + 1 == endExclusiveCol) {
            startInclusiveCol = 0;
            rawOffset = 0;
            startInclusiveRaw += 1;
        } else {
            startInclusiveCol += 1;
            rawOffset++;
        }

        action.accept(value);
        return true;
//        throw new UnsupportedOperationException();
    }
}
