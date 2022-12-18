# iSummary: Workload-based, Personalized summaries for Knowledge Graphs
The explosion in the size and the complexity of the available Knowledge Graphs on the web has led to the need for efficient
and effective methods for their understanding and exploration. Semantic
summaries have recently emerged as methods to quickly explore and understand the contents of various sources. However, in most cases, they are
static, not incorporating user needs and preferences, and cannot scale.
In this paper, we present iSummary, a novel, scalable approach for constructing personalized summaries. As the size and the complexity of the
Knowledge Graphs for constructing personalized summaries prohibit efficient summary construction, in our approach we exploit query logs.
The main idea behind our approach is to exploit knowledge captured in
existing user queries for identifying the most interesting resources and
linking them, constructing as such high-quality, personalized summaries.
We present an algorithm with theoretical guarantees on the summary’s
quality, linear in the number of queries available in the query log. We
evaluate our approach using three real-world datasets and several baselines, showing that our approach dominates other methods in terms of
both quality and efficiency.

## Datasets section

## Installation Requiremetns

####          java_code : The code to run the summary creator for dbpedia (can be used for other datasets too)
####          data : The data including the train-test set and rank of DBPedia nodes based on frequencies in the query log

#####  ***********         APACHE Jena required
#####  ***********         Java 18 compatible code

 

