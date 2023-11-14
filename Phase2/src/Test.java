import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    
 // Declare the input file and the output file
    private static FileReader input;
    private static FileWriter output;

    // Declare the input and output reader
    private BufferedReader inputReader;
    private static BufferedWriter outputReader;

    //constructor
    
    public static void main(String[] args) {
         // Initializing the input and output file
           

             try {
            
            output = new FileWriter("output.txt");
            outputReader = new BufferedWriter(output);
        } catch (Exception e) {
            System.out.println("Error in initializing the input and output file");
        }
            //calling the terminate function
           TERMINATE(2);
    }
    

    
      private static void TERMINATE(int em) {
        System.out.println("Terminate called");
        
        try {
                    String line = getEM(em);
                    
                    System.out.println("The value of EM is : "+line+"\n");
                    
                    outputReader.write(line);
                    // outputReader.write("\n============================\n");
                    // outputReader.write("IC: "+cpu.getIC()+"\n");
                    // outputReader.write("IR: "+ String.valueOf(cpu.getIR())+"\n");
                    // outputReader.write("================================\n");
                    // outputReader.write("SI: "+cpu.getSI()+"\n");
                    // outputReader.write("PI: "+cpu.getPI()+"\n");
                    // outputReader.write("TI: "+cpu.getTI()+"\n");
                    // outputReader.write("================================\n");
                    // outputReader.write("TTL: "+ttl+"\n");
                    // outputReader.write("TLL: "+tll+"\n");
                    // outputReader.write("================================\n");
                    // outputReader.write("TTC: "+totalTimeCounter+"\n");
                    // outputReader.write("TLC: "+totalLineCounter+"\n");
                    // outputReader.write("================================\n");
                    // outputReader.write("\n\n");
                    // cpu.setSI(0);
                    // cpu.setTI(0);
                    // cpu.setPI(0);

                    // FileReader fr = new FileReader("output.txt");
                    // BufferedReader br = new BufferedReader(fr);
                    // String s;
                    // while((s = br.readLine()) != null) {
                    //     System.out.println(s);
                    // }
                    outputReader.newLine();
                    outputReader.flush();
                    outputReader.close();
        } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
        }

    }

    private static String getEM(int em) {
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

}
