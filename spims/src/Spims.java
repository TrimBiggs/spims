import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;
import java.util.Iterator;

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
        int tolerance = 30;
        int gifTolerance = 45;
        int[] result;

        //take main method inputs and ensure they are correct and valid
        Inputter inputs = new Inputter(args);
        inputs.processInputs();

        HashMap<String, BufferedImage> patterns = inputs.getPatterns();
        HashMap<String, BufferedImage> sources = inputs.getSources();


        BufferedImage patternImg = null;
        BufferedImage sourceImg = null;
        String curPattern;
        String curSource;
        int outputSizeBefore;

        Iterator<String> patternKeys = patterns.keySet().iterator();
        Iterator<String> sourceKeys;

        while(patternKeys.hasNext()){
            curPattern = patternKeys.next();
            sourceKeys = sources.keySet().iterator();

            while(sourceKeys.hasNext()){
                try{
                    curSource = sourceKeys.next();

                    patternImg = patterns.get(curPattern);
                    sourceImg = sources.get(curSource);

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

                    PatternImage patternObject = new PatternImage(patternPixels);

                    int patternWidth = patternImg.getWidth();
                    int patternHeight = patternImg.getHeight();

                    //If images are exact width/height, compare them
                    outputSizeBefore = output.size();

                    int givenTolerance = tolerance;

                    //check for gifs
                    if (curPattern.endsWith("gif") || curSource.endsWith("gif")) {
                        givenTolerance = gifTolerance;
                    }

                    if (patternWidth == sourceImg.getWidth() && patternHeight == sourceImg.getHeight()) {
                        //Check if any results, if not call compareScaleUp()
                        compareExact(sourceInts, patternInts, curPattern, curSource, patternWidth, patternHeight);
                        if (output.size() == outputSizeBefore) {
                            compareNoScale(patternObject, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
                        }
                    } else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                        compareNoScale(patternObject, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
                    } //else
                    //compareScaleUp(patternPixels, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
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
    public static void compareNoScale(PatternImage pattern, Pixel[][] source, int tolerance,
                                       String pname, String sname, int pwidth, int pheight) {
        //setup return string
        String strResult = "false";
        int patternLengthi = pattern.pixels.length;
        int patternLengthj = pattern.pixels[0].length;
        int sourceLengthi = source.length;
        int sourceLengthj = source[0].length;
        int[] res;

        //go through each row and each column
        //TODO: Maybe computer the (source.length - pattern.length) things outside
        for (int i = 0; i <= (sourceLengthi - patternLengthi); i++){
            for (int j = 0; j <= (sourceLengthj - patternLengthj); j++){
                //check if the first pixel of the pattern
                //matches the given pixel of the source
                if (Pixel.isSimilar(pattern.pixels[0][0], source[i][j], tolerance) >= 0) {
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
    public static boolean justCompare(PatternImage pattern, Pixel[][] source, int i, int j, int tolerance) {
        //go through each row and column of the pattern image
        int[] result;
        for (int ii = 0; ii < pattern.pixels.length; ii++){
            for (int jj = 0; jj < pattern.pixels[ii].length; jj++){
                //check if the pattern and source match
                int offset = Pixel.isSimilar(pattern.pixels[ii][jj], source[i+ii][j+jj], tolerance);
                if (offset >= 0)
                    pattern.offset += offset;
                else
                    return false;
            }
        }
        return true;
    }
}
