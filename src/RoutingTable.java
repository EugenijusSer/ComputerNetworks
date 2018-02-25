import java.util.ArrayList;

public class RoutingTable {
    private String source;
    public ArrayList<String> destination = new ArrayList<>();
    public ArrayList<String> nextHop = new ArrayList<>();
    public ArrayList<String> path = new ArrayList<>();

    RoutingTable(String name){
        source = name;
    }

    public void printRoutingTable(){

        System.out.println("Source|Destination|Next hop|\t\tPath");
        System.out.println("-------------------------------------------------");
        if(path.size() != 0) {
            for (int i = 0; i < path.size(); i++) {
                System.out.println("  " + source + "   |\t\t" + destination.get(i) + "\t  |   " + nextHop.get(i) + "\t   | " + path.get(i));
            }
        }
        else
            System.out.println("Router " + source + " has no info");

    }

    public void getNextHop(ArrayList<String> path){
        nextHop.clear();
        for (String pat:path) {
            if(pat.length()>4)
                nextHop.add(pat.substring(5,6));
            else
                nextHop.add("-");
        }
    }
}
