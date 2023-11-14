//defining the PCB class
import java.util.*;
class PCB{
    private char jobID[] = new char[4];
    private char TTL[] = new char[4];
    private char TLL[] = new char[4];

    //Constructor for the PCB class
    PCB() {
        //jobID
        Arrays.fill(jobID, '0');

        //TTL
        Arrays.fill(TTL, '0');

        //TLL
        Arrays.fill(TLL, '0');
    }

    public PCB(char jobID[], char TTL[], char TLL[]){
        this.jobID = jobID;
        this.TTL = TTL;
        this.TLL = TLL;
    }

    //Creating getters and setters for the PCB class

    public char[] getJobID() {
        return jobID;
    }

    public void setJobID(char[] jobID) {
        this.jobID = jobID;
    }

    public char[] getTTL() {
        return TTL;
    }

    public void setTTL(char[] TTL) {
        this.TTL = TTL;
    }

    public char[] getTLL() {
        return TLL;
    }

    public void setTLL(char[] TLL) {
        this.TLL = TLL;
    }

    //toString method to print the PCB object

    @Override

    public String toString() {
        return "PCB{" +
                "jobID=" + String.valueOf(jobID) +
                ", TTL=" + String.valueOf(TTL) +
                ", TLL=" + String.valueOf(TLL) +
                '}';
    }

}