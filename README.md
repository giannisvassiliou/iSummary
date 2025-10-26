# 🧠 iSummary: Workload-based Selective Summaries for Knowledge Graph Exploration

![Java](https://img.shields.io/badge/Java-16%2B-blue?style=flat-square)
![Build](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-lightgrey?style=flat-square)
![Paper](https://img.shields.io/badge/paper-Semantic_Web_Journal_2025-lightblue?style=flat-square)

> Official repository for the paper  
> **“iSummary: Workload-based Selective Summaries for Knowledge Graph Exploration”**  
> *Semantic Web Journal (IOS Press), 2025*  
>  
> **Authors:** Giannis Vassiliou, Nikolaos Papadakis, Haridimos Kondylakis  
>  
> **Affiliations:**  
> - Electrical and Computer Engineering, Hellenic Mediterranean University, Greece  
> - Computer Science Department, University of Crete & FORTH-ICS, Greece  

---

## 📘 Overview

**iSummary** introduces a *workload-based, scalable, and adaptive* method for generating **selective semantic summaries** over large Knowledge Graphs (KGs).  
Unlike static approaches that process the entire KG, iSummary leverages **query logs** to extract user-driven relevance and relationships — enabling **fast, context-aware summaries** tailored to user needs.

---

### 🔍 Problem Motivation

- Knowledge Graphs (KGs) now contain **billions of RDF triples**, making direct exploration impractical.  
- Existing summarization techniques are **static** and **structure-based**, often ignoring how users actually explore data.  
- iSummary instead uses **query workloads** (SPARQL query logs) to guide summarization toward what users really query.

---


## 🧪 Experimental Evaluation

### **Datasets**

| Dataset | #Queries | Graph Size | Source |
|----------|-----------|------------|---------|
| DBpedia v3.8 | 58,610 | 2.3 M instances | [DBpedia SPARQL Endpoint](https://dbpedia.org/sparql) |
| Wikidata | 192,325 | 1.4 B statements | [Wikidata Query Logs](https://www.wikidata.org/wiki/Wikidata:SPARQL_query_service/queries) |

### **Setup**

- Java 17 (OpenJDK)  
- Intel i3-10100 @ 3.6 GHz, 16 GB RAM  
- Windows 10 environment  

### **Metrics**

- 🧩 **Coverage** — summary completeness  
- ⚡ **Execution time** — summarization efficiency  
- 📈 **Summary size efficiency** — compactness and relevance

### **Results Summary**

- 🚀 iSummary produces summaries **up to 40× faster** than state-of-the-art baselines.  
- 📊 Achieves **higher coverage** on both DBpedia and Wikidata workloads.  
- 🔁 Demonstrates **linear scalability** with increasing query log size.

---

## ⚡ Installation & Usage

### **Requirements**

- **Java 16+**

Check installation:
```bash
java -version
## ▶️ Running iSummary

Run the provided JAR file with:

```bash
java -jar isummary.jar <testdata> <traindata> <node_ranking> <top_k> <choose_from>
```

| Argument | Description |
|-----------|-------------|
| `<testdata>` | Test portion of query log |
| `<traindata>` | Training portion of query log |
| `<node_ranking>` | File containing ranked nodes by query frequency |
| `<top_k>` | Minimum number of nodes in the summary |
| `<choose_from>` | Number of top-ranked nodes to choose seed from |

### **Example**
```bash
java -jar isummary.jar w20test.tsv w80train.tsv dballnodes.txt 5 20
```

---

## 📂 Data Files

| File | Description |
|------|--------------|
| `w20test.tsv` | DBpedia, Test portion of query log |
| `w80train.tsv` | DBpedia, Training portion (used for summary construction) |
| `dballnodes.txt` | Ranking of DBpedia nodes by frequency |
| https://drive.google.com/file/d/146VSxhCMnm1Qn7ySenRp9i9JERNEtSW8/view?usp=sharing 'train.txt', 'test.txt','nodes2.txt;  | Wikidata train-test portion and ranking of nodes
---

## 🧩 System Architecture

```
+-------------------------------+
|         Query Log Q           |
+-------------------------------+
             |
             v
+-------------------------------+
|  iSummary Algorithm           |
|  - Filter queries by seeds    |
|  - Compute node weights       |
|  - Extract frequent patterns  |
+-------------------------------+
             |
             v
+-------------------------------+
|  (λ, κ)-Selective Summary     |
|  - Compact, interpretable     |
|  - Relevance-driven subgraph  |
+-------------------------------+
```



## 🤝 Authors & Contact

**Giannis Vassiliou** — Hellenic Mediterranean University  
📧 [giannisvas@ics.forth.gr](mailto:giannisvas@ics.forth.gr)

**Nikolaos Papadakis** — Hellenic Mediterranean University  
📧 [npapadak@cs.hmu.gr](mailto:npapadak@cs.hmu.gr)

**Haridimos Kondylakis** — University of Crete & FORTH-ICS  
📧 [kondylak@ics.forth.gr](mailto:kondylak@ics.forth.gr)

---

## 📜 License

Released under the [MIT License](LICENSE).  
Free to use, modify, and distribute for academic and research purposes.
