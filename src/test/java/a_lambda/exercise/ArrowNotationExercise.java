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
        // compareAges: (Person, Person) -> boolean
        BiPredicate<Person, Person> compareAges = (person1, person2) -> person1.getAge() == person2.getAge();

        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // getFullName: Person -> String
    private static String getFullName(Person person) {
        return person.getFirstName() + " " + person.getLastName();
    }

    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    //
    private static Integer ageOfPersonWithTheLongestFullName(Person p1, Person p2) {
        if (getFullName(p1).length() > getFullName(p2).length()) {
            return p1.getAge();
        } else {
            return p2.getAge();
        }
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // (Person, Person) -> Integer
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ArrowNotationExercise::ageOfPersonWithTheLongestFullName;

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
