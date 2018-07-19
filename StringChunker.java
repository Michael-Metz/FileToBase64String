/**
 * Stringer chunker will be a list of Strings with a set max length default is 140(thanks twitter).
 * The idea of this class is to have really long strings be devided into chunks instead.
 * Thus you can easily iterate over them and output them.
 */
public class StringChunker {

    final static int DEFAULT_CHUNK_LENGTH= 140;
    private int maxChunkLength;
    private int size;

    private class Node{
        private Node next;
        private Node previous;
        private String data;

        public Node(String data){
            this.data = data;
        }
    }
    public StringChunker(){
        this.size = 0;
        this.maxChunkLength = DEFAULT_CHUNK_LENGTH;
    }
    public StringChunker(int maxChunkLength){
        this.maxChunkLength = maxChunkLength;
    }

    private void addChunk(String chunk){

    }
}
