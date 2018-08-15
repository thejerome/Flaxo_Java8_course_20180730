package a_lambda.exercise;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import a_lambda.data.Person;

public class ArrowNotationExercise {

    @Test
    public void getAge() {
        // Person -> Integer
        final Function<Person, Integer> getAge = Person::getAge; // TODO

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean
        final BiPredicate<Person, Person> compareAges = (a, b) -> a.getAge() == b.getAge();

        //throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    Function<Person, String> getFullPersonName = p -> p.getFirstName() + " " + p.getLastName();
    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    //
    BiFunction<Person, Person, Integer> getAgeOfPersonWithLongestFullName = (a, b) -> {
        if (getFullPersonName.apply(a).length() > getFullPersonName.apply(b).length())
            return a.getAge();
        else
            return b.getAge();
    };

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        // TODO use getFullName
        final Function<Person, String> getFullName = getFullPersonName;

        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = getAgeOfPersonWithLongestFullName;

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
