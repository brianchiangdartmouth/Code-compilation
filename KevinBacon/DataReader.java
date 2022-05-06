import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DataReader {
    Map<String, String> actorMap, movieMap, reverseActorMap, reverseMovieMap;
    Map<String, ArrayList<String>> movieActorMap, actorMovieMap;
    AdjacencyMapGraph<String, HashSet<String>> map;


    public DataReader(String actorPath, String moviePath, String movieActorPath) throws Exception {
        actorMap = arrayToStringHash(readerToString(actorPath).split("\\|"));
        reverseActorMap = arrayToReverseStringHash(readerToString(actorPath).split("\\|"));
        movieMap = arrayToStringHash(readerToString(moviePath).split("\\|"));
        reverseMovieMap = arrayToReverseStringHash(readerToString(moviePath).split("\\|"));
        movieActorMap = movieActorHash(readerToString(movieActorPath).split("\\|"));
        actorMovieMap = actorMovieHash(readerToString(movieActorPath).split("\\|"));

        System.out.println("Actor movie map coming up...");

        map = new AdjacencyMapGraph<>();
        for(String actor : actorMap.values()){
            map.insertVertex(actor); //insert every actor as vertex in map
        }
        System.out.println("Graph completed");
        for(String movieID : movieActorMap.keySet()){
            String movie = movieMap.get(movieID); //get name of every movie in map (to use as an edge)
            ArrayList<String> actors = movieActorMap.get(movieID);

            //draw an undirected edge from a to b
            for(int a=0; a<actors.size(); a++){
                for(int b=a+1; b<actors.size(); b++){
                    String actorNameA = actorMap.get(movieActorMap.get(movieID).get(a));
                    String actorNameB = actorMap.get(movieActorMap.get(movieID).get(b));

                    if (map.hasEdge(actorNameA,actorNameB)){ //if the edge already exists, add to existing HashSet edge
                        HashSet<String> relationship = map.getLabel(actorNameA,actorNameB);
                        relationship.add(movie);
                        map.insertUndirected(actorNameA,actorNameB,relationship);
                    }
                    else{//if the edge is not there, make a new HashSet
                        map.insertUndirected(actorNameA,actorNameB,new HashSet<String>(Arrays.asList(movie)));
                    }
                }
            }
        }
    }

    /**
     * @return map with actors as vertices and shared movies as (undirected) edges
     */
    public Graph<String, HashSet<String>> getMap(){
        return map;
    }

    /**
     * Turns file into string
     * @param path to a text file
     * @return the text as a string
     * @throws Exception
     */
    public String readerToString(String path) throws Exception{
        StringBuilder str = new StringBuilder();
        BufferedReader temp = new BufferedReader(new FileReader(path));
        String line = temp.readLine();
        while(line!=null) {
            str.append(line).append("|");
            line = temp.readLine();
        }
        return str.toString();
    }

    /**
     * Turns the movie-actors file into a map with movies as keys and actors as values
     * @param arr
     * @return
     */
    public HashMap<String, ArrayList<String>> movieActorHash(String[] arr){
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        for(int i=0; i<arr.length-1; i+=2){
            ArrayList<String> inp = map.get(arr[i]);
            if(inp==null)
                inp = new ArrayList<>();
            inp.add(arr[i+1]);
            map.put(arr[i], inp);
        }
        return map;
    }

    /**
     * Turns the movie-actors files into a map with actors as key and a string of movies as the value
     * @param arr
     * @return
     */
    public HashMap<String, ArrayList<String>> actorMovieHash(String[] arr){
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        for(int i=0; i<arr.length-1; i+=2){
            ArrayList<String> inp = map.get(arr[i+1]);
            if(inp==null)
                inp = new ArrayList<>();
            inp.add(arr[i]);
            map.put(arr[i+1],inp);
        }
        return map;
    }

    /**
     * Turns an actor or movie file into a map with ids as keys and names as values
     * @param arr
     * @return
     */
    public HashMap<String, String> arrayToStringHash(String[] arr){
        HashMap<String, String> map = new HashMap<>();
        for(int i=0; i<arr.length-1; i+=2){
            map.put(arr[i], arr[i+1]);
        }
        return map;
    }

    /**
     * Turns an actor or movie file into a map with names as keys and ids as values
     * @param arr
     * @return
     */
    public HashMap<String, String> arrayToReverseStringHash(String[] arr){
        HashMap<String, String> map = new HashMap<>();
        for(int i=0; i<arr.length-1; i+=1){
            map.put(arr[i+1], arr[i]);
        }
        return map;
    }

    /**
     * @return actor map where actor's id is key and actor's name is value
     */
    public Map<String, String> getActorMap() {
        return actorMap;
    }

    /**
     * @return reverse actor map where actor's name is key and actor's id is value
     */
    public Map<String, String> getReverseActorMap(){
        return reverseActorMap;
    }

    /**
     * @return reverse movie map where movie name is key and movie id is value
     */
    public Map<String, String> getReverseMovieMap(){
        return reverseMovieMap;
    }

    /**
     * @return movie map where movie id is key and movie name is value
     */
    public Map<String, String> getMovieMap() {
        return movieMap;
    }

    /**
     * @return movie actor map where movie id is key and actor id is value
     */
    public Map<String, ArrayList<String>> getMovieActorMap() {
        return movieActorMap;
    }

    /**
     * @return actor movie map where actor id is key and movie id is value
     */
    public Map<String, ArrayList<String>> getActorMovieMap() {
        return actorMovieMap;
    }

    /**
     * main method for testing DataReader
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DataReader d = new DataReader("Pset 4/bacon/actorsTest.txt", "Pset 4/bacon/moviesTest.txt", "Pset 4/bacon/movie-actorsTest.txt");
        System.out.println(d.getActorMovieMap());
    }
}
