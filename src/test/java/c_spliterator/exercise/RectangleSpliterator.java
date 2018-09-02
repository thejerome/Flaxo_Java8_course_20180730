package c_spliterator.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;

    private int startX;
    private int endX;
    private int startY;
    private int currentY;
    private int endY;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, 0,  array[0].length);
    }

    private RectangleSpliterator(int[][] array, int startX, int endX, int currentY, int startY, int endY) {

        super((endX - startX) * (endY - startY), Spliterator.IMMUTABLE | Spliterator.SIZED
                | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.NONNULL);

        this.array = array;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.currentY = currentY;
        this.endY = endY;
    }

    @Override
    public RectangleSpliterator trySplit() {

        int lengthX = endX - startX;
        int lengthY = endY - startY;
        int middle;
        final RectangleSpliterator spliterator;

        if (lengthX <= 1 && lengthY <= 1) {
            spliterator = null;
        }

        else if (lengthX < lengthY) {
            middle = startY + lengthY / 2;
            if (currentY < middle) {
                spliterator = new RectangleSpliterator(array, startX, endX, startY, currentY, middle);
                currentY = middle;
            } else {
                spliterator = new RectangleSpliterator(array, ++startY, endX, startY, startY, middle);
            }
            startY = middle;
        }
        else {
            middle = startX + lengthX / 2;
            spliterator = new RectangleSpliterator(array, startX, middle, startY, currentY, endY);
            startX = middle;
            currentY = startY;
        }
        return spliterator;
    }

    @Override
    public long estimateSize() {

        return (endX - startX - 1) * endY + (endY - currentY);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {

        if (startX < endX) {
            action.accept(array[startX][currentY++]);
            if (currentY == endY) {
                currentY = startY;
                ++startX;
            }
            return true;
        } else {
            return false;
        }
    }
}
