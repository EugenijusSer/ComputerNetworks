import java.util.ArrayList;

public class Simulation {

    private String destination,currentRouter, nextRouter;

    Simulation(String start, String end){
        this.destination = end;
        this.currentRouter = start;
    }

    void start(ArrayList<Node> nodes){
        Runnable list = () -> simulate(nodes);
        new Thread(list).start();
    }

    void simulate(ArrayList<Node> nodes){
        try {
            System.out.println("STARTING SIMULATION");
            while(!currentRouter.equals(destination)){
                System.out.println("Packet is currently in " + currentRouter);
                for (Node node:nodes) {
                    if(currentRouter.equals(node.name)){
                        if(node.table.destination.contains(destination))
                        nextRouter = node.table.nextHop.get(node.table.destination.indexOf(destination));
                        else{
                            System.out.println("Destination router can't be reached");
                            return;
                        }
                    }
                }
                System.out.println("Sending packet from " + currentRouter + " to " + nextRouter);

                Thread.sleep(15000);

                currentRouter = nextRouter;
                System.out.println("Packet transferred to " + currentRouter + "\n");
            }
            System.out.println("Packet reached destination");

        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
