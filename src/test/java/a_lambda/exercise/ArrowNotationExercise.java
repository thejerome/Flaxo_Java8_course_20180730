package a_lambda.exercise;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.print.attribute.standard.Finishings;
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
        final BiPredicate<Person, Person> compareAges = (p1, p2) -> p1.getAge() == p2.getAge();

//        throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String

    public static String getFullName(Person person){
        return person.getFirstName() + " " + person.getLastName();
    }

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    //

    public static int ageOfPersonWithTheLongestFullName (Person p1, Person p2){
        return ((p1.getFirstName()+p1.getLastName()).length() > (p2.getFirstName() + p2.getLastName()).length()) ? p1.getAge(): p2.getAge();
    }


    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        // TODO use getFullName
        final Function<Person, String> getFullName = ArrowNotationExercise::getFullName;

        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ArrowNotationExercise::ageOfPersonWithTheLongestFullName;

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
