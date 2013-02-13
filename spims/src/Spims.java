import java.io.*; //??? not sure

public class Spims {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//  
		String patternFile = "";
        String sourceFile = "";
        //Single argument given                                                                                                                                                                                                    
        if (args.length < 4) {
        	//TODO Modify this to have more information for errors
        	System.err.println("ERROR! ERROR! NOT ENOUGH INPUTS!\n"+
        					   "WILL SELF DESTRUCT IN 5 SECONDS!");
        	return;
        }
        //TODO Modify to take in more arguments and check validity/flags
		if (args.length == 4) {
			//args[0] should be -p
			patternFile = args[1];
			//args3 should be -s
			sourceFile = args[3];
        }
		//TODO Separate input and output into two if statements to have better error messages 
		if (!(patternFile.toLowerCase().endsWith(".png") ||
			  patternFile.toLowerCase().endsWith(".gif") ||
			  patternFile.toLowerCase().endsWith(".jpg")) ||
			!(sourceFile.toLowerCase().endsWith(".png") ||
			  sourceFile.toLowerCase().endsWith(".gif") ||
			  sourceFile.toLowerCase().endsWith(".jpg"))) {
			System.err.println("ERROR! ERROR! WRONG FILE EXTENSION!!!");
			return;
		}
        //Multiple arguments given, check for '-r' flag                                                                                                                                                                            
        else if (args.length >= 2) {
        	patternFile = args[1];
        }
        else {
        	System.err.println("Invalid command, one or more arguments required.");
        	return;
        }
        try{
                
        	//Open FileReader to view file contents                                                                                                                                                                            
                //FileReader fr = new FileReader(inputFile);
                //String inputString = new BufferedReader(fr).readLine();
                //Call parse function on the input and pass in the reduce flag bit                                                                                                                                                 
                //String prefix = Prefixer.parseInfix(inputString, reduce);
                //Print the input string before and after to the console                                                                                                                                                           
                System.out.println("result");
                //Close FileReader                                                                                                                                                                                                 
                //fr.close();
        }
        catch (Exception e){
                System.err.println("Error: " + e.getMessage());
        }
	}
	
	
	
	//TODO Implement
	//Method to check if pattern matches exactly the base image
	
	//Call this if same size arrays and arrays within are same length
	public void compareExact(Array[] pattern, Array[] source) {
		
	}
	
	//TODO Implement
	//Method to check if patternis a cropped version of source
	public void compareNoResize(Array[] pattern, Array[] source) {	
		
	}
	
	//TODO Implement
	//Else - LOOOOONG check
	public void compareFull(Array[] pattern, Array[] source) {
		
	}
	
	
	
	/*
	THEORY: Like numbers should be near others. 
	30 		 36 		 20 	  50
	30 31 31 36 35 21 20 21 20 49 50
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
