import java.io.*; //??? not sure
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
        int smallPatternTolerance = 15;
        int tolerance = 30;
        int gifTolerance = 45;

        //take main method inputs and ensure they are correct and valid
        Inputter inputs = new Inputter(args);
        inputs.processInputs();

        HashMap<String, BufferedImage> patterns = inputs.getPatterns();
        HashMap<String, BufferedImage> sources = inputs.getSources();

        int outputSizeBefore;

        Iterator<String> patternKeys = patterns.keySet().iterator();

        while(patternKeys.hasNext()){
            String curPattern = patternKeys.next();
            Iterator<String> sourceKeys = sources.keySet().iterator();

            BufferedImage patternImg = patterns.get(curPattern);
            int[][] patternPixels = inputs.generatePixels(patternImg);

            while(sourceKeys.hasNext()){
                String curSource = sourceKeys.next();

                BufferedImage sourceImg = sources.get(curSource);

                int[][] sourcePixels = inputs.generatePixels(sourceImg);

                int patternWidth = patternImg.getWidth();
                int patternHeight = patternImg.getHeight();

                outputSizeBefore = output.size();

                int givenTolerance = tolerance;

                //check for gifs
                //TODO BEFORE FINAL SUBMIT: CHANGE THIS TO ACT LIKE FILTER
                if ((patternWidth * patternHeight) < 10)
                    givenTolerance = smallPatternTolerance;
                    //check for gifs
                else if (curPattern.endsWith("gif") || curSource.endsWith("gif"))
                    givenTolerance = gifTolerance;

                if (patternWidth == sourceImg.getWidth() && patternHeight == sourceImg.getHeight()) {
                    //Check if any results, if not call compareScaleUp()
                    compareExact(sourcePixels, patternPixels, curPattern, curSource, patternWidth, patternHeight);

                    //TODO: Remove this section
                    /*if (output.size() == outputSizeBefore) {
                        compareNoScale(patternPixels, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
                        System.out.println("Output size is now the same 1");
                        if (output.size() == outputSizeBefore && ! (patternWidth == 1 && patternHeight == 1)) {
                            System.out.println("Output size is now the same 2");
                            compareScaleUp(patternPixels, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
                        }
                    }*/

                } else if (patternImg.getWidth() < sourceImg.getWidth() && patternImg.getHeight() < sourceImg.getHeight()){
                    compareNoScale(patternPixels, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
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
                if (!Pixel.isSimilar(pattern[i][j], source[i][j], 0)){
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
    public static void compareNoScale(int[][] pattern, int[][] source, int tolerance,
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
    public static boolean justCompare(int[][] pattern, int[][] source, int i, int j, int tolerance) {
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
}
