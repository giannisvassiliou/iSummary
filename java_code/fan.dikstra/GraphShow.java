package fan.disjksta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class GraphShow {
	
	
	
	
	
	
	
	
	
    public static void main(String[] args) {
        GraphWeighted graphWeighted = new GraphWeighted(true);
        NodeWeighted zero = new NodeWeighted(0, "0");
        NodeWeighted one = new NodeWeighted(1, "1");
        NodeWeighted two = new NodeWeighted(2, "2");
        NodeWeighted three = new NodeWeighted(3, "3");
        NodeWeighted four = new NodeWeighted(4, "4");
        NodeWeighted five = new NodeWeighted(5, "5");
        NodeWeighted six = new NodeWeighted(6, "6");

        // Our addEdge method automatically adds Nodes as well.
        // The addNode method is only there for unconnected Nodes,
        // if we wish to add any
        graphWeighted.addEdge(zero, one, 8,"f");
        graphWeighted.addEdge(zero, two, 11,"af");
        graphWeighted.addEdge(one, three, 3,"g");
        graphWeighted.addEdge(one, four, 8,"gh");
        graphWeighted.addEdge(one, two, 7,"g");
        graphWeighted.addEdge(two, four, 9,"p");
        graphWeighted.addEdge(three, four, 5,"ff");
        graphWeighted.addEdge(three, five, 2,"t");
        graphWeighted.addEdge(four, six, 6,"ka");
        graphWeighted.addEdge(five, four, 1,"y");
        graphWeighted.addEdge(five, six, 8,"dfh");

        graphWeighted.DijkstraShortestPath(zero, six);
    }
}