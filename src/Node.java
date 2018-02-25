import java.util.ArrayList;

public class Node {

    public String name;
    private ArrayList<Node> neighbours = new ArrayList<>();
    private Graph.Edge[] edges = {};
    public ArrayList<LSA> packets = new ArrayList<>(); // all received lsa packets
    private ArrayList<Graph.Edge> graph = new ArrayList<>();
    public ArrayList<Graph.Edge> myEdges = new ArrayList<>();
    public LSA myPacket = new LSA(name,myEdges);
    public RoutingTable table;

    public Node(String name){
        this.name = name;
        Graph.addEdge(new Graph.Edge(name,name,0));
        packets.add(myPacket);
        table = new RoutingTable(name);
    }

    public void createGraph(){
        for (LSA packet:packets) {
            for (Graph.Edge edge: packet.edges) {
                graph.add(edge);
            }
        }
    }

    public void addNeighbour(Node neighbour){
        if(!neighbours.contains(neighbour))
        neighbours.add(neighbour);
    }

    public ArrayList<Node> getNeighbours(){
        return neighbours;
    }

    public void printNeighbours(){
        for (Node neighbour: neighbours)
            System.out.println(neighbour.name);
    }

    public Graph.Edge[] getGraph(){
        return edges;
    }

    public void saveLSA(LSA lsa){
        lsa.sequenceNo--;
        if(packets.contains(lsa))
            packets.remove(packets.indexOf(lsa));
        lsa.sequenceNo++;
        packets.add(lsa);
    }

    public void createLSA(){
        myPacket.origin = name;
        myPacket.sequenceNo++;
        myPacket.edges = myEdges;

    }

    public void graph(){ edges = Graph.graph(); }
}
