package org.kelava.examples.relfection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Zoo {

    private String city;
    private String name;
    private List<Animal> animals;

    public Zoo(String city, String name) {
        this.city = city;
        this.name = name;
        animals = new ArrayList<>();
    }

    public void add(Animal animal) {
        Objects.requireNonNull(animal, "Animal must not be null");
        animals.add(animal);
    }
}
