import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class main {

    private static ArrayList<Node> nodes = new ArrayList<>(); //array of routers

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        nodes.add(new Node("a"));
        nodes.add(new Node("b"));
        nodes.add(new Node("c"));
        nodes.add(new Node("d"));
        nodes.add(new Node("e"));
        nodes.add(new Node("f"));

        addEdge("a","b",7);
        addEdge("a","c",9);
        addEdge("b","c",10);
        addEdge("a","f",14);
        addEdge("c","f",2);
        addEdge("c","d",11);
        addEdge("b","d",15);
        addEdge("d","e",6);
        addEdge("e","f",9);

        loop:
        while (true){
            String routerName;
            String first;
            String second;
            int weight;
            int found;
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();
            switch (command){
                case 0:
                    System.out.println("Goodbye!");
                    break loop;
                case 1:
                    for (Node node:nodes) {
                    node.table.printRoutingTable();
                    System.out.println();
                    }
                    break;
                case 2:
                    System.out.println("Enter router name:");
                    routerName = scanner.nextLine();
                    for (Node node:nodes) {
                        if(node.name.equals(routerName))
                            node.table.printRoutingTable();
                    }
                    break;
                case 3:
                    found = 0;
                    System.out.println("Enter router name (one char): ");
                    routerName = scanner.nextLine();
                    for (Node node:nodes) {
                        if(node.name.equals(routerName)){
                            System.out.println("There is already a router with this name");
                            found++;
                        }
                    }
                    if(found == 0){
                    nodes.add(new Node(routerName));
                    System.out.println("Router successfully added");
                    }
                    break;
                case 4:
                    found = 0;
                    System.out.println("Enter name of first router:");
                    first = scanner.nextLine();
                    System.out.println("Enter name of second router:");
                    second = scanner.nextLine();
                    System.out.println("Enter the weight of the edge:");
                    weight = scanner.nextInt();
                    for (Node node:nodes) {
                        if(node.name.equals(first) || node.name.equals(second))
                            found++;
                    }
                    if(found == 2) {
                        addEdge(first, second, weight);
                        System.out.println("Edge successfully added");
                    }
                    else
                        System.out.println("There is no router with this name");
                    break;
                case 5:
                    System.out.println("Enter name of first router:");
                    first = scanner.nextLine();
                    System.out.println("Enter name of second router:");
                    second = scanner.nextLine();
                    System.out.println("Enter the weight of the edge:");
                    weight = scanner.nextInt();
                    changeWeight(first,second,weight);
                    System.out.println("Weight successfully changed");
                    break;
                case 6:
                    System.out.println("Enter start router");
                    first = scanner.nextLine();
                    System.out.println("Enter end router");
                    second = scanner.nextLine();
                    Simulation sim = new Simulation(first,second);
                    sim.start(nodes);
            }
        }

    }

    private static void printMenu(){
        System.out.println("\n--------------------------");
        System.out.println("Choose your action:");
        System.out.println("0. Quit");
        System.out.println("1. Print all routing tables");
        System.out.println("2. Print routing table of selected router");
        System.out.println("3. Add router");
        System.out.println("4. Add edge");
        System.out.println("5. Change weight of the edge");
        System.out.println("6. Simulate packet sending");
        System.out.println("--------------------------\n");
    }

    private static void addEdge(String from, String to, int weight){
        Graph.addEdge(new Graph.Edge(from,to,weight));
        for (Node node: nodes) {
            //add neighbours
            if((node.name).equals(from))
                node.addNeighbour(findNodeByName(to));
            else if ((node.name).equals(to))
                node.addNeighbour(findNodeByName(from));
            node.graph();
            if((node.name).equals(from)){
                node.myEdges.add(new Graph.Edge(from,to, weight));
            }
        }
        flood();
        calculateTables();
    }

    private static void changeWeight(String from, String to, int weight){
        Graph.changeWeight(from,to,weight);
        for (Node node: nodes) {
            for (Graph.Edge edge :node.myEdges) {
                if(edge.v1.equals(from) && edge.v2.equals(to))
                    edge.dist = weight;
                else if(edge.v1.equals(to) && edge.v2.equals(from))
                    edge.dist = weight;
            }
            node.graph();
        }
        flood();
        calculateTables();
    }



    private static void flood(){
        for (Node node: nodes) {
            node.createLSA();
            sendLSA(node,node.myPacket);
        }
    }

    private static void sendLSA(Node node,LSA packet){
        if(packet.sequenceNo > findSequenceNo(node,packet)){
            if(!node.packets.contains(packet)) {
                node.saveLSA(packet);
                for (Node neighbour : node.getNeighbours()) {
                    sendLSA(neighbour, packet);
                }
             }
        }
    }

    private static int findSequenceNo(Node node, LSA packet){
        packet.sequenceNo--;
        for (LSA packs: node.packets) {
            if(packet.equals(packs))
                return packs.sequenceNo;
        }
        return -1;
    }

    private static Node findNodeByName(String name){
        for (Node node:nodes) {
            if(node.name.equals(name))
                return node;
        }
        return null;
    }

    private static void calculateTables(){
        for (Node node:nodes) {
            node.createGraph();
            Graph g = new Graph(node.getGraph());
            g.dijkstra(node.name);
            g.fillTable(node);
            Collections.reverse(node.table.path);
            Collections.reverse(node.table.destination);
            node.table.getNextHop(node.table.path);
        }
    }
}