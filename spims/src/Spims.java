import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.DataBufferInt;


public class Spims {

    /**
     * @param args
     */
    public static void main(String[] args) {
	//  
	String strPatternFile = "";
	String strSourceFile = "";
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
	    strPatternFile = args[1];
	    //args3 should be -s
	    strSourceFile = args[3];
        }
	//TODO Separate input and output into two if statements to have better error messages 
	if (!(strPatternFile.toLowerCase().endsWith(".png") ||
	      strPatternFile.toLowerCase().endsWith(".gif") ||
	      strPatternFile.toLowerCase().endsWith(".jpg")) ||
	    !(strSourceFile.toLowerCase().endsWith(".png") ||
	      strSourceFile.toLowerCase().endsWith(".gif") ||
	      strSourceFile.toLowerCase().endsWith(".jpg"))) {
	    System.err.println("ERROR! ERROR! WRONG FILE EXTENSION!!!");
	    return;
	}
	//Multiple arguments given, check for '-r' flag                                                                                                                                                                            
	if (args.length >= 2) {
		strPatternFile = args[1];
	}
    else {
        System.err.println("Invalid command, one or more arguments required.");
        return;
    }
	
	//setup both pattern and source bufferimage with null
	BufferedImage biPatternFile = null;
	BufferedImage biSourceFile = null;
	
	//Convert pattern image into a buffer image
	try{
	      biPatternFile = ImageIO.read(new File(strPatternFile));
	    } catch(IOException e){
	      System.err.println(e);
	    }

	//Convert source image into a buffer image
	try{
	      biSourceFile = ImageIO.read(new File(strSourceFile));
	    } catch(IOException e){
	      System.err.println(e);
	    }
	
	//get the height of the pattern image
	int iHeight = biPatternFile.getHeight();
	//get the width of the pattern image
	int iWidth = biPatternFile.getWidth();
	//set up a two dimensional matrix with the correct rows and columns 
	Pixel[][] pxPatternImage = new Pixel[iHeight][iWidth];
	
	//fill the matrix with corresponding pixel RGB info from the image
	for (int i = 0; i < iHeight; i++){
		for (int j = 0; j < iWidth; j++){
			pxPatternImage[i][j]= new Pixel(biPatternFile.getRGB(j, i));
		}
	}
	
	//get the height of the source image
	iHeight = biSourceFile.getHeight();
	//get the width of the source image
	iWidth = biSourceFile.getWidth();
	//set up a two dimensional matrix with the correct rows and columns
	Pixel[][] pxSourceImage = new Pixel[iHeight][iWidth];
	
	//fill the matrix with corresponding pixel RGB info from the image
	for (int i = 0; i < iHeight; i++){
		for (int j = 0; j < iWidth; j++){
			pxSourceImage[i][j]= new Pixel(biSourceFile.getRGB(j, i));
			
		}
	}
}	

	       
	
	//TODO Implement
	//Method to check if pattern matches exactly the base image
	
	//Call this if same size arrays and arrays within are same length
    public String compareExact(Pixel[][] pattern, Pixel[][] source) {
		if (pattern == source) {
			return "true";
		}
		else 
			return "false";
		
	}
	
	//TODO Implement
	//Method to check if patternis a cropped version of source
    public String compareNoResize(Pixel[][] pattern, Pixel[][] source) {
    	//setup return string
    	String strResult = "false";
    	//go through each row and each column
		for (int i = 0; i < source.length; i++){
			for (int j = 0; j < source[1].length; j++){
				//check if the first pixel of the pattern 
				//matches the given pixel of the source
				if (pattern[0][0] == source[i][j]){
					// if so, call helper to see if we have found the match spot
					strResult = justCompare(pattern, source, i, j);
					//if we have, then return true
					if (strResult == "true"){
						return strResult;
					}
				}
			}
		}
		//if we can't find anything, return false
		return strResult;
	}
	
	public String justCompare(Pixel[][] pattern, Pixel[][] source, int i, int j) {
		//go through each row and column of the pattern image
		for (int ii = 0; i < pattern.length; i++){
			for (int jj = 0; j < pattern[1].length; j++){
				//check if the pattern and source match
				if (pattern[ii][jj] != source[i+ii][j+jj]){
					return "false";
				}
			}
		}
		return "true";
	}



	//TODO Implement
	//Else - LOOOOONG check
    public void compareScaleUp(Pixel[][] pattern, Pixel[][] source, int tolerance) {
		
	}

    public void compareScaleDown(Pixel[][] pattern, Pixel[][] source, int tolerance) {
	
    }
	
	
	
    /**
     *THEORY: Like numbers should be near others. 
     *30	 36	  20          50
     *30 31 31 36 35 21 20 21 20 49 50
     */
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
