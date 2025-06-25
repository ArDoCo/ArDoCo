
There are currently three kinds of TLR approaches that we describe in their corresponding sections:

* [Software Architecture Documentation (SAD) to Model (SAM)](#sad-sam)
* [Software Architecture Model (SAM) to Code](#sam-code)
* [SAD to Code via SAM](#sad-sam-code)


## SAD-SAM

For Traceability Link Recovery between Software Architecture Documentations (SADs) and Software Architecture Models (SAMs), we use a pipeline approach with different major processing steps:

1. Model Extraction: Processes the architecture model.
2. Text Preprocessing: Processes the text initially, including basic text processing like tokenization, part-of-speech tagging, dependency parsing.
3. Text Extraction: Identification of potential parts of interest in the text.
4. Recommendation Generator: Further processing of interesting parts of text to generate recommendations for parts that should/could be model elements.
5. Connection Generator: Mapping of recommended parts to model parts.

## SAM-Code

The project ARCOTL (Architecture-Code-Trace Links) aims to automatically generate trace links between Code and a Software Architecture Model (SAM).
It supports multiple programming languages for the code (Java and Shell) and metamodels for the architecture model (PCM and UML).
To this end the project introduces intermediate models for the architecture (AMTL - Architecture Model for Trace Links) and Code (CMTL - Code Model for Trace
Links).
Trace links are created between instances of these metamodels.
The trace links each have exactly one architecture endpoint and one code endpoint. This is specified by the TLM (Trace Link Model).
The AMTL- and CMTL-instances get extracted from the architecture model and from the code.

## SAD-SAM-Code

To recover trace links between SADs and code, we combine the traceability link recovery between [SAD-SAM](#sad-sam) and [SAM-Code](#sam-code).
Both approaches are executed, and their resulting trace links combined via `TransitiveTraceLinks` that match the parts of the documentation to the parts of code using the model.
