package c_spliterator.exercise;


import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int[][] array;
    private int current;

    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0, array[0].length, 0);
    }

    private RectangleSpliterator(int[][] array, int startX, int endX, int startY, int endY, int current) {

        super((endY - startY) * (endX - startX),
                IMMUTABLE | ORDERED | SIZED | SUBSIZED | NONNULL);

        this.array = array;
        this.current = current;

        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    public RectangleSpliterator trySplit() {
        int width = endX - startX;
        int height = endY - startY;

        if (width < 2 && height < 2) {
            return null;
        }

        RectangleSpliterator spliterator;

        if (height <= width) {
            spliterator = splitVertically(width);
        } else {
            spliterator = splitHorizontally(height);
        }

        return spliterator;
    }

    private RectangleSpliterator splitVertically (int width) {
        int middleX = startX + width / 2;

        RectangleSpliterator spliterator = new RectangleSpliterator(array, startX, middleX, startY, endY, current);

        startX = middleX;
        current = startY;

        return spliterator;
    }

    private RectangleSpliterator splitHorizontally (int height) {
        int middleY = startY + height / 2;
        RectangleSpliterator spliterator;

        if (middleY > current) {
            spliterator = new RectangleSpliterator(array, startX, endX, startY, middleY, current);
            current = middleY;
        } else {
            startX++;
            spliterator = new RectangleSpliterator(array, startX, endX, startY, middleY, startY);
        }

        startY = middleY;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        return (endX - startX) * endY - current;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {

        if (startX >= endX) {
            return false;
        }

        action.accept(array[startX][current]);
        current++;

        if (current == endY) {
            startX++;
            current = startY;
        }

        return true;
    }
}
