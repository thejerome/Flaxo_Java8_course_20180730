package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import b_streams.data.Employee;
import b_streams.data.JobHistoryEntry;
import b_streams.data.Person;

public class StreamsExercise2 {
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    // TODO class PersonEmployerPair
    private class PersonEmployerPair {
        protected final String employer;
        protected final Person person;

        public PersonEmployerPair(Person person, String employer) {
            this.person = person;
            this.employer = employer;
        }

        public String getEmployer() {
            return employer;
        }

        public Person getPerson() {
            return person;
        }
    }

    @Test
    public void employersStuffLists() {
        final List<Employee> employees = getEmployees();

        Map<String, List<Person>> employersStuffLists = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                                .map(jobHistoryEntry -> new PersonEmployerPair(employee.getPerson(), jobHistoryEntry.getEmployer())))
                .collect(Collectors.groupingBy(
                        PersonEmployerPair::getEmployer,
                        Collectors.mapping(PersonEmployerPair::getPerson, Collectors.toList())
                ));
        // TODO map employer vs persons with job history related to it

        assertEquals(getExpectedEmployersStuffLists(), employersStuffLists);
    }

    @Test
    public void indexByFirstEmployer() {
        final List<Employee> employees = getEmployees();

        Map<String, List<Person>> employeesIndex = employees.stream()
                .map(employee -> new PersonEmployerPair(employee.getPerson(), employee.getJobHistory().iterator().next().getEmployer()))
                .collect(Collectors.groupingBy(
                        PersonEmployerPair::getEmployer,
                        Collectors.mapping(PersonEmployerPair::getPerson, Collectors.toList()))
                );
        // TODO map employer vs persons with first job history related to it

        assertEquals(getExpectedEmployeesIndexByFirstEmployer(), employeesIndex);

    }

    private class PersonEmployerPairWithDuration extends PersonEmployerPair {
        private final int duration;

        public PersonEmployerPairWithDuration(String employer, Person person, int duration) {
            super(person, employer);
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }
    }

    @Test
    public void greatestExperiencePerEmployer() {

        Map<String, Person> employeesIndex = getEmployees().stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .map(jobHistoryEntry -> new PersonEmployerPairWithDuration(jobHistoryEntry.getEmployer(), employee.getPerson(), jobHistoryEntry.getDuration())))
                .collect(Collectors.groupingBy(
                        PersonEmployerPairWithDuration::getEmployer,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(PersonEmployerPairWithDuration::getDuration)),
                                opt -> opt.map(PersonEmployerPairWithDuration::getPerson).orElseThrow(NoSuchElementException::new)
                        )
                ));
        // TODO map employer vs person with greatest duration in it

        assertEquals(new Person("John", "White", 28), employeesIndex.get("epam"));
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
                                new JobHistoryEntry(666, "BA", "epam")
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

    private static Map<String, List<Person>> getExpectedEmployersStuffLists() {
        Map<String, List<Person>> map = ImmutableMap.of(
                "abc", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "yandex", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "epam", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "Doe", 21),
                        new Person("John", "White", 22),
                        new Person("John", "Galt", 23),
                        new Person("John", "Doe", 24),
                        new Person("John", "White", 25),
                        new Person("John", "Galt", 26),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "White", 28),
                        new Person("John", "Galt", 29),
                        new Person("John", "Doe", 30),
                        new Person("Bob", "White", 31)
                ),
                "google", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "Galt", 23),
                        new Person("John", "Galt", 26),
                        new Person("John", "Galt", 29)
                )
        );

        return map;
    }

    private static Map<String, List<Person>> getExpectedEmployeesIndexByFirstEmployer() {
        Map<String, List<Person>> map = ImmutableMap.of(
                "yandex", ImmutableList.of(
                        new Person("John", "Doe", 21),
                        new Person("John", "Doe", 24),
                        new Person("Bob", "Doe", 27),
                        new Person("John", "Doe", 30)
                ),
                "epam", ImmutableList.of(
                        new Person("John", "Galt", 20),
                        new Person("John", "White", 22),
                        new Person("John", "Galt", 23),
                        new Person("John", "White", 25),
                        new Person("John", "Galt", 26),
                        new Person("John", "White", 28),
                        new Person("John", "Galt", 29),
                        new Person("Bob", "White", 31)
                )
        );

        return map;
    }

}
