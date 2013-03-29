import java.io.*; //??? not sure
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
*   Spims is the main class of the image matching software.
*   It accepts input, validates it, handles sending the input files
*   to the appropriate algorithms, and then ensures appropriate
*   output is printed with results.
*
*   @author Corey Hanson
*   @author Tim Briggs
*   @author Reed Lockwood
*   @author Wen Cao
*/
public class Spims {

    private static Outputter output = new Outputter();

    /**
     * Main method of class Spims. Runs the program functions.
     * @param args Arguments sent to the main method, files or directories to use,
     * and indicitive flags telling whether inputs are directories or files
     */
    public static void main(String[] args) {
    	String patternFile = "";
    	String sourceFile = "";
        File pattern;
        File source; 
        int tolerance = 30;
        int gifTolerance = 45;
        int[] result;

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

        boolean badInput = false;
        //TODO Modify to take in more arguments and check validity/flags
    	if (!args[0].equals("-p") && !args[0].equals("-pdir")) {
            System.err.println("Invalid flag provided: " + args[0]);
            return;
    	}
        if(!args[2].equals("-s") && !args[2].equals("-sdir")) {
            System.err.println("Invalid flag provided: " + args[2]);
            return;
        }

    	//Do a search on the '-p' and '-s' flags
    	if (args.length == 4) {
	    //args[0] should be -p
    		pattern = new File(args[1]);
	    //args[2] should be -s
    		source = new File(args[3]);
    	}
      else{
        System.err.println("Too many arguments. Expected 4 got " + args.length);
        return;
        //stupid hack to make java convinced the vars are initialized
      }

      File[] patterns;
      File[] sources;
      ImageFilter filter = new ImageFilter();

      if (args[0].equals("-pdir") && pattern.isDirectory()){
        patterns = pattern.listFiles(filter);
      } else if(args[0].equals("-p") && !pattern.isDirectory()){
        patterns = new File[]{pattern};
      } else {
        System.err.println("Pattern input does not match flag. Program terminating.");
        return;
      }
      if (args[2].equals("-sdir") && source.isDirectory()){ sources = source.listFiles(filter); }
      else if(args[2].equals("-s") && !source.isDirectory()){ sources = new File[]{source}; }
      else{
        System.err.println("Source input does not match flag. Program terminating.");
        return;
      }

      //TODO: Create more informative/specific error messages
      if(patterns.length == 0 || !filter.accept(patterns[0])){
        System.err.println("Error: Expected pattern input(s) with extension .png, .jpg, or .gif.");
        return;
      }
      if(sources.length == 0 || !filter.accept(sources[0])){
        System.err.println("Error: Expected source input(s) with extension .png, .jpg, or .gif.");
        return;
      }

    	BufferedImage patternImg = null;
    	BufferedImage sourceImg = null;
        File curPattern;
        File curSource;
        int outputSizeBefore;

      for(int x = 0; x < patterns.length; x++){
        curPattern = patterns[x];
        for(int y = 0; y < sources.length; y++){
            curSource = sources[y];
        	try{
        		patternImg = ImageIO.read(curPattern);
        		sourceImg = ImageIO.read(curSource);
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
                for(int i = 0; i < patternPixels.length; i++) {
                    for(int j = 0; j < patternPixels[i].length; j++) {
                        patternPixels[i][j] = new Pixel(patternImg.getRGB(j,i));
                    }
                }
                for(int i = 0; i < sourcePixels.length; i++) {
                    for(int j = 0; j < sourcePixels[i].length; j++) {
                        sourcePixels[i][j] = new Pixel(sourceImg.getRGB(j,i));
                    }
                }

                patternFile = curPattern.getName().substring(patternFile.lastIndexOf("/") + 1);
                sourceFile = curSource.getName().substring(sourceFile.lastIndexOf("/") + 1);
                //System.out.println(patternFile);
                //System.out.println(sourceFile);
                int patternWidth = patternImg.getWidth();
                int patternHeight = patternImg.getHeight();

                //If images are exact width/height, compare them
                outputSizeBefore = output.size();

                int givenTolerance = tolerance;

                //check for gifs
                if (patternFile.endsWith("gif") || sourceFile.endsWith("gif")) {
                    givenTolerance = gifTolerance;
                }

                if (patternWidth == sourceImg.getWidth() && patternHeight == sourceImg.getHeight()) {
				    //Check if any results, if not call compareScaleUp()
                    //System.out.println("In compareExact(...) function.");
                    compareExact(sourceInts, patternInts, patternFile, sourceFile, patternWidth, patternHeight);
                    if (output.size() == outputSizeBefore) {
                        //System.out.println("In compareNoScale(...) function.");
                        compareNoScale(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                        System.out.println("Output size is now the same 1");
                        if (output.size() == outputSizeBefore && ! (patternWidth == 1 && patternHeight == 1)) {
                        //System.out.println("In compareScaleUp(...) function.");
                            System.out.println("Output size is now the same 2");
                            compareScaleUp(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                        }
                    }
                } else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                //System.out.println("In compareNoScale(...) function.");
                    compareNoScale(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                    /*if (result.length == 0){
                        //System.out.println("In compareScaleUp(...) function.");
                        result = compareScaleUp(patternPixels, sourcePixels, tolerance);
                        //result = compareScaleDown(patternPixels, sourcePixels, tolerance);
                    }*/
                } else
                compareScaleUp(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);

                //debugInts(patternInts, sourceInts);
                //debugPixels(patternPixels, sourcePixels);

            }
            catch (Exception e){
    		    //System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
    	    //catch (IOException e){System.err.println(e);}
        }
      }
      output.output();
    }



    //Call this if same size arrays and arrays within are same length
    /**
    * Check to see if two images are the exact same image.
    *
    * @param pattern an Array of Pixels for each pixel in the pattern image
    * @param source an Array of Pixels for each pixel in the source image
    * @param pname the name of the pattern image
    * @param sname the name of the source image
    * @param pwidth width of the pattern image for output purposes
    * @param pheight height of the pattern image for output purposes
    *
    * @return void; class's output object will be updated if there is a match
    */
    public static void compareExact(int[][] pattern, int[][] source, String pname, String sname, int pwidth, int pheight) {
        for (int i = 0; i < pattern.length; i++){
            for (int j = 0; j < pattern[i].length; j++){
                if (pattern[i][j] != source[i][j]){
                    return;
                }
            }
        }
        output.add(pname, sname, pwidth, pheight, 0, 0);
    }

    //TODO Implement
    //Method to check if patternis a cropped version of source
    public static void compareNoScale(Pixel[][] pattern, Pixel[][] source, int tolerance,
                                       String pname, String sname, int pwidth, int pheight) {
        //setup return string
        String strResult = "false";
        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;
        int[] res;

        //go through each row and each column
        //TODO: Maybe computer the (source.length - pattern.length) things outside
        for (int i = 0; i <= (sourceLengthi - patternLengthi); i++){
            for (int j = 0; j <= (sourceLengthj - patternLengthj); j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern[0][0], source[i][j], tolerance)) {
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    // if so, call helper to see if we have found the match spot
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    //if we have, then return true
                    if (justCompare(pattern, source, i, j, tolerance)){
                        output.add(pname, sname, pwidth, pheight, j, i);
                    }
                }
            }
        }
        //if we can't find anything, return false
        return;
    }

    /**
    * @param pattern an Array of Pixels for each pixel in the pattern image
    * @param source an Array of Pixels for each pixel in the source image
    * @param i
    * @param j
    * @param tolerance and int representing how forgiving the algorithm will be when searching for equal pixels
    *
    * @return an array of ints showing the starting corner of the match
    */
    public static boolean justCompare(Pixel[][] pattern, Pixel[][] source, int i, int j, int tolerance) {
        //go through each row and column of the pattern image
        int[] result;
        for (int ii = 0; ii < pattern.length; ii++){
            for (int jj = 0; jj < pattern[ii].length; jj++){
                //check if the pattern and source match
                if (!(Pixel.isSimilar(pattern[ii][jj], source[i+ii][j+jj], tolerance))) {
                    //System.out.println("No Match");
                    return false;
                }
            }
        }
        return true;
    }

	//TODO Implement
	//Else - LOOOOONG check
    public static void compareScaleUp(Pixel[][] pattern, Pixel[][] source, int tolerance,
                                      String pname, String sname, int pwidth, int pheight){
                                       //Pixel[][] pattern, Pixel[][] source, int tolerance) {
        int scale = 2;

        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;
        int[] res;

        for (int i = 0; i < sourceLengthi; i++){
            for (int j = 0; j < sourceLengthj; j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern[0][0], source[i][j], tolerance)) {
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    // if so, call helper to see if we have found the match spot

                    if (compareScale(pattern, source, i, j, tolerance)){
                    //if (compareUpHelper(pattern, source, i, j, scale, tolerance)){
                        //int[] res = new int[]{j,i};
                        //return res;
                        output.add(pname, sname, pwidth, pheight, j, i);
                    }
                } else {
                    //System.out.println("No first pixel match.");
                }
            }
        }
        return;
    }

    public static boolean compareUpHelper(Pixel[][] pattern, Pixel[][] source, int i, int j, int scale, int tolerance) {
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
           } else if (compareBetweenHeights(pattern, pii, source, (i+sii), j, scale, tolerance)) {
                pii++;
            } else if (!((i+sii+1) >= source.length)){
                if (compareUpWidth(pattern[pii], source[i+sii+1], j, scale, tolerance)) {
                    sii++;
                } else {
                    return false;// new int[0];
                }
            } else {
               return false;// new int[0];
            }

        }
        return true;
    }

    public static boolean compareUpWidth(Pixel[] pattern, Pixel[] source, int j, int scale, int tolerance){
        int patternLength = pattern.length;
        int sourceLength = source.length;
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
            } else if (Pixel.isSimilar(source[j+sjj], pattern[pjj+1], tolerance)      ||
                       Pixel.getBetween(source[j+sjj], source[j+sjj+1], pattern[pjj], tolerance) ){
                       //Pixel.isSimilar(Pixel.getQuotient(source[j+sjj], scale), pattern[pjj], tolerance)) {

                pjj++;
            } else if (Pixel.isSimilar(source[j+sjj+1], pattern[pjj], tolerance)) {
                sjj++;
                pjj++;
            } else if ((j+sjj) != 0) {
                if (Pixel.isSimilar(source[j+sjj-1], pattern[pjj], tolerance) ||
                    Pixel.getBetween(source[j+sjj-1], source[j+sjj], pattern[pjj], tolerance)){
                    pjj++;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static boolean compareBetweenHeights(Pixel[][] pattern, int pi, Pixel[][] source, int si, int j, int scale, int tolerance) {
        int patternLength = pattern.length;
        int sourceLength = source.length;
        int sjj = 0;
        int pjj = 0;
        while (pjj < patternLength) {
            if ((j+sjj) >= sourceLength){
                    //if (Pixel.isSimilar(source[i][j+sjj-1], pattern[ii][pjj], tolerance)) {
                pjj++;
                    //}
                    //if (Pixel.isSimilar(source[i][j+sjj-1])
            } else if (Pixel.getBetween(source[si][j+sjj], source[si+1][j+sjj], pattern[pi][pjj], tolerance) ||
                        Pixel.isSimilar(source[si+1][j+sjj], pattern[pi][pjj], tolerance)) {
                //System.out.println("getBetween IF ++++++++++++++++++++++++++++++");
                pjj++;
                sjj++;
            } else if (Pixel.getBetween(source[si][j+sjj], source[si-1][j+sjj], pattern[pi][pjj], tolerance)) {
                //System.out.println("getBetween ELSE ++++++++++++++++++++++++++++");
                //sjj++;
                pjj++;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean compareScale(Pixel[][] pattern, Pixel[][] source, int sBaseHeightPointer, int sBaseWidthPointer, int tolerance) {
        int pHeight = pattern.length;
        int pWidth = pattern[0].length;
        int sHeight = source.length;
        int sWidth = source[0].length;

        int pHeightPointer = 0;
        int pWidthPointer = 0;
        int sHeightPointer = sBaseHeightPointer;
        int sWidthPointer = sBaseWidthPointer;
        int scaleTemp = 4;
        int scaleFound = scaleTemp;
        while (pHeightPointer < pHeight) {
            while (pWidthPointer < pWidth) {
                if ((sWidth - sWidthPointer) == 1) {
                //if ((sWidth - sWidthPointer) == 1) {
                    if (Pixel.isSimilar(pattern[pHeightPointer][pWidthPointer], source[sHeightPointer][sWidthPointer], tolerance) ||
                        compareSourceDown(pattern, source, sHeightPointer, sWidthPointer, pHeightPointer, pWidthPointer, scaleFound, tolerance)) {
                        pWidthPointer++;
                    }
                    else return false;
                }
                else if (Pixel.isSimilar(pattern[pHeightPointer][pWidthPointer], source[sHeightPointer][sWidthPointer], tolerance)) {
                    pWidthPointer++;
                    sWidthPointer++;
                }
                else if ((sWidth - sWidthPointer) <= scaleFound) {
                    if (compareSourceDown(pattern, source, sHeightPointer, sWidthPointer, pHeightPointer, pWidthPointer, scaleFound, tolerance)){
                        pWidthPointer++;
                        //sWidthPointer++;
                    }
                    else if (compareSourceUp(pattern, source, sHeightPointer, sWidthPointer, pHeightPointer, pWidthPointer, (sWidth - sWidthPointer), tolerance)) {
                        //System.out.println("First source up");
                        sWidthPointer++;
                    }
                    else {
                        //System.out.println("Mismatch at end");
                        return false;
                    }
                }
                else if (compareSourceUp(pattern, source, sHeightPointer, sWidthPointer, pHeightPointer, pWidthPointer, scaleFound, tolerance)) {
                    //System.out.println("sWidth = "+sWidth);
                    //System.out.println("sWidthPointer = "+sWidthPointer);
                    sWidthPointer++;
                }
                else if (Pixel.isSimilar(source[sHeightPointer][sWidthPointer], pattern[pHeightPointer][pWidthPointer + 1], tolerance) ||
                         Pixel.isSimilar(pattern[pHeightPointer][pWidthPointer], source[sHeightPointer][sWidthPointer - scaleFound], tolerance)) {
                    pWidthPointer++;
                }
                else if (compareSourceDown(pattern, source, sHeightPointer, sWidthPointer, pHeightPointer, pWidthPointer, scaleFound, tolerance)) {
                    pWidthPointer++;
                }
                else {
                    //System.out.println("Eventual Mismatch");
                    return false;
                }
            }
            pWidthPointer = 0;
            sWidthPointer = sBaseWidthPointer;
            pHeightPointer++;
            sHeightPointer++;
        }
        return true;
    }
/*
    public static boolean compareSourceMaxed(Pixel[][] pattern, Pixel[][] source, int sh, int sw, int ph, int pw, int scale, int tolerance) {

        for (int i = 0 ; i <= scale ; i++) {
            if (Pixel.isSimilar(source[sh][sw - i],
                                pattern[ph][pw],
                                tolerance))
                return true;
        }
        for (int j = 0 ; j < scale ; j++){
            //compare between heights...
        }
        return false;
    }
    public static boolean compareSourceCloseToMaxed(Pixel[][] pattern, Pixel[][] source, int sh, int sw, int ph, int pw, int scale, int tolerance) {
        int sourceWidth = source[0].length;
        while (sw < sourceWidth)
            for (int i = 0 ; i <= scale; i++) {
                if (Pixel.isSimilar(source[sh][sw - i],
                                    pattern[ph][pw],
                                    tolerance))
                    return true;
            }
            sw++;
        }
        return false;
    }
*/
    public static boolean compareSourceUp(Pixel[][] pattern, Pixel[][] source, int sh, int sw, int ph, int pw, int scale, int tolerance) {
        int topBoundsInit = scale - 1;
        int topBounds = topBoundsInit;
        //System.out.println(topBoundsInit);
        int bottomBounds = 0;
        while (bottomBounds < topBoundsInit) {
            while (bottomBounds < topBounds) {
                if (Pixel.getBetween(source[sh][sw+bottomBounds], source[sh][sw+topBounds], pattern[ph][pw], tolerance)) {
                    return true;
                }
                bottomBounds++;
            }
            bottomBounds = 0;
            topBoundsInit = topBoundsInit - 1;
        }
        return false;
    }

    public static boolean compareSourceDown(Pixel[][] pattern, Pixel[][] source, int sh, int sw, int ph, int pw, int scale, int tolerance) {
        int topBoundsInit = scale;
        int topBounds = topBoundsInit;
        int bottomBounds = 0;
        while (bottomBounds < topBoundsInit) {
            if (Pixel.isSimilar(pattern[ph][pw], source[sh][sw-topBoundsInit], tolerance)) {
                return true;
            }
            while (bottomBounds < topBounds) {
                if (Pixel.getBetween(source[sh][sw-bottomBounds], source[sh][sw-topBounds], pattern[ph][pw], tolerance)) {
                    return true;
                }
                bottomBounds++;
            }
            bottomBounds = 0;
            topBoundsInit = topBoundsInit - 1;
        }
        return false;
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

    public static void printResults(String pattern, String source, int width, int height, int[] result){
    	//TODO Check number of results
    	//for (int i = 0; i < results.length; i = i+4) {
        if (result.length == 0){
            return;
        } else {
    		System.out.println("" + pattern + " matches " + source +
    			" at " + width + "x" + height + "+" + result[0] + "+" + result[1]);
            return;
        }
        //}
    }
}
