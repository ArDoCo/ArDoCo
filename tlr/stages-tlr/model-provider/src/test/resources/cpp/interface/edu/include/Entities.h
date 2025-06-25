// Entities.h
#ifndef ENTITIES_H
#define ENTITIES_H
#include <iostream>
#include <string>

namespace Entities {
    class Car {
    private:
        std::string make;
        std::string model;
        int year;

    public:
        Car(std::string make, std::string model, int year);
        std::string displayInfo() const;
        friend std::ostream& operator<<(std::ostream& os, const Car& car);
    };

    class Person {
    private:
        std::string name;
        int age;
        Car* ownedCar;

    public:
        Person(std::string name, int age);
        void displayInfo() const;
        void buyCar(Car& car);
    };

    class Garage {
    public:
        class Mechanic {
        private:
            std::string mechanicName;
        public:
            Mechanic(std::string name);
            void repairCar(Car& car);
        };
    };

    class Child : public Person {
    public:
        Child(std::string name, int age);
    };
}

#endif // ENTITIES_H