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
       BiPredicate<Person, Person> compareAges = (person1, person2) -> (person1.getAge() == person2.getAge());
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean

//       throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    String getFullName (Person p){
       StringBuilder sj = new StringBuilder();
       sj.append(p.getFirstName());
       sj.append(p.getLastName());
       return sj.toString();
    }
    // TODO
    // getFullName: Person -> String

    public BiFunction <Person, Person, Integer> ageOfPersonWithTheLongestFullName(Function<Person, String> getFullName) {
            return (person1, person2) -> Integer.compare(getFullName(person1).length(), getFullName(person2).length()) > 0 ?
                    person1.getAge(): person2.getAge();}
    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    //

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        // TODO use getFullName
        final Function<Person, String> getFullName = this::getFullName;

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
