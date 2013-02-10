import java.io.*;

public class Spims {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFile = "";
        boolean reduce = false;
        //Single argument given                                                                                                                                                                                                    
        if (args.length == 1) {
                inputFile = args[0];
        }
        //Multiple arguments given, check for '-r' flag                                                                                                                                                                            
        else if (args.length >= 2) {
                inputFile = args[1];
        }
        else {
                System.err.println("Invalid command, one or more arguments required.");
                return;
        }
        try{
                //Open FileReader to view file contents                                                                                                                                                                            
                FileReader fr = new FileReader(inputFile);
                String inputString = new BufferedReader(fr).readLine();
                //Call parse function on the input and pass in the reduce flag bit                                                                                                                                                 
                //String prefix = Prefixer.parseInfix(inputString, reduce);
                //Print the input string before and after to the console                                                                                                                                                           
                System.out.println(inputString + " -> " + prefix);
                //Close FileReader                                                                                                                                                                                                 
                fr.close();
        }
        catch (Exception e){
                System.err.println("Error: " + e.getMessage());
        }
}
	}

}
