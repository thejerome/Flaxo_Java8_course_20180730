package b_streams.exercise;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import b_streams.data.Employee;
import b_streams.data.JobHistoryEntry;
import b_streams.data.Person;

public class CollectorsExercise1 {

    @Test
    public void testPersonToHisLongestJobDuration() {

        Map<Person, Integer> collected = getEmployees()
                .stream()
                .collect(Collectors.toMap(Employee::getPerson, employee -> employee.getJobHistory()
                        .stream()
                        .mapToInt(jobHistoryEntry -> jobHistoryEntry.getDuration())
                        .max()
                        .getAsInt()
                ));


        Map<Person, Integer> expected = ImmutableMap.<Person, Integer>builder()
                .put(new Person("John", "Galt", 20), 3)
                .put(new Person("John", "Doe", 21), 4)
                .put(new Person("John", "White", 22), 6)
                .put(new Person("John", "Galt", 23), 3)
                .put(new Person("John", "Doe", 24), 4)
                .put(new Person("John", "White", 25), 6)
                .put(new Person("John", "Galt", 26), 3)
                .put(new Person("Bob", "Doe", 27), 4)
                .put(new Person("John", "White", 28), 6)
                .put(new Person("John", "Galt", 29), 3)
                .put(new Person("John", "Doe", 30), 5)
                .put(new Person("Bob", "White", 31), 6)
                .build();
        assertEquals(expected, collected);
    }

    @Test
    public void testPersonToHisTotalJobDuration() {

        Map<Person, Integer> collected = getEmployees()
                .stream()
                .collect(Collectors.toMap(Employee::getPerson, employee -> employee.getJobHistory()
                        .stream()
                        .mapToInt(value -> value.getDuration())
                        .sum()));


        Map<Person, Integer> expected = ImmutableMap.<Person, Integer>builder()
                .put(new Person("John", "Galt", 20), 5)
                .put(new Person("John", "Doe", 21), 8)
                .put(new Person("John", "White", 22), 6)
                .put(new Person("John", "Galt", 23), 5)
                .put(new Person("John", "Doe", 24), 8)
                .put(new Person("John", "White", 25), 6)
                .put(new Person("John", "Galt", 26), 4)
                .put(new Person("Bob", "Doe", 27), 8)
                .put(new Person("John", "White", 28), 6)
                .put(new Person("John", "Galt", 29), 4)
                .put(new Person("John", "Doe", 30), 11)
                .put(new Person("Bob", "White", 31), 6)
                .build();

        assertEquals(expected, collected);
    }

    @Test
    public void testTotalJobDurationPerNameAndSurname() {

        //Implement custom Collector
        Map<String, Integer> collected = getEmployees()
                .stream()
                .collect(HashMap::new,
                        (stringIntegerHashMap, employee) ->
                        {
                            stringIntegerHashMap.put(employee.getPerson().getFirstName(), stringIntegerHashMap.getOrDefault(employee.getPerson().getFirstName(), 0) + employee.getJobHistory().stream().mapToInt(JobHistoryEntry::getDuration).sum());
                            stringIntegerHashMap.put(employee.getPerson().getLastName(), stringIntegerHashMap.getOrDefault(employee.getPerson().getLastName(), 0) + employee.getJobHistory().stream().mapToInt(JobHistoryEntry::getDuration).sum());
                        },
                        HashMap::putAll);

        Map<String, Integer> expected = ImmutableMap.<String, Integer>builder()
                .put("John", 5 + 8 + 6 + 5 + 8 + 6 + 4 + 8 + 6 + 4 + 11 + 6 - 8 - 6)
                .put("Bob", 8 + 6)
                .put("Galt", 5 + 5 + 4 + 4)
                .put("Doe", 8 + 8 + 8 + 11)
                .put("White", 6 + 6 + 6 + 6)
                .build();

        assertEquals(expected, collected);
    }

    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 21),
                        Arrays.asList(
                                new JobHistoryEntry(4, "BA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 22),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 23),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 24),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "BA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 25),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 26),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Bob", "Doe", 27),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 28),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "BA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 29),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 30),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(5, "dev", "abc")
                        )),
                new Employee(
                        new Person("Bob", "White", 31),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        ))
        );
    }

}
