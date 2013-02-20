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
        int tolerance = 60;
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
    			result = compareExact(sourceInts, patternInts);
                if (result.length == 0) {
                    result = compareNoScale(patternPixels, sourcePixels, tolerance);
                    if (result.length == 0) {
                        //result = compareScaleUp(patternPixels, sourcePixels, tolerance);
                    }
                }
    		} else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                System.out.println("In compareNoScale(...) function.");
                result = compareNoScale(patternPixels, sourcePixels, tolerance);
                
                    /*if (result.length == 0){
                        System.out.println("In compareScaleUp(...) function.");
                        result = compareScaleUp(patternPixels, sourcePixels, tolerance);
                        //result = compareScaleDown(patternPixels, sourcePixels, tolerance);
                    }*/
            } else {
                result = new int[0];
            }

            patternFile = patternFile.substring(patternFile.lastIndexOf("/") + 1);
            sourceFile = sourceFile.substring(sourceFile.lastIndexOf("/") + 1);

            printResults(patternFile, sourceFile, patternImg.getWidth(), patternImg.getHeight(), result);


            //debugInts(patternInts, sourceInts);
            System.out.println();
            //debugPixels(patternPixels, sourcePixels);

    		//TODO: This takes in the array or whatever 
    		//printResults(results);
    	}
    	catch (Exception e){
    		//System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
    	}
    	//catch (IOException e){System.err.println(e);}
    }


    //Call this if same size arrays and arrays within are same length
    public static int[] compareExact(int[][] pattern, int[][] source) {
        for (int i = 0; i < pattern.length; i++){
            for (int j = 0; j < pattern[i].length; j++){
                if (pattern[i][j] != source[i][j]){
                    return new int[0];
                }
            }
        }
        return new int[]{0,0};
    }

    //TODO Implement
    //Method to check if patternis a cropped version of source
    public static int[] compareNoScale(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        //setup return string
        String strResult = "false";
        int patternLengthi = pattern.length;
        int patternLengthj = pattern[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;
        int[] res;

        //go through each row and each column
        //TODO: Maybe computer the (source.length - pattern.length) things outside
        for (int i = 0; i < (sourceLengthi - patternLengthi); i++){
            for (int j = 0; j < (sourceLengthj - patternLengthj); j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern[0][0], source[i][j], tolerance)) {
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    // if so, call helper to see if we have found the match spot
                    //System.out.println("Pixel match at (" + j + ", " + i + ")");
                    res = justCompare(pattern, source, i, j, tolerance);
                    //if we have, then return true
                    if (res.length == 2){
                        return res;
                    }
                }
            }
        }
        //if we can't find anything, return false
        return new int[0];
    }
    
    public static int[] justCompare(Pixel[][] pattern, Pixel[][] source, int i, int j, int tolerance) {
        //go through each row and column of the pattern image
        int[] result;
        for (int ii = 0; ii < pattern.length; ii++){
            for (int jj = 0; jj < pattern[ii].length; jj++){
                //check if the pattern and source match
                if (!(Pixel.isSimilar(pattern[ii][jj], source[i+ii][j+jj], tolerance))) {
                    //System.out.println("No Match");
                    return new int[0];
                }
            }
        }
        result = new int[]{j,i};
        return result;
    }

	//TODO Implement
	//Else - LOOOOONG check
    public static int[] compareScaleUp(Pixel[][] pattern, Pixel[][] source, int tolerance) {
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
                    System.out.println("Pixel match at (" + j + ", " + i + ")");
                    // if so, call helper to see if we have found the match spot
                    res = compareUpHelper(pattern, source, i, j, scale, tolerance);
                    if (res.length == 2){
                        //int[] res = new int[]{j,i};
                        return res;
                    }
                } else {
                    //System.out.println("No first pixel match.");
                }
            }
        }
        return new int[0];
    }

    public static int[] compareUpHelper(Pixel[][] pattern, Pixel[][] source, int i, int j, int scale, int tolerance) {
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
                //System.out.println("Got through first comparison");
            /*} else if (compareUpWidth(pattern[pii], source[i+sii-1], j, scale, tolerance)) {
                pii++;
            } else if (compareUpWidth(pattern[pii], source[i+sii-2], j, scale, tolerance)) {
                pii++;*/
            } else if (compareBetweenHeights(pattern, pii, source, (i+sii), j, scale, tolerance)) {
                pii++;
            } else if (!((i+sii+1) >= source.length)){
                if (compareUpWidth(pattern[pii], source[i+sii+1], j, scale, tolerance)) {
                    sii++;
                } else {
                    return new int[0];
                }
            } else {
                /*System.out.println("Failed at source: (" + ii + ","+(j+sjj)+") and pattern: ("+ii+", "+pjj+")\n"+
                    "Source Pixel =  ("+source[ii][j+sjj].r+", "+source[ii][j+sjj].g+", "+source[ii][j+sjj].b+")\n"+
                    "Pattern Pixel = ("+pattern[ii][pjj].r+", "+pattern[ii][pjj].g+", "+pattern[ii][pjj].b+")\n");*/
                return new int[0];
            }

        }
        return new int[]{j,i};
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
                    //System.out.println("FAILS:\nsjj = "+sjj);
                    //System.out.println("pjj = "+pjj);
                /*System.out.println("Failed at source: (" + ii + ","+(j+sjj)+") and pattern: ("+ii+", "+pjj+")\n"+
                    "Source Pixel =  ("+source[j+sjj].r+", "+source[j+sjj].g+", "+source[j+sjj].b+")\n"+
                    "Pattern Pixel = ("+pattern[pjj].r+", "+pattern[pjj].g+", "+pattern[pjj].b+")\n");*/
                    return false;
                }
            }
            else {
            /*System.out.println("Failed at source: (" + ii + ","+(j+sjj)+") and pattern: ("+ii+", "+pjj+")\n"+
                "Source Pixel =  ("+source[j+sjj].r+", "+source[j+sjj].g+", "+source[j+sjj].b+")\n"+
                "Pattern Pixel = ("+pattern[pjj].r+", "+pattern[pjj].g+", "+pattern[pjj].b+")\n");*/
                //System.out.println("FAILS:\nsjj = "+sjj);
                //    System.out.println("pjj = "+pjj);
                return false;
            }
        }
        return true;
    }

    public static boolean compareBetweenHeights(Pixel[][] pattern, int pi, Pixel[][] source, int si, int j, int scale, int tolerance) {
        int sjj = 0;
        int pjj = 0;
        //System.out.println("OH YEAAAAAAAH --------------------------------------");
        while (pjj < pattern.length) {
            if ((j+sjj) >= source.length){
                    //if (Pixel.isSimilar(source[i][j+sjj-1], pattern[ii][pjj], tolerance)) {
                pjj++;
                    //}
                    //if (Pixel.isSimilar(source[i][j+sjj-1])
            } else if (Pixel.getBetween(source[si][j+sjj], source[si+1][j+sjj], pattern[pi][pjj], tolerance)) {
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

    public static int[] compareScaleDown(Pixel[][] pattern, Pixel[][] source, int tolerance) {
        System.out.println();
        System.out.println("TODO: implement compareScaleDown");
        System.out.println();
        return new int[0];
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
