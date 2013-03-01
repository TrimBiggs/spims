import java.io.*; //??? not sure
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Spims {

    /**
     * @param args
     */
    private static Outputter output = new Outputter();
    public static void main(String[] args) {
    	String patternFile = "";
    	String sourceFile = "";
        File pattern;
        File source;
        int[] results = new int[1];
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
                        if (output.size() == outputSizeBefore) {
                        //System.out.println("In compareScaleUp(...) function.");
                        //result = compareScaleUp(patternPixels, sourcePixels, tolerance);
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
                }//else compareScale(); .....


                //This should take in array of type Results[]
                //printResults(patternFile, sourceFile, patternImg.getWidth(), patternImg.getHeight(), result);

                //debugInts(patternInts, sourceInts);
                //debugPixels(patternPixels, sourcePixels);

            }
            catch (Exception e){
    		    System.err.println("Error: " + e.getMessage());
                //e.printStackTrace();
            }
    	    //catch (IOException e){System.err.println(e);}
        }
      }
      output.output();
    }



    //Call this if same size arrays and arrays within are same length
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
                    res = justCompare(pattern, source, i, j, tolerance);
                    //if we have, then return true
                    if (res.length == 2){
                        output.add(pname, sname, pwidth, pheight, j, i);
                    }
                }
            }
        }
        //if we can't find anything, return false
        return;
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
