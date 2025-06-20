# ArDoCo

The goal of the ArDoCo project is to connect architecture documentation and models with Traceability Link Recovery (TLR) while identifying missing or deviating elements (inconsistencies).
An element can be any representable item of the model, like a component or a relation.
To do so, we first create trace links and then make use of them and other information to identify inconsistencies.

ArDoCo is actively developed by researchers of the _[Modelling for Continuous Software Engineering (MCSE) group](https://mcse.kastel.kit.edu)_ of _[KASTEL - Institute of Information Security and Dependability](https://kastel.kit.edu)_ at the [KIT](https://www.kit.edu).

This **Core** repository contains the framework and core definitions for the other approaches.
As such, there is the definition of our pipeline and the data handling as well as the definitions for the various pipeline steps, inputs, outputs, etc.

For more information about the setup, the project structure, or the architecture, please have a look at the [Wiki](https://github.com/ArDoCo/Core/wiki).

## Maven

```xml

<dependencies>
	<dependency>
		<groupId>io.github.ardoco.core</groupId>
		<artifactId>framework</artifactId> <!-- or any other subproject -->
		<version>VERSION</version>
	</dependency>
</dependencies>
```

## Relevant repositories
The following is an excerpt of repositories that use this framework and implement the different approaches and pipelines of ArDoCo:
* [ArDoCo/Core](https://github.com/ArDoCo/Core): implementing the core of the approach
* [ArDoCo/TLR](https://github.com/ArDoCo/TLR): implementing different traceability link recovery approaches
* [ArDoCo/InconsistencyDetection](https://github.com/ArDoCo/InconsistencyDetection): implementing inconsistency detection approaches

### Pull
* git subtree pull --prefix=core git@github.com:ArDoCo/Core feature/v2
* git subtree pull --prefix=tlr git@github.com:ArDoCo/TLR feature/v2
* git subtree pull --prefix=inconsistency-detection git@github.com:ArDoCo/InconsistencyDetection feature/v2

### Push
* git subtree push --prefix=core git@github.com:ArDoCo/Core feature/v2
* git subtree push --prefix=tlr git@github.com:ArDoCo/TLR feature/v2
* git subtree push --prefix=inconsistency-detection git@github.com:ArDoCo/InconsistencyDetection feature/v2
