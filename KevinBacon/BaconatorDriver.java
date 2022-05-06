import java.util.*;

public class BaconatorDriver {
    private static Graph<String, HashSet<String>> map;
    private static String center;
    private static DataReader d;
    private static Graph<String,HashSet<String>> baconMap;

    /**
     * Interface for user input
     * Loops until q is pressed
     * Creates DataReader object to create useable hashmaps of data files
     */
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("Commands:\n" +
                "1: test on hard-coded simpler graph\n" +
                "2: test on graph from simpler test file\n" +
                "3: test on default actor graph\n" +
                "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
                "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                "i: list actors with infinite separation from the current center\n" +
                "p <name>: find path from <name> to current center of the universe\n" +
                "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                "u <name>: make <name> the center of the universe\n" +
                "q: quit game\n");

        //sets data to default (aka full movie actor lists)
        normalMap();

        while(true){
                System.out.println("\n" + center + " game >");
                String inp = sc.nextLine(); // user input

                String firstChar = inp.substring(0,1); // first char of input for command
                String detail = "";
                if(inp.length()>1)
                    detail = inp.substring(2); // third char of input for parameter

                if(firstChar.equals("1"))
                    hardCodedMap();
                if(firstChar.equals("2"))
                    testMap();
                if(firstChar.equals("3"))
                    normalMap();
                if(firstChar.equals("c"))
                    centersSortedAvgSep(detail);
                if(firstChar.equals("d"))
                    actorsSortedDegree(detail);
                if(firstChar.equals("i"))
                    infiniteBaconNumber();
                if(firstChar.equals("p"))
                    pathToCenter(detail);
                if(firstChar.equals("s"))
                    actorsSortedSep(detail);
                if(firstChar.equals("u"))
                    baconMap = changeCenter(detail);
                if(firstChar.equals("q"))
                    break;

//                //EC
//                if(firstChar.equals("r"))
//                    multiplexity(detail);
        }
        System.out.println("Thanks for playing!");

    }

    /**
     * Actor class
     */
    private static class Actor implements Comparable {
        private final String name;
        private final double separation;

        /**
         * @param name          actor name
         * @param separation    average separation between name and all other actors //is this right??
         */
        private Actor(String name, double separation){
            this.name = name;
            this.separation = separation;
        }

        private String getName(){
            return name;
        }

        private double getSeparation(){
            return separation;
        }

        @Override
        public int compareTo(Object a){
            return Double.compare(this.separation, ((Actor) a).separation); //compareTo based on average separation
        }
    }

    /**
     * DirectActor class
     * For R command when finding out which actors share the most movies with center
     */
    private static class DirectActor implements Comparable<DirectActor> {
        private final String name;
        private final HashSet<String> sharedMovies;

        /**
         * @param name          actor name
         * @param sharedMovies  set of shared movies between actor and center
         */
        private DirectActor(String name, HashSet<String> sharedMovies){
            this.name = name;
            this.sharedMovies = sharedMovies;
        }

        private String getName(){
            return name;
        }

        private int getSharedMoviesNo(){
            return sharedMovies.size();
        }

        private HashSet<String> getSeparation(){
            return sharedMovies;
        }

        @Override
        public int compareTo(DirectActor a){
            return (-Integer.compare(this.sharedMovies.size(), a.sharedMovies.size()));

        }

    }


    /**
     * Test case for hard-coded map
     */
    public static void hardCodedMap(){
        BaconatorDriverTest temp = new BaconatorDriverTest();
        map = temp.getMap();
        System.out.println("Graph has become the hard-coded test map");
        center = "Kevin Bacon";
        baconMap = changeCenter(center);
    }

    /**
     * Test case for default actor map
     */
    public static void normalMap() throws Exception {
        d = new DataReader("KevinBacon/bacon/actors.txt", "KevinBacon/bacon/movies.txt", "KevinBacon/bacon/movie-actors.txt");
        map = d.getMap();
        center = "Kevin Bacon";
        baconMap = changeCenter(center);
        System.out.println("Graph has become the test map");
    }


    /**
     * Test case for default actor map
     */
    public static void testMap() throws Exception {
        d = new DataReader("KevinBacon/bacon/actorsTest.txt", "KevinBacon/bacon/moviesTest.txt", "KevinBacon/bacon/movie-actorsTest.txt");
        map = d.getMap();
        center = "Kevin Bacon";
        baconMap = changeCenter(center);
        System.out.println("Graph has become the test map");
    }

    /**
     * Lists top/bottom number of centers of universe
     * Top: avg. separation between centers + reachable actors is lowest
     * Bottom: avg. separation between centers + reachable actors is highest. Activate by inputting negative number as param
     * @param detail for number of actors
     */
    public static void centersSortedAvgSep(String detail){
        try{
            int num = Integer.parseInt(detail);
            PriorityQueue<Actor> q;
            if(num<1)
                q = new PriorityQueue<>((a,b) -> (int)(b.getSeparation()-a.getSeparation()));
            else
                q = new PriorityQueue<>();
            for(String actor : map.vertices()){
                Graph<String, HashSet<String>> actorMap = Baconator.bfs(map,actor); //puts each actor as a root (center of universe)
                q.add(new Actor(actor, Baconator.averageSeparation(actorMap,actor)));  //records average separation for each actor
            }

            StringBuilder str = new StringBuilder();
            for(int i=0; i<Math.abs(num); i++){
                Actor a = q.remove(); //priority is based on average separation – negative param = highest separation avg, positive param = lowest separation avg
                str.append("Actor ").append(a.getName()).append(" with ").append(a.getSeparation()).append(" average separations\n");
            }
            System.out.println(str);
        }
        catch(Exception e){
            System.out.println("Put your C command in following format: c <int>, like c 3");
        }
    }

    /**
     * Lists actors with number of costars between a min and max
     * @param detail for min and max boundary
     */
    public static void actorsSortedDegree(String detail){
        try{
            String[] bounds = detail.split(" "); //splits min and max
            ArrayList<Actor> lst = new ArrayList<>();
            for(String actor : map.vertices()){
                int degrees = map.outDegree(actor);
                if(Integer.parseInt(bounds[0])<=degrees && degrees<=Integer.parseInt(bounds[1])) //only returns actor if outDegrees is within bounds
                    lst.add(new Actor(actor,degrees));
            }

            StringBuilder str = new StringBuilder();
            for(Actor a : lst)
                str.append("\tActor ").append(a.getName()).append(" has movies with ").append(a.getSeparation()).append(" actors\n");
            System.out.println(str);
        }
        catch(Exception e){
            System.out.println("Put your D command in following format: d <low> <high>, like d 2 5 ");
        }
    }

    /**
     * Lists actors that don't have reachable path to center of universe
     */
    public static void infiniteBaconNumber(){
        System.out.print(center+" has an infinite degree of separation with the following actors: \n");
        for(String actor : Baconator.missingVertices(map,baconMap))
            System.out.print("\t"+actor);
        System.out.println();
    }

    /**
     * Finds path from specific actor to center
     * @param detail for specific actor
     */
    public static void pathToCenter(String detail){

        try{
            List<String> path = Baconator.getPath(baconMap, detail);
            System.out.println(detail + "'s number is " + (path.size()-1));

            while(path.size()>1){

                HashSet<String> commonMovies = map.getLabel(path.get(0),path.get(1));

                String str = path.get(0)+" appeared in [";
                for(String movie : commonMovies)
                    str+=(movie+",");
                str = str.substring(0,str.length()-1);
                str+= "] with " + path.get(1); //path.get(1) is next costar in path
                System.out.println(str);

                path.remove(0); //next loop advances in path to center
            }

            }
        catch(Exception e){
            System.out.println("Actor is not in database!");
        }

    }

    /**
     * Lists actors and their Bacon number from the current center, assuming there is a reachable path to center
     * Prints actors sorted from lowest to highest with Bacon numbers within min and max range
     * @param detail – min and max (inclusive)
     */
    public static void actorsSortedSep(String detail){

        try{
            String[] bounds = detail.split(" ");
            PriorityQueue<Actor> q = new PriorityQueue<>();

            for(String actor : map.vertices()){
                try{
                    int separation = Baconator.getPath(baconMap, actor).size()-1; //finds Bacon number
                    if(Integer.parseInt(bounds[0])<=separation && separation<=Integer.parseInt(bounds[1]))
                        q.add(new Actor(actor, separation)); //only adds if number is within range
                }
                catch(NullPointerException e){}
            }
            StringBuilder str = new StringBuilder();
            while(!q.isEmpty()){
                Actor a = q.remove(); //priority based on smallest separation number
                str.append(a.getName()).append(" with ").append(a.getSeparation()).append(" separations,\n");
            }
            System.out.println(str);
        }
        catch(Exception e){
            System.out.println("Put your S command in following format: s <low> <high>, like s 2 5");
        }

    }

    /**
     * Sets another actor as center of universe
     * @param detail    actor to be center
     * @return bfs, graph of paths from other actors to center
     */
    public static Graph<String,HashSet<String>> changeCenter(String detail){
        try{
            center = detail;
            baconMap = Baconator.bfs(map,center);
            System.out.println(center + " is now the center of the acting universe, connected to "+(baconMap.numVertices()-1)+"/"+ baconMap.numVertices()+" actors with average separation "+(Baconator.averageSeparation(baconMap, center))+"\n");
            return Baconator.bfs(map, center);
        }
        catch(Exception e){ //if actor is not in database
            System.out.println("Actor is not in database!");
            return null;
        }

    }



}
