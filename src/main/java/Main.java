import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Main {
    public static void main(String args[]) {
        new Main();
    }

    Generator gen;
    public Main() {
        Graph graph = new SingleGraph("Random");
        gen = new RandomGenerator(2.0);
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<100; i++)
            gen.nextEvents();
        gen.end();
        Node s = graph.getNode(findLargestComponent(graph));
        TreeSet<Integer> cc = new TreeSet<Integer>();
        Iterator<Node> bfsIterator = s.getBreadthFirstIterator();
        ArrayList<Node> removedNodes = new ArrayList<Node>();
        cc.add(s.getIndex());
        while (bfsIterator.hasNext()) {
            Node n = bfsIterator.next();
            cc.add(n.getIndex());
            System.out.println("Node " + n.getIndex() + " added");
        }
        for(Node node : graph) {
            if (!cc.contains(node.getIndex())) {
                removedNodes.add(node);
            }
        }
        for(Node node : removedNodes) {
            graph.removeNode(node);
        }
        graph.display();
    }

    int findLargestComponent(Graph graph) {
        int largestCcSize = 0, largestCcId =0, ccId[] = new int[graph.getNodeCount()];
        ArrayList<Integer> ccSizes = new ArrayList<Integer>();
        Iterator<Node> bfsIterator;
        Arrays.fill(ccId, -1);
        for(Node node : graph) {
            if (ccId[node.getIndex()] == -1) {
                ccSizes.add(0);
                ccId[node.getIndex()] = ccSizes.size() - 1;
                bfsIterator = node.getBreadthFirstIterator();
                while (bfsIterator.hasNext()) {
                    Node nextNode = bfsIterator.next();
                    ccId[nextNode.getIndex()] = ccSizes.size() - 1;
                    ccSizes.set(ccSizes.size() - 1, ccSizes.get(ccSizes.size() - 1) + 1);
                }
                if (largestCcSize < ccSizes.get(ccSizes.size() - 1)) {
                    largestCcSize = ccSizes.get(ccSizes.size() - 1);
                    largestCcId = node.getIndex();
                }
            }
        }
        return largestCcId;
    }
}