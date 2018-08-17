package a_lambda.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FilterMap {

    public static class Container<T, R> {
        private final Predicate<T> predicate;
        private final Function<T, R> function;

        public Container(Predicate<T> predicate) {
            this.predicate = predicate;
            this.function = null;
        }

        public Container(Function<T, R> function) {
            this.function = function;
            this.predicate = null;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }

        public Function<T, R> getFunction() {
            return function;
        }
    }

    public static class LazyCollectionHelper<T> {
        private final List<Container<Object, Object>> actions;
        private final List<T> list;

        public LazyCollectionHelper(List<T> list, List<Container<Object, Object>> actions) {
            this.actions = actions;
            this.list = list;
        }

        public LazyCollectionHelper(List<T> list) {
            this(list, new ArrayList<>());
        }

        public List<T> getList() {
            return list;
        }

        public LazyCollectionHelper<T> filter(Predicate<T> condition) {
            List<Container<Object, Object>> newActions = new ArrayList<>(actions);
            newActions.add(new Container<>((Predicate<Object>) condition));
            return new LazyCollectionHelper<>(list, newActions);
        }

        public <R> LazyCollectionHelper<R> map(Function<T, R> function) {
            List<Container<Object, Object>> newActions = new ArrayList<>(actions);
            newActions.add(new Container<>((Function<Object, Object>) function));
            List<R> newList = new ArrayList<>();
            for (T element : list) {
                newList.add(function.apply(element));
            }
            return new LazyCollectionHelper<>(newList, newActions);
        }

        public List<T> force() {

            if (actions == null || actions.size() == 0) {
                return list;
            }

            LazyCollectionHelper newLazyCollectionHelper = new LazyCollectionHelper(list, actions);
            for (Container action : actions) {
                if (action.getPredicate() != null) {
                    newLazyCollectionHelper = newLazyCollectionHelper.filter(action.getPredicate());
                } else {
                    newLazyCollectionHelper = newLazyCollectionHelper.map(action.getFunction());
                }
            }
            return newLazyCollectionHelper.getList();
        }
    }
}
