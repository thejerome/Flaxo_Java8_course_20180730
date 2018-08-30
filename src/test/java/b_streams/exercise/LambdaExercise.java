package b_streams.exercise;

import b_streams.data.Person;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaExercise {

    @Test
    public void supply() {
        final Person person = new Person("John", "Galt", 30);

        // Return person from Supplier
        final Supplier<Person> getPerson = () -> person;

        assertEquals(person, getPerson.get());
    }

    @Test
    public void function() {

        // Get the name of person using expression lambda
        final Function<Person, String> getPersonName1 = person -> person.getFirstName();

        // Get the name of person using method reference
        final Function<Person, String> getPersonName2 = Person::getFirstName;

        // Get the name of person and log it to System.out using statement lambda: {}
        final Function<Person, String> getPersonNameAndLogIt = person -> {
            System.out.println(person.getFirstName());
            return person.getFirstName();
        };

        final Person person = new Person("John", "Galt", 30);

        assertEquals(person.getFirstName(), getPersonName1.apply(person));
        assertEquals(person.getFirstName(), getPersonName2.apply(person));
        assertEquals(person.getFirstName(), getPersonNameAndLogIt.apply(person));
    }

    @Test
    public void combineFunctions() {

        // Get the name of person
        final Function<Person, String> getPersonName = Person::getFirstName;

        assertEquals("John", getPersonName.apply(new Person("John", "Galt", 30)));

        // Get string length
        final Function<String, Integer> getStringLength = String::length;

        assertEquals(Integer.valueOf(3), getStringLength.apply("ABC"));

        // Get person name length using getPersonName and getStringLength without andThen
        final Function<Person, Integer> getPersonNameLength1 =
                person -> getStringLength.apply(getPersonName.apply(person));

        // Get person name length using getPersonName and getStringLength with andThen
        final Function<Person, Integer> getPersonNameLength2 =
                person -> getPersonName.andThen(getStringLength).apply(person);

        final Person person = new Person("John", "Galt", 30);

        assertEquals(Integer.valueOf(4), getPersonNameLength1.apply(person));
        assertEquals(Integer.valueOf(4), getPersonNameLength2.apply(person));
    }

    private interface PersonFactory {
        Person create(String name, String lastName, int age);
    }

    private Person createPerson(PersonFactory pf) {
        return pf.create("John", "Galt", 66);
    }

    // ((T -> R), (R -> boolean)) -> (T -> boolean)
    private <T, R> Predicate<T> combine(Function<T, R> f, Predicate<R> p) {
        return t -> p.test(f.apply(t));
    }

    // Use only method references here
    @Test
    public void methodReference() {

        final Person person = createPerson(Person::new);

        assertEquals(new Person("John", "Galt", 66), person);

        final Function<Person, String> getPersonName = Person::getFirstName;

        assertEquals("John", getPersonName.apply(person));

        // Using method reference check that "John" equals string parameter
        final Predicate<String> isJohnString = "John"::equals;

        final Predicate<Person> isJohnPerson = combine(getPersonName, isJohnString);

        assertEquals(true, isJohnPerson.test(person));
    }

}
