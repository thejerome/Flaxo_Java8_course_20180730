package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private int[][] array;
    private int fromI, fromJ;
    private int toI, toJ;
    private int originI, originJ;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, 0, array.length, array[0].length, 0, 0);
    }

    private RectangleSpliterator(int[][] array, int fromI, int fromJ, int toI, int toJ, int originI, int originJ) {
        super((toI - originI - 1) * (toJ - fromJ) + (toJ - originJ), ORDERED | SIZED | SUBSIZED);
        this.array = array;
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
        this.originI = originI;
        this.originJ = originJ;
    }

    private int initialWidth() {
        return toJ - fromJ;
    }

    private int width() {
        if (originI == toI - 1) {
            return toJ - originJ;
        } else {
            return initialWidth();
        }
    }

    private int height() {
        return toI - originI;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final int height = height();
        final int width = width();
        if (height > width) { // Split vertically then
            final int newHeight = height / 2;
            RectangleSpliterator newSpliterator = new RectangleSpliterator(array, toI - newHeight, fromJ, toI, toJ, toI - newHeight, fromJ);
            toI -= newHeight;
            return newSpliterator;
        } else if (width >= 2) { // Split horizontally then
            final int newWidth = width / 2;
            int elementsProcessedFromNewSpliterator = originJ - (toJ - newWidth);
            if (elementsProcessedFromNewSpliterator < 0)
                elementsProcessedFromNewSpliterator = 0;
            final int newOriginJ = toJ - newWidth + elementsProcessedFromNewSpliterator;
            RectangleSpliterator newSpliterator = new RectangleSpliterator(array, originI, toJ - newWidth, toI, toJ, fromI, newOriginJ);
            toJ = toJ - newWidth;
            if (originJ > toJ) {
                originJ = fromJ;
                ++originI;
            }
            return newSpliterator;
        } else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        //TODO
        final int initialWidth = initialWidth();
        return (height() - 1) * initialWidth + (initialWidth - originJ);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (originI < toI) {
            action.accept(array[originI][originJ]);
            ++originJ;
            if (originJ >= toJ) {
                ++originI;
                originJ = fromJ;
            }
            return true;
        } else {
            return false;
        }
    }
}
