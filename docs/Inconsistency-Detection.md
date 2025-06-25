
Currently, there are two kinds of inconsistencies that are supported by the approach: Missing Model Elements (MMEs) and Undocumented Model Elements (UMEs).

Undocumented Model Elements (UMEs) are elements within the Software Architecture Model (SAM) that are not documented in the natural language Software Architecture Documentation (SAD).
Our heuristic looks for model elements that do not have any (or below a certain threshold, per default 1) trace links associated with them.
In the configuration options, you can fine tune the threshold as well as set up a regex-based whitelist.

Missing Model Elements (MMEs) are architecture elements that are described within the SAD that cannot be traced to the SAM.
For this, we make use of the recommendations from the Recommendation Generator within the [Traceability Link Recovery (TLR)](traceability-link-recovery).
Each of these recommendations that are not linked with a model element are potential inconsistencies.
To further increase precision, we make use of filters.
For example, we use a filter to get rid of commonly used software (development) terminology that look similar to, e.g., components but rarely are model elements.
