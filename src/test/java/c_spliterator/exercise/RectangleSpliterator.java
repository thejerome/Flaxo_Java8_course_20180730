package c_spliterator.exercise;


import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int[][] array;
    private int[] startInc = new int[2];
    private int[] endExc = new int[2];
    private int[] length = new int[2];
    private int curPos;

    public RectangleSpliterator(int[][] array) {
        this(array, new int[]{0, 0}, new int[]{array.length, array[0].length}, 0);

    }

    private RectangleSpliterator(int[][] array, int[] startInc, int[] endExc, int curPos) {
        super((endExc[0] - startInc[0]) * (endExc[1] - startInc[1]) - curPos, IMMUTABLE
                | ORDERED
                | SIZED
                | SUBSIZED
                | NONNULL);
        this.array = array;
        this.startInc = startInc;
        this.endExc = endExc;
        this.length[0] = endExc[0] - startInc[0];
        this.length[1] = endExc[1] - startInc[1];
        this.curPos = curPos;
    }

    @Override
    public RectangleSpliterator trySplit() {
        final int maxLength = Math.max(length[0], length[1]);
        if (maxLength < 2) {
            return null;
        }
        int flag = maxLength == length[0] ? 0 : 1;
        int mid = startInc[flag] + maxLength / 2;


        int[] newStart = Arrays.copyOf(startInc, startInc.length);
        int[] newEnd = Arrays.copyOf(endExc, endExc.length);
        int newCurPos = curPos;
        startInc[flag] = mid;
        newEnd[flag] = mid;


        if (flag == 1) {
            if (curPos >= mid) {
                curPos = curPos - mid;
                newStart[0]++;
                newCurPos = 0;
            } else {
                curPos = 0;
            }
        } else {
            curPos = 0;
        }
        length[0] = endExc[0] - startInc[0];
        length[1] = endExc[1] - startInc[1];
        return new RectangleSpliterator(array, newStart, newEnd, newCurPos);
    }

    @Override
    public long estimateSize() {
        return length[0] * length[1] - curPos;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (curPos < 0) {
            return false;
        }
        action.accept(array[startInc[0]][startInc[1] + curPos]);
        curPos++;
        if (curPos >= length[1]) {
            if (length[0] > 1) {
                length[0]--;
                startInc[0]++;
                curPos = 0;
            } else {
                curPos = -1;
            }
        }
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action){
        for (int i = startInc[0]; i < endExc[0]; i++) {
            for (int j = startInc[1] + curPos; j < endExc[1] ; j++) {
                action.accept(array[i][j]);
            }
            curPos =0;
        }
        curPos = -1;
    }

}
