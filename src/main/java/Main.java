import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Main {
    Main() throws Exception {
        System.out.println("what do you want to generate? type help if you're not sure.");
        PrintWriter out = new PrintWriter(System.out);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            out.printf("~$ ").flush();
            String[] tokens = in.readLine().split(" ");
            if (tokens[0].equals("exit")) {
                return;
            } else if (tokens[0].equals("help")) {
                out.printf("List of commands\n" +
                        "1) GenerateConnectedRandomGraph <num iterations>: generates connected random graph\n" +
                        "2) GeneratePlanarRandomGraph <num iterations>: generates " +
                        "Dorogovtsev-Mendes planar random graph\n" +
                        "3) GeneratePreferentialAttachmentGraph <num iterations> <max edges per node step> <exact>" +
                        ": Generates a Barbasi-Albert preferential attachment graph, always planar/connected\n" +
                        "4) GenerateGridGraph [cross] [torus] [xy]: generates grid graph randomly\n" +
                        "5) GenerateSmallWorldGraph <n> <k> <beta>: Generates a Watts-Strogatz small world graph\n" +
                        "6) GenerateEuclideanGraph <num iterations>: generates a random Euclidean graph\n" +
                        "7) exit: quit this program.\n");
            } else if (tokens[0].equals("GenerateConnectedRandomGraph")) {
                if (tokens.length != 2) {
                    out.printf("Wrong amount of arguments, you bastard!\n");
                    continue;
                }
                int numIterations = Integer.parseInt(tokens[1]);
                out.printf("Generate: numIterations=%d\n", numIterations);
                generateConnectedUndirectedRandomGraph(numIterations);
            } else if (tokens[0].equals("GeneratePlanarRandomGraph")) {
                if (tokens.length != 2) {
                    out.printf("Wrong amount of arguments, you bastard!\n");
                    continue;
                }
                int numIterations = Integer.parseInt(tokens[2]);
                out.printf("Generate: numIterations=%d\n", numIterations);
                generateDorogovtsevMendesPlanarRandomGraph(numIterations);
            } else if (tokens[0].equals("GeneratePreferentialAttachmentGraph")) {
                if (tokens.length < 2 || tokens.length > 3) {
                    out.printf("Wrong amount of arguments, you bastard!\n");
                    continue;
                }
                int numIterations, maxEdgesPerNodeStep;
                boolean exact = false;
                numIterations = Integer.parseInt(tokens[0]);
                maxEdgesPerNodeStep = Integer.parseInt(tokens[1]);
                if (tokens.length == 3) {
                  exact = true;
                }
                out.println("numIterations=" + numIterations + ", maxEdgesPerNodeStep=" + maxEdgesPerNodeStep +
                        ", exact=" + exact);
                generateBarbasiAlbertPreferentialAttachmentGraph(numIterations, maxEdgesPerNodeStep, exact);
            } else if (tokens[0].equals("GenerateGridGraph")) {
                System.out.println("err: small world not ready yet");
                //   int numIterations = Integer.parseInt(tokens[1]);
           //     generateGrid(10, false, false, false);

            } else if (tokens[0].equals("GenerateSmallWorldGraph")) {
               // generateWattsStrogatzSmallWorldGraph(10, 2, 0.5);
                System.out.println("err: small world not ready yet");
            } else {
                // euclidean
                System.out.println("err: euclidean not ready yet");
            }
        }
    }

    public static void main(String args[]) throws Exception {
        new Main();
    }

    // returns index of a representative node in the largest component in a graph
    int findLargestComponent(Graph graph) {
        int largestCcSize = 0, largestCcId = 0, ccId[] = new int[graph.getNodeCount()];
        ArrayList<Integer> ccSizes = new ArrayList<Integer>();
        Iterator<Node> bfsIterator;
        Arrays.fill(ccId, -1);
        for (Node node : graph) {
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

    // Uses PRNG to generate random graph and picks the largest connected component
    void generateConnectedUndirectedRandomGraph(int numIterations) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator(2.0);
        gen.addSink(graph);
        generate(numIterations, gen);
        Node s = graph.getNode(findLargestComponent(graph));
        TreeSet<Integer> cc = new TreeSet<Integer>();
        Iterator<Node> bfsIterator = s.getBreadthFirstIterator();
        ArrayList<Node> removedNodes = new ArrayList<Node>();
        cc.add(s.getIndex());
        while (bfsIterator.hasNext()) {
            Node n = bfsIterator.next();
            cc.add(n.getIndex());
       //     System.out.println("Node " + n.getIndex() + " added");
        }
        for (Node node : graph) {
            if (!cc.contains(node.getIndex())) {
                removedNodes.add(node);
            }
        }
        for (Node node : removedNodes) {
            graph.removeNode(node);
        }
        graph.display();
    }

    // Dorogovtsev-Mendes planar graph generation
    // First, generate a random edge (u,v).
    // Next, genrate a new vertex p.
    // Create a triangle: (u,p) and (p,v).
    void generateDorogovtsevMendesPlanarRandomGraph(int numIterations) {
        Graph graph = new SingleGraph("RandomPlanarGraph");
        Generator gen = new DorogovtsevMendesGenerator();
        gen.addSink(graph);
        generate(numIterations, gen);
        graph.display();
    }

    void generateBarbasiAlbertPreferentialAttachmentGraph(int numIterations, int maxEdgesPerNodeStep, boolean exact) {
        Graph graph = new SingleGraph("PreferentialAttachment");
        Generator gen = new BarabasiAlbertGenerator(maxEdgesPerNodeStep, exact);
        gen.addSink(graph);
        generate(numIterations, gen);
        graph.display();
    }

    void generateGrid(int numIterations, boolean cross, boolean tore, boolean generateXY) {
        Graph graph = new SingleGraph("Grid");
        Generator gen = new GridGenerator(cross, tore, generateXY);
        gen.addSink(graph);
        generate(numIterations, gen);
        graph.display();
    }

    // n-element ring, each node connected to k nearest neighbors, rewires
    // in clockwise order with probability of beta towards nodes further "down" the ring in
    // clockwise order
    void generateWattsStrogatzSmallWorldGraph(int n, int k, double beta) {
        Graph graph = new SingleGraph("SmallWorld");
        Generator gen = new WattsStrogatzGenerator(n, k, beta);
        gen.begin();
        while (gen.nextEvents());
        gen.end();
        graph.display();
    }

    void generateRandomEuclideanDistanceGraph(int numIterations) {
        Graph graph = new SingleGraph("Euclidean");
        Generator gen = new RandomEuclideanGenerator(3, false, false);
        generate(numIterations, gen);
        graph.display(false);
    }

    void generate(int numIterations, Generator gen) {
        gen.begin();
        for(int i = 0; i < numIterations; ++i) {
            gen.nextEvents();
        }
        gen.end();
    }
}