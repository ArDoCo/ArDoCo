# ArDoCo

<p align="center">
 <img alt="ArDoCo" src="https://github.com/ArDoCo/.github/raw/main/profile/logo.png" height="210"/>
</p>

ArDoCo (Architecture Documentation Consistency) is a framework to connect architecture documentation and models while
identifying missing or deviating elements (inconsistencies). An element can be any representable item of the model, like
a component or a relation. To do so, ArDoCo first creates trace links and then makes use of them and other information
to identify inconsistencies.

You can find ArDoCo on the [website](https://ardoco.de) and [on GitHub](https://github.com/ArDoCo).

Before contributing, please read the [Quickstart Guide](quickstart).

<!-- JavaDocs can be found [here](https://ardoco.github.io/Core-Docs/). -->

To get to know the project, please read the following pages:

* [Core Pipeline Definition](pipeline)
* [Intermediate Artifacts](intermediate-artifacts)
* [Text Preprocessing Microservice](Text-Preprocessing-Microservice)
* [Traceability Link Recovery (TLR)](traceability-link-recovery)
* [Inconsistency Detection (ID)](inconsistency-detection)
* [Linking Sketches and Software Architecture (LiSSA)](LiSSA)

## Project Structure

* [Core](https://github.com/ArDoCo/Core): Core framework with framework and API definitions
* Pipelines
  * [TLR](https://github.com/ArDoCo/TLR): Traceability Link Recovery (TLR) Modules
  * [StanfordCoreNLP-Provider-Service](https://github.com/ArDoCo/StanfordCoreNLP-Provider-Service): RESTful web service for text preprocessing
  * [InconsistencyDetection](https://github.com/ArDoCo/InconsistencyDetection): Inconsistency Detection (ID) Modules
  * [LiSSA](https://github.com/ArDoCo/LiSSA): Linking Sketches and Software Architecture Modules
* Testing and Evaluation
  * [IntegrationTests](https://github.com/ArDoCo/IntegrationTests): Integration Tests
  * [Benchmark](https://github.com/ArDoCo/Benchmark): Benchmarks
  * [Evaluator](https://github.com/ArDoCo/Evaluator): Evaluation code that compares CSVs (e.g., output and gold standard)
  * [SimpleTracelinkDiscovery](https://github.com/ArDoCo/SimpleTracelinkDiscovery): Baseline approach
* GUIs, CLIs, etc.
  * [TraceView](https://github.com/ArDoCo/TraceView): WIP visualisation of the outputs for TLR and ID
  * *outdated* [CLI](https://github.com/ArDoCo/CLI): Command Line Interface (*outdated*)
* [actions](https://github.com/ArDoCo/actions): Reusable GitHub Actions

## System Requirements

The project requires **JDK 21**.
Furthermore, we advise at least **4 GB of RAM**.

## Benchmarks

You can test ArDoCo using the projects provided in our [Benchmark repository](https://github.com/ArDoCo/Benchmark).

## Related Publications

* J. Keim, S. Corallo, D. Fuchß, T. Hey, T. Telge und A. Koziolek. "Recovering Trace Links Between Software Documentation And Code". 2024. In: Proceedings of 46th IEEE International Conference on Software Engineering (ICSE 2024). [doi:10.5445/IR/1000165692](https://doi.org/10.5445/IR/1000165692/post)

* J. Keim, S. Corallo, D. Fuchß und A. Koziolek. "Detecting Inconsistencies in Software Architecture Documentation Using Traceability Link Recovery". 2023. In: IEEE 20th International Conference on Software Architecture (ICSA 2023). [doi:10.1109/ICSA56044.2023.00021](https://doi.org/10.1109/ICSA56044.2023.00021)

* D. Fuchß, S. Corallo, J. Keim, J. Speit und A. Koziolek. "Establishing a Benchmark Dataset for Traceability Link Recovery between Software Architecture Documentation and Models". 2022. In: 2nd International Workshop on Mining Software Repositories for Software Architecture - Co-located with 16th European Conference on Software Architecture.

* J. Keim, S. Schulz, D. Fuchß, C. Kocher, J. Speit, A. Koziolek. "Trace Link Recovery for Software Architecture Documentation". 2021. In: Software Architecture: 15th European Conference (ECSA 2021). [doi:10.1007/978-3-030-86044-8_7](https://doi.org/10.1007/978-3-030-86044-8_7)

* J. Keim and A. Koziolek. "Towards Consistency Checking Between Software Architecture and Informal Documentation". 2019. In: IEEE 16th International Conference on Software Architecture Companion (ICSA-C). [doi:10.1109/ICSA-C.2019.00052](https://doi.org/10.1109/ICSA-C.2019.00052)


The initial version of ArDoCo is based on the master thesis [Linking Software Architecture Documentation and Models](https://publikationen.bibliothek.kit.edu/1000126194).

## Contact

This project is currently developed by researchers of the Karlsruhe Institute of Technology (KIT).

You find us on our websites:

* [Jan Keim](https://mcse.kastel.kit.edu/staff_Keim_Jan.php),
* [Sophie Corallo](https://mcse.kastel.kit.edu/staff_sophie_corallo.php), and
* [Dominik Fuchß](https://mcse.kastel.kit.edu/staff_dominik_fuchss.php)
