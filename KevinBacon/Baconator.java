import java.util.*;

public class Baconator {

    /**
     * BFS to find shortest path from other actors to center of universe
     * When traversing, records relationship from current vertex to the one that came before it
     * @param g        graph to implement bfs
     * @param source   center of universe in graph
     * @return tree of shortest paths represented as graph
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
        AdjacencyMapGraph<V,E> graph = new AdjacencyMapGraph<>();
        Queue<V> q = new LinkedList<>();   //queue to implement BFS
        HashSet<V> visited = new HashSet<>();

        graph.insertVertex(source);
        visited.add(source);
        q.add(source);

        while(q.size()!=0){
            V current = q.remove();
            for(V neighbor : g.outNeighbors(current)) {
                if(!visited.contains(neighbor)) {
                    graph.insertVertex(neighbor);   //inserts neighbor into graph of shortest paths
                    graph.insertDirected(neighbor, current, g.getLabel(source, neighbor));   //relationship pointing from neighbor to current
                    q.add(neighbor);   //neighbor will be looked at as current vertex in next loop
                    visited.add(neighbor);   //marks neighbor as visited
                }
            }
        }

        return graph;
    }

    /**
     * Get shortest path from specific actor to center of universe
     * Starts at actor and records/traverses outneighbor path until it leads to center
     * @param tree     graph of shortest paths from bfs
     * @param v        specific actor used to find path to center
     * @return list of pathway from actor to center
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
        ArrayList<V> path = new ArrayList<>();

        while (tree.outNeighbors(v).iterator().hasNext()){
            path.add(v);
            v = tree.outNeighbors(v).iterator().next();
        }
        path.add(v);
        return path;
    }

    /**
     * Gets actors that don't have a path to center
     * @param graph     graph of all relationships between actors
     * @param subgraph  graph only of actors that link to center
     * @return set of actors that don't link to center
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        HashSet<V> set = new HashSet<>();
        for(V neighbor : graph.vertices())
            set.add(neighbor);
        for(V neighbor : subgraph.vertices())
            set.remove(neighbor);
        return set;
    }

    /**
     * Gets avg path length between the center and all actors that have path to center
     * @param tree     graph of shortest paths from bfs
     * @param root     actor used for center
     * @return avg path length as double
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
        int sum = sumSeparation(tree, root, 1);   //total sum of path lengths
        int counter = tree.numEdges();   //use edges & not vertices so universe center is not counted
        return (double)sum/counter;
    }

    /**
     * Gets path length between the center and all actors that have path to center
     * @param tree     graph of shortest paths from bfs
     * @param root     actor used for center
     * @param depth    keep track of path length in recursion
     * @return sum path length as int
     */
    public static <V, E> int sumSeparation(Graph<V,E> tree, V root, int depth){
        int sum = 0;
        for(V neighbor : tree.inNeighbors(root)){   // repeats for all actors that point to root
            sum+=depth;   // adds length of path to total sum
            sum+=sumSeparation(tree, neighbor,depth+1);
        }
        return sum;
    }
}
