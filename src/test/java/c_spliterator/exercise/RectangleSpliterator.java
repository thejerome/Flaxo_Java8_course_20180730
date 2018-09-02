package c_spliterator.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private int currentY;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startX, int endX, int startY, int currentY, int endY) {
        super((endX - startX) * (endY - startY), Spliterator.IMMUTABLE
                | Spliterator.NONNULL
                | Spliterator.ORDERED
                | Spliterator.SIZED
                | Spliterator.SUBSIZED
        );
        this.array = array;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.currentY = currentY;
    }

    @Override
    public RectangleSpliterator trySplit() {
        // TODO
        final RectangleSpliterator rectangleSpliterator;
        int rows = endX - startX;
        int columns = endY - startY;
        int middle;

        if (rows < 2 && columns < 2) {
            return null;
        }

        if (rows >= columns) {
            middle = startX + rows / 2;
            rectangleSpliterator = new RectangleSpliterator(array, startX, middle, startY, currentY, endY);
            startX = middle;
            currentY = startY;
        } else {
            middle = startY + columns / 2;
            if (currentY >= middle) {
                rectangleSpliterator = new RectangleSpliterator(array, ++startX, endX, startY, startY, middle);
            } else {
                rectangleSpliterator = new RectangleSpliterator(
                        array, startX, endX, startY, currentY, middle);
                currentY = middle;
            }
            startY = middle;
        }
        return rectangleSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (endX - startX - 1) * endY + (endY - currentY);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        if (startX < endX) {
            action.accept(array[startX][currentY]);
            if (++currentY == endY) {
                startX++;
                currentY = startY;
            }
            return true;
        } else {
            return false;
        }
    }
}