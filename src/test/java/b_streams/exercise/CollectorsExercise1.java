package b_streams.exercise;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import b_streams.data.Employee;
import b_streams.data.JobHistoryEntry;
import b_streams.data.Person;

public class CollectorsExercise1 {

    @Test
    public void testPersonToHisLongestJobDuration() {

        Map<Person, Integer> collected = getEmployees().parallelStream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .map(jobHistoryEntry -> new PersonEmployerPair(employee.getPerson(), jobHistoryEntry.getEmployer(), jobHistoryEntry.getDuration())))
                .sorted(Comparator.comparingInt(PersonEmployerPair::getDuration))
                .collect(
                        HashMap::new,
                        (map, pair) -> map.put(pair.getEmployee(), pair.getDuration()),
                        HashMap::putAll
                );

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

        Map<Person, Integer> collected = getEmployees().stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .map(jobHistoryEntry -> new PersonEmployerPair(employee.getPerson(), jobHistoryEntry.getEmployer(), jobHistoryEntry.getDuration())))
                .collect(
                        HashMap::new,
                        (map, pair) -> map.put(pair.getEmployee(), map.getOrDefault(pair.getEmployee(), 0) + pair.getDuration()),
                        Map::putAll
                );


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
        Map<String, Integer> collected = getEmployees().stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .map(jobHistoryEntry -> new PersonEmployerPair(employee.getPerson(), jobHistoryEntry.getEmployer(), jobHistoryEntry.getDuration())))
                .collect(
                        HashMap::new,
                        (map, pair) ->
                        {
                            map.put(pair.getEmployee().getFirstName(), map.getOrDefault(pair.getEmployee().getFirstName(), 0) + pair.getDuration());
                            map.put(pair.getEmployee().getLastName(), map.getOrDefault(pair.getEmployee().getLastName(), 0) + pair.getDuration());
                        },
                        HashMap::putAll
                );

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


    class PersonEmployerPair {
        private Person employee;
        private String employer;
        private int duration;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public Person getEmployee() {
            return employee;
        }

        public void setEmployee(Person employee) {
            this.employee = employee;
        }

        public String getEmployer() {
            return employer;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }

        public PersonEmployerPair(Person person, String employer, int duration) {
            this.employee = person;
            this.employer = employer;
            this.duration = duration;
        }
    }

}
