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

    //Stores matches that are found
  private static Outputter output = new Outputter();

    /**
     * Main method of class Spims. Runs the program functions.
     * @param args Arguments sent to the main method, files or directories to use,
     * and indicitive flags telling whether inputs are directories or files
     */
    public static void main(String[] args) {
        //TODO: REMOVE THIS
      long startTime = System.currentTimeMillis();

      int smallPatternTolerance = 15;
      int majorTolerance = 60;
      int smallPatternSize = 10;

        //take in main method inputs and ensure they are correct and valid
      Inputter inputs = new Inputter(args);
      inputs.processInputs();

      HashMap<String, BufferedImage> patterns = inputs.getPatterns();
      HashMap<String, BufferedImage> sources = inputs.getSources();

      Iterator<String> patternKeys = patterns.keySet().iterator();

      while(patternKeys.hasNext()){
        String curPattern = patternKeys.next();
        Iterator<String> sourceKeys = sources.keySet().iterator();

        BufferedImage patternImg = patterns.get(curPattern);
        PatternObject patternObj = new PatternObject(inputs.generatePixels(patternImg));
        int patternWidth = patternImg.getWidth();
        int patternHeight = patternImg.getHeight();

        while(sourceKeys.hasNext()){
          String curSource = sourceKeys.next();

          BufferedImage sourceImg = sources.get(curSource);
          int sourceWidth = sourceImg.getWidth();
          int sourceHeight = sourceImg.getHeight();

          int[][] sourcePixels = inputs.generatePixels(sourceImg);

          int givenTolerance = majorTolerance;
          int outputSizeBefore = output.size();

                //Use tighter tolerance for images with area less than 10
          if ((patternWidth * patternHeight) < smallPatternSize)
            givenTolerance = smallPatternTolerance;

          if (patternWidth == sourceWidth && patternHeight == sourceHeight) {
            compareExact(sourcePixels, patternObj.pixels, curPattern, curSource, patternWidth, patternHeight);
            if (output.size() == outputSizeBefore) {
              compareCropped(patternObj, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
            }
          } else if (patternWidth < sourceWidth && patternHeight < sourceHeight){
            compareCropped(patternObj, sourcePixels, givenTolerance, curPattern, curSource, patternWidth, patternHeight);
          }
        }
      }
      output.output();
        //TODO: REMOVE THIS
      long endTime = System.currentTimeMillis();
      System.out.println("Elapsed Time: " + ((endTime - startTime) / 60000) + "' " + ((endTime - startTime) / 1000) + "\"");
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
      for (int y = 0; y < pattern.length; y++) {
        for (int x = 0; x < pattern[y].length; x++) {
          if (!PatternObject.isExactlySimilar(pattern[y][x], source[y][x])) {
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
    public static void compareCropped(PatternObject patternObj, int[][] source, int tolerance,
     String pname, String sname, int pwidth, int pheight) {
      int patternHeight = patternObj.pixels.length;
      int patternWidth = patternObj.pixels[0].length;
      int sourceHeight = source.length;
      int sourceWidth = source[0].length;

      for (int sy = 0; sy <= (sourceHeight - patternHeight); sy++) {
        for (int sx = 0; sx <= (sourceWidth - patternWidth); sx++) {
          //check if the corners of the pattern
          //match the corresponding pixels of the source
          if (PatternObject.isSimilar(patternObj.pixels[0][0], source[sy][sx], tolerance) >= 0 &&
            PatternObject.isSimilar(patternObj.pixels[0][(patternWidth-1)], source[sy][sx+(patternWidth-1)], tolerance) >= 0 &&
            PatternObject.isSimilar(patternObj.pixels[(patternHeight-1)][0], source[sy+(patternHeight-1)][sx], tolerance) >= 0 &&
            PatternObject.isSimilar(patternObj.pixels[(patternHeight-1)][(patternWidth-1)], source[sy+(patternHeight-1)][sx+(patternWidth-1)], tolerance) >= 0) {
            if (compareCroppedHelper(patternObj, source, sy, sx, tolerance)) {
              output.add(pname, sname, pwidth, pheight, sx, sy, patternObj.offset);
            }
          }
        }
      }
    }

    /**
    * REDO
    * The compareCroppedHelper function iterates through the pattern and source int arrays
    * and compares each corresponding pixel
    *
    * @param pattern an Array of Pixels for each pixel in the pattern image
    * @param source an Array of Pixels for each pixel in the source image
    * @param sy an int referring to the y index of the source array
    * @param sx an int referring to the x index of the source array
    * @param tolerance and int representing how forgiving the algorithm will be when searching for equal pixels
    *
    * @return boolean. Whether or not the two array's (and therefore images) are identical
    */
    public static boolean compareCroppedHelper(PatternObject patternObj, int[][] source, int sy, int sx, int tolerance) {
      for (int py = 0; py < patternObj.pixels.length; py++) {
        for (int px = 0; px < patternObj.pixels[py].length; px++) {
          int offset = PatternObject.isSimilar(patternObj.pixels[py][px], source[sy+py][sx+px], tolerance);
          if (offset >= 0)
            patternObj.offset += offset;
          else {
            patternObj.offset = 0;
            return false;
          }
        }
      }
      return true;
    }
  }
