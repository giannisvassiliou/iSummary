# TO BE UPDATED 
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



## Installation Requiremetns
JAVA 16 and newer is required


####          Data : The data including the train-test set and rank of DBPedia nodes based on frequencies in the query log
w20test.tsv
w80train.tsv
dballnodes.txt 

##### HOW TO RUN THE PROVIDED JAR FILE ?

You have to run in with java.  
#### java -jar isummary testdatafilename traindatafilename nodes_ranking #how_many_nodes #choose_from
##### testdatafilename  is the filename of the query portion to test the final summary
##### traindatafilename is the filename of the query portion to train (to construct the summary)
##### nodes_ranking is the filename of the descending ranking of the nodes in the query log based on their frequency of presence
##### how_many_nodes is a number to tell the system how many nodes will (at least) the final summary will have
##### choose_from for the nodes_ranking file, you should use the first #choose_from of them


 

