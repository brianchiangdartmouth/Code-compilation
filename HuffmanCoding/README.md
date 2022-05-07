## Summary
Using trees, maps, and priority queues, I create a Huffman code for lossless data compression. When compressing a file to save disk space, methods can either be lossy (permanently removes data) or lossless (does not eliminate any data). 

## What can I do?
Run *CompressionTester*. For every txt file in the *inputs* folder, it creates two files: a compressed version, and a version that decompresses the compressed version. By using a method to check, the initial file and the final decompressed version are identical – proving that the decompression and compression methods work.

## File summary
*inputs*: txt files of tests cases to see if there is proper compression and decompression

*BinaryTree*: generic binary tree, storing nodes with their values and children as parameters

*BufferedBitReader*: reads bits from a given file

*BufferedBitWrtier*: writes bits into a given file

*Compress*: creates character frequency map, Huffman tree (used for decryption), and encoding key Hashmap (character keys return binary code values). also contains encode() and decode() methods, and helper method to check if two files are identical

*CompressionTester*: creates one compressed and one decompressed file from *inputs*, storing them in the same folder. Checks if initial txt file and final txt file (after compressing and decompressing) are identical
