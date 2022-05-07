import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compress {
    private HashMap<Character, Integer> freqMap;
    public HashMap<Character, ArrayList<Boolean>> encodingKey;
    public static BinaryTree<Element> decodingKey;
    public BufferedReader input;
    private BufferedBitWriter output;
    private String path;

    /**
     * Stores character and frequency
     * Used by tree
     */
    private class Element{
        private char c;
        private int freq;

        private Element(char c, int freq){
            this.c = c;
            this.freq = freq;
        }

        private char getChar(){
            return c;
        }

        private int getFreq(){
            return freq;
        }

        public String toString(){
            return c+":\t"+freq;
        }
    }

    /**
     * Initializes input file (and other variables)
     * @param path to file to be compressed
     * @throws FileNotFoundException
     */
    public Compress(String path) throws FileNotFoundException {
        input = new BufferedReader(new FileReader(path));
        freqMap = new HashMap<>();
        output = new BufferedBitWriter(path+"Compressed.txt");
        this.path = path;
    }

    /**
     * Rewrites file to be compressed to inputted file
     * @param input file to be compressed
     */
    public void setInput(BufferedReader input) {
        this.input = input;
    }

    /**
     * @return path of file to be compressed
     */
    public String getPath(){
        return path;
    }

    /**
     * @return path of compressed file
     */
    public String getEncodedOuputPath(){
        return path+"Compressed.txt";
    }

    /**
     * @return path of decompressed file
     */
    public String getDecodedOutputPath(){
        return path+"Decompressed.txt";
    }

    /**
     * used while testing and debugging
     * @return map of characters and their frequencies
     */
    public HashMap<Character, Integer> getFreqMap() {
        return freqMap;
    }

    /**
     * Fills the character frequency map using the inputted file
     * @throws IOException
     */
    public void chartFrequencies() throws IOException {
        if(input!=null){
            BufferedReader copyOfInput = new BufferedReader(new FileReader(path));
            int c = copyOfInput.read();
            while(c!=-1){
                try{
                    freqMap.put((char)c,freqMap.get((char)c)+1);
                }
                catch(Exception e){
                    freqMap.put((char)c,1);
                }
                c = copyOfInput.read();
            }
        }
    }

    /**
     * Makes decoding key (a tree where left is 0 and right is 1)
     * Uses a priority queue to optimize the tree (most frequent characters are
     * closest to the top of the tree, least frequent are the furthest down)
     */
    public void makeTree(){
        //make sure map has at least 2 elements
        while(freqMap.size()<2){
            //if map has 0 or 1 elements, try adding a space (if not already in the map)
            if(!freqMap.containsKey(' '))
                freqMap.put(' ',1);
            //map has to have a size of 1, and the element must be a space if this else statement is reached -- this means it's safe to add any character
            else
                freqMap.put('.',1);
        }

        // initialize queue
        PriorityQueue<BinaryTree<Element>> q = new PriorityQueue<>((BinaryTree<Element> t1, BinaryTree<Element> t2) -> t1.getData().getFreq()-t2.getData().getFreq());

        // add all elements in queue
        for(Character c : freqMap.keySet())
            q.add(new BinaryTree<>(new Element(c, freqMap.get(c))));

        // condenses all elements into one tree, with the lowest frequencies deepest in the tree
        while(q.size()>1){
            BinaryTree<Element> t1, t2;
            t1 = q.poll();
            t2 = q.poll();
            q.add(new BinaryTree<>(new Element((char)0,t1.getData().getFreq()+t2.getData().getFreq()),t1,t2));
        }
        decodingKey = q.poll();     // final tree

        encodingKey = treeToKey(decodingKey, new ArrayList<Boolean>());
    }

    /**
     * Makes encoding key (a map where character keys return binary code values)
     * using decoding key (tree)
     * @param tree
     * @param path
     * @return
     */
    public HashMap<Character, ArrayList<Boolean>> treeToKey(BinaryTree<Element> tree, ArrayList<Boolean> path){
        HashMap<Character, ArrayList<Boolean>> map = new HashMap<>();
        if(tree.hasLeft()){
            ArrayList<Boolean> leftPath = new ArrayList<>(path);
            leftPath.add(false);
            map.putAll(treeToKey(tree.getLeft(),leftPath));
        }
        if(tree.hasRight()) {
            ArrayList<Boolean> rightPath = new ArrayList<>(path);
            rightPath.add(true);
            map.putAll(treeToKey(tree.getRight(), rightPath));
        }
        if(tree.isLeaf()){
            map.put(tree.getData().getChar(),path);
        }
        return map;
    }

    /**
     * Encodes the inputted file using the char/binary code map
     * @throws IOException
     */
    public void encode() throws IOException {
        System.out.println("Encoding "+path+"...");
        chartFrequencies();
        makeTree();
        if(input!=null) {
            BufferedReader copy = new BufferedReader(new FileReader(path));
            int c = copy.read();
            while (c != -1) {
                ArrayList<Boolean> code = encodingKey.get((char)c);
                for(Boolean b : code){
                    output.writeBit(b);
                }
                c = copy.read();
            }
        }
        output.close();
        System.out.println("encoding finished");
    }

    /**
     * Decodes binary code by traversing the tree until it reaches a leaf node
     * Goes left if 0, right if 1.
     * If it reaches a leaf node it will save the character and start at the top of the tree
     * @param path
     * @throws IOException
     */
    public static void decode(String path) throws IOException {
        BufferedBitReader read = new BufferedBitReader(path+"Compressed.txt");
        BufferedWriter decodedOutput = new BufferedWriter(new FileWriter(path+"Decompressed.txt"));

        BinaryTree<Element> tree = decodingKey;
        while(read.hasNext()){
            boolean c = read.readBit();
            if(c)
                tree=tree.getRight();
            else
                tree=tree.getLeft();

            if(tree.isLeaf()) {
                decodedOutput.write(tree.getData().getChar());
                tree = decodingKey;
            }
        }
        decodedOutput.close();
    }

    /**
     * Helper method -- checks if two files are identical (character by character)
     * @param path1
     * @param path2
     * @return
     * @throws Exception
     */
    public static boolean checkTwoFiles(String path1, String path2) throws Exception {
        BufferedReader r1 = new BufferedReader(new FileReader(path1));
        BufferedReader r2 = new BufferedReader(new FileReader(path2));

        int c1 = r1.read();
        int c2 = r2.read();
        while(c1!=-1 || c2!=-1){
            if(c1!=c2)
                return false;
            c1 = r1.read();
            c2 = r2.read();
        }
        return true;
    }
}
