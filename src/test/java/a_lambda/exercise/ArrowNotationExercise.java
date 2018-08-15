package a_lambda.exercise;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import java.util.function.BiPredicate;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import a_lambda.data.Person;

public class ArrowNotationExercise {

    @Test
    public void getAge() {
        // Person -> Integer
        final Function<Person, Integer> getAge = person -> person.getAge(); // DONE

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean
        BiPredicate<Person, Person> compareAges = (person1, person2) -> person1.getAge() == person2.getAge();

        //throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    public static String getFullName(Person person) {
        return (person.getFirstName() != null ? person.getFirstName() : "")
                        + " "
                        + (person.getLastName() != null ? person.getLastName() : "");
    }

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    public static BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName (Function<Person, String> function ) {
        return (person1, person2) -> function.apply(person1).length() > function.apply(person2).length()
                    ? person1.getAge()
                    : person2.getAge();
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        // TODO use getFullName
        final Function<Person, String> getFullName = ArrowNotationExercise::getFullName;
        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ageOfPersonWithTheLongestFullName(getFullName);

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
