import java.io.*; //??? not sure

public class Pixel {
	public Pixel(int p){
		b = p & 0x0000FF;
		g = (p & 0x00FF00) >> 8;
		r = (p & 0xFF0000) >> 16;
	}

	public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
		return (abs(a.r - b.r) <= tolerance) &&
		(abs(a.g - b.g) <= tolerance) &&
		(abs(a.b - b.b) <= tolerance);
	}

	public int r;
	public int g;
	public int b;
}

public class Spims {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	String patternFile = "";
    	String sourceFile = "";

		//Less than apprpriate number of arguments given                                                                                                                                                                                                    
    	if (args.length < 4) {
    		System.err.println("Error: Expected at least 4 inputs.\n"+
    			"Got " + args.length + ": " + args);
    		return;
    	}
        //TODO Modify to take in more arguments and check validity/flags
    	if (args[0].equals("-p")) {

    	}
    	//Do a search on the '-p' and '-s' flags
    	if (args.length == 4) {
	    //args[0] should be -p
    		patternFile = args[1];
	    //args[2] should be -s
    		sourceFile = args[3];
    	}
		//TODO Separate input and output into two if statements to have better error messages 
    	if (!(patternFile.toLowerCase().endsWith(".png") ||
    		  patternFile.toLowerCase().endsWith(".gif") ||
    		  patternFile.toLowerCase().endsWith(".jpg"))) {
    		System.err.println("Error: Expected pattern input with extension .png, .jpg, or .gif.\n"+
    			"Got " + patternFile);
    		return;
    	}
    	if (!(sourceFile.toLowerCase().endsWith(".png") ||
    		  sourceFile.toLowerCase().endsWith(".gif") ||
    		  sourceFile.toLowerCase().endsWith(".jpg"))) {
    		System.err.println("Error: Expected source input with extension .png, .jpg, or .gif.\n"+
    			"Got " + sourceFile);
    		return;
    	}
    
    	BufferedReader patternImg = null;
    	BufferedReader sourceImg = null;

    	try{
    		patternImg = ImageIO.read(new File(patternFile));
    		sourceImg = ImageIO.read(new File(sourceFile));
    		int[][] paternInts = new Pixel[patternImg.getWidth()*patternImg.getHeight()];
    		int[][] sourceInts = new Pixel[sourceImg.getWidth()*sourceImg.getHeight()];
    		
    		if (img1.width == img2.width && img1.height == img2.height){

				//Check if any results, if not call compareScaleUp()
    			compareExact(sourceIntArray, patternIntArray);
    		}
            

    		//TODO: Modify these to be Arrays of Arrays
    		Pixel[][] patternPixels = new Pixel[patternImg.getWidth()*patternImg.getHeight()];
    		Pixel[][] sourcePixels = new Pixel[sourceImg.getWidth()*sourceImg.getHeight()];

    		// System.out.println(pixels1.length);
			patternImg.getRGB(0, 0, img2.getWidth(), patternFile.getHeight(), 
    							pixels1, 0, patternFile.getWidth());
			sourceImg.getRGB(0, 0, sourceImg.getWidth(), sourceImg.getHeight(),
    							sourcePixels, 0, sourceImg.getWidth());

    		//TODO: This takes in the array or whatever 
    		printResults();

    	}
    	catch (Exception e){
    		System.err.println("Error: " + e.getMessage());
    	}
    	//catch (IOException e){System.err.println(e);}
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
    public static int[] compareScaleUp(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        int width = pattern.length;
        int height = pattern[0].length;

        Pixel upperLeftCorner = pattern[0][0];
        Pixel upperRightCorner = pattern[0][width-1];
        Pixel lowerLeftCorner = pattern[height-1][0];
        Pixel lowerRightCorner = pattern[width-1][height-1];

        Pixel[] corners = new Pixel[]{upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner};

        int curPatternRow = 0;
        int curPatternCol = 0;

        boolean[] progress = new boolean[]{false, false, false, false};

        int currentCorner = 0;

        int[] resultX = new int[4];
        int[] resultY = new int[4];

        for(int i = 0; i < source.length; i++){
            for(int j = 0; j < source[0].length; j++){
                if(source[i][j].val == corners[currentCorner].val){
                    System.out.println("found a corner: " + currentCorner);
                    resultY[currentCorner] = i;
                    resultX[currentCorner] = j;

                    if(currentCorner == 0){
                        curPatternCol++;
                    } else if(currentCorner == 1){
                        curPatternCol = 0;
                        curPatternRow++;
                    } else if(currentCorner == 2){
                        curPatternCol++;
                    } else if(currentCorner == 3){
                        System.out.println("incoming results");
                    }
                    currentCorner++;
                }
                else if(Pixel.isSimilar(source[i][j], pattern[curPatternRow][curPatternCol], tolerance)){
                    System.out.println("and the next pixel is similar! " + j + ", " + i);
                    if(curPatternCol == pattern.length-1){
                        curPatternRow++;
                        curPatternCol = 0;
                    }
                }
                else if(j > 0 && Pixel.isSimilar(source[i][j-1], pattern[curPatternRow][curPatternCol], tolerance)){
                    System.out.println("time to fill that gap!");
                    boolean fillingGap = true;
                    while(fillingGap){
                        curPatternCol++;
                        curPatternRow++;
                        if(source[i][j].val == pattern[curPatternRow][curPatternCol].val){
                            fillingGap = false; //but due to match so move on
                        } else if(!Pixel.isSimilar(source[i][j], pattern[curPatternRow][curPatternCol], tolerance)){
                            fillingGap = false;
                            resultX = new int[4];
                            resultY = new int[4];
                            currentCorner = 0;
                            curPatternRow = 0;
                            curPatternCol = 0;
                        } //else they are similar so proceed filling the gap
                    }
                }
                else { //not similar, not equal
                    resultX = new int[4];
                    resultY = new int[4];
                    currentCorner = 0;
                    curPatternRow = 0;
                    curPatternCol = 0;
                }
            }
        }
        return resultX;
    }

    public static int currentCornerCheck(Pixel[] corners, Pixel current){
        for(int i = 0; i < corners.length; i++){
            if(corners[i].val == current.val) return i;
        }
        return -1;
    }

    public void compareScaleDown(Pixel[][] pattern, Pixel[][] source, int tolerance) {

    }



    public void debugPixels(){
    	System.out.println("Pattern:\n");
    	for(int i = 0; i < pixels1.length; i++){
				if(i % img2.getWidth() == 0){
					System.out.println();
				}
    		}
    	System.out.println();
    	return;
    }

    public void debugInts(){
    	System.out.println("Pattern:\n");
    	for(int i = 0; i < patternInt.length; i++){
				if(i % img2.getWidth() == 0){
					System.out.println();
				}
    		}
    	System.out.println();
    	return;
    }

    public void printResults(int[] results){
    	//TODO Check number of results
    	System.out.println("");
    	for (i = 0; i < results.length; i = i+4) {
    		System.out.println("" + results[i] + " matches " +results[i+1] + 
    			" at topleft " + result[i+2] + " bottomright " + result[i+3]);
    	}
    	return;
    }
    /**
     *THEORY: Like numbers should be near others. 
     *30	 36	  20          50
     *30 31 31 36 35 21 20 21 20 49 50
     */















}
