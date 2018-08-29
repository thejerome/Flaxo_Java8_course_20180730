package b_streams.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import b_streams.data.Person;

public class LambdaExercise {

    @Test
    public void supply() {
        final Person person = new Person("John", "Galt", 30);

        final Supplier<Person> getPerson = () -> new Person("John", "Galt", 30);
        assertEquals(person, getPerson.get());
    }

    @Test
    public void function() {
        final Function<Person, String> getPersonName1 = person -> person.getFirstName();

        final Function<Person, String> getPersonName2 = Person::getFirstName;

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
        final Function<Person, String> getPersonName = Person::getFirstName;

        assertEquals("John", getPersonName.apply(new Person("John", "Galt", 30)));

        final Function<String, Integer> getStringLength = String::length;

        assertEquals(Integer.valueOf(3), getStringLength.apply("ABC"));

        final Function<Person, Integer> getPersonNameLength1 = person -> getStringLength.apply(getPersonName.apply(person));

        final Function<Person, Integer> getPersonNameLength2 = person -> getPersonName.andThen(getStringLength).apply(person);

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

    @Test
    public void methodReference() {
        final Person person = createPerson(Person::new);

        assertEquals(new Person("John", "Galt", 66), person);

        final Function<Person, String> getPersonName = Person::getFirstName;

        assertEquals("John", getPersonName.apply(person));

        final Predicate<String> isJohnString = Objects::nonNull;

        final Predicate<Person> isJohnPerson = combine(getPersonName, isJohnString);

        assertEquals(true, isJohnPerson.test(person));
    }

}
