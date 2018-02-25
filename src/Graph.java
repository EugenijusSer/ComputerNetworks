import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Graph {
    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
    /**
     * One edge of the graph (only used by Graph constructor)
     */
    public static class Edge {
        public String v1, v2;
        public int dist;

        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /**
     * One vertex of the graph, complete with mappings to neighbouring vertices
     */

    public class Vertex implements Comparable<Vertex> {
        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<>();

        public Vertex(String name) {
            this.name = name;
        }

        private void printPath(Node node, int i) {
            if (this == this.previous) {
                //System.out.printf("%s", this.name);
                node.table.path.add(i,this.name);
                node.table.destination.add(i,this.name);
            } else if (this.previous == null) {
                //System.out.printf("%s(unreached)", this.name);
            } else {
                this.previous.printPath(node,i);
                //System.out.printf(" -> %s(%d)", this.name, this.dist);
                String temp = node.table.path.get(i);
                temp += " -> " + this.name + "(" + this.dist + ")";
                node.table.path.remove(i);
                node.table.path.add(i,temp);
                node.table.destination.remove(i);
                node.table.destination.add(i,this.name);
            }
        }

        public int compareTo(Vertex other) {
            if (dist == other.dist)
                return name.compareTo(other.name);

            return Integer.compare(dist, other.dist);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + dist + ")";
        }
    }

    /**
     * Builds a graph from a set of edges
     */
    public Graph(Edge[] edges) {
        graph = new HashMap<>(edges.length);
        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }

        //removes vertex from graph, but doesn't remove edge
        //if (graph.containsKey("a")) graph.remove("a");
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /**
     * Implementation of dijkstra's algorithm using a binary heap.
     */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE)
                break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /**
     * Prints the path from the source to every vertex (output order is not guaranteed)
     */
    public void fillTable(Node node) {
        node.table.path.clear();
        node.table.destination.clear();
        for (Vertex v : graph.values()) {
            int i = 0;
            v.printPath(node,i);
            //System.out.println();
        }
    }



    /**
     * Prints a path from the source to the specified vertex
     */
    public void printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        //graph.get(endName).printPath();
        System.out.println();
    }

    private static Graph.Edge[] edges = {};
    private static int edgeNo; //using edgeNo - 1 for initializing arrays
    public static void addEdge(Graph.Edge edge){

        Graph.Edge[] temp = new Graph.Edge[edgeNo];
        System.arraycopy( edges, 0, temp, 0, edges.length );

        edges =  new Graph.Edge[edgeNo+1];
        System.arraycopy( temp, 0, edges, 0, temp.length );
        edges[edgeNo] = edge;
        edgeNo++;
    }
    public static void changeWeight(String from, String to, int weight){
        for (Graph.Edge edge:edges) {
            if(edge.v1.equals(from) && edge.v2.equals(to))
                edge.dist = weight;
            else if(edge.v1.equals(to) && edge.v2.equals(from))
                edge.dist = weight;
        }
    }
    public static Graph.Edge[] graph(){
        return edges;
    }
}