package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int[][] array;

    private int startInclusiveRow;
    private final int endExclusiveRow;
    private int startInclusiveClmn;
    private final int endExclusiveClmn;

    private int curIndClmn;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    public RectangleSpliterator(int[][] array, int startInclusiveRow, int endExclusiveRow, int startInclusiveClmn, int curIndClmn, int endExclusiveClmn) {
        super((endExclusiveRow - startInclusiveRow) * (endExclusiveClmn - endExclusiveRow),
                IMMUTABLE
                | ORDERED
                | SIZED
                | SUBSIZED
                | NONNULL);

        this.array = array;
        this.startInclusiveRow = startInclusiveRow;
        this.endExclusiveRow = endExclusiveRow;
        this.startInclusiveClmn = startInclusiveClmn;
        this.curIndClmn = curIndClmn;
        this.endExclusiveClmn = endExclusiveClmn;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        RectangleSpliterator res;
        int mid;

        int lengthRow = endExclusiveRow - startInclusiveRow;
        int lengthClmn = endExclusiveClmn - startInclusiveClmn;

        if (lengthRow < 2 && lengthClmn < 2) {
            return null;
        }

        if (lengthRow >= lengthClmn) {
            mid = startInclusiveRow + lengthRow / 2;

            res = new RectangleSpliterator(array, startInclusiveRow, mid, startInclusiveClmn, curIndClmn, endExclusiveClmn);
            startInclusiveRow = mid;
            curIndClmn = startInclusiveClmn;
        } else {
            mid = startInclusiveClmn + lengthClmn / 2;

            if (mid > curIndClmn) {
                res = new RectangleSpliterator(array, startInclusiveRow, endExclusiveRow, startInclusiveClmn, curIndClmn, mid);
                curIndClmn = mid;
            } else {
                startInclusiveRow += 1;
                res = new RectangleSpliterator(array, startInclusiveRow, endExclusiveRow, startInclusiveClmn, startInclusiveClmn, mid);
            }

            startInclusiveClmn = mid;
        }

        return res;
    }

    @Override
    public long estimateSize() {
        //TODO
        return ((endExclusiveRow - startInclusiveRow) * endExclusiveClmn - curIndClmn);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startInclusiveRow >= endExclusiveRow) {
            return false;
        }

        final int value = array[startInclusiveRow][curIndClmn];
        action.accept(value);

        if (++curIndClmn == endExclusiveClmn) {
            startInclusiveRow += 1;
            curIndClmn = startInclusiveClmn;
        }

        return true;
    }
}