import java.util.ArrayList;

public class LSA {
    public int sequenceNo;
    public String origin;
    public ArrayList<Graph.Edge> edges;

    public LSA(String origin,ArrayList<Graph.Edge> edges){
        this.sequenceNo = 0;
        this.origin = origin;
        this.edges = edges;
    }
}