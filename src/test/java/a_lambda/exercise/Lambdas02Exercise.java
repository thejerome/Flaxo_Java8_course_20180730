package a_lambda.exercise;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.FluentIterable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import a_lambda.data.Person;

public class Lambdas02Exercise {


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
        // TODO use Arrays.sort and lambda
        Arrays.sort(persons, Comparator.comparing(Person::getAge));
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
      // TODO use FluentIterable and lambda
      Person person = FluentIterable.from(persons)
          .filter(p -> p.getAge() > 30)
          .first()
          .get();
      assertEquals(person, new Person("name 2", "lastName 1", 33));
    }
}
