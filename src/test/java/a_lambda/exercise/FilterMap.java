package a_lambda.exercise;

import a_lambda.data.Employee;
import a_lambda.data.JobHistoryEntry;
import a_lambda.data.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static boolean hasDevExperience(Employee e) {
        return new LazyCollectionHelper<>(e.getJobHistory())
                .filter(j -> j.getPosition().equals("dev"))
                .getList()
                .size() > 0;
    }

    private static boolean workedInEpamMoreThenOneYearLazy(Employee e) {
        return new LazyCollectionHelper<>(e.getJobHistory())
                .filter(j -> j.getEmployer().equals("epam"))
                .filter(j -> j.getDuration() > 1)
                .force()
                .size() > 0;
    }

    @Test
    public void lazy_filtering() {
        final List<Employee> employees =
                Arrays.asList(
                        new Employee(
                                new Person("John", "Galt", 30),
                                Arrays.asList(
                                        new JobHistoryEntry(2, "dev", "epam"),
                                        new JobHistoryEntry(1, "dev", "google")
                                )),
                        new Employee(
                                new Person("John", "Doe", 40),
                                Arrays.asList(
                                        new JobHistoryEntry(3, "QA", "yandex"),
                                        new JobHistoryEntry(1, "QA", "epam"),
                                        new JobHistoryEntry(1, "dev", "abc")
                                )),
                        new Employee(
                                new Person("John", "White", 50),
                                Collections.singletonList(
                                        new JobHistoryEntry(5, "QA", "epam")
                                ))
                );

        final List<Employee> filteredList = new LazyCollectionHelper<>(employees)
                .filter(e -> e.getPerson().getFirstName().equals("John"))
                .filter(FilterMap::hasDevExperience)
                .filter(FilterMap::workedInEpamMoreThenOneYearLazy)
                .force();

        assertEquals(1, filteredList.size());
        assertEquals(filteredList.get(0).getPerson(), new Person("John", "Galt", 30));
    }
}
