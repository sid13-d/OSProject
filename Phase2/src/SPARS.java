import java.io.*;
import java.util.*;

class Helper{
    protected Map.Entry<Integer, int[]> allocate(int[] arr){

        
        //generating a random value from 0-29
        int value = (int)(Math.random() * 30);
        System.out.println("The value is : "+value+"\n");

        //check wheather it is generated if it is then again generate new value
        while(true){
            if(arr[value] == 0){
                arr[value] = 1;
                break;
            }
            else{
                value = (int)(Math.random() * 30);
            }
        }

        //returning the value and the arr
        //creating a map entry
        Map.Entry<Integer, int[]> pair = new AbstractMap.SimpleEntry<Integer, int[]>(value, arr);

        return pair;
    }
}


public class SPARS extends Helper {

    //is Exceeded
    boolean isExceeded = false;
    boolean reachedH = false;
    // Memory
    private MainMemory M;

    // CPU
    private Cpu cpu;

    // Declare the input file and the output file
    private FileReader input;
    private FileWriter output;

    // Declare the input and output reader
    private BufferedReader inputReader;
    private BufferedWriter outputReader;

    // The buffer to store the data
    private char[] buffer = new char[40];

    // used_memory to store the used memory
    private int used_memory = 0;

    //Page Table Register
    private int pageTableRegister;
            //generated random numbers
            public int generated[] = new int[30];
           

    //PCB
    private PCB pcb;
            //counters
            private int totalTimeCounter = 0;
            private int totalLineCounter = 0;
            //Temporary variables to get the values in int format
            private int ttl;
            private int tll;
    
    private int valid=0;

    int realAddress =0;

    boolean flag= true;

    public SPARS(String input, String output) {
        // Initializing the input and output file
        try {
            this.input = new FileReader(input);
            this.output = new FileWriter(output);
            this.inputReader = new BufferedReader(this.input);
            this.outputReader = new BufferedWriter(this.output);
        } catch (Exception e) {
            System.out.println("Error in initializing the input and output file");
        }
    }

    // init Method to initialize everything to zero
    public void init() {
         Arrays.fill(this.generated, 0);
        // Inititalizing the main memory
        System.out.println("Initializing the main memory\n");


        used_memory =0;
        // Initializing the main memory
        this.M = new MainMemory();

        // Initializing the page table register
        this.pageTableRegister = 0;

        // Initializing the cpu
        this.cpu = new Cpu();

        //Initializing the PCB
        this.pcb = new PCB();
    }

    // LOAD method to load the data from the input file to the main memory
    public void LOAD() {
        String line = "";

        try {
            while ((line = inputReader.readLine()) != null) {
                buffer = line.toCharArray();
                System.out.println("The buffer is : "+String.valueOf(buffer)+"\n");

                if (buffer[0] == '$' && buffer[1] == 'A' && buffer[2] == 'M' && buffer[3] == 'J') {
                    System.out.println("Program card detected " + buffer[4] + buffer[5] + buffer[6] + buffer[7]);

                    
                    init();

                    // Initializing the TotalTimeCounter and TotalLineCounter
                    totalTimeCounter = 0;
                    totalLineCounter = 0;

                    //Initializing the PCB
                    pcb.setJobID(new char[] {buffer[4],buffer[5],buffer[6],buffer[7]}); //jobID

                    pcb.setTTL(new char[] {buffer[8],buffer[9],buffer[10],buffer[11]}); //Total Time Limit

                    pcb.setTLL(new char[] {buffer[12],buffer[13],buffer[14],buffer[15]}); //Total Line Limit

                    //Printing the PCB
                    System.out.println("\n The PCB is : \n"+pcb.toString()+"\n");

                    //Converting the TTL and TLL to int
                    ttl = Integer.parseInt(String.valueOf(pcb.getTTL()));
                    tll = Integer.parseInt(String.valueOf(pcb.getTLL()));

                    


                    //Now calling the allocate method to allocate the memory
                   //Creating the pageTableRegister
                    //assiging a random value from 0-29 to the pageTableRegister variable 
                    Map.Entry<Integer, int[]> pair = allocate(generated); 
                    pageTableRegister = pair.getKey() * 10; //Page table register 
                    used_memory = pageTableRegister;
                    generated = pair.getValue();
                    //Printing the pageTableRegister
                    System.out.println("\n The pageTableRegister is : "+this.pageTableRegister+"\n");

                    //Initializing the PTR block with special characters
                    char[][] memory = M.getMemory();

                    for(int i=pageTableRegister; i<(pageTableRegister+10); i++){
                        System.out.println("The value of i is : "+i+"\n");
                        for(int j=0; j<4; j++){
                            memory[i][j] = '*';
                        }
                    }
                    M.setMemory(memory);
                    //printMemory();

                    continue;


                } else if (buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A') {
                    System.out.println("Data card detected");

                    startExecution();

                    continue;
                } else if (buffer[0] == '$' && buffer[1] == 'E' && buffer[2] == 'N' && buffer[3] == 'D') {
                    System.out.println("End card detected\n");
                   // printMemory();
                
                    continue;
                } else {
                     System.out.println("Loading the instructions to memory");
                    char memory[][] = M.getMemory();

                    loadProgram(memory, buffer);
                }

                
               
                
                // for (int i = 0; i < line.length();) {
                //     memory[used_memory][i % 4] = buffer[i];
                //     if(buffer[i] == 'H' || buffer[i] == '\0')
                //         used_memory += 10 - (used_memory%10);
                    
                //     i++;
                //     if (i % 4 == 0) {
                //         used_memory++;
                //     }

                // }
                // if(used_memory%10==9)
                //     used_memory += 10 - (used_memory%10);
                printMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loadProgram method to load the program to the memory
    private void loadProgram(char[][] memory, char[] buffer) {
        if (used_memory >= (pageTableRegister+10)) {
                    System.out.println("Memory is full");
        }

        //getting the frame number for storing the porgram card
        Map.Entry<Integer, int[]> pair = allocate(generated); 

        //getting the frame number
        int frameNumber = pair.getKey();
        System.out.println("The page table register is : "+pageTableRegister+"\n");
        System.out.println("the current pointer is : "+used_memory+"\n");
        System.out.println("The frame number is : "+frameNumber*10+"\n");

        //getting the generated array
        generated = pair.getValue();
        System.out.println("With the addition The Generated array is : ");
        for(int i=0;i<30;i++)
            System.out.print(generated[i]+" ");
        System.out.println("\n");

        //storing the frame number into the page table register
        memory[used_memory][2] = (char)(frameNumber/10 + '0');
        memory[used_memory][3] = (char)(frameNumber%10 + '0');
        //printMemory();

        //storing the data into the frame
        int framePtr = frameNumber*10;
        System.out.println("The frame pointer is :: "+ framePtr);
    //    for (int i = 0; i < buffer.length;) {
    //                 memory[framePtr][i % 4] = buffer[i];
    //                 if(buffer[i] == 'H' || buffer[i] == '\0')
    //                     framePtr += 10 - (framePtr%10);
                    
    //                 i++;
    //                 if (i % 4 == 0) {
    //                     framePtr++;
    //                 }

    //             }
    int k = 0;
    for(int i=framePtr; i<(framePtr+10) && k<buffer.length; i++) {
        for(int j=0; j<4 && k<buffer.length; j++) {
                memory[i][j] = buffer[k++];
        }
    }

        //printing the memory
        System.out.println("The memory after loading the program card is : \n");
       // printMemory();

        M.setMemory(memory);
        used_memory++;
        System.out.println("The  used memory Now is : "+used_memory+"\n");

    }

    private void startExecution() {
        System.out.println("Starting the execution \n");

        //Looking at all the values again to check if everything is fine
        System.out.println("The pageTableRegister is : "+pageTableRegister+"\n");
        System.out.println("The used_memory is : "+used_memory+"\n");
        //the generated array
        System.out.println("The generated array is : ");
        for(int i=0;i<30;i++)
            System.out.print(generated[i]+" ");

        System.out.println("\n");

        char memory[][] = M.getMemory();
        cpu.setIC(0);
        executeUserProgram(memory);
    }

    private void executeUserProgram(char[][] memory) {
       
       

       System.out.println("=============");
       System.out.println("The IC is : "+cpu.getIC()+"\n");
       System.out.println("=============");

        while(true){
            int RIC = addressMap(cpu.getIC());
            cpu.setIR(new char[] {
                memory[RIC][0],
                memory[RIC][1],
                memory[RIC][2],
                memory[RIC][3]

            });
            //Looking at the IR
            System.out.println("The IR is : "+String.valueOf(cpu.getIR())+"\n");

            cpu.setIC(cpu.getIC()+1); //incrementing the IC
                System.out.println("=============");
                System.out.println("The IC is : "+cpu.getIC()+"\n");
                System.out.println("=============");
            

                System.out.println("The operands is : "+cpu.getIR()[2]+"  " + cpu.getIR()[3] + "\n");
            //checking if the operand is number or not
            if(cpu.getIR()[0] != 'H' && (!Character.isDigit(cpu.getIR()[2]) || !Character.isDigit(cpu.getIR()[3]))){

                System.out.println("Invalid operand");
                cpu.setPI(2);
                MOS();
                break;
                //System.exit(0);
            }
            if(cpu.getIR(0) != 'H'){
                realAddress = addressMap(cpu.getOperand());
            }
            // else if(cpu.getIR(0) != 'B' && cpu.getIR(1) != 'T')
            //     realAddress = addressMap(cpu.getOperand());

            //if realAdrees is -1 then there is pagefault
            if(cpu.getPI() != 0 || (cpu.getTI() != 0 && cpu.getPI() != 0)){
                //cpu.setPI(3);
                MOS();
                if(!flag){
                    flag = true;
                    return;
                }
                realAddress = addressMap(cpu.getOperand());
                System.out.println("The real address Now has become is : "+realAddress+"\n");
                cpu.setPI(0);
               // System.exit(0);
            }



            String opcode = cpu.getOpcode();
            System.out.println("The opcode is : "+opcode+"\n");
            M.setMemory(memory);
            
            examine();

            if (isExceeded) {
                isExceeded = false;
                return;
            }
            if (reachedH) {
                reachedH = false;
                return;
            }
            if(cpu.getPI()!=0 || cpu.getTI()!=0){
                System.out.println("The PI is : "+cpu.getPI()+"\n");
                MOS();
                return;
            }

            if(!flag) {
                flag = true;
                return;
            }
                
            //for looking what is the register value
            // System.out.println("The General Register is : ");
            // for(int i=0;i<4;i++)
            //     System.out.print(cpu.getR()[i]);
            M.setMemory(memory);

            //Calling the Simulation method to handle the TTL and TLL
            SIMULATION();
           
            
        }
        
    }

    private void examine() {
        String opcode = cpu.getOpcode();
        System.out.println("The opcode is : "+opcode+"\n");
        char[][] memory = M.getMemory();
            switch(opcode) {
                case "LR": {
                        
                        cpu.setR(
                            new char[] {
                                memory[realAddress][0],
                                memory[realAddress][1],
                                memory[realAddress][2],
                                memory[realAddress][3]
                            }
                        );
                }
                break;
                case "SR": {
                    System.out.println("The operand is : "+cpu.getOperand()+"\n");
                    char[] arr = cpu.getR();
                    for(char c: arr) {
                        System.out.println(c);
                    }
                    memory[realAddress][0] = arr[0];
                    memory[realAddress][1] = arr[1];
                    memory[realAddress][2] = arr[2];
                    memory[realAddress][3] = arr[3];
                }
                break;
                case "CR": {
                    if(cpu.getR()[0] == memory[realAddress][0] &&
                        cpu.getR()[1] == memory[realAddress][1] &&
                        cpu.getR()[2] ==  memory[realAddress][2] &&
                        cpu.getR()[3] == memory[realAddress][3]
                    ) {
                        cpu.setC(true);
                    }else {
                        cpu.setC(false);
                    }
                }
                break;
                case "BT": {
                    if(cpu.getC())
                    cpu.setIC(cpu.getOperand());

                }
                break;
                case "GD": {
                   System.out.println("\n Inside GD Instructions \n");
                    cpu.setSI(1);
                    M.setMemory(memory);
                    MOS();

                }
                break;
                case "PD": {
                     
                    cpu.setSI(2);
                    M.setMemory(memory);
                    MOS();
                }
                break;
                case "H": {
                     
                    cpu.setSI(3);
                    M.setMemory(memory);
                    MOS();
                    reachedH = true;
                    return;
                }
                

                default: {
                    System.out.println("Invalid opcode");
                    cpu.setPI(1);
                    
                }
            }
            

            M.setMemory(memory);

    }

    private void SIMULATION() {

        totalTimeCounter++;
        //totalLineCounter++;
        if(totalTimeCounter == ttl){
            cpu.setTI(2);
            System.out.println("Time Limit Exceeded");
            
            //printing the TI and SI values to see if they are 0 or not
            System.out.println("The SIMULATION is : \n");
            System.out.println("The TI is : "+cpu.getTI()+"\n");
            System.out.println("The SI is : "+cpu.getSI()+"\n");
        }

        // if(cpu.getSI()!=0 || cpu.getTI()!=0 || cpu.getPI()!=0)
		// 	MOS();
        //     return;
        // if(totalLineCounter > tll){
        //     cpu.setTI(1);
        //     System.out.println("Line Limit Exceeded");
        //     System.exit(0);
        // }
    }

    private int addressMap(int va) {
        System.out.println("The value of va is : "+va+"\n");
        int pte = pageTableRegister+va/10;

        System.out.println("==============================================================");
        System.out.println("The page table register is : "+pageTableRegister+"\n");
        System.out.println("The page table Entry is : "+pte+"\n");

        System.out.println("....Maping the Virtual Address To Real Address");

        //checking wheather the page table register is empty or not
        if(M.getMemory()[pte][2] != '*') {
            int realAddress = Integer.parseInt(String.valueOf(M.getMemory()[pte][2]) + String.valueOf(M.getMemory()[pte][3])) * 10 + va%10;

            System.out.println("The real address is : "+realAddress+"\n");
            return realAddress;


        } else {
            cpu.setPI(3);

            System.out.println("The page table register is empty\n");
            System.out.println("==============================================================");
            return -1;
        }

        

    }

    

     protected int allocate(){

        
        //generating a random value from 0-29
        int value = (int)(Math.random() * 30);
        

        //check wheather it is generated if it is then again generate new value
        while(true){
            if(generated[value] == 0){
                generated[value] = 1;
                System.out.println("The value is : "+value+"\n");
                break;
            }
            else{
                value = (int)(Math.random() * 30);
            }
        }

        //returning the value and the arr
        //creating a map entry
        return value;
     }


    private void WRITE() {
        //incrementing the line counter
        totalLineCounter++;
        //checking if the line counter is greater than the tll
        if(totalLineCounter > tll){
            System.out.println("The TTL exceeded so calling terminate");
            isExceeded = true;
            cpu.setTI(2);
            TERMINATE(2);
            return;
            //System.out.println("Line Limit Exceeded");
            //System.exit(0);
        }

        //take the data from the memory and put it into the output file
        StringBuilder line = new StringBuilder();
        char[][] memory = M.getMemory();
        int oprand = cpu.getOperand();

        //--converting the las bit to 0
        if(oprand%10 != 0){
            //convert that las bit to 0
            oprand = oprand - (oprand%10);
            
        }
        //printing the memory for refernce
        printMemory();

        for(int i=realAddress; i<realAddress+10; i++){
            for(int j=0; j<4; j++){
                if(memory[i][j] != '\0'){
                System.out.println("The line is : "+memory[i][j] +"\n");
                    // line.append(memory[i][j]);
                    try {
                        outputReader.write(memory[i][j]);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    
                }
            }
        }

        System.out.println("The realAddress NOW in write fun() is : "+realAddress+"\n");

        try {
            //outputReader.write(line.toString());
            outputReader.newLine();
            outputReader.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cpu.setSI(0);
    }

    

    private void READ() {
        flag = true;
         String line = "";
        try {
             line = inputReader.readLine();
             System.out.println("The Line in read is::" + line);
            if(line == null) {
                System.out.println("Input file is empty");
                
            }else if(line.startsWith("$END")){
                System.out.println("Out of data");
                flag = false;
                TERMINATE(1);
                return;
                //System.out.println("Out of Data Card");
            }
            char[] buffer = line.toCharArray();
            char[][] memory = M.getMemory();
            int oprand = cpu.getOperand();

            //--converting the las bit to 0
            if(oprand%10 != 0){
                //convert that las bit to 0
                oprand = oprand - (oprand%10);
                
            }
            //putting the whole buffer starting from the given operand address
            for (int i = 0; i < line.length();) {
                    memory[realAddress][i % 4] = buffer[i];
                    
                    i++;
                    if (i % 4 == 0) {
                        realAddress++;
                    }

                }
            M.setMemory(memory);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        cpu.setSI(0);

    }

////---------For MOS---------////
    private void MOS() {
        System.out.println("The Switch value is : for TI SI" + cpu.getTI() + cpu.getSI() + "For TI PI " + cpu.getTI() + cpu.getPI());
        switch(""+cpu.getTI()+cpu.getSI()) {
            case "01": {
                READ();
                return;
            }   
            
            case "02": {
                WRITE();
                 return;
            }
            case "03": {
                cpu.setSI(0);
                TERMINATE(0);
                return;
            }
            case "21":TERMINATE(1);
            return;

			case "22": {
                isExceeded= true;
                WRITE();
				TERMINATE(3);
             return;   
            }

			case "23":{
                TERMINATE(0);
                return;
            }

            
        }
        
        switch ("" + cpu.getTI() + cpu.getPI()) {
            case "01":{
                
                TERMINATE(4);
              return;  
            }
            

            case "02":{
                TERMINATE(5);
            return;
            }
            

            case "03": {
                    
                    if(cpu.getIR()[0] == 'G' || cpu.getIR()[0] == 'S') {
                        valid = 1;

                    }
                    if(valid==1){

                        System.out.println("The page fault was valid so: ");
                        //Valid Page Fault
                        //allocate2(br.readLine());
                            valid =0;
                        int al = allocate();
                        char [][] memory = M.getMemory();
                        //storing the frame in the page table 
                        System.out.println("The value of AL is : "+al+"\n");
                        System.out.println("The location in PTR to store GD is : "+pageTableRegister+Integer.parseInt(String.valueOf(cpu.getIR()[2]))+"\n");
                        int ir = cpu.getIR(2) - '0';
                        //System.out.print("The value of IR is : "+ir+"\n");
                        memory[pageTableRegister+ir][2] = (char)(al/10 + '0');
                        memory[pageTableRegister+ir][3] = (char)(al%10 + '0');
                        M.setMemory(memory);
                        //printMemory();
                        //cpu.setIC(cpu.getIC()+1);
                        System.out.println("NOW IC:"+cpu.getIC());
                        cpu.setPI(0);
                    }else{
                        flag = false;
                        TERMINATE(6);
                        
                    }
                return;
            }
            

            case "21":
				TERMINATE(3+4);
				return;
				
			case "22":
				TERMINATE(3+5);
				return;
			
        }

        

        if((""+cpu.getTI()+cpu.getPI()).equals("23")){
            TERMINATE(3);
        }else if((""+cpu.getTI()+cpu.getSI()).equals("20")){
            TERMINATE(3);
        }
cpu.setSI(0);
cpu.setPI(0);

    }

      private void TERMINATE(int em) {
        System.out.println("Terminate called");
        
        try {
                    String line = getEM(em);
                    
                    System.out.println("The value of EM is : "+line+"\n");
                    
                    outputReader.write(line);
                    outputReader.write("\n============================\n");
                    outputReader.write("IC: "+cpu.getIC()+"\n");
                    outputReader.write("IR: "+ String.valueOf(cpu.getIR())+"\n");
                    outputReader.write("================================\n");
                    outputReader.write("SI: "+cpu.getSI()+"\n");
                    outputReader.write("PI: "+cpu.getPI()+"\n");
                    outputReader.write("TI: "+cpu.getTI()+"\n");
                    outputReader.write("================================\n");
                    outputReader.write("TTL: "+ttl+"\n");
                    outputReader.write("TLL: "+tll+"\n");
                    outputReader.write("================================\n");
                    outputReader.write("TTC: "+totalTimeCounter+"\n");
                    outputReader.write("TLC: "+totalLineCounter+"\n");
                    outputReader.write("================================\n");
                    outputReader.write("\n\n");
                    cpu.setSI(0);
                    cpu.setTI(0);
                    cpu.setPI(0);

                    // FileReader fr = new FileReader("output.txt");
                    // BufferedReader br = new BufferedReader(fr);
                    // String s;
                    // while((s = br.readLine()) != null) {
                    //     System.out.println(s);
                    // }
                    
                    outputReader.newLine();
                    outputReader.flush();
        } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
        }

    }

    private String getEM(int em) {
        switch(em) {
            case 0: {
                return "No error";
            }
            case 1: {
                return "Out of Data";
            }
            case 2: {
                return "Line Limit Exceeded";
            }
            case 3: {
                return "Time Limit Exceeded";
            }
            case 4: {
                return "Operation Code Error";
            }
            case 5: {
                return "Operand Error";
            }
            case 6: {
                return "Invalid Page Fault";
            }
            case 7: {
                return "Time Limit Exceeded with Opcode Error";
            }
            case 8: {
                return "Time Limit Exceeded with Operand Error";
            }
            default: {
                return "Invalid Error";
            }

        }

    }

    ////---------For Printing the MEMORY---------////
    public void printMemory() {
        char m[][] = M.getMemory();
        System.out.println("Printing the memory...\n");
        // print memory in form of boxes
        for (int i = 0; i < m.length; i++) {
            //for showing the numbers at side as reference
            if (i < 10) {
                System.out.print(i + "  ");
            }else{
                System.out.print(i + " ");
            }

            for (int j = 0; j < 4; j++) {
                if (m[i][j] == 0) {
                    System.out.print(" - ");
                } else {
                    System.out.print(" " + m[i][j] + " ");
                }
                if (j == 3) {
                    System.out.println();
                } else {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
    }
}