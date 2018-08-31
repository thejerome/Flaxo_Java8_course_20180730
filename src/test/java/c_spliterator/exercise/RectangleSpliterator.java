package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private int firstDimStartInclusive;
    private int firstDimEndExclusive;
    private int secondDimStartInclusive;
    private int secondDimEndExclusive;
    private int currentPosition;


    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, array[0].length, 0);
    }

    private RectangleSpliterator(int[][] array, int firstDimStartInclusive, int firstDimEndExclusive,
                                 int secondDimStartInclusive, int secondDimEndExclusive, int currentPosition) {
        super((firstDimEndExclusive - firstDimStartInclusive) * (secondDimEndExclusive - secondDimStartInclusive),
                IMMUTABLE
                        | ORDERED
                        | SIZED
                        | SUBSIZED
                        | NONNULL);
        this.array = array;
        this.firstDimStartInclusive = firstDimStartInclusive;
        this.firstDimEndExclusive = firstDimEndExclusive;
        this.secondDimStartInclusive = secondDimStartInclusive;
        this.secondDimEndExclusive = secondDimEndExclusive;
        this.currentPosition = currentPosition;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final int firstDimlength = firstDimEndExclusive - firstDimStartInclusive;
        final int secondDimlength = secondDimEndExclusive - secondDimStartInclusive;
        if (firstDimlength < 2 && secondDimlength < 2) {
            return null;
        }
        RectangleSpliterator rectangleSpliterator;

        if (firstDimEndExclusive > secondDimEndExclusive) {
            int firstMid = firstDimStartInclusive + firstDimlength / 2;
            int secondMid = secondDimEndExclusive;
            rectangleSpliterator = new RectangleSpliterator(array, firstDimStartInclusive, firstMid, secondDimStartInclusive, secondMid, currentPosition);
            firstDimStartInclusive = firstMid;
        } else {
            int firstMid = firstDimEndExclusive;
            int secondMid = secondDimStartInclusive + secondDimlength / 2;
            rectangleSpliterator = new RectangleSpliterator(array, firstDimStartInclusive, firstMid, secondDimStartInclusive, secondMid, currentPosition);
            secondDimStartInclusive = secondMid;
        }
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (firstDimEndExclusive - firstDimStartInclusive - 1) * secondDimEndExclusive + (secondDimEndExclusive - currentPosition);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (firstDimStartInclusive >= firstDimEndExclusive || secondDimStartInclusive >= secondDimEndExclusive) {
            return false;
        }
        final int value = array[firstDimStartInclusive][secondDimStartInclusive];
        secondDimStartInclusive += 1;
        ++currentPosition;
        action.accept(value);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int i = firstDimStartInclusive; i < firstDimEndExclusive; i++) {
            for (int j = secondDimStartInclusive; j < secondDimEndExclusive; j++) {
                action.accept(array[i][j]);
            }
        }
        firstDimStartInclusive = firstDimEndExclusive;
        secondDimStartInclusive = secondDimEndExclusive;
    }
}
