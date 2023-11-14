public class Main {
    public static void main(String[] args) {
        SPARS spars = new SPARS("input.txt", "output.txt");
        System.out.println("\nOS initialized");
        spars.init();
        spars.LOAD();
    }
}