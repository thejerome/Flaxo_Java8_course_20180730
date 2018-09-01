package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private int startInclusive;
    private final int endExclusive;

    private int rowLength;
    private int clmnLength;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length);
    }

    public RectangleSpliterator(int[][] array, int startInclusive, int endExclusive) {
        super(endExclusive - startInclusive, IMMUTABLE
                | ORDERED
                | SIZED
                | SUBSIZED
                | NONNULL);

        this.array = array;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;

        rowLength = array.length;
        clmnLength = array[0].length;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final int maxLength = Math.max(rowLength, clmnLength);

        System.out.println("maxLength = " + maxLength);

        final int length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }

        final int mid = startInclusive + length/2;
        final RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(array, startInclusive, mid);
        startInclusive = mid;


        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return endExclusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startInclusive >= endExclusive) {
            return false;
        }

        System.out.println("startInclusive = " + startInclusive);

        System.out.println("array.length = " + array.length);

        final int value = array[startInclusive][startInclusive];

        startInclusive += 1;
        action.accept(value);

        return true;
    }
}
