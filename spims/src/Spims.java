import java.io.*; //??? not sure
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Spims {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	String patternFile = "";
    	String sourceFile = "";
        int[] results = new int[1];
        int tolerance = 20;

		//Less than apprpriate number of arguments given                                                                                                                                                                                                    
    	if (args.length < 4) {
            System.err.print("Error: Expected at least 4 inputs.\n"+
    			"Got " + args.length + ": ");
                for (int i = 0; i < args.length; i++) {
                    System.err.print("" + args[i] + " ");
                }
            System.out.println();
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
    
    	BufferedImage patternImg = null;
    	BufferedImage sourceImg = null;

    	try{
    		patternImg = ImageIO.read(new File(patternFile));
    		sourceImg = ImageIO.read(new File(sourceFile));
    		int[][] patternInts = new int[patternImg.getHeight()][patternImg.getWidth()];
    		int[][] sourceInts = new int[sourceImg.getHeight()][sourceImg.getWidth()];

            //Create integer arrays of pattern and source
            for(int i = 0; i < patternInts.length; i++){
                for(int j = 0; j < patternInts[0].length; j++){
                    patternInts[i][j] = patternImg.getRGB(j,i);
                }
            }
            for(int i = 0; i < sourceInts.length; i++){
                for(int j = 0; j < sourceInts[0].length; j++){
                    sourceInts[i][j] = sourceImg.getRGB(j,i);
                }
            }

            Pixel[][] patternPixels = new Pixel[patternImg.getHeight()][patternImg.getWidth()];
            Pixel[][] sourcePixels = new Pixel[sourceImg.getHeight()][sourceImg.getWidth()];
            // System.out.println(pixels1.length);
            for(int i = 0; i < patternPixels.length; i++){
                for(int j = 0; j < patternPixels[i].length; j++){
                    patternPixels[i][j] = new Pixel(patternImg.getRGB(j,i));
                }
            }
            for(int i = 0; i < sourcePixels.length; i++){
                for(int j = 0; j < sourcePixels[i].length; j++){
                    sourcePixels[i][j] = new Pixel(sourceImg.getRGB(j,i));
                }
            }

            //If images are exact width/height, compare them
    		if (patternImg.getWidth() == sourceImg.getWidth() && patternImg.getHeight() == sourceImg.getHeight()){
				//Check if any results, if not call compareScaleUp()
                System.out.println("In compareExact(...) function.");
    			System.out.println(compareExact(sourceInts, patternInts));
    		} else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                System.out.println("In compareNoScale(...) function.");
                System.out.println(compareNoScale(patternPixels, sourcePixels, tolerance));
            } else {
                System.out.println(compareScaleUp(patternPixels, sourcePixels, tolerance));
                compareScaleDown(patternPixels, sourcePixels, tolerance);
            }


            //debugInts(patternInts, sourceInts);
            System.out.println();
            debugPixels(patternPixels, sourcePixels);

    		//TODO: This takes in the array or whatever 
    		//printResults(results);
    	}
    	catch (Exception e){
    		System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
    	}
    	//catch (IOException e){System.err.println(e);}
    }



	//TODO Implement
	//Method to check if pattern matches exactly the base image

	   //Call this if same size arrays and arrays within are same length
    public static String compareExact(int[][] pattern, int[][] source) {
        for (int i = 0; i < pattern.length; i++){
            for (int j = 0; j < pattern[i].length; j++){
                if (pattern[i][j] != source[i][j]){
                    return "false";
                }
            }
        }
        return "true";
    }

    //TODO Implement
    //Method to check if patternis a cropped version of source
    public static String compareNoScale(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        //setup return string
        String strResult = "false";
        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;

        //go through each row and each column
        //TODO: Maybe computer the (source.length - pattern.length) things outside
        for (int i = 0; i < (sourceLengthi - patternLengthi); i++){
            for (int j = 0; j < (sourceLengthj - patternLengthj); j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern[0][0], source[i][j], tolerance)) {
                    // if so, call helper to see if we have found the match spot
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    strResult = justCompare(pattern, source, i, j, tolerance);
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
    
    public static String justCompare(Pixel[][] pattern, Pixel[][] source, int i, int j, int tolerance) {
    //go through each row and column of the pattern image

        for (int ii = 0; ii < pattern.length; ii++){
            for (int jj = 0; jj < pattern[ii].length; jj++){
                //check if the pattern and source match
                if (!(Pixel.isSimilar(pattern[ii][jj], source[i+ii][j+jj], tolerance))) {
                    
                    return "false";
                }
            }
        }
        return "true";
    }

	//TODO Implement
	//Else - LOOOOONG check
    public static String compareScaleUp(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        String strResult = "false";
        int scale = 2;

        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;

        for (int i = 0; i < sourceLengthi; i++){
            for (int j = 0; j < sourceLengthj; j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern[0][0], source[i][j], tolerance)) {
                    System.out.println("Pixel match at (" + j + ", " + i + ")");
                    // if so, call helper to see if we have found the match spot
                    strResult = compareUpHelper(pattern, source, i, j, scale, tolerance);
                    //if we have, then return true
                    if (strResult == "true"){
                        return strResult;
                    }
                }
            }
        }
        return strResult;
    }

    public static String compareUpHelper(Pixel[][] pattern, Pixel[][] source, int i, int j, int scale, int tolerance) {
        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int pii = 0;
        int sii = 0;

        while (pii < patternLengthi) {
            if ((i+sii) >= source.length){
                    //if (Pixel.isSimilar(source[i][j+sjj-1], pattern[ii][pjj], tolerance)) {
                pii++;
                    //}
            } else if (compareUpWidth(pattern[pii], source[i+sii], j, scale, tolerance)) {
                pii++;
                sii++;
            }/* else if (compareUpWidth(pattern[pii], source[i+sii+1], j, scale, tolerance)) {
                pii++;
            }
                sjj++;
                pjj++;
                    //System.out.println("Similar actual pixel");
            } else if (Pixel.isSimilar(Pixel.getQuotient(source[ii][j+sjj], scale), pattern[ii][pjj], tolerance) ||
             Pixel.isSimilar(source[ii][j+sjj-1], pattern[ii][pjj], tolerance) ||
             Pixel.isSimilar(source[ii][j+sjj], pattern[ii][pjj+1], tolerance)) {
                pjj++;
            } else if (Pixel.isSimilar(source[ii][j+sjj+1], pattern[ii][pjj], tolerance)) {
                sjj++;
            } */
                //else check if at end of source and don't increase it 
            else {
                /*System.out.println("Failed at source: (" + ii + ","+(j+sjj)+") and pattern: ("+ii+", "+pjj+")\n"+
                    "Source Pixel =  ("+source[ii][j+sjj].r+", "+source[ii][j+sjj].g+", "+source[ii][j+sjj].b+")\n"+
                    "Pattern Pixel = ("+pattern[ii][pjj].r+", "+pattern[ii][pjj].g+", "+pattern[ii][pjj].b+")\n");*/
                return "false";
            }

        }
        return "true";
    }

    public static boolean compareUpWidth(Pixel[] pattern, Pixel[] source, int j, int scale, int tolerance){
        int patternLength = pattern.length; 
        int sjj = 0;
        int pjj = 0;
        while (pjj < patternLength) {
            if ((j+sjj) >= source.length){
                    //if (Pixel.isSimilar(source[i][j+sjj-1], pattern[ii][pjj], tolerance)) {
                pjj++;
                    //}
                    //if (Pixel.isSimilar(source[i][j+sjj-1])
            } else if (Pixel.isSimilar(source[j+sjj], pattern[pjj], tolerance)){
                sjj++;
                pjj++;
                    //System.out.println("Similar actual pixel");
            } else if (Pixel.isSimilar(Pixel.getQuotient(source[j+sjj], scale), pattern[pjj], tolerance) ||
             Pixel.isSimilar(source[j+sjj-1], pattern[pjj], tolerance) ||
             Pixel.isSimilar(source[j+sjj], pattern[pjj+1], tolerance)) {
                pjj++;
            } else if (Pixel.isSimilar(source[j+sjj+1], pattern[pjj], tolerance)) {
                sjj++;
                //pjj++;
            } else {
                /*System.out.println("Failed at source: (" + ii + ","+(j+sjj)+") and pattern: ("+ii+", "+pjj+")\n"+
                    "Source Pixel =  ("+source[j+sjj].r+", "+source[j+sjj].g+", "+source[j+sjj].b+")\n"+
                    "Pattern Pixel = ("+pattern[pjj].r+", "+pattern[pjj].g+", "+pattern[pjj].b+")\n");*/
                return false;
            }
        }
        return true;
    }
        /*
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
                            System.out.println("Filling in If");
                            fillingGap = false; //but due to match so move on
                        } else if(!(Pixel.isSimilar(source[i][j], pattern[curPatternRow][curPatternCol], tolerance) ||
                                    Pixel.isSimilar(Pixel.getQuotient(pattern[curPatternRow][curPatternCol], 2), source[i][j], tolerance))) {
                            System.out.println("Filling in Else");
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
*/

    public static int currentCornerCheck(Pixel[] corners, Pixel current){
        for(int i = 0; i < corners.length; i++){
            if(corners[i].val == current.val) return i;
        }
        return -1;
    }

    public static void compareScaleDown(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        System.out.println();
        System.out.println("TODO: implement compareScaleDown");
        System.out.println();
        return;
    }

    public static void debugPixels(Pixel[][] pattern, Pixel[][] source){
    	System.out.println("Pattern:\n");
    	for(int j = 0; j < pattern.length; j++) {
            for(int i = 0; i < pattern[0].length; i++){
                System.out.print("(" + pattern[j][i].r + ",");
                System.out.print("" + pattern[j][i].g + ",");
                System.out.print("" + pattern[j][i].b + ")");
            }
            System.out.println();
        }
    	System.out.println();
        System.out.println("Source:");
        for(int j = 0; j < source.length; j++) {
            for(int i = 0; i < source[j].length; i++){
                System.out.print("(" + source[j][i].r + ",");
                System.out.print("" + source[j][i].g + ",");
                System.out.print("" + source[j][i].b + ")");
            }
            System.out.println();
        }
        System.out.println();
    	return;
    }

    public static void debugInts(int[][] pattern, int[][] source){
    	System.out.println("Pattern:\n");
        for(int j = 0; j < pattern.length; j++) {
            for(int i = 0; i < pattern[j].length; i++){
                System.out.print(Integer.toHexString(pattern[j][i]) + "_");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Source:");
        for(int j = 0; j < source.length; j++) {
            for(int i = 0; i < source[j].length; i++){
                System.out.print(Integer.toHexString(source[j][i]) + "_");
            }
            System.out.println();
        }
        System.out.println();
        return;
    }

    public static void printResults(int[] results){
    	//TODO Check number of results
    	System.out.println("");
    	for (int i = 0; i < results.length; i = i+4) {
    		System.out.println("" + results[i] + " matches " +results[i+1] + 
    			" at topleft " + results[i+2] + " bottomright " + results[i+3]);
    	}
    	return;
    }
    /**
     *THEORY: Like numbers should be near others. 
     *30	 36	  20          50
     *30 31 31 36 35 21 20 21 20 49 50
     */















}
