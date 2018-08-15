package a_lambda.exercise;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.junit.jupiter.api.Test;

import a_lambda.data.Person;

import javax.annotation.Nullable;

public class Lambdas01Exercise {

    private Person[] getPersons() {
        return new Person[]{
                new Person("name 5", "lastName 3", 22),
                new Person("name 4", "lastName 7", 21),
                new Person("name 2", "lastName 1", 33),
                new Person("name 3", "lastName 4", 31),
                new Person("name 8", "lastName 8", 41),
                new Person("name 1", "lastName 5", 32),
                new Person("name 9", "lastName 2", 43),
                new Person("name 7", "lastName 6", 42),
                new Person("name 6", "lastName 9", 23)
        };
    }

    @Test
    public void sortPersonsByAge() {
        final Person[] persons = getPersons();
        // TODO use Arrays.sort and anonymous class

        Arrays.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.getAge(), p2.getAge());
            }
        });

        assertArrayEquals(persons, new Person[]{
                new Person("name 4", "lastName 7", 21),
                new Person("name 5", "lastName 3", 22),
                new Person("name 6", "lastName 9", 23),
                new Person("name 3", "lastName 4", 31),
                new Person("name 1", "lastName 5", 32),
                new Person("name 2", "lastName 1", 33),
                new Person("name 8", "lastName 8", 41),
                new Person("name 7", "lastName 6", 42),
                new Person("name 9", "lastName 2", 43)
        });
    }

    @Test
    public void findFirstWithAgeGreaterThan30() {
        final List<Person> persons = new ArrayList<>(Arrays.asList(getPersons()));
        Person person = null;
        // TODO use FluentIterable and anonymous class

        Optional<Person> firstPersonOlderThan30 = FluentIterable.from(persons)
                .firstMatch(new Predicate<Person>() {
                    @Override
                    public boolean apply(@Nullable Person p) {
                        return p.getAge() > 30;
                    }
                });

        person = firstPersonOlderThan30.get();

        assertEquals(person, new Person("name 2", "lastName 1", 33));
    }
}
