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
 *   @version 1.0
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

    int smallPatternTolerance = 15;
    int majorTolerance = 60;
    int smallPatternSize = 10;

      //Take in main method inputs and ensure they are correct and valid
    Inputter inputs = new Inputter(args);
    inputs.processInputs();

    HashMap<String, BufferedImage> patterns = inputs.getPatterns();
    HashMap<String, BufferedImage> sources = inputs.getSources();

    Iterator<String> patternKeys = patterns.keySet().iterator();

      //Iterate through pattern images
    while(patternKeys.hasNext()){
      String curPattern = patternKeys.next();
      Iterator<String> sourceKeys = sources.keySet().iterator();

      BufferedImage patternImg = patterns.get(curPattern);
      PatternObject patternObj = new PatternObject(inputs.generatePixels(patternImg));
      int patternWidth = patternImg.getWidth();
      int patternHeight = patternImg.getHeight();

        //Iterate through source images
      while(sourceKeys.hasNext()){
        String curSource = sourceKeys.next();

        BufferedImage sourceImg = sources.get(curSource);
        int sourceWidth = sourceImg.getWidth();
        int sourceHeight = sourceImg.getHeight();

        int[][] sourcePixels = inputs.generatePixels(sourceImg);

        int givenTolerance = majorTolerance;
        int outputSizeBefore = output.size();

          //Use tighter comparison tolerance for images with area less than 10
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
  }




  /**
   * Check to see if two images are the exact same image.
   *
   * @param pattern an Array of RGB int values representing each pixel in the pattern image
   * @param source an Array of RGB int values representing each pixel in the source image
   * @param pname the name of the pattern image
   * @param sname the name of the source image
   * @param patternWidth width of the pattern image for output purposes
   * @param patternHeight height of the pattern image for output purposes
   *
   * @return void; class's output object will be updated if there is a match
   */
  public static void compareExact(int[][] pattern, int[][] source, String pname,
                                  String sname, int patternWidth, int patternHeight) {
    for (int y = 0; y < patternHeight; y++) {
      for (int x = 0; x < patternWidth; x++) {
        if (!PatternObject.isExactlySimilar(pattern[y][x], source[y][x])) {
          return;
        }
      }
    }
    output.add(pname, sname, patternWidth, patternHeight, 0, 0);
  }

  /**
   * This compareCropped function iterates through the source image and determines if the
   * RGB int values of the corners of the pattern image match the corresponding RGB int values
   * of the source image.  The function then calls comparedCroppedHelper function, which
   * compares the two RGB int array of arrays and checks against the tolerance value.
   *
   * @param patternObj a PatternObject containing the array of ints representing each pixel
   *          in the pattern image as well as the offset between the pattern and source values
   * @param source an Array of RGB int values representing each pixel in the source image
   * @param tolerance an int representing how forgiving the algorithm will be when comparing pixels
   * @param pname the name of the pattern image
   * @param sname the name of the source image
   * @param patternWidth width of the pattern image for output purposes
   * @param patternHeight height of the pattern image for output purposes
   *
   * @return void; class's output object will be updated if there is a match
   */
  public static void compareCropped(PatternObject patternObj, int[][] source, int tolerance,
                                    String pname, String sname, int patternWidth, int patternHeight) {
    int sourceHeight = source.length;
    int sourceWidth = source[0].length;

    for (int sy = 0; sy <= (sourceHeight - patternHeight); sy++) {
      for (int sx = 0; sx <= (sourceWidth - patternWidth); sx++) {
          //check if the corners of the pattern
          //match the corresponding pixels of the source
        if (PatternObject.isSimilar(patternObj.pixels[0][0], source[sy][sx], tolerance) >= 0 &&
          PatternObject.isSimilar(patternObj.pixels[0][(patternWidth-1)], source[sy][sx+(patternWidth-1)], tolerance) >= 0 &&
          PatternObject.isSimilar(patternObj.pixels[(patternHeight-1)][0], source[sy+(patternHeight-1)][sx], tolerance) >= 0 &&
          PatternObject.isSimilar(patternObj.pixels[(patternHeight-1)][(patternWidth-1)],
                                  source[sy+(patternHeight-1)][sx+(patternWidth-1)], tolerance) >= 0) {
          if (compareCroppedHelper(patternObj, source, sy, sx, tolerance, patternWidth, patternHeight)) {
            output.add(pname, sname, patternWidth, patternHeight, sx, sy, patternObj.offset);
          }
        }
      }
    }
  }

  /**
   * The compareCroppedHelper function iterates through the pattern and source int arrays
   * and compares each corresponding pixel
   *
   * @param patternObj a PatternObject containing the array of ints representing each pixel
   *          in the pattern image as well as the offset between the pattern and source values
   * @param source an array of ints representing each pixel in the source image
   * @param sy an int referring to the y index of the source array
   * @param sx an int referring to the x index of the source array
   * @param tolerance an int representing how forgiving the algorithm will be when comparing pixels
   * @param patternWidth width of the pattern image for output purposes
   * @param patternHeight height of the pattern image for output purposes
   *
   * @return boolean. Whether or not the two array's (and therefore images) are identical within the tolerance
   */
  public static boolean compareCroppedHelper(PatternObject patternObj, int[][] source, int sy, int sx,
                                             int tolerance, int patternWidth, int patternHeight) {
    for (int py = 0; py < patternHeight; py++) {
      for (int px = 0; px < patternWidth; px++) {
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
