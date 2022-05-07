public class CompressionTester {
    public static void main(String[] args) throws Exception {
        String path1 = "HuffmanCoding/inputs/Test";                       //original debugging test case, fairly short, lots of characters
        String path2 = "HuffmanCoding/inputs/USConstitution.txt";         //provided test case, longer
        String path3 = "HuffmanCoding/inputs/WarAndPeace.txt";            //provided test case, very long
        String path4 = "HuffmanCoding/inputs/EmptyTest.txt";              //edge case, empty file
        String path5 = "HuffmanCoding/inputs/OneCharTest.txt";            //edge case, only one character in file
        String path6 = "HuffmanCoding/inputs/OneCharRepeatedTest.txt";    //edge case, the file only contains some multiple of one character

        System.out.println(test(path1));
        System.out.println(test(path2));
        System.out.println(test(path3));
        System.out.println(test(path4));
        System.out.println(test(path5));
        System.out.println(test(path6));
    }

    /**
     * helper method to test if a file has been compressed and decompressed properly
     * compresses file, decompresses file, then checks if inputted file is identical
     * @param path to file to be compressed
     * @return true if test is successful
     * @throws Exception
     */
    public static boolean test(String path) throws Exception{
        Compress compressor = new Compress(path);
        compressor.encode();
        Compress.decode(path);
        //Testing – unsure if you wanted this in final product, so we have it commented out just in case
        //System.out.println(compressor.getFreqMap());
        //System.out.println(Compress.decodingKey);
        //System.out.println(compressor.encodingKey);

        
        return Compress.checkTwoFiles(path,path+"Decompressed.txt");
    }
}
