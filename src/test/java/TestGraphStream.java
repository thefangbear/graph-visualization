import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class TestGraphStream {
    public static void main(String[] args) {

        Graph graph = new SingleGraph("Tutorial 1");
        graph.setAutoCreate(true);
        graph.setStrict(false);
      //  graph.addNode("A" );
       // graph.addNode("B" );
      //  graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.display();
    }
}
