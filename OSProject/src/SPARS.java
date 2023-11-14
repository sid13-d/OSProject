import java.io.*;

public class SPARS {
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
        // Inititalizing the main memory
        System.out.println("Initializing the main memory\n");
        used_memory =0;
        // Initializing the main memory
        this.M = new MainMemory();

        // Initializing the cpu
        this.cpu = new Cpu();
    }

    // LOAD method to load the data from the input file to the main memory
    public void LOAD() {
        String line = "";

        try {
            while ((line = inputReader.readLine()) != null) {
                buffer = line.toCharArray();

                if (buffer[0] == '$' && buffer[1] == 'A' && buffer[2] == 'M' && buffer[3] == 'J') {
                    System.out.println("Program card detected " + buffer[4] + buffer[5] + buffer[6] + buffer[7]);
                    init();
                    continue;
                } else if (buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A') {
                    System.out.println("Data card detected");
                    startExecution();
                    continue;
                } else if (buffer[0] == '$' && buffer[1] == 'E' && buffer[2] == 'N' && buffer[3] == 'D') {
                    System.out.println("End card detected\n");
                    printMemory();
                    continue;
                }

                if (used_memory == 100) {
                    System.out.println("Memory is full");
                }

                System.out.println("Loading the instructions to memory");
                char memory[][] = M.getMemory();

                
                for (int i = 0; i < line.length();) {
                    memory[used_memory][i % 4] = buffer[i];
                    if(buffer[i] == 'H' || buffer[i] == '\0')
                        used_memory += 10 - (used_memory%10);
                    
                    i++;
                    if (i % 4 == 0) {
                        used_memory++;
                    }

                }
                if(used_memory%10==9)
                    used_memory += 10 - (used_memory%10);
                M.setMemory(memory);
                printMemory();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Memory is full\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startExecution() {
        System.out.println("Starting the execution \n");
        char memory[][] = M.getMemory();
        cpu.setIC(0);
        executeUserProgram(memory);
    }

    private void executeUserProgram(char[][] memory) {
       
        while(true){
            cpu.setIR(new char[] {
                memory[cpu.getIC()][0],
                memory[cpu.getIC()][1],
                memory[cpu.getIC()][2],
                memory[cpu.getIC()][3]

            });

            cpu.setIC(cpu.getIC()+1);

            String opcode = cpu.getOpcode();
            System.out.println("The opcode is : "+opcode+"\n");
           

            switch(opcode) {
                case "LR": {
                        cpu.setR(
                            new char[] {
                                memory[cpu.getOperand()][0],
                                memory[cpu.getOperand()][1],
                                memory[cpu.getOperand()][2],
                                memory[cpu.getOperand()][3]
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
                    memory[cpu.getOperand()][0] = arr[0];
                    memory[cpu.getOperand()][1] = arr[1];
                    memory[cpu.getOperand()][2] = arr[2];
                    memory[cpu.getOperand()][3] = arr[3];
                }
                break;
                case "CR": {
                    if(cpu.getR()[0] == memory[cpu.getOperand()][0] &&
                    cpu.getR()[1] == memory[cpu.getOperand()][1] &&
                    cpu.getR()[2] ==  memory[cpu.getOperand()][2] &&
                    cpu.getR()[3] == memory[cpu.getOperand()][3]
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
                    return;
                }
                

                default: {
                    System.out.println("Invalid opcode");
                    System.exit(0);
                }
            }

            //for looking what is the register value
            // System.out.println("The General Register is : ");
            // for(int i=0;i<4;i++)
            //     System.out.print(cpu.getR()[i]);
            M.setMemory(memory);
        }
        
    }



    ////---------For MOS---------////
    private void MOS() {
        switch(cpu.getSI()) {
            case 1: {
                READ();
            }
            break;
            case 2: {
                WRITE();
            }break;
            case 3: {
                cpu.setSI(0);
                TERMINATE();
            }
            break;
            default: {
                System.out.println("Invalid SI");
                System.exit(0);
            }
        }
    }

    private void TERMINATE() {
        try {
                    outputReader.write("\n\n");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    }

    private void WRITE() {
        //take the data from the memory and put it into the output file
        StringBuilder line = new StringBuilder();
        char[][] memory = M.getMemory();
        int oprand = cpu.getOperand();

        //--converting the las bit to 0
        if(oprand%10 != 0){
            //convert that las bit to 0
            oprand = oprand - (oprand%10);
            
        }

        for(int i=oprand; i<oprand+10; i++){
            for(int j=0; j<4; j++){
                if(memory[i][j] != '\0')
                    line.append(memory[i][j]);
            }
        }

        try {
            outputReader.write(line.toString());
            outputReader.newLine();
            outputReader.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cpu.setSI(0);
    }

    

    private void READ() {
         String line = "";
        try {
             line = inputReader.readLine();
            if(line == null) {
                System.out.println("Input file is empty");
                System.exit(0);
            }else if(line.equals("$END")){
                System.out.println("Out of Data Card");
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
                    memory[oprand][i % 4] = buffer[i];
                    
                    i++;
                    if (i % 4 == 0) {
                        oprand++;
                    }

                }
            M.setMemory(memory);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        cpu.setSI(0);

    }

    ////---------For Printing the MEMORY---------////
    public void printMemory() {
        char m[][] = M.getMemory();
        System.out.println("Printing the memory...\n");
        // print memory in form of boxes
        for (int i = 0; i < 100; i++) {
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