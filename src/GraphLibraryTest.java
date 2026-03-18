import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 *  Tester Class for Kevin Bacon
 *  @author Alex Tang
 */

public class GraphLibraryTest {
    public static void main(String[] args) throws IOException {
        String input1 = "/Users/alex/Desktop/CS10/CS10/PS4/moviesTest.txt";
        String input2 = "/Users/alex/Desktop/CS10/CS10/PS4/actorsTest.txt";
        String input3 = "/Users/alex/Desktop/CS10/CS10/PS4/movie-actorsTest.txt";
        KevinBacon test = new KevinBacon(input1, input2, input3);
        Graph<String, Set<String>> graph = test.getGraph();

        Scanner scanner = new Scanner(System.in);

        String centerOfUniverse = "Kevin Bacon";
        Graph<String, Set<String>> bfsTree = KevinBacon.bfs(graph, centerOfUniverse);
        double averageSeparation = KevinBacon.averageSeparation(bfsTree, centerOfUniverse);

        while (true) {
            System.out.println(centerOfUniverse + " is now the center of the acting universe. Average Separation to nodes: " + averageSeparation + ". Connects to " + bfsTree.numVertices() + "/" + graph.numVertices() + " actors");
            System.out.print("Enter command: ");
            String command = scanner.nextLine();
            String[] parts = command.split(", ");

            switch (parts[0]) {
                case "w":
                    input1 = "/Users/alex/Desktop/CS10/CS10/PS4/movies.txt";
                    input2 = "/Users/alex/Desktop/CS10/CS10/PS4/actors.txt";
                    input3 = "/Users/alex/Desktop/CS10/CS10/PS4/movie-actors.txt";
                    test = new KevinBacon(input1, input2, input3);
                    graph = test.getGraph();

                    centerOfUniverse = "Kevin Bacon";
                    bfsTree = KevinBacon.bfs(graph, centerOfUniverse);
                    averageSeparation = KevinBacon.averageSeparation(bfsTree, centerOfUniverse);
                    break;
                case "c":
                    int number = Integer.parseInt(parts[1]);
                    test.listCentersBySeparation(graph, number);
                    break;

                case "d":
                    int low = Integer.parseInt(parts[1]);
                    int high = Integer.parseInt(parts[2]);
                    KevinBacon.listActorsByDegree(graph, low, high);
                    break;

                case "i":
                    KevinBacon.listActorsWithInfiniteSeparation(graph, bfsTree);
                    break;

                case "p":
                    String actorName = parts[1];
                    Queue<String> path = KevinBacon.getPath(bfsTree, actorName, true);
                    if (path.isEmpty()) {
                        System.out.println("No path found to " + actorName);
                    } else {
                        System.out.println("Path from " + actorName + " to " + centerOfUniverse + ": " + path);
                    }
                    break;

                case "s":
                    low = Integer.parseInt(parts[1]);
                    high = Integer.parseInt(parts[2]);
                    KevinBacon.listActorsBySeparation(bfsTree, low, high);
                    break;

                case "u":
                    String newCenter = parts[1];
                    if (graph.hasVertex(newCenter)) {
                        centerOfUniverse = newCenter;
                        bfsTree = KevinBacon.bfs(graph, centerOfUniverse);
                        averageSeparation = KevinBacon.averageSeparation(bfsTree, centerOfUniverse);
                    }
                    break;

                case "q":
                    System.out.println("Quitting the game.");
                    scanner.close();
                    return;
            }
        }
    }
}
