package b_streams.exercise;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.function.Function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import b_streams.data.Employee;
import b_streams.data.JobHistoryEntry;
import b_streams.data.Person;

public class StreamsExercise {

    @Test
    public void getAllJobHistoryEntries() {
        final List<Employee> employees = getEmployees();

        final List<JobHistoryEntry> jobHistoryEntries = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream())
                .collect(toList());

        assertEquals(22, jobHistoryEntries.size());
    }

    /**
     * Sum all durations for all persons
     */
    @Test
    public void getSumDuration() {

        final List<Employee> employees = getEmployees();

        final int sumDurations = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream())
                .mapToInt(JobHistoryEntry::getDuration)
                .sum();

        assertEquals(72, sumDurations);
    }

    private static class PersonEmployer {
        private final Person person;
        private final String employer;

        public PersonEmployer(Person person, String employer) {
            this.person = person;
            this.employer = employer;
        }

        public Person getPerson() {
            return person;
        }

        public String getEmployer() {
            return employer;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("person", person)
                    .append("employer", employer)
                    .toString();
        }
    }

    @Test
    public void indexPersonsByEmployer1() {
        final List<Employee> employees = getEmployees();

        final Map<String, List<PersonEmployer>> index =
                employees.stream()
                        .flatMap(employee -> employee.getJobHistory().stream()
                                .map(jobHistoryEntry -> new PersonEmployer(employee.getPerson(), jobHistoryEntry.getEmployer())))
                        .collect(groupingBy(
                                PersonEmployer::getEmployer,
                                mapping(Function.identity(), toList())
                        ));

        assertEquals(11, index.get("epam").size());
    }

    @Test
    public void indexPersonsByEmployer2() {
        final List<Employee> employees = getEmployees();

        final Map<String, List<Person>> index =
                employees.stream()
                        .flatMap(employee -> employee.getJobHistory().stream()
                                .map(jobHistoryEntry -> new PersonEmployer(employee.getPerson(), jobHistoryEntry.getEmployer())))
                        .collect(groupingBy(
                                PersonEmployer::getEmployer,
                                mapping(PersonEmployer::getPerson, toList())
                        ));

        assertEquals(11, index.get("epam").size());
    }

    private static class PersonDuration {
        private final Person person;
        private final int duration;

        public PersonDuration(Person person, int duration) {
            this.person = person;
            this.duration = duration;
        }

        public Person getPerson() {
            return person;
        }

        public int getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("person", person)
                    .append("duration", duration)
                    .toString();
        }
    }

    private PersonDuration sumAllPersonDurations(Employee employee) {
        return new PersonDuration(
                employee.getPerson(),
                employee.getJobHistory().stream()
                        .mapToInt(JobHistoryEntry::getDuration)
                        .sum());
    }

    /**
     * Sum all durations for each person
     */
    @Test
    public void getSumPersonDuration() {

        final List<Employee> employees = getEmployees();

        final Map<Person, Integer> personDuration = employees.stream()
                .map(this::sumAllPersonDurations)
                .collect(groupingBy(
                        PersonDuration::getPerson,
                        summingInt(PersonDuration::getDuration)));

        assertEquals(Integer.valueOf(8), personDuration.get(new Person("John", "Doe", 24)));
    }

    private static class PersonPositionIndex {
        private final Person person;
        private final Map<String, Integer> durationByPositionIndex;

        public PersonPositionIndex(Person person, Map<String, Integer> durationByPositionIndex) {
            this.person = person;
            this.durationByPositionIndex = durationByPositionIndex;
        }

        public Person getPerson() {
            return person;
        }

        public Map<String, Integer> getDurationByPositionIndex() {
            return durationByPositionIndex;
        }
    }

    private static PersonPositionIndex getPersonPositionIndex(Employee e) {
        return new PersonPositionIndex(
                e.getPerson(),
                e.getJobHistory().stream()
                        .collect(groupingBy(
                                JobHistoryEntry::getPosition,
                                summingInt(JobHistoryEntry::getDuration))));
    }

    @Test
    public void getSumDurationsForPersonByPosition() {
        final List<Employee> employees = getEmployees();

        final List<PersonPositionIndex> personIndexes = employees.stream()
                .map(employee -> getPersonPositionIndex(employee))
                .collect(toList());

        assertEquals(1, personIndexes.get(3).getDurationByPositionIndex().size());
    }

    private static class PersonPositionDuration {
        private final Person person;
        private final String position;
        private final int duration;

        public PersonPositionDuration(Person person, String position, int duration) {
            this.person = person;
            this.position = position;
            this.duration = duration;
        }

        public Person getPerson() {
            return person;
        }

        public String getPosition() {
            return position;
        }

        public int getDuration() {
            return duration;
        }
    }

    @Test
    public void getDurationsForEachPersonByPosition() {
        final List<Employee> employees = getEmployees();

        final List<PersonPositionDuration> personPositionDurations = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .collect(groupingBy(
                                JobHistoryEntry::getPosition,
                                summingInt(JobHistoryEntry::getDuration)))
                        .entrySet().stream()
                        .map(stringIntegerEntry -> new PersonPositionDuration(
                                employee.getPerson(),
                                stringIntegerEntry.getKey(),
                                stringIntegerEntry.getValue())))
                .collect(toList());

        assertEquals(17, personPositionDurations.size());
    }

    /**
     * Get person with max duration on given position
     */
    @Test
    public void getCoolestPersonByPosition1() {
        final List<Employee> employees = getEmployees();

        final Map<String, PersonPositionDuration> coolestPersonByPosition = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .collect(groupingBy(
                                JobHistoryEntry::getPosition,
                                summingInt(JobHistoryEntry::getDuration)))
                        .entrySet().stream()
                        .map(stringIntegerEntry -> new PersonPositionDuration(
                                employee.getPerson(),
                                stringIntegerEntry.getKey(),
                                stringIntegerEntry.getValue())))
                .collect(groupingBy(
                        PersonPositionDuration::getPosition,
                        collectingAndThen(
                                maxBy(comparing(PersonPositionDuration::getDuration)),
                                Optional::get)));

        assertEquals(new Person("John", "White", 22), coolestPersonByPosition.get("QA").getPerson());
    }

    /**
     * Get person with max duration on given position
     */
    @Test
    public void getCoolestPersonByPosition2() {
        final List<Employee> employees = getEmployees();

        final Map<String, Person> coolestPersonByPosition = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream()
                        .collect(groupingBy(
                                JobHistoryEntry::getPosition,
                                summingInt(JobHistoryEntry::getDuration)))
                        .entrySet().stream()
                        .map(stringIntegerEntry -> new PersonPositionDuration(
                                employee.getPerson(),
                                stringIntegerEntry.getKey(),
                                stringIntegerEntry.getValue())))
                .collect(groupingBy(
                        PersonPositionDuration::getPosition,
                        collectingAndThen(
                                maxBy(comparing(PersonPositionDuration::getDuration)),
                                personPositionDuration -> personPositionDuration.get().getPerson())));

        assertEquals(new Person("John", "White", 22), coolestPersonByPosition.get("QA"));
    }

    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Collections.emptyList()),
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
