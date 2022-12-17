package fan;

import fan.disjksta.*;

import java.io.BufferedReader;
import java.io.File; // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.logging.log4j.core.util.SystemNanoClock;

public class UrlDecode6X3NonStrictvars_dbpedia {
	
	
//	public static String replacer(StringBuffer outBuffer) {
//
//	    String data = outBuffer.toString();
//	    try {
//	        StringBuffer tempBuffer = new StringBuffer();
//	        int incrementor = 0;
//	        int dataLength = data.length();
//	        while (incrementor < dataLength) {
//	            char charecterAt = data.charAt(incrementor);
//	            if (charecterAt == '%') {
//	                tempBuffer.append("<percentage>");
//	            } else if (charecterAt == '+') {
//	                tempBuffer.append("<plus>");
//	            } else {
//	                tempBuffer.append(charecterAt);
//	            }
//	            incrementor++;
//	        }
//	        data = tempBuffer.toString();
//	        data = URLDecoder.decode(data, "utf-8");
//	        data = data.replaceAll("<percentage>", "%");
//	        data = data.replaceAll("<plus>", "+");
//	    } catch(Exception e) {
//	        e.printStackTrace();
//	    }
//	    return data;
//	}
//	
	
	static int poss = 80;
	// static String trainfilestring="fandata/yagoallnew.tsv";
	// static String trainnodesdesc="fandata/yagoallnodesf2.txt";
	static FileWriter resuli = null;
	// static String trainnodesdesc="fandata/nodes1train.txt";
	static int topk = 8;
	static int tttt = 10;
	static int lathos = 0;
	// static String trainnodesdesc="fandata/wikidata/train/w"+poss+"train.txt";
	// static String trainfilestring="fandata/wikidata/w"+poss+"train.tsv";

	// static String testingstring="fandata/wikidata/w"+(100-poss)+"test.tsv";

	static String testingstring = "fandata/dbpedia/w" + (100 - poss) + "test.tsv";

	static String trainfilestring = "fandata/dbpedia/w" + poss + "train.tsv";
	static String trainnodesdesc = "fandata/dballnodesf.txt";
	static HashMap<String, Integer> hm;
	static HashMap<String, HashSet<String>> pred;
	static HashSet<NodeWeighted> nn;
	static HashMap<String, Integer> reso = null;
	static HashSet<String> summary = new HashSet<String>();
	static HashSet<String> visited = new HashSet<String>();
	static String ooriginal = "";
	static HashMap<String, HashSet<String>> variables;;
	static float nnode = 0;
	static float nedge = 0;
	static float exein = 0;
	static float exeie = 0;
	static float totcovn = 0;
	static float totcove = 0;
	static float totcovnr = 0;
	static float totcover = 0;
	static float totr;
	static float totc;

	static int countFreq(String pat, String txt) {
		int M = pat.length();
		int N = txt.length();
		int res = 0;

		/* A loop to slide pat[] one by one */
		for (int i = 0; i <= N - M; i++) {
			/*
			 * For current index i, check for pattern match
			 */
			int j;
			for (j = 0; j < M; j++) {
				if (txt.charAt(i + j) != pat.charAt(j)) {
					break;
				}
			}

			// if pat[0...M-1] = txt[i, i+1, ...i+M-1]
			if (j == M) {
				res++;
				j = 0;
			}
		}
		return res;
	}

	public static boolean check2times(ArrayList<String> rett, String check) {

		int total = 0;
		for (String ct : rett) {

			if (ct.contains(check) && (countFreq("<?", ct) == 1)) {
				total++;
			}
		}
		if (total == 2)
			return true;
		else
			return false;
	}

	public static <T> T mostCommon(List<T> list) {
		Map<T, Integer> map = new HashMap<>();

		for (T t : list) {
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}

		Entry<T, Integer> max = null;

		for (Entry<T, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}

	public static void randomtest(int nonodes, int noedges, String original, HashSet<String> nodes,
			HashSet<String> edges) throws FileNotFoundException {

		int total = 0;

		String pre = "";
		int counter = 0;

		// for (String rr : nodes)
		// System.out.println(" NODA " + rr);
		//
		// for (String rr : edges)
		// System.out.println(" Edga " + rr);

		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		int chavenodes = 1;
		int chaveedges = 0;
		float totpn = 0;
		float totpe = 0;

		HashSet<String> havenodes = new HashSet<String>();
		HashSet<String> haveedges = new HashSet<String>();
		while (myReader.hasNextLine()) {
			GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();
			// System.out.println(data);

			String[] splited = data.split("\t");
			data = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);
			// System.out.println(result);
			data = data.replace('\n', ' ');
			data = data.replace("SELECT", " SELECT ");
			data = data.replace("WHERE", " WHERE");
			data = data.replace("{", " { ");
			data = data.replace("*", " ");
			// data = data.replace("(", "[");
			// data = data.replace(")", "]");
			// data=data.replace("|", ";");
			data = data.replace(" / ", ";");
			int nooccu = data.split("WHERE", -1).length - 1;
			float percnodes = 0;
			float perceddes = 0;

			if ((nooccu == 1) && (data.contains(original))) {

				try {

					// System.out.println("NEW QUERY \n\n\n"+data);
					Query query = QueryFactory.create(data);

					total++;
					nnode = 0;
					nedge = 0;
					exein = 0;
					exeie = 0;

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...
							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el) {
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();

									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										// objects.add(cur.getObject());
										// subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();

										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										// System.out.println(sub+" OBJ "+obj);

										if ((!sub.contains("?")) && (!havenodes.contains(sub)))
											havenodes.add(sub);

										if ((!obj.contains("?")) && (!havenodes.contains(obj)))
											havenodes.add(obj);

										Node pree = cur.getPredicate();
										if (pree != null) {
											String pre = "<" + cur.getPredicate().toString() + ">";
											if ((!pre.contains("?")) && (!haveedges.contains(pre)))
												haveedges.add(pre);

										}

									}

								}
							});

				} catch (Exception e) {

				}

			}
		}
		Random rand = new Random();

		// Generate random integers in range 0 to 999
		// int rand_int1 = rand.nextInt(nonodes)+1;

		// System.out.println("SIZE NODES "+havenodes.size()+" SIZE EDGES
		// "+haveedges.size());

		nodes.add(original);

		String[] arrayNodes = havenodes.toArray(new String[havenodes.size()]);
		String[] arrayEdges = haveedges.toArray(new String[haveedges.size()]);

		nodes.add(ooriginal);
		int chances = 0;
		while (chavenodes < nonodes) {
			int r = rand.nextInt(havenodes.size());
			chances++;
			if (chances == 500)
				break;
			if (arrayNodes[r].contains("<http") && (!arrayNodes[r].contains("rdf#serviceParam"))
					&& (!nodes.contains(arrayNodes[r]))) {
				nodes.add(arrayNodes[r]);
				chavenodes++;
			}
		}

		chances = 0;
		while (chaveedges < noedges) {
			int r = rand.nextInt(haveedges.size());
			chances++;
			if (chances == 500)
				break;
			if (arrayEdges[r].contains("<http") && (!edges.contains(arrayEdges[r]))) {
				edges.add(arrayEdges[r]);
				chaveedges++;
			}
		}

		System.out.println(" CUR " + nodes.size() + " EN " + edges.size());

	}

	public static void analyseSparqltest(HashSet<String> nodes, HashSet<String> edges, String original, int typ)
			throws FileNotFoundException {

		int total = 0;

		String pre = "";
		int counter = 0;
		//
		for (String rr : nodes)
			System.out.println(" NODA " + rr);

		for (String rr : edges)
			System.out.println(" Edga " + rr);

		// File myObj = new File("fandata/1test.tsv");

		File myObj = new File(testingstring);

		Scanner myReader = new Scanner(myObj);

		float totpn = 0;
		float totpe = 0;
		while (myReader.hasNextLine()) {
			GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();
			// System.out.println(data);

			String[] splited = data.split("\t");
			data = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);
			// System.out.println(result);
			data = data.replace('\n', ' ');
			data = data.replace("SELECT", " SELECT ");
			data = data.replace("WHERE", " WHERE");
			data = data.replace("{", " { ");
			data = data.replace("*", " ");
			// data = data.replace("(", "[");
			// data = data.replace(")", "]");
			// data=data.replace("|", ";");
			data = data.replace(" / ", ";");
			int nooccu = data.split("WHERE", -1).length - 1;
			float percnodes = 0;
			float perceddes = 0;
			if ((nooccu == 1) && (data.contains(original))) {

				try {

					// System.out.println("NEW QUERY \n\n\n"+data);
					Query query = QueryFactory.create(data);

					total++;
					nnode = 0;
					nedge = 0;
					exein = 0;
					exeie = 0;
					HashSet<String> havenodes = new HashSet<String>();
					HashSet<String> haveedges = new HashSet<String>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...
							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el) {
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();

									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										// objects.add(cur.getObject());
										// subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();

										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										// System.out.println(sub+" OBJ "+obj);

										if ((!sub.contains("?")) && (!havenodes.contains(sub)))
											havenodes.add(sub);

										if ((!obj.contains("?")) && (!havenodes.contains(obj)))
											havenodes.add(obj);

										Node pree = cur.getPredicate();
										if (pree != null) {
											String pre = "<" + cur.getPredicate().toString() + ">";
											if ((!pre.contains("?")) && (!haveedges.contains(pre)))
												haveedges.add(pre);

										}

									}

								}
							});

					for (String no : havenodes) {
						if (typ == 1)
							System.out.print(no + " ");
						if (nodes.contains(no))
							exein++;

					}
					for (String no : haveedges) {
						if (edges.contains(no))
							exeie++;
					}

					nnode = havenodes.size();
					nedge = haveedges.size();
					// if (typ==1) System.out.println("nnodes "+nnode+ "COV NODES " + exein + " COV
					// EDGES " + exeie );
					if (nnode > 0) {
						totpn = totpn + (exein / nnode);
					}
					if (nedge > 0) {
						totpe = totpe + (exeie / nedge);
					}
					// System.out.println("OLA TA NODES " + nnode + " EXEI " + exein + " OLA TA EDGE
					// " + nedge + " EXIE " + exeie);
				} catch (Exception e) {

				}

			}
		}

		System.out.println("HERE " + totpn + " " + total + " " + totpe);
		if (typ == 1) {
			totcovn += (totpn / total);
			totcove += (totpe / total);
		} else {
			totcovnr += (totpn / total);
			totcover += (totpe / total);
		}

		System.out.println(
				total + " =================>  PER CENT NODES " + (totpn / total) + " PER CENT EDGE " + (totpe / total));

		try {
			if ((totpn / total) > 0)
				resuli.write((totpn / total) + "\t" + (totpe / total) + "\n");
			else
				lathos++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRROR WRITE!");
			System.exit(1);
		}
	}

	static int minpathsofar = 0;

	public static NodeWeighted returnnode(String name) {

		for (NodeWeighted na : nn) {
			if (na.name.equals(name)) {
				return na;
			}

		}
		return null;
	}

	public static node returnNode(Set<node> nodes, String name) {
		for (node s : nodes) {
			if (s.nodename.equals(name))
				return s;

		}
		return null;
	}

	public static final HashSet<String> findallconnections2(String base, String node, String nodeb, int tofind)
			throws FileNotFoundException {
		// tofind 1 node
		// tofind 2 predicate
		String nodd = node;

		// .out.println("NODD " + nodd + " NODE B " + nodeb);
		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		final HashSet<String> results = new HashSet<String>();
		int cou = 0;
		int exe = 0;
		while (myReader.hasNextLine()) {
			// GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();

			String[] splited = data.split("\t");
			String data3 = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);

			data3 = data3.replace('\n', ' ');
			data3 = data3.replace("SELECT", " SELECT ");
			data3 = data3.replace("WHERE", " WHERE");
			data3 = data3.replace("{", " { ");

			// System.out.println("DATA5 " + data3);
			// System.out.println(result);
			if ((data3.contains(nodd) && data3.contains(nodeb)))

			{
				cou++;
				// System.out.println("DATA6 " + data3);

				try {
					Query query = QueryFactory.create(data3);

					/// System.out.println("------- All Variables --------");
					// Remember distinct subjects in this
					final Set<Node> subjects = new HashSet<Node>();
					final Set<Node> objects = new HashSet<Node>();
					final Set<Node> preds = new HashSet<Node>();
					final Set<node> nodes = new HashSet<node>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...

							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el)

							{
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();
									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										objects.add(cur.getObject());
										subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();
										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										if (cur.getPredicate() != null) {

											String pre1 = cur.getPredicate().toString();
											pre1 = "<" + pre1 + ">";
											// String pre = cur.getPredicate().toString();

											if (tofind == 1) {

												if ((sub.equals(nodd))) {
													if (!results.contains(obj) && (!obj.contains("?"))
															&& (!obj.equals(nodd)))
														results.add(obj);
												}
												if ((obj.equals(nodd))) {
													if (!results.contains(sub) && (!sub.contains("?"))
															&& (!sub.equals(nodd)))
														results.add(sub);
												}

												if (pre1.equals(nodeb)) {
													if (!results.contains(sub) && (!sub.contains("?")))
														results.add(sub);

													if (!results.contains(obj) && (!obj.contains("?")))
														results.add(obj);
												}

											}
										} else if (tofind == 2) {
											if ((cur.getPredicate() != null)
													&& (!("<" + cur.getPredicate().toString() + ">").equals(nodd))) {

												if (!results.contains("<" + cur.getPredicate().toString() + ">"))
													results.add("<" + cur.getPredicate().toString() + ">");

											}
										}
									}

								}

							});
				} catch (Exception e) {
					// System.out.println("NEW EXCEPTION !"+e);

					// System.out.println(e.getStackTrace()[0]);

					exe++;
				}
			}

		}
		// System.out.println("exe " + exe + " cou " + cou + "RESULTS " +
		// results.size());

		for (String s : results) {
			// System.out.println("RES "+s);
		}

		return results;
	}

	public static final ArrayList<String> findallconnections4(String base, String node, String nodeb, int tofind,
			int typos) throws FileNotFoundException {
		// tofind 1 node
		// tofind 2 predicate
		String nodd = node;

		// .out.println("NODD " + nodd + " NODE B " + nodeb);
		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		final ArrayList<String> results = new ArrayList<String>();
		int cou = 0;
		int exe = 0;
		while (myReader.hasNextLine()) {
			// GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();

			String[] splited = data.split("\t");
			String data3 = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);

			data3 = data3.replace('\n', ' ');
			data3 = data3.replace("SELECT", " SELECT ");
			data3 = data3.replace("WHERE", " WHERE");
			data3 = data3.replace("{", " { ");

			// System.out.println("DATA5 " + data3);
			// System.out.println(result);
			if ((data3.contains(nodd) && data3.contains(nodeb)))

			{
				cou++;
				// System.out.println("DATA6 " + data3);

				try {
					Query query = QueryFactory.create(data3);

					/// System.out.println("------- All Variables --------");
					// Remember distinct subjects in this
					final Set<Node> subjects = new HashSet<Node>();
					final Set<Node> objects = new HashSet<Node>();
					final Set<Node> preds = new HashSet<Node>();
					final Set<node> nodes = new HashSet<node>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...

							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el)

							{
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();
									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										objects.add(cur.getObject());
										subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();
										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										if (cur.getPredicate() != null) {

											String pre1 = cur.getPredicate().toString();
											pre1 = "<" + pre1 + ">";
											// String pre = cur.getPredicate().toString();

											if (tofind == 1) {
												if ((pre1.equals(nodeb))) {

													// System.out.println("SAL1 "+sub+" "+pre1+" "+obj);
													if ((!obj.contains("?")) && (!obj.equals(nodd))
															&& (!obj.equals(base)))
														results.add(obj);
												}
												if ((pre1.equals(nodeb))) {

													// System.out.println("SAL2 "+sub);
													// System.out.println("SAL2 "+sub+" "+pre1+" "+obj);

													if ((!sub.contains("?")) && (!sub.equals(nodd))
															&& (!sub.equals(base)))
														results.add(sub);
												}
											}
										} else if (tofind == 2) {
											if ((cur.getPredicate() != null)
													&& (!("<" + cur.getPredicate().toString() + ">").equals(nodd))) {

												if (!results.contains("<" + cur.getPredicate().toString() + ">"))
													results.add("<" + cur.getPredicate().toString() + ">");

											}
										}
									}

								}

							});
				} catch (Exception e) {
					// System.out.println("NEW EXCEPTION !"+e);

					// System.out.println(e.getStackTrace()[0]);

					exe++;
				}
			}

		}
		// System.out.println("exe " + exe + " cou " + cou + "RESULTS " +
		// results.size());

		for (String s : results) {
			// System.out.println("RES "+s);
		}

		return results;
	}

	public static final ArrayList<String> findallconnections3(String base, String node, String nodeb, int tofind,
			int typos) throws FileNotFoundException {
		// tofind 1 node
		// tofind 2 predicate
		String nodd = node;

		// .out.println("NODD " + nodd + " NODE B " + nodeb);
		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		final ArrayList<String> results = new ArrayList<String>();
		int cou = 0;
		int exe = 0;
		while (myReader.hasNextLine()) {
			// GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();

			String[] splited = data.split("\t");
			String data3 = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);

			data3 = data3.replace('\n', ' ');
			data3 = data3.replace("SELECT", " SELECT ");
			data3 = data3.replace("WHERE", " WHERE");
			data3 = data3.replace("{", " { ");

			// System.out.println("DATA5 " + data3);
			// System.out.println(result);
			if ((data3.contains(nodd) && data3.contains(nodeb)))

			{
				cou++;
				// System.out.println("DATA6 " + data3);

				try {
					Query query = QueryFactory.create(data3);

					/// System.out.println("------- All Variables --------");
					// Remember distinct subjects in this
					final Set<Node> subjects = new HashSet<Node>();
					final Set<Node> objects = new HashSet<Node>();
					final Set<Node> preds = new HashSet<Node>();
					final Set<node> nodes = new HashSet<node>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...

							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el)

							{
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();
									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										objects.add(cur.getObject());
										subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();
										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										if (cur.getPredicate() != null) {

											String pre1 = cur.getPredicate().toString();
											pre1 = "<" + pre1 + ">";
											// String pre = cur.getPredicate().toString();

											if (tofind == 1) {

												if ((typos == 2) && (sub.equals(nodd)) && ((pre1.equals(nodeb)))) {
													if ((!obj.contains("?")) && (!obj.equals(nodd)))
														results.add(obj);
												} else if ((typos == 1) && (obj.equals(nodd)) && (pre1.equals(nodeb))) {
													if ((!sub.contains("?")) && (!sub.equals(nodd)))
														results.add(sub);
												}

												// if (pre1.equals(nodeb)) {
												// if (!results.contains(sub) && (!sub.contains("?")))
												// results.add(sub);
												//
												// if (!results.contains(obj) && (!obj.contains("?")))
												// results.add(obj);
												// }

											}
										} else if (tofind == 2) {
											if ((cur.getPredicate() != null)
													&& (!("<" + cur.getPredicate().toString() + ">").equals(nodd))) {

												if (!results.contains("<" + cur.getPredicate().toString() + ">"))
													results.add("<" + cur.getPredicate().toString() + ">");

											}
										}
									}

								}

							});
				} catch (Exception e) {
					// System.out.println("NEW EXCEPTION !"+e);

					// System.out.println(e.getStackTrace()[0]);

					exe++;
				}
			}

		}
		// System.out.println("exe " + exe + " cou " + cou + "RESULTS " +
		// results.size());

		for (String s : results) {
			// System.out.println("RES "+s);
		}

		return results;
	}

	public static final HashSet<String> findallconnectionswith2(String base, String node, String check1, String check2,
			int tofind) throws FileNotFoundException {
		// tofind 1 node
		// tofind 2 predicate
		String nodd = node;

		System.out.println("NODD " + nodd);
		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		final HashSet<String> results = new HashSet<String>();
		int cou = 0;
		int exe = 0;
		while (myReader.hasNextLine()) {
			// GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();

			String[] splited = data.split("\t");
			String data3 = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);

			data3 = data3.replace('\n', ' ');
			data3 = data3.replace("SELECT", " SELECT ");
			data3 = data3.replace("WHERE", " WHERE");
			data3 = data3.replace("{", " { ");

			// System.out.println("DATA5 " + data3);
			// System.out.println(result);
			if (data3.contains(nodd))

			{
				cou++;
				// System.out.println("DATA6 " + data3);

				try {
					Query query = QueryFactory.create(data3);

					/// System.out.println("------- All Variables --------");
					// Remember distinct subjects in this
					final Set<Node> subjects = new HashSet<Node>();
					final Set<Node> objects = new HashSet<Node>();
					final Set<Node> preds = new HashSet<Node>();
					final Set<node> nodes = new HashSet<node>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...

							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el)

							{
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();
									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										objects.add(cur.getObject());
										subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();
										sub = "<" + sub + ">";
										obj = "<" + obj + ">";

										if (cur.getPredicate() != null) {
											// System.out.print("T");
											String pra = "<" + cur.getPredicate().toString() + ">";
											if (pra.equals(nodd)) {
												String pre1 = cur.getPredicate().toString();
												System.out.println("PAOK " + nodd);
												pre1 = "<" + pre1 + ">";
												;
												// String pre = cur.getPredicate().toString();

												if (tofind == 1) {
													System.out.println("KALOS");

													HashSet<String> v1 = variables.get(check1);
													HashSet<String> v2 = variables.get(check2);
													System.out.println("SIZE 1 " + v1.size() + " SIZE2 " + v2.size());

													for (String t : v1) {
														System.out.println(t);
													}
													System.out.println("KOKO");
													for (String t : v2) {
														System.out.println(t);
													}
													System.exit(1);
												} else if (tofind == 2) {
													if ((cur.getPredicate() != null)
															&& (!("<" + cur.getPredicate().toString() + ">")
																	.equals(nodd))) {

														if (!results
																.contains("<" + cur.getPredicate().toString() + ">"))
															results.add("<" + cur.getPredicate().toString() + ">");

													}
												}
											}

										}
									}
								}

							});
				} catch (Exception e) {
					// System.out.println("NEW EXCEPTION !"+e);

					// System.out.println(e.getStackTrace()[0]);

					exe++;
				}
			}

		}
		System.out.println("exe " + exe + " cou " + cou + "RESULTS " + results.size());
		for (String s : results) {
			// System.out.println("RES "+s);
		}

		return results;
	}

	public static final HashSet<String> findallconnections(String node, int tofind) throws FileNotFoundException {
		// tofind 1 node
		// tofind 2 predicate
		String nodd = node;
		System.out.println("NODD " + nodd);
		File myObj = new File(trainfilestring);
		Scanner myReader = new Scanner(myObj);
		final HashSet<String> results = new HashSet<String>();
		int cou = 0;
		int exe = 0;
		while (myReader.hasNextLine()) {
			// GraphWeighted graphWeighted = new GraphWeighted(true);

			String data = myReader.nextLine();

			String[] splited = data.split("\t");
			String data3 = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);

			data3 = data3.replace('\n', ' ');
			data3 = data3.replace("SELECT", " SELECT ");
			data3 = data3.replace("WHERE", " WHERE");
			data3 = data3.replace("{", " { ");

			// System.out.println("DATA5 " + data3);
			// System.out.println(result);
			if (data3.contains(nodd))

			{
				cou++;
				// System.out.println("DATA6 " + data3);

				try {
					Query query = QueryFactory.create(data3);

					/// System.out.println("------- All Variables --------");
					// Remember distinct subjects in this
					final Set<Node> subjects = new HashSet<Node>();
					final Set<Node> objects = new HashSet<Node>();
					final Set<Node> preds = new HashSet<Node>();
					final Set<node> nodes = new HashSet<node>();

					// This will walk through all parts of the query
					ElementWalker.walk(query.getQueryPattern(),
							// For each element...

							new ElementVisitorBase() {
								// ...when it's a block of triples...
								public void visit(ElementPathBlock el)

							{
									// ...go through all the triples...
									Iterator<TriplePath> triples = el.patternElts();
									while (triples.hasNext()) {

										TriplePath cur = triples.next();
										// ...and grab the subject
										objects.add(cur.getObject());
										subjects.add(cur.getSubject());

										String sub = cur.getSubject().toString();
										String obj = cur.getObject().toString();
										sub = "<" + sub + ">";
										obj = "<" + obj + ">";
										// String pre = cur.getPredicate().toString();

										if (tofind == 1) {
											if ((!sub.equals(nodd))) {
												if (!results.contains(sub))
													results.add(sub);
											}
											if ((!obj.equals(nodd))) {
												if (!results.contains(obj))
													results.add(obj);
											}
										} else if (tofind == 2) {
											if ((cur.getPredicate() != null)
													&& (!("<" + cur.getPredicate().toString() + ">").equals(nodd))) {

												if (!results.contains("<" + cur.getPredicate().toString() + ">"))
													results.add("<" + cur.getPredicate().toString() + ">");

											}
										}
									}

								}

							});
				} catch (Exception e) {
					// System.out.println("NEW EXCEPTION !"+e);

					// System.out.println(e.getStackTrace()[0]);

					exe++;
				}
			}

		}
		System.out.println("exe " + exe + " cou " + cou + "RESULTS " + results.size());
		for (String s : results) {
			// System.out.println("RES "+s);
		}

		return results;
	}

	public static int analyseSparql(String data, GraphWeighted graphWeighted) {

		int total = 0;
		int noval = 0;

		total++;

		String pre = "";
		int counter = 0;

		data = data.replace('\n', ' ');
		data = data.replace("SELECT", " SELECT ");
		data = data.replace("WHERE", " WHERE");
		data = data.replace("{", " { ");
		data = data.replace("*", " ");
		// data = data.replace("(", "[");
		// data = data.replace(")", "]");
		// data=data.replace("|", ";");
		data = data.replace(" / ", ";");

		// System.out.println("\n\nDATA2 " + data);
		nn = new HashSet<NodeWeighted>();
		try {
			Query query = QueryFactory.create(data);

			/// System.out.println("------- All Variables --------");
			// Remember distinct subjects in this
			final Set<Node> subjects = new HashSet<Node>();
			final Set<Node> objects = new HashSet<Node>();
			final Set<Node> preds = new HashSet<Node>();
			final Set<node> nodes = new HashSet<node>();
			final int broke = 0;
			// This will walk through all parts of the query
			ElementWalker.walk(query.getQueryPattern(),
					// For each element...
					new ElementVisitorBase() {
						// ...when it's a block of triples...
						public void visit(ElementPathBlock el) {
							// ...go through all the triples...
							Iterator<TriplePath> triples = el.patternElts();
							while (triples.hasNext()) {

								TriplePath cur = triples.next();
								// ...and grab the subject
								objects.add(cur.getObject());
								subjects.add(cur.getSubject());

								String sub = cur.getSubject().toString();
								String obj = cur.getObject().toString();
								sub = "<" + sub + ">";
								obj = "<" + obj + ">";
								Node pree = cur.getPredicate();
								if (pree != null) {
									preds.add(cur.getPredicate());

									String pre = cur.getPredicate().toString();
									pre = "<" + pre + ">";
									NodeWeighted s = null;
									NodeWeighted e = null;
									if (returnnode(sub) == null) {
										NodeWeighted c = new NodeWeighted(0, sub);
										nn.add(c);
										s = c;
									} else
										s = returnnode(sub);
									if (returnnode(obj) == null) {
										NodeWeighted c = new NodeWeighted(0, obj);
										nn.add(c);
										e = c;
									} else
										e = returnnode(obj);

									graphWeighted.addEdge(s, e, 1, pre);
									graphWeighted.addEdge(e, s, 1, pre);
									// System.out.println("ADDED " + s.name + " " + e.name + " " + pre);
								} else
									System.out.print("false");
								if (cur.getPredicate() != null) {
									if (!pred.containsKey(cur.getPredicate().toString())) {
										HashSet<String> old = new HashSet<String>();
										old.add(cur.getObject().toString());
										old.add(cur.getSubject().toString());
										pred.put(cur.getPredicate().toString(), old);

									} else {
										HashSet<String> oldie = pred.get(cur.getPredicate().toString());
										oldie.add(cur.getSubject().toString());
										oldie.add(cur.getObject().toString());
										pred.remove(cur.getPredicate().toString());
										pred.put(cur.getPredicate().toString(), oldie);
									}

									// System.out.println("class "+triples.next().getClass().toString());
								}
							}
						}
					});

			for (Node s : subjects) {
				// System.out.println("sub HERE4 " + s.toString());
				//

				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if (!hm.containsKey("<" + s.toString() + ">")) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}
			}

			for (Node s : preds) {
				// System.out.println("pred HERE " + s.toString());
				if (!pred.containsKey(s.toString())) {

					pred.put(s.toString(), new HashSet<String>());

				} else {
					HashSet<String> old = pred.get(s.toString());
					old.add(s.toString());
					pred.remove(s.toString());
					pred.put(s.toString(), old);
				}
			}
			for (Node s : objects) {
				// System.out.println("obj HERE " + s.toString());
				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if ((!hm.containsKey("<" + s.toString() + ">"))) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}

			}
			for (node n : nodes) {

				System.out.println("node  " + n.nodename);

				for (String s : n.edges.keySet()) {
					System.out.println("EDGE " + s + " to " + n.edges.get(s));
				}
			}

			// HashSet<String> rett = graphWeighted.DijkstraShortestPath(
			// returnnode("<http://www.wikidata.org/entity/Q29974940>"),
			// returnnode("<http://www.wikidata.org/entity/Q16707842>"));
			// // if (nn.size()>0) {
			// // int
			// //
			// rett=graphWeighted.DijkstraShortestPath(returnnode("<http://www.wikidata.org/entity/Q177220>"),
			// // returnnode("<http://www.wikidata.org/entity/Q183>"));
			//
			// // }
			// if (rett != null) {
			// HashSet<String> commoncheck=null;
			//
			// for (String s : rett) {
			//
			//
			//
			// int cc = 0;
			// int findnode = 0;
			// String check = "";
			// String[] nodess = new String[2];
			//
			// System.out.println(s);
			// String[] splitq = s.split("->");
			// System.out.println(splitq[0] + " " + splitq[1] + " " + splitq[2]);
			//
			// String sub = splitq[0];
			// String obj = splitq[2];
			// String pree = splitq[1];
			// if (sub.contains("?")) {
			// check = sub;
			// findnode = 1;
			// } else
			// nodess[cc++] = sub;
			// if (obj.contains("?")) {
			// check = obj;
			// findnode = 1;
			// } else
			// nodess[cc++] = obj;
			// if (pree.contains("?")) {
			// check = pree;
			// findnode = 2;
			// } else
			// nodess[cc++] = pree;
			// String anode = nodess[0];
			// String bnode = nodess[1];
			// HashSet<String> results1 = findallconnections(anode, findnode);
			// HashSet<String> results2 = findallconnections(bnode, findnode);
			//
			// HashSet<String> common = new HashSet<String>();
			//
			// for (String s1 : results1) {
			// for (String s2 : results2) {
			// if (s2.equals(s1)) {
			// common.add(s1);
			// //System.out.println(anode + " " + bnode + " COMMON between " + s1);
			// }
			// }
			//
			// }
			// if (commoncheck==null)
			//
			// commoncheck = new HashSet<String>(common);
			// else
			// {
			// for (String ss:common) {
			// for (String s2:commoncheck) {
			// if ( (!ss.equals(anode))&&(!ss.equals(bnode))&&(ss.equals(s2)&&
			// (!ss.contains("?"))&&(!ss.contains("\""))))
			// System.out.println("match:"+ss);
			// }
			// }
			//
			// }
			// }
			//
			//
			//
			// } else
			// System.out.println("NULL RET");
			// System.out.println("HERE -");
			// if (rett==1) {
			// String anode="http://www.wikidata.org/entity/Q29974940";
			// String bnode="http://www.wikidata.org/prop/direct/P2937";
			// HashSet<String> results1=findallconnections(anode);
			// HashSet<String> results2= findallconnections(bnode);
			//
			// HashSet<String> common=new HashSet<String>();
			//
			// for (String s:results1)
			// {
			// for (String s2:results2) {
			// if (s2.equals(s)) {
			// common.add(s);
			// //System.out.println(anode+" "+bnode+" COMMON beteen"+s);
			// }
			// }
			//
			// }
			//
			//
			// anode="http://www.wikidata.org/prop/direct/P39";
			// bnode="http://www.wikidata.org/entity/Q16707842";
			// HashSet<String> results1b=findallconnections(anode);
			// HashSet<String> results2b= findallconnections(bnode);
			//
			// HashSet<String> common2=new HashSet<String>();
			//
			// for (String s:results1b)
			// {
			// for (String s2:results2b) {
			// if (s2.equals(s)) {
			// common2.add(s);
			// //System.out.println(anode+" "+bnode+" COMMON beteen"+s);
			// }
			// }
			//
			// }
			//
			// for (String s:common) {
			// for (String s2:common2) {
			// if ((s.equals(s2)&& (!s.contains("?"))&&(!s.contains("\""))))
			// System.out.println("match:"+s);
			// }
			// }
			//
			// }
			// findway(nodes, "http://www.wikidata.org/entity/Q29974940"",
			// "http://www.wikidata.org/entity/Q5");
		} catch (Exception e) {

			// System.out.print(" e CATCHED !" + e);
			// System.out.println(e.getStackTrace()[0]);

			noval++;
			return -1;
		}

		return 1;

	}

	public static HashMap<String, Integer> analyseSparql2(String data, GraphWeighted graphWeighted, String base,
			String second) {

		int total = 0;
		int noval = 0;
		total++;

		String pre = "";
		int counter = 0;

		data = data.replace('\n', ' ');
		data = data.replace("SELECT", " SELECT ");
		data = data.replace("WHERE", " WHERE");
		data = data.replace("{", " { ");
		data = data.replace("*", " ");
		// data = data.replace("(", "[");
		// data = data.replace(")", "]");
		// data=data.replace("|", ";");
		data = data.replace(" / ", ";");
		// System.out.println("\n\nDATA7 " + data);
		nn = new HashSet<NodeWeighted>();
		try {
			Query query = QueryFactory.create(data);

			/// System.out.println("------- All Variables --------");
			// Remember distinct subjects in this
			final Set<Node> subjects = new HashSet<Node>();
			final Set<Node> objects = new HashSet<Node>();
			final Set<Node> preds = new HashSet<Node>();
			final Set<node> nodes = new HashSet<node>();
			final int broke = 0;
			// This will walk through all parts of the query
			ElementWalker.walk(query.getQueryPattern(),
					// For each element...
					new ElementVisitorBase() {
						// ...when it's a block of triples...
						public void visit(ElementPathBlock el) {
							// ...go through all the triples...
							Iterator<TriplePath> triples = el.patternElts();
							while (triples.hasNext()) {

								TriplePath cur = triples.next();
								// ...and grab the subject
								objects.add(cur.getObject());
								subjects.add(cur.getSubject());

								String sub = cur.getSubject().toString();
								String obj = cur.getObject().toString();
								sub = "<" + sub + ">";
								obj = "<" + obj + ">";
								Node pree = cur.getPredicate();
								if (pree != null) {
									preds.add(cur.getPredicate());

									String pre = cur.getPredicate().toString();
									pre = "<" + pre + ">";
									NodeWeighted s = null;
									NodeWeighted e = null;
									if (returnnode(sub) == null) {
										NodeWeighted c = new NodeWeighted(0, sub);
										nn.add(c);
										s = c;
									} else
										s = returnnode(sub);
									if (returnnode(obj) == null) {
										NodeWeighted c = new NodeWeighted(0, obj);
										nn.add(c);
										e = c;
									} else
										e = returnnode(obj);

									graphWeighted.addEdge(s, e, 1, pre);
									graphWeighted.addEdge(e, s, 1, pre);
									// System.out.println("ADDED " + s.name + " " + e.name + " " + pre);
								} else
									System.out.print("false");
								if (cur.getPredicate() != null) {
									if (!pred.containsKey(cur.getPredicate().toString())) {
										HashSet<String> old = new HashSet<String>();
										old.add(cur.getObject().toString());
										old.add(cur.getSubject().toString());
										pred.put(cur.getPredicate().toString(), old);

									} else {
										HashSet<String> oldie = pred.get(cur.getPredicate().toString());
										oldie.add(cur.getSubject().toString());
										oldie.add(cur.getObject().toString());
										pred.remove(cur.getPredicate().toString());
										pred.put(cur.getPredicate().toString(), oldie);
									}

									// System.out.println("class "+triples.next().getClass().toString());
								}
							}
						}
					});

			for (Node s : subjects) {
				// System.out.println("sub HERE4 " + s.toString());
				//

				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if (!hm.containsKey("<" + s.toString() + ">")) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}
			}

			for (Node s : preds) {
				// System.out.println("pred HERE " + s.toString());
				if (!pred.containsKey(s.toString())) {

					pred.put(s.toString(), new HashSet<String>());

				} else {
					HashSet<String> old = pred.get(s.toString());
					old.add(s.toString());
					pred.remove(s.toString());
					pred.put(s.toString(), old);
				}
			}
			for (Node s : objects) {
				// System.out.println("obj HERE " + s.toString());
				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if ((!hm.containsKey("<" + s.toString() + ">"))) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}

			}
			for (node n : nodes) {

				System.out.println("node  " + n.nodename);

				for (String s : n.edges.keySet()) {
					System.out.println("EDGE " + s + " to " + n.edges.get(s));
				}
			}

			ArrayList<String> rett = graphWeighted.DijkstraShortestPath(returnnode(base), returnnode(second));
			// // if (nn.size()>0) {
			// // int
			// rett=graphWeighted.DijkstraShortestPath(returnnode("<http://www.wikidata.org/entity/Q177220>"),
			// returnnode("<http://www.wikidata.org/entity/Q183>"));
			//
			// // } minpath=rett.size()

			// rata

			if ((rett != null)) {

				String orp11 = "";
				for (String q : rett) {

					orp11 = orp11 + q + "\t";
				}

				reso.put(orp11, 1);

				int totalpaths = rett.size();
				// System.out.println("MAGOS");
				HashSet<String> hs1 = new HashSet<String>();
				for (String st : rett) {

					String[] spl = st.split("->");
					if (spl[0].contains("?"))
						hs1.add(spl[0]);
					if (spl[1].contains("?"))
						hs1.add(spl[1]);
					if (spl[2].contains("?"))
						hs1.add(spl[2]);

				}
				// System.out.println("HS SISZ " + hs1.size());
				for (String y : hs1) {
					// System.out.println("EINAI " + y);
				}

				HashSet<String> onevars = new HashSet<String>();
				int tripsize = rett.size();
				int ok = 0;
				int courett = 0;
				// while (ok < tripsize)
				HashSet<String> varvis = new HashSet<String>();
				for (String st : rett) {

					int noc = countFreq("<?", st);
					if ((noc == 1)) {

						System.out.println("1 only..." + st);
						// System.out.println("mar " + st);

						int findnode = 0;
						String check = "";

						String[] splitq = st.split("->");
						int cc = 0;

						String[] nodess = new String[2];

						String sub = splitq[0];
						String obj = splitq[2];
						String pree = splitq[1];
						// presub = sub;
						// preobj = obj;
						// prepree = pree;
						int typos = 0;
						if (sub.contains("?")) {
							check = sub;
							typos = 1;
							findnode = 1;
						} else
							nodess[cc++] = sub;

						if (obj.contains("?")) {
							check = obj;
							findnode = 1;
							typos = 2;
						} else
							nodess[cc++] = obj;

						if (pree.contains("?")) {
							check = pree;
							findnode = 2;
						} else
							nodess[cc++] = pree;

						// System.out.println(
						// "RETU " + noc + " ns0 " + nodess[0] + " ns1 " + nodess[1] + " find " +
						// findnode);

						String anode = nodess[0];

						ArrayList<String> results1 = findallconnections3(base, nodess[0], nodess[1], findnode, typos);
						String mostc = "";
						if (results1.size() > 0) {
							// for (String u:results1) System.out.println(u);

							mostc = mostCommon(results1);
							System.out.println("SIZE >0 " + results1.size() + " MOST COMMON " + mostc);

						}
						results1.remove((nodess[0]));
						int sizz = results1.size();
						int kot = 0;
						for (String po : results1) {
							if (!po.contains("?"))
								kot = 1;
							//
							// System.out.println(" MAOS " + po);

						}

						if ((kot == 0) || (sizz == 0)) {
							if ((kot == 0) && (sizz > 0))
								System.out.println("ALL CONATINED ? " + check);
							else {
								System.out.println("ZERO !");

								if (check2times(rett, check)) {

									if (!onevars.contains(check)) {
										onevars.add(check);

									} else {
										onevars.add(check);

										System.out.println("FILL IT " + check + " ANYWAY !" + nodess[0] + " "
												+ nodess[1] + " TYPOS " + typos);
										ArrayList<String> results4 = findallconnections4(base, nodess[0], nodess[1],
												findnode, typos);
										String mostcc = "";
										if (results4.size() > 0) {
											// for (String u:results1) System.out.println(u);

											mostcc = mostCommon(results4);
											System.out.println("SIZE >0 " + results4.size() + " MOST COMMON " + mostcc);

											HashSet<String> aek = new HashSet<String>();
											// for (String resa : results1) {
											aek.add(mostcc);
											ok++;
											// System.out.println("KOLOS " + resa);
											// }
											variables.put(check, aek);
											System.out.println("-I GOT " + check);

											for (int i = 0; i < rett.size(); i++) {

												String ka = rett.get(i);
												ka = ka.replace(check, mostcc);
												rett.set(i, ka);
											}
											System.out.println("-I REPLACED ALL " + check + " IN RETT! " + " with -"
													+ mostc + "-");
										} else {
											System.out.println("-NO RESULTS " + check);

										}
										// both zero... try to fill check
									}
								} else {
									System.out.println("ONE TIME! " + check);
									System.out.println("ONE TIME FILL IT !!");

									onevars.add(check);

									System.out.println("--FILL IT " + check + " ANYWAY !");
									ArrayList<String> results4 = findallconnections4(base, nodess[0], nodess[1],
											findnode, typos);
									String mostcc = "";
									if (results4.size() > 0) {
										// for (String u:results1) System.out.println(u);

										mostcc = mostCommon(results4);
										System.out.println("--SIZE >0 " + results4.size() + " MOST COMMON " + mostcc);

									} else {
										mostcc = "<*var>";

									}
									HashSet<String> aek = new HashSet<String>();
									// for (String resa : results1) {
									aek.add(mostcc);
									ok++;
									// System.out.println("KOLOS " + resa);
									// }
									variables.put(check, aek);
									System.out.println("--I GOT " + check);

									for (int i = 0; i < rett.size(); i++) {

										String ka = rett.get(i);
										ka = ka.replace(check, mostcc);
										rett.set(i, ka);

									}
									System.out.println("--I REPLACED ALL " + check + " IN RETT!");

									// both zero... try to fill check

								}
							}
							// return reso;

							// HashSet<String> a = new HashSet<String>();
							// a.add(check);
							// variables.put(check, a);

						} else {
							onevars.add(check);

							HashSet<String> aek = new HashSet<String>();
							// for (String resa : results1) {
							aek.add(mostc);
							ok++;
							// System.out.println("KOLOS " + resa);
							// }
							variables.put(check, aek);
							System.out.println("I GOT " + check);

							for (int i = 0; i < rett.size(); i++) {

								String ka = rett.get(i);
								ka = ka.replace(check, mostc);
								rett.set(i, ka);

							}
							System.out.println("I REPLACED ALL " + check + " IN RETT!");

						}

					}
				}

				for (String ch : onevars) {
					if (variables.get(ch) == null) {

						System.out.println("was " + ch + " null add " + ch);
						HashSet<String> a = new HashSet<String>();
						a.add(ch);
						variables.put(ch, a);

					}
				}

				HashSet<Entry<String, HashSet<String>>> HashSet = new LinkedHashSet<Entry<String, HashSet<String>>>(
						variables.entrySet());
				int flag = 0;
				String fist = "";

				String orp = "";
				int cttt = 0;
				for (String q : rett) {

					if (cttt > 0)
						orp = orp + "\t" + q;
					else {
						orp = q;
						cttt = 1;
					}
				}

				ArrayList<String> retto = null;
				System.out.println("!ORIGINAL PATH " + orp);
				int flog = 0;

				String orp2 = orp;
				for (Entry<String, HashSet<String>> entry : HashSet) {

					// System.out.println("KEY " + fist);
					String ll = entry.getKey();
					String key = ll.toString();

					String val = entry.getValue().toString();
					val = val.substring(1, val.length() - 1);

					System.out.println("vari " + ll.toString() + " val " + val);
					orp2 = orp2.replace(key, val);

				}
				retto = new ArrayList<String>();
				retto.add(orp2);

				// if (retto.size()>0) { reso = new HashMap<String, Integer>();}
				if (retto == null) {
					retto = new ArrayList<String>();
					retto.add(orp);
					System.out.println("EMPTY!");
				} else
					System.out.println("SIZOE  " + retto.size());
				for (String io : retto) {
					;
					System.out.println("SUMMAKES " + io);
					if (!summary.contains(io))
						summary.add(io);

					reso.put(io, 1);
				}

				System.out.println("END!");
				// System.out.println("FINITO TO NEXT OPLA !!!!");

				return reso;
				// System.exit(1);

			}

			else {
				// System.out.println("NULL RET");
				return null;
			}

		} catch (Exception e) {

			// System.out.print(" e CATCHED !" + e);
			// System.out.println(e.getStackTrace()[0]);

			noval++;
			return null;
		}

	}

	public void analyseSparql3(String data, GraphWeighted graphWeighted, String base, String second) {

		int total = 0;
		int noval = 0;
		total++;

		String pre = "";
		int counter = 0;

		data = data.replace('\n', ' ');
		data = data.replace("SELECT", " SELECT ");
		data = data.replace("WHERE", " WHERE");
		data = data.replace("{", " { ");
		data = data.replace("*", " ");
		// data = data.replace("(", "[");
		// data = data.replace(")", "]");
		// data=data.replace("|", ";");
		data = data.replace(" / ", ";");
		// System.out.println("\n\nDATA7 " + data);
		nn = new HashSet<NodeWeighted>();
		try {
			Query query = QueryFactory.create(data);

			/// System.out.println("------- All Variables --------");
			// Remember distinct subjects in this
			final Set<Node> subjects = new HashSet<Node>();
			final Set<Node> objects = new HashSet<Node>();
			final Set<Node> preds = new HashSet<Node>();
			final Set<node> nodes = new HashSet<node>();
			final int broke = 0;
			// This will walk through all parts of the query
			ElementWalker.walk(query.getQueryPattern(),
					// For each element...
					new ElementVisitorBase() {
						// ...when it's a block of triples...
						public void visit(ElementPathBlock el) {
							// ...go through all the triples...
							Iterator<TriplePath> triples = el.patternElts();
							while (triples.hasNext()) {

								TriplePath cur = triples.next();
								// ...and grab the subject
								objects.add(cur.getObject());
								subjects.add(cur.getSubject());

								String sub = cur.getSubject().toString();
								String obj = cur.getObject().toString();
								sub = "<" + sub + ">";
								obj = "<" + obj + ">";
								Node pree = cur.getPredicate();
								if (pree != null) {
									preds.add(cur.getPredicate());

									String pre = cur.getPredicate().toString();
									pre = "<" + pre + ">";
									NodeWeighted s = null;
									NodeWeighted e = null;
									if (returnnode(sub) == null) {
										NodeWeighted c = new NodeWeighted(0, sub);
										nn.add(c);
										s = c;
									} else
										s = returnnode(sub);
									if (returnnode(obj) == null) {
										NodeWeighted c = new NodeWeighted(0, obj);
										nn.add(c);
										e = c;
									} else
										e = returnnode(obj);

									graphWeighted.addEdge(s, e, 1, pre);
									graphWeighted.addEdge(e, s, 1, pre);
									// System.out.println("ADDED " + s.name + " " + e.name + " " + pre);
								} else
									System.out.print("false");
								if (cur.getPredicate() != null) {
									if (!pred.containsKey(cur.getPredicate().toString())) {
										HashSet<String> old = new HashSet<String>();
										old.add(cur.getObject().toString());
										old.add(cur.getSubject().toString());
										pred.put(cur.getPredicate().toString(), old);

									} else {
										HashSet<String> oldie = pred.get(cur.getPredicate().toString());
										oldie.add(cur.getSubject().toString());
										oldie.add(cur.getObject().toString());
										pred.remove(cur.getPredicate().toString());
										pred.put(cur.getPredicate().toString(), oldie);
									}

									// System.out.println("class "+triples.next().getClass().toString());
								}
							}
						}
					});

			for (Node s : subjects) {
				// System.out.println("sub HERE4 " + s.toString());
				//

				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if (!hm.containsKey("<" + s.toString() + ">")) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}
			}

			for (Node s : preds) {
				// System.out.println("pred HERE " + s.toString());
				if (!pred.containsKey(s.toString())) {

					pred.put(s.toString(), new HashSet<String>());

				} else {
					HashSet<String> old = pred.get(s.toString());
					old.add(s.toString());
					pred.remove(s.toString());
					pred.put(s.toString(), old);
				}
			}
			for (Node s : objects) {
				// System.out.println("obj HERE " + s.toString());
				if ((!s.toString().contains(("?")) && (s.toString().contains("http://"))
						&& (!s.toString().contains("geosparql")))) {

					if ((!hm.containsKey("<" + s.toString() + ">"))) {
						hm.put("<" + s.toString() + ">", 1);
					} else {
						int no = hm.get("<" + s.toString() + ">");

						hm.remove("<" + s.toString() + ">");
						hm.put("<" + s.toString() + ">", no + 1);
					}

				}

			}
			for (node n : nodes) {

				System.out.println("node  " + n.nodename);

				for (String s : n.edges.keySet()) {
					System.out.println("EDGE " + s + " to " + n.edges.get(s));
				}
			}

			ArrayList<String> rett = graphWeighted.DijkstraShortestPath(returnnode(base), returnnode(second));
			// // if (nn.size()>0) {
			// // int
			// rett=graphWeighted.DijkstraShortestPath(returnnode("<http://www.wikidata.org/entity/Q177220>"),
			// returnnode("<http://www.wikidata.org/entity/Q183>"));
			//
			// // } minpath=rett.size()

			// rata

			System.out.println("SIZE PATH  " + rett.size());

		} catch (Exception e) {

			System.out.print(" e CATCHED !" + e);
			System.out.println(e.getStackTrace()[0]);

			noval++;

		}

	}

	public static void printMap(Map<String, Integer> map) {
		// System.out.println("Company\t Price ");
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("----------" + entry.getKey() + "\t" + entry.getValue());
		}
		System.out.println("\n");
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		resuli = new FileWriter("fandata/dbpedia/results/" + poss + "results.tsv", true);
		HashSet<String> inside = new HashSet<String>();

		for (int times = 1; times <= tttt; times++) {
			// for (int times = 1; times <=1; times++) {

			summary = new HashSet<String>();

			System.out.println("TIMES " + times + " \n\n\n");
			int tot = 0;
			int fal = 0;

			int all = 0;

			// String nodd = "<http://www.wikidata.org/entity/Q177220>";

			// String nodd = "<http://www.wikidata.org/entity/Q29974940>";
			String nodd = "<http://www.wikidata.org/entity/Q177220>";
			nodd = "<http://www.wikidata.org/entity/Q177220>";

			nodd = "<http://www.wikidata.org/entity/Q29974940>";

			int count = 0;
			while (tot == 0) {
				String occur = "";
				while (true) {
					int randomNum = ThreadLocalRandom.current().nextInt(1, 30 + 1);
					FileInputStream fs = new FileInputStream(trainnodesdesc);
					BufferedReader br = new BufferedReader(new InputStreamReader(fs));

					for (int i = 0; i < randomNum; ++i) {

						String lineIWant = br.readLine();

						String aa[] = lineIWant.split("\t");
						nodd = aa[0];
						occur = aa[1];

					}

					if (inside.contains(nodd)) {
						// System.out.println("I FOUND AGAIN :"+nodd);
					} else {
						resuli.write(nodd + "\n");
						inside.add(nodd);
						break;
					}

				}
				nodd = "<" + nodd + ">";
				// nodd="<http://bio2rdf.org/go:0031707>";

				// nodd = "<http://www.wikidata.org/entity/Q16970>";
				// nodd = "<http://www.wikidata.org/entity/Q70208>";
				// nodd="<http://www.wikidata.org/entity/Q105731>";
				// nodd="<"+"http://www.wikidata.org/entity/Q43229"+">";
				// nodd="<http://wikiba.se/ontology#GlobecoordinateValue>";
				// nodd="<http://www.wikidata.org/entity/Q3863>";
				// nodd="<http://www.wikidata.org/entity/Q15978631>";

				// nodd = "<http://www.wikidata.org/entity/Q16970>";
				// nodd = "<http://www.wikidata.org/entity/Q183>";
				System.out.println("NODDII -" + nodd + " " + occur);

				ooriginal = nodd;

				hm = new HashMap<String, Integer>();
				pred = new HashMap<String, HashSet<String>>();
				nn = new HashSet<NodeWeighted>();
                   all=0;
				try {
					File myObj = new File(trainfilestring);
					Scanner myReader = new Scanner(myObj);

					while (myReader.hasNextLine()) {
						GraphWeighted graphWeighted = new GraphWeighted(true);

						all++;
						// if (tot > topk)
						// break;
						// count++;
						String data = myReader.nextLine();
						// System.out.println(data);

						String[] splited = data.split("\t");
						try {
							String result = java.net.URLDecoder.decode(splited[0], StandardCharsets.UTF_8);
							// System.out.println(result);

							int nooccu = result.split("WHERE", -1).length - 1;
							// System.out.println(nooccu);

							if ((result.contains(nodd)) && (nooccu == 1)) {
								int res = analyseSparql(result, graphWeighted);

								// System.out.println("RES = "+res);
								if (res == 1) {
									// System.out.println(tot+" "+result);
									// .out.println("GOOD TOTAL :" + tot);
									tot++;
								} else
									fal++;
								count++;
								// System.out.println("COUN "+count);
							}

						} catch (Exception e) {
							continue;
						}
						// int lu=result.lastIndexOf("}", 1);
						// result=result.substring(0, lu-1);

						// System.out.println("_________________________________________________");
					}
					myReader.close();
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				System.out.println("COUN1 " + count);
				System.out.println("GOOD TOTAL :" + tot + " false " + fal + " ALL " + all);
			}
			List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(hm.entrySet());

			Collections.sort(list, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					if (false) {
						// compare two object and return an integer
						return o1.getValue().compareTo(o2.getValue());
					} else {
						return o2.getValue().compareTo(o1.getValue());
					}
				}
			});

			System.out.println("all " + all + " good " + tot + " FASLE " + fal);

			Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
			for (Entry<String, Integer> entry : list) {
				sortedMap.put(entry.getKey(), entry.getValue());
				// System.out.println("ek "+entry.getKey());
			}
			sortedMap.remove(nodd);

			sortedMap.remove("<http://www.bigdata.com/rdf#serviceParam>");

			// printMap(sortedMap);

			// System.exit(0);
			// System.out.println("NODDII "+nodd+" "+occur);
			// System.exit(1);
			String anode = nodd;
			String bnode = "";

			int gettop = topk;
			System.out.println("HERE 1");
			int counter = 0;
			for (Entry<String, Integer> entry : sortedMap.entrySet()) {
				visited = new HashSet<String>();
				minpathsofar = 20;
				System.out.println(entry.getKey() + "\t" + entry.getValue());
				reso = new HashMap<String, Integer>();
				counter++;
				System.out.println("CONTIS " + counter);
				if (counter > gettop)
					break;
				tot = 0;
				fal = 0;
				// topk = 30;
				count = 0;
				all = 0;
				// String nodd = "<http://www.wikidata.org/entity/Q177220>";

				bnode = entry.getKey();

				hm = new HashMap<String, Integer>();
				pred = new HashMap<String, HashSet<String>>();
				nn = new HashSet<NodeWeighted>();
				System.out.println("HERE 2");

				try {
					File myObj = new File(trainfilestring);
					Scanner myReader = new Scanner(myObj);

					while (myReader.hasNextLine()) {

						// System.out.println("NR !");
						GraphWeighted graphWeighted = new GraphWeighted(true);

						all++;
						// if ((tot > topk) && (count>5000))
						// if (tot>topk)
						// break;
						// count++;
						String data = myReader.nextLine();
						// System.out.println(data);

						String[] splited = data.split("\t");
						//System.out.println(splited[0]);
						
						//String used=replacer(new StringBuffer(splited[0]));
						String used=splited[0];
						used=used.replace("%"," ");
						String result = java.net.URLDecoder.decode(used, StandardCharsets.UTF_8);
						// System.out.println(result);

						int nooccu = result.split("WHERE", -1).length - 1;
						// System.out.println(nooccu);

						if ((result.contains(nodd)) && (result.contains(bnode)) && (nooccu == 1)) {
							if (!visited.contains(result)) {
								visited.add(result);
								// System.out.println("NV -:" + result);
								variables = new HashMap<String, HashSet<String>>();

								HashMap<String, Integer> res = analyseSparql2(result, graphWeighted, anode, bnode);

								// System.out.println("RES = "+res);
								if (res != null) {

									for (String key : res.keySet()) {
										System.out.println("DAT " + key);
									}

									System.out.println(" v GOOD TOTAL :" + tot);

									// if (tot > 10)
									break;
									// tot++;

								} else {
									fal++;
								}
								count++;

								// System.out.println("COUN "+count);
							}
						} else {
							// System.out.println ("V ");
						}
						// int lu=result.lastIndexOf("}", 1);
						// result=result.substring(0, lu-1);

						// System.out.println("_________________________________________________");
					}
					System.out.println("CONTA " + count);
					myReader.close();
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			}

			HashSet<String> nod = new HashSet<String>();
			HashSet<String> edg = new HashSet<String>();
			HashSet<String> nodr = new HashSet<String>();
			HashSet<String> edgr = new HashSet<String>();

			ArrayList<String> summ = new ArrayList<String>(summary);

			for (int i = 0; i < summ.size(); i++) {

				String k = summ.get(i);
				k = k.replace("*", "?");
				summ.set(i, k);

			}
			System.out.println("SUMMARY------");

			for (String sum : summ) {

				System.out.println(sum);

				String[] sple = sum.split("\t");

				for (String parts : sple) {
					System.out.println("---------- PARTI " + parts);
					String[] ssple = parts.split("->");
					nod.add(ssple[0]);
					nod.add(ssple[2]);
					edg.add(ssple[1]);
				}
			}
			// System.exit(1);
			System.out.println("ORA " + ooriginal);
			int nonodes = nod.size();
			int noedges = edg.size();
			if (nonodes <= topk) {
				times = times - 1;
				System.out.println("I CONTINUE - NO EVALUTAION ");
				resuli.write("I CONTINUE - NO EVALUATION \n");
				continue;
			}
			HashSet<String> nod2 = new HashSet<String>();

			for (String as : nod) {
				if ((!as.contains("?")) && (as.contains("http")) && (!as.contains("rdf#serviceParam"))) {
					nod2.add(as);
				}
			}
			int subtr = 0;
			for (String as : nod) {
				if (as.contains("?"))
					subtr++;
			}
			nonodes = nonodes - subtr;

			subtr = 0;
			for (String as : edg) {
				if (as.contains("?"))
					subtr++;
			}
			noedges = noedges - subtr;
			System.out.println();

			resuli.write(" CUR N " + nonodes + " CUR E " + noedges + "\n");

			for (String sn : nod2) {
				System.out.println(sn);
				resuli.write(sn + "\n");
			}

			for (String sn : edg) {
				System.out.println(sn);
				resuli.write(sn + "\n");
			}

			// HashSet<String> nodetest = new HashSet<String>();
			//
			// nodetest.add("<http://wikiba.se/ontology-beta#Item>");
			// nodetest.add("<http://www.w3.org/2002/07/owl#ObjectProperty>");
			// nodetest.add("<http://wikiba.se/ontology-beta#Property>");
			// nodetest.add("<http://www.wikidata.org/entity/Q8205328>");
			// nodetest.add("<http://www.w3.org/2002/07/owl#DatatypeProperty>");
			// nodetest.add("<http://www.wikidata.org/entity/Q4167836>");
			// nodetest.add("<http://www.wikidata.org/entity/Q21503250>");
			// nodetest.add("<http://www.wikidata.org/entity/Q238372>");
			// nodetest.add("<http://www.wikidata.org/entity/Q21502838");
			// nodetest.add("<http://www.wikidata.org/entity/Q602358>");
			// nodetest.add("<http://www.wikidata.org/entity/Q5>");
			//

			// System.exit(1);
			// analyseSparqltest(nodetest, edg, ooriginal, 1);

			// System.exit(1);
			analyseSparqltest(nod2, edg, ooriginal, 1);
			System.out.println("RANDOM--------------");

			randomtest(nonodes, noedges, ooriginal, nodr, edgr);
			System.out.println(" CUR N " + nodr.size() + " CUR E " + edgr.size());

			analyseSparqltest(nodr, edgr, ooriginal, 2);

			System.out.println("1NORMAR NODEC= " + totcovn / times + " EDGEC " + totcove / times);
			System.out.println("1RANDOM NODEC= " + totcovnr / times + " EDGEC " + totcover / times);
			// resul.write(""+(totcovn/times)+"\t"+(totcove / times)+"\n");
			// resul.write(""+(totcovnr/times)+"\t"+(totcover / times)+"\n");

			resuli.write("\n\n");
			// break;
		}
		System.out.println("LATHOS " + lathos);
		System.out.println("NORMAR NODEC= " + totcovn / (tttt - lathos) + " EDGEC " + totcove / (tttt - lathos));
		System.out.println("RANDOM NODEC= " + totcovnr / (tttt - lathos) + " EDGEC " + totcover / (tttt - lathos));

		resuli.write("" + (totcovn / (tttt - lathos)) + "\t" + (totcove / (tttt - lathos)) + "\n");
		resuli.write("" + (totcovnr / (tttt - lathos)) + "\t" + (totcover / (tttt - lathos)) + "\n");
		resuli.close();

		//
		// System.out.println("\n");

		// for (String s : pred.keySet()) {
		// System.out.println("PRED " + s);
		// for (String s1 : pred.get(s))
		// System.out.println(s1);
		// System.out.println("");
		// }

		// NodeWeighted zero = new NodeWeighted(0, "0");
		// NodeWeighted one = new NodeWeighted(1, "1");
		// NodeWeighted two = new NodeWeighted(2, "2");
		// NodeWeighted three = new NodeWeighted(3, "3");
		// NodeWeighted four = new NodeWeighted(4, "4");
		// NodeWeighted five = new NodeWeighted(5, "5");
		// NodeWeighted six = new NodeWeighted(6, "6");
		//
		// // Our addEdge method automatically adds Nodes as well.
		// // The addNode method is only there for unconnected Nodes,
		// // if we wish to add any
		// graphWeighted.addEdge(zero, one, 8,"f");
		// graphWeighted.addEdge(zero, two, 11,"af");
		// graphWeighted.addEdge(one, three, 3,"g");
		// graphWeighted.addEdge(one, four, 8,"gh");
		// graphWeighted.addEdge(one, two, 7,"g");
		// graphWeighted.addEdge(two, four, 9,"p");
		// graphWeighted.addEdge(three, four, 5,"ff");
		// graphWeighted.addEdge(three, five, 2,"t");
		// graphWeighted.addEdge(four, six, 6,"ka");
		// graphWeighted.addEdge(five, four, 1,"y");
		// graphWeighted.addEdge(five, six, 8,"dfh");

		// graphWeighted.DijkstraShortestPath(zero, six);

	}
}
