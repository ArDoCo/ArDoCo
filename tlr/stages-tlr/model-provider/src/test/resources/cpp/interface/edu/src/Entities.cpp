
// Entities.cpp
#include "Entities.h"

namespace Entities {
    Car::Car(std::string make, std::string model, int year) : make(make), model(model), year(year) {}

    std::string Car::displayInfo() const {
        return "Car: " + std::to_string(year) + " " + make + " " + model;
    }

    std::ostream& operator<<(std::ostream& os, const Car& car) {
        os << car.displayInfo();
        return os;
    }

    Person::Person(std::string name, int age) : name(name), age(age), ownedCar(nullptr) {}

    void Person::displayInfo() const {
        std::cout << "Person: " << name << ", Age: " << age << std::endl;
    }

    void Person::buyCar(Car& car) {
        ownedCar = &car;
        std::cout << name << " bought a " << car << std::endl;
    }

    Garage::Mechanic::Mechanic(std::string name) : mechanicName(name) {}

    void Garage::Mechanic::repairCar(Car& car) {
        std::cout << mechanicName << " is repairing the " << car << std::endl;
    }

    Child::Child(std::string name, int age): Person(name, age) {}
}