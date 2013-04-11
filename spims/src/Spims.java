import java.io.*; //??? not sure
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
/**
*   Spims is the main class of the image matching software.
*   It accepts input, validates it, handles sending the input files
*   to the appropriate algorithms, and then ensures appropriate
*   output is printed with results.
*
*   @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*   @version 0.4
*   @since 1/30/2012/
*
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
        int tolerance = 30;
        int gifTolerance = 45;
        int[] result;

        //take main method inputs and ensure they are correct and valid
        Inputter inputs = new Inputter(args);
        inputs.processInputs();

        ArrayList<BufferedImage> patterns = inputs.getPatterns();
        ArrayList<BufferedImage> sources = inputs.getSources();


        BufferedImage patternImg = null;
        BufferedImage sourceImg = null;
        File curPattern;
        File curSource;
        int outputSizeBefore;

      for(int x = 0; x < patterns.size(); x++){
        for(int y = 0; y < sources.size(); y++){
            try{
                patternImg = patterns.get(x);
                sourceImg = sources.get(y);
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
                    compareExact(sourceInts, patternInts, patternFile, sourceFile, patternWidth, patternHeight);
                    if (output.size() == outputSizeBefore) {
                        compareNoScale(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                        System.out.println("Output size is now the same 1");
                        if (output.size() == outputSizeBefore && ! (patternWidth == 1 && patternHeight == 1)) {
                            System.out.println("Output size is now the same 2");
                            compareScaleUp(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                        }
                    }
                } else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                    compareNoScale(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
                } else
                compareScaleUp(patternPixels, sourcePixels, givenTolerance, patternFile, sourceFile, patternWidth, patternHeight);
            }
            catch (Exception e){
                e.printStackTrace();
            }
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

    /**
    * This function calls the helper function just compare, which compares two Pixel
    * array of arrays and checks against the tolerance value. This function checks if
    * the pattern image matches exactly (without scaling) to any sub sections
    * of the source image
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
    * This function compares array of arrays of Pixels against another one of exact same
    * rows and columns
    *
    * @param pattern an Array of Pixels for each pixel in the pattern image
    * @param source an Array of Pixels for each pixel in the source image
    * @param i
    * @param j
    * @param tolerance and int representing how forgiving the algorithm will be when searching for equal pixels
    *
    * @return boolean. Whether or not the two array's (and therefore images) are identical
    */
    public static boolean justCompare(Pixel[][] pattern, Pixel[][] source, int i, int j, int tolerance) {
        //go through each row and column of the pattern image
        int[] result;
        for (int ii = 0; ii < pattern.length; ii++){
            for (int jj = 0; jj < pattern[ii].length; jj++){
                //check if the pattern and source match
                if (!(Pixel.isSimilar(pattern[ii][jj], source[i+ii][j+jj], tolerance))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
    *
    * @param pattern Array of arrays of Piexel. This is the pattern image.
    * @param source  Array of arrays of Piexel. This is the source image.
    * @param tolerance Integer. Used to determine if the differences are too large to ignore
    * @return Null. Returns nothing. Just add matching results to the outputter object
    */
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
                    // if so, call helper to see if we have found the match spot

                    if (compareUpHelper(pattern, source, i, j, scale, tolerance)){
                        output.add(pname, sname, pwidth, pheight, j, i);
                    }
                } else {
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
                pii++;
            } else if (compareUpWidth(pattern[pii], source[i+sii], j, scale, tolerance)) {
                pii++;
                sii++;
           } else if (compareBetweenHeights(pattern, pii, source, (i+sii), j, scale, tolerance)) {
                pii++;
            } else if (!((i+sii+1) >= source.length)){
                if (compareUpWidth(pattern[pii], source[i+sii+1], j, scale, tolerance)) {
                    sii++;
                } else {
                    return false;
                }
            } else {
               return false;
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
                pjj++;
            } else if (Pixel.isSimilar(source[j+sjj], pattern[pjj], tolerance)){
                sjj++;
                pjj++;
            } else if (Pixel.isSimilar(source[j+sjj], pattern[pjj+1], tolerance)      ||
                       Pixel.getBetween(source[j+sjj], source[j+sjj+1], pattern[pjj], tolerance) ){

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
                pjj++;
            } else if (Pixel.getBetween(source[si][j+sjj], source[si+1][j+sjj], pattern[pi][pjj], tolerance) ||
                        Pixel.isSimilar(source[si+1][j+sjj], pattern[pi][pjj], tolerance)) {
                pjj++;
                sjj++;
            } else if (Pixel.getBetween(source[si][j+sjj], source[si-1][j+sjj], pattern[pi][pjj], tolerance)) {
                pjj++;
            } else {
                return false;
            }
        }
        return true;
    }

}
