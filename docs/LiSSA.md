# Linking Sketches and Software Architecture (LiSSA)

The LiSSA approach aims to connect sketches and informal diagrams (such as class diagrams, component diagrams, ...) with
formal models like component models.

The following diagram shows the pipeline that is planned for the LiSSA approach.

```mermaid
stateDiagram-v2
    DiagramDetection
    TextPreprocessing
    ArchitectureModel
    TextExtraction
    EntityRecognition
    RecommendationGeneration
    ConnectionGeneration
    InconsistencyDetection

    DiagramDetection --> RecommendationGeneration
    TextPreprocessing --> TextExtraction
    ArchitectureModel --> RecommendationGeneration
    TextExtraction --> EntityRecognition
    DiagramDetection --> EntityRecognition
    EntityRecognition --> RecommendationGeneration
    RecommendationGeneration --> ConnectionGeneration
    ConnectionGeneration --> InconsistencyDetection
```