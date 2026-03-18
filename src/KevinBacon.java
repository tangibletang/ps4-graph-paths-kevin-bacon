import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Kevin bacon game
 * @author Alex Tang CS10 Fall 2025
 */
public class KevinBacon {
    //stores everything in map
    private Graph<String, Set<String>> graph;
    private Map<String, String> moviesID;
    private Map<String, String> actorsID;
    private Map<String, ArrayList<String>> moviesActorMap;

    public KevinBacon(String moviesFileName, String actorsFileName, String maFileName) throws IOException {
        this.graph = new AdjacencyMapGraph<>();
        this.moviesID = new HashMap<>();
        this.actorsID = new HashMap<>();
        this.moviesActorMap = new HashMap<>();


        BufferedReader input = new BufferedReader(new FileReader(moviesFileName));

        String line;
        while ((line = input.readLine()) != null) {
            String[] parts = line.split("\\|");
            moviesID.put(parts[0], parts[1]);
        }
        input.close();


        BufferedReader input2 = new BufferedReader(new FileReader(actorsFileName));
        while ((line = input2.readLine()) != null) {
            String[] parts = line.split("\\|");
            actorsID.put(parts[0], parts[1]);
        }
        input2.close();

        BufferedReader input3 = new BufferedReader(new FileReader(maFileName));
        while ((line = input3.readLine()) != null) {
            String[] parts = line.split("\\|");
            String movieName = moviesID.get(parts[0]);
            String actorName = actorsID.get(parts[1]);
            if (moviesActorMap.containsKey(movieName)) {
                moviesActorMap.get(movieName).add(actorName);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(actorName);
                moviesActorMap.put(movieName, list);
            }
        }
        input3.close();

        for (String actor : actorsID.values()) {
            graph.insertVertex(actor);
        }

        for (String movie : moviesActorMap.keySet()) {
            ArrayList<String> actors = moviesActorMap.get(movie);

            for (int i = 0; i < actors.size(); i++) {
                for (int j = i + 1; j < actors.size(); j++) {
                    String actor1 = actors.get(i);
                    String actor2 = actors.get(j);

                    // Get or create the Set<String> label
                    Set<String> label;
                    if (graph.hasEdge(actor1, actor2)) {
                        label = graph.getLabel(actor1, actor2);
                    } else {
                        label = new HashSet<>();
                        graph.insertUndirected(actor1, actor2, label);
                    }

                    // Add this movie to the set of movies they share
                    label.add(movie);
                }
            }
        }
    }
    public Graph<String, Set<String>> getGraph() {
        return this.graph;
    }



    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        Graph<V,E> newGraphTree = new AdjacencyMapGraph<>();
        newGraphTree.insertVertex(source);
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        queue.add(source);
        visited.add(source);
        while (!queue.isEmpty()) {
            V u = queue.remove();
            for (V v : g.outNeighbors(u)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                    newGraphTree.insertVertex(v);
                    newGraphTree.insertDirected(v, u, g.getLabel(u, v));
                }
            }
        }
        return newGraphTree;
    }

    public static <V,E> Queue<V> getPath(Graph<V,E> tree, V v, boolean logConnections) {
        if (!tree.hasVertex(v)) {
            System.out.println("There was no path found");
            return new LinkedList<>();
        }

        Queue<V> path = new LinkedList<>();
        List<List<String>> movieConnections = new LinkedList<>();
        V current = v;

        while (current != null) {
            path.add(current);
            List<V> inNeighbors = new ArrayList<>();

            for (V neighbor: tree.outNeighbors(current)) {
                inNeighbors.add(neighbor);
                if (logConnections) {
                    System.out.println(current + " appeared in " + tree.getLabel(current, neighbor) + " with " + neighbor);
                }
            }

            if (!inNeighbors.isEmpty()) {
                current = inNeighbors.get(0);
            } else {
                current = null;
            }
        }
        return path;
    }

    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        Set<V> missing = new HashSet<>();
        for (V v : graph.vertices()) {
            missing.add(v);
        }
        for (V v : subgraph.vertices()) {
            if (missing.contains(v)) {
                missing.remove(v);
            }
        }
        return missing;
    }

    public static <V, E> double averageSeparation(Graph<V, E> tree, V root) {
        int totalDistance = sumDistanceHelper(tree, root, 0);
        int numVertices = tree.numVertices();

        // If the tree has no vertices or only the root, return 0.0
        if (numVertices <= 1) {
            return 0.0;
        }

        // Divide by (numVertices - 1) to exclude the root itself
        double average = (double) totalDistance / (double) (numVertices - 1);
        return average;
    }

    public static <V,E> int sumDistanceHelper(Graph<V,E> tree, V root, int sum) {
        int result = sum;
        for (V v : tree.inNeighbors(root)) {
            result += sumDistanceHelper(tree, v, sum + 1);
        }
        return result;
    }

    //list centers by separation
    public void listCentersBySeparation(Graph<String, Set<String>> graph, int number) {
        System.out.println("Listing top/bottom " + number + " centers by average separation.");
        PriorityQueue<ActorWithPriority> maxPQ = new PriorityQueue<>((a, b) -> (b.priority - a.priority > 0) ? 1 : b.priority - a.priority < 0 ? -1 : 0);
        PriorityQueue<ActorWithPriority> minPQ = new PriorityQueue<>((a, b) -> (a.priority - b.priority > 0) ? 1 : a.priority - b.priority < 0 ? -1 : 0);

        for (String v: graph.vertices()) {
            double priority = averageSeparation(bfs(graph, v), v);
            maxPQ.add(new ActorWithPriority(v, priority));
            minPQ.add(new ActorWithPriority(v, priority));
        }

        System.out.println("Top " + number);
        for (int i = 0; i < number; i++) {
            if (maxPQ.isEmpty()) break;
            System.out.println(maxPQ.poll());
        }

        System.out.println("Bottom " + number);
        for (int i = 0; i < number; i++) {
            if (minPQ.isEmpty()) break;
            System.out.println(minPQ.poll());
        }
    }

    private static class ActorWithPriority {
        public String name;
        public double priority;

        public ActorWithPriority(String name, double priority) {
            this.name = name;
            this.priority = priority;
        }

        public String toString() {
            return name + ", Average Separation: " + priority;
        }
    }

    public static void listActorsByDegree(Graph<String, Set<String>> graph, int low, int high) {
        PriorityQueue<ActorWithPriority> minPQ = new PriorityQueue<>((a, b) -> (a.priority - b.priority > 0) ? 1 : (a.priority - b.priority == 0) ? 0 : -1);

        for (String actor : graph.vertices()) {
            int degree = graph.outDegree(actor);
            degree += graph.inDegree(actor);
            if (degree >= low && degree <= high) {
                minPQ.add(new ActorWithPriority(actor, degree));
            }
        }

        ActorWithPriority actor;
        while ((actor = minPQ.poll()) != null) {
            System.out.println(actor.name + " - Degree: " + actor.priority);
        }
    }

    public static void listActorsWithInfiniteSeparation(
            Graph<String, Set<String>> graph,
            Graph<String, Set<String>> bfsTree) {

        Set<String> missing = KevinBacon.missingVertices(graph, bfsTree);
        System.out.println("Actors with infinite separation:");
        for (String actor : missing) {
            System.out.println(actor);
        }
    }
    public static void listActorsBySeparation(Graph<String, Set<String>>  bfsTree, int low, int high) {
        PriorityQueue<ActorWithPriority> minPQ = new PriorityQueue<>((a, b) -> (a.priority - b.priority > 0) ? 1 : a.priority - b.priority < 0 ? -1 : 0);

        for (String v: bfsTree.vertices()) {
            Queue<String> path = getPath(bfsTree, v, false);
            int pathSize = path.size() - 1;
            if (pathSize >= low && pathSize <= high) {
                minPQ.add(new ActorWithPriority(v, pathSize));
            }
        }

        while (!minPQ.isEmpty()) {
            ActorWithPriority nextActor = minPQ.poll();

            if (nextActor.priority >= low && nextActor.priority <= high) {
                System.out.println(nextActor.name + ", Separation: " + nextActor.priority);
            }
        }

    }





}