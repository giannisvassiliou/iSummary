
# iSummary:Workload-based Selective Summaries for Knowledge Graph Exploration

The rapid growth in size and complexity of Knowledge Graphs available on the web has created a pressing need for
efficient and effective methods to facilitate their understanding and exploration. Recently, semantic summaries have emerged as a
means to quickly comprehend and explore them. However, most existing approaches are static, failing to adapt to user needs and
preferences, and often struggle to scale. In this paper, we introduce iSummary, a novel and scalable approach for constructing
summaries given specific user requests in terms of nodes for the summary to be based on. Given that the size and complexity
of Knowledge Graphs pose challenges to efficient summary construction, our approach leverages query logs. The core idea is
to harness the knowledge embedded in existing user queries to identify the most relevant resources and establish meaningful
connections, thereby generating high-quality summaries. We propose an algorithm with theoretical guarantees on summary
quality, operating linearly with respect to the number of queries in the log. To assess our method, we conduct experiments using
two real-world datasets and multiple baselines, demonstrating that iSummary consistently outperforms existing techniques in
both quality and efficiency


## Installation Requiremetns
JAVA 16 and newer is required


###          Data : The data including the train-test set and rank of DBPedia nodes based on frequencies in the query log
#### w20test.tsv -- the test portion of query log
#### w80train.tsv -- the construct (train) portion of the query log
#### dballnodes.txt  -- rank of nodes present in query log

## HOW TO RUN THE PROVIDED JAR FILE ?

### You have to run it with java.  
## java -jar isummary testdatafilename traindatafilename nodes_ranking #top_k #choose_from
#### 'testdatafilename'  is the filename of the query log portion to test the final summary
#### 'traindatafilename' is the filename of the query log  portion to train (to construct the summary)
#### 'nodes_ranking' is the filename of the descending ranking of the nodes in the query log based on their frequency of presence
#### #top_k is a number to tell the system how many nodes will (at least) the final summary will have for the initial node provided
#### choose ONE NODE from the first #choose_from nodes in the nodes_ranking file,  as initial node to connect it with its top_k
## e.g. java -jar isummary.jar w20test.tsv w80train.tsv dballnodes.txt 5 20
### will result to a result_sum.txt file


 

