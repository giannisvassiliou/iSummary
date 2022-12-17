package fan.disjksta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GraphWeighted {
    private Set<NodeWeighted> nodes;
    private boolean directed;
 // Doesn't need to be called for any node that has an edge to another node
 	// since addEdge makes sure that both nodes are in the nodes Set
 	public void addNode(NodeWeighted... n) {
 	    // We're using a var arg method so we don't have to call
 	    // addNode repeatedly
 	    nodes.addAll(Arrays.asList(n));
 	}
 	
 	public void addEdge(NodeWeighted source, NodeWeighted destination, double weight,String name) {
 	    // Since we're using a Set, it will only add the nodes
 	    // if they don't already exist in our graph
 	    nodes.add(source);
 	    nodes.add(destination);

 	    // We're using addEdgeHelper to make sure we don't have duplicate edges
 	    addEdgeHelper(source, destination, weight,name);

 	    if (!directed && source != destination) {
 	        addEdgeHelper(destination, source, weight,name);
 	    }
 	}
	private String findedge(NodeWeighted a, NodeWeighted b) {
 	    // Go through all the edges and see whether that edge has
 	    // already been added
 	    for (EdgeWeighted edge : a.edges) {
 	        if (edge.source == a && edge.destination == b) {
 	            // Update the value in case it's a different one now
 	            //edge.weight = weight;
 	            return edge.name;
 	        }
 	        
 	        
 	        
 	    }
 	    
 	   for (EdgeWeighted edge : b.edges) {
	        if (edge.source == b && edge.destination == a) {
	            // Update the value in case it's a different one now
	            //edge.weight = weight;
	            return edge.name;
	        }
	        
	        
	        
	    }
 	    
 	 return "EMPTY";
 	}
	
	
	
	
	
 	private void addEdgeHelper(NodeWeighted a, NodeWeighted b, double weight,String n) {
 	    // Go through all the edges and see whether that edge has
 	    // already been added
 	    for (EdgeWeighted edge : a.edges) {
 	        if (edge.source == a && edge.destination == b) {
 	            // Update the value in case it's a different one now
 	            edge.weight = weight;
 	            return;
 	        }
 	    }
 	    // If it hasn't been added already (we haven't returned
 	    // from the for loop), add the edge
 	    a.edges.add(new EdgeWeighted(a, b, weight,n));
 	}
 	
 	
 	
 	
 	public ArrayList<String> DijkstraShortestPath(NodeWeighted start, NodeWeighted end) {
	    // We keep track of which path gives us the shortest path for each node
	    // by keeping track how we arrived at a particular node, we effectively
	    // keep a "pointer" to the parent node of each node, and we follow that
	    // path to the start
 		ArrayList<String> re=new ArrayList<String>();
	    HashMap<NodeWeighted, NodeWeighted> changedAt = new HashMap<>();
	    changedAt.put(start, null);

	    // Keeps track of the shortest path we've found so far for every node
	    HashMap<NodeWeighted, Double> shortestPathMap = new HashMap<>();

	    // Setting every node's shortest path weight to positive infinity to start
	    // except the starting node, whose shortest path weight is 0
	    for (NodeWeighted node : nodes) {
	        if (node == start)
	            shortestPathMap.put(start, 0.0);
	        else shortestPathMap.put(node, Double.POSITIVE_INFINITY);
	    }

	    // Now we go through all the nodes we can go to from the starting node
	    // (this keeps the loop a bit simpler)
	    for (EdgeWeighted edge : start.edges) {
	        shortestPathMap.put(edge.destination, edge.weight);
	        changedAt.put(edge.destination, start);
	    }

	    start.visit();

	    // This loop runs as long as there is an unvisited node that we can
	    // reach from any of the nodes we could till then
	    while (true) {
	        NodeWeighted currentNode = closestReachableUnvisited(shortestPathMap);
	        // If we haven't reached the end node yet, and there isn't another
	        // reachable node the path between start and end doesn't exist
	        // (they aren't connected)
	        if (currentNode == null) {
	        	if ((end!=null)&&(start!=null)) {
	            System.out.println("There isn't a path between " + start.name + " and " + end.name);return null;}
	        	else if (end==null) {System.out.println("End node not exists!");return null;}
	        	else if (start==null) { System.out.println("Start node not exists!");return null;}
	        	
	        }

	        List<String> edd=new ArrayList<String>();
	        // If the closest non-visited node is our destination, we want to print the path
	        if (currentNode == end) {
	            System.out.println("The path with the smallest weight between "
	                                   + start.name + " and " + end.name + " is:");

	            NodeWeighted child = end;

	            // It makes no sense to use StringBuilder, since
	            // repeatedly adding to the beginning of the string
	            // defeats the purpose of using StringBuilder
	            String path = end.name;
	            String pre=end.name;
	            
	            NodeWeighted prenode=end;
	            while (true) {
	                NodeWeighted parent = changedAt.get(child);
	                if (parent == null) {
	                    break;
	                }
                    
	                // Since our changedAt map keeps track of child -> parent relations
	                // in order to print the path we need to add the parent before the child and
	                // it's descendants
	                String cur=parent.name;
	                NodeWeighted curnode=parent;
	                path = parent.name + " " + path;
	                child = parent;	            
	                
	              //  System.out.println("previous "+pre+" cur "+cur);
	              //  System.out.println(findedge(curnode,prenode));
	                
	             //   System.out.println(findedge(prenode,curnode));
	                edd.add(curnode.name+"->"+findedge(curnode,prenode)+"->"+prenode.name);
	                re.add(curnode.name+"->"+findedge(curnode,prenode)+"->"+prenode.name);
	                prenode=curnode;
	                pre=cur;
	            }
	            System.out.println(path);
	            String[] pp=path.split(path,' ');
	           // System.out.println(edd);
	           Collections.reverse(edd);
	            System.out.println(edd);
	            Collections.reverse(re);
	            System.out.println("------------------->The path costs: " + shortestPathMap.get(end));
	            return re;
	        }
	        currentNode.visit();

	        // Now we go through all the unvisited nodes our current node has an edge to
	        // and check whether its shortest path value is better when going through our
	        // current node than whatever we had before
	        for (EdgeWeighted edge : currentNode.edges) {
	            if (edge.destination.isVisited())
	                continue;

	            if (shortestPathMap.get(currentNode)
	               + edge.weight
	               < shortestPathMap.get(edge.destination)) {
	                shortestPathMap.put(edge.destination,
	                                   shortestPathMap.get(currentNode) + edge.weight);
	                changedAt.put(edge.destination, currentNode);
	            }
	        }
	    }
	}
	private NodeWeighted closestReachableUnvisited(HashMap<NodeWeighted, Double> shortestPathMap) {

	    double shortestDistance = Double.POSITIVE_INFINITY;
	    NodeWeighted closestReachableNode = null;
	    for (NodeWeighted node : nodes) {
	        if (node.isVisited())
	            continue;

	        double currentDistance = shortestPathMap.get(node);
	        if (currentDistance == Double.POSITIVE_INFINITY)
	            continue;

	        if (currentDistance < shortestDistance) {
	            shortestDistance = currentDistance;
	            closestReachableNode = node;
	        }
	    }
	    return closestReachableNode;
	}
	
	
	
 	
 	
 	public void printEdges() {
 	    for (NodeWeighted node : nodes) {
 	        LinkedList<EdgeWeighted> edges = node.edges;

 	        if (edges.isEmpty()) {
 	            System.out.println("Node " + node.name + " has no edges.");
 	            continue;
 	        }
 	        System.out.print("Node " + node.name + " has edges to: ");

 	        for (EdgeWeighted edge : edges) {
 	            System.out.print(edge.destination.name + "(" + edge.weight + ") ");
 	        }
 	        System.out.println();
 	    }
 	}
 	
 	// Necessary call if we want to run the algorithm multiple times
 	public void resetNodesVisited() {
 	    for (NodeWeighted node : nodes) {
 	        node.unvisit();
 	    }
 	}
 	
 	
 	
 	
 	
 	
 	
    public GraphWeighted(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    // ...
}