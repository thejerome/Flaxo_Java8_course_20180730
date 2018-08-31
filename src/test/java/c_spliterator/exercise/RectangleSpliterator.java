package c_spliterator.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;

    private int startInclusiveX;
    private int endExclusiveX;
    private int startInclusiveY;
    private int currentY;
    private int endExclusiveY;

    public RectangleSpliterator(int[][] array) {
        //TODO
        this(array, 0, array.length, 0, 0, array[0].length);
    }

    private RectangleSpliterator(
            int[][] array,
            int startInclusiveX, int endExclusiveX,
            int startInclusiveY, int currentY, int endExclusiveY
    )
    {

        super((endExclusiveX - startInclusiveX) * (endExclusiveY - startInclusiveY),
                Spliterator.IMMUTABLE
                        | Spliterator.ORDERED
                        | Spliterator.SIZED
                        | Spliterator.SUBSIZED
                        | Spliterator.NONNULL);

        this.array = array;
        this.startInclusiveX = startInclusiveX;
        this.endExclusiveX = endExclusiveX;
        this.startInclusiveY = startInclusiveY;
        this.currentY = currentY;
        this.endExclusiveY = endExclusiveY;
    }

    @Override
    public RectangleSpliterator trySplit() {

        // TODO
        int lengthX = endExclusiveX - startInclusiveX;
        int lengthY = endExclusiveY - startInclusiveY;

        if (lengthX <= 1 && lengthY <= 1) {
            return null;
        }

        int middle;
        final RectangleSpliterator newSpliterator;

        if (lengthX >= lengthY) {

            middle = startInclusiveX + lengthX / 2;
            newSpliterator = new RectangleSpliterator(
                    array,
                    startInclusiveX, middle,
                    startInclusiveY, currentY, endExclusiveY
            );

            startInclusiveX = middle;
            currentY = startInclusiveY;
        } else {
            middle = startInclusiveY + lengthY / 2;

            if (currentY >= middle) {

                newSpliterator = new RectangleSpliterator(
                        array,
                        ++startInclusiveX, endExclusiveX,
                        startInclusiveY, startInclusiveY, middle
                );
            } else {

                newSpliterator = new RectangleSpliterator(
                        array,
                        startInclusiveX, endExclusiveX,
                        startInclusiveY, currentY, middle
                );

                currentY = middle;
            }

            startInclusiveY = middle;
        }

        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return (endExclusiveX - startInclusiveX - 1) * endExclusiveY + (endExclusiveY - currentY);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {

        // TODO
        if (startInclusiveX < endExclusiveX) {
            action.accept(array[startInclusiveX][currentY]);

            ++currentY;
            if (currentY == endExclusiveY) {
                currentY = startInclusiveY;
                ++startInclusiveX;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super Integer> action) {

        for (int j = currentY; j < endExclusiveY && startInclusiveX < endExclusiveX; ++j)
        {
            action.accept(array[startInclusiveX][j]);
        }

        ++startInclusiveX;

        for (int i = startInclusiveX; i < endExclusiveX; ++i) {
            for (int j = startInclusiveY; j < endExclusiveY; ++j) {
                action.accept(array[i][j]);
            }
        }

        startInclusiveX = endExclusiveX;
        startInclusiveY = endExclusiveY;
    }
}
