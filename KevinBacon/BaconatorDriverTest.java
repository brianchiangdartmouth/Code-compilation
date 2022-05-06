import java.util.*;

public class BaconatorDriverTest {
    private static Graph<String, HashSet<String>> map;
    private static String center;
    private static Graph<String, String> baconMap;

    /**
     * Hard-coded simple graph
     */
    BaconatorDriverTest(){
        center = "Kevin Bacon";

        map = new AdjacencyMapGraph<String, HashSet<String>>();

        map.insertVertex("Kevin Bacon");
        map.insertVertex("Alice");
        map.insertVertex("Bob");
        map.insertVertex("Charlie");
        map.insertVertex("Dartmouth");
        map.insertVertex("Nobody");
        map.insertVertex("Nobody's Friend");

        map.insertUndirected("Kevin Bacon", "Alice", new HashSet<>(Arrays.asList("A Movie", "E Movie")));
        map.insertUndirected("Kevin Bacon", "Bob", new HashSet<>(Collections.singletonList("A Movie")));
        map.insertUndirected("Alice", "Bob", new HashSet<>(Collections.singletonList("A Movie")));
        map.insertUndirected("Alice", "Charlie", new HashSet<>(Collections.singletonList("D Movie")));
        map.insertUndirected("Bob", "Charlie", new HashSet<>(Collections.singletonList("C Movie")));
        map.insertUndirected("Charlie", "Dartmouth", new HashSet<>(Collections.singletonList("B Movie")));
        map.insertUndirected("Nobody", "Nobody's Friend", new HashSet<>(Collections.singletonList("F Movie")));


    }

    public Graph<String, HashSet<String>> getMap(){
        return map;
    }

}
