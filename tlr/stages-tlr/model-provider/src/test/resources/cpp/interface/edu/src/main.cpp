/*
 * Simple C++ Project
 * Author: Your Name
 * Description: A basic C++ project with a simple structure.
 */

// main.cpp
#include <iostream>
#include "Entities.h"

int main() {
    // Create a Car object
    Entities::Car myCar("Toyota", "Corolla", 2022);
    myCar.displayInfo();

    // Create a Person object
    Entities::Person person("John Doe", 30);
    person.displayInfo();
    person.buyCar(myCar);

    return 0;
}