import java.util.Arrays;

public class Cpu {
    // Instruction register
    private char[] IR = new char[4];

    // toggle
    private boolean C = false;

    // Instruction counter
    private int IC = 0;

    // General purpose Register
    private char[] R = new char[4];

    //Interrupt
    private int SI =0;
    private int PI =0;
    private int TI =0;

    

    // Constructor
    public Cpu() {
        // Instruction register
        Arrays.fill(IR, '0');

        // toggle
        this.C = false;

        // Instruction counter
        IC = 0;

        // General purpose register
        Arrays.fill(R, '0');
    }

    // getter for the instruction register
    //this will return the whole array
    public char[] getIR() {
        return this.IR;
    }

    //also create a getIR method which will accept a parameter of type int
    //and return the value of the IR at that index
    public int getIR(int index) {
        return this.IR[index];
    }

    // setter for the instruction register
    public void setIR(char[] IR) {
        this.IR = IR;
    }

    // getter for the toggle
    public boolean getC() {
        return this.C;
    }

    // setter for the toggle
    public void setC(boolean C) {
        this.C = C;
    }

    // getter for the instruction counter
    public int getIC() {
        return this.IC;
    }

    // setter for the instruction counter
    public void setIC(int IC) {
        this.IC = IC;
    }

    // getter for the general purpose register
    public char[] getR() {
        return this.R;
    }

    // setter for the general purpose register
    public void setR(char[] R) {
        this.R = R;
    }

    //getter for the PI
    public int getPI() {
        return PI;
    }

    //setter for the PI
    public void setPI(int PI) {
        this.PI = PI;
    }

    //getter for the TI
    public int getTI() {
        return TI;
    }

    //setter for the TI
    public void setTI(int TI) {
        this.TI = TI;
    }

    // getter for the interrupt
    public int getSI() {
        return SI;
    }
    //setter for the interrupt
    public void setSI(int sI) {
        SI = sI;
    }


    //// getter for getting the opcode
    //which is nothing but the first two bytes of the instruction register
    //which can be GD,PD,SR,LR,BT,H,CR
    public String getOpcode() {
        String opcode = "";
        opcode += this.IR[0];
        if(opcode.equals("H")){
            return opcode;
        }
        opcode += this.IR[1];
        return opcode;
    }

    //Similarly creating the getOperand Method which will return
    //the next 2 bytes of the instruction register which is mostly the mem loc
    public int getOperand() {
        return Integer.parseInt(String.valueOf(this.IR[2]) + String.valueOf(this.IR[3]));
    }

    
}
