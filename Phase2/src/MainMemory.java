public class MainMemory {
    // Main memory
    private char[][] M = new char[300][4];

    // Constructor
    public MainMemory() {
        // this is the main memory
        this.M = new char[300][4];
    }

    // getter for the main memory
    public char[][] getMemory() {
        return this.M;
    }

    // setter for the main memory
    public void setMemory(char[][] M) {
        this.M = M;
    }
}