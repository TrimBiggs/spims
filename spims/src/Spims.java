import java.io.*; //??? not sure
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Spims {

    /**
     * @param args
     */
    public static void main(String[] args) {
	//
	String patternFile = "";
	String sourceFile = "";
	//Single argument given
	// if (args.length < 4) {
	//     //TODO Modify this to have more information for errors
	//     System.err.println("ERROR! ERROR! NOT ENOUGH INPUTS!\n"+
	// 		       "WILL SELF DESTRUCT IN 5 SECONDS!");
	//     return;
 //        }
 //        //TODO Modify to take in more arguments and check validity/flags
	// if (args.length == 4) {
	//     //args[0] should be -p
	//     patternFile = args[1];
	//     //args3 should be -s
	//     sourceFile = args[3];
 //        }
	// //TODO Separate input and output into two if statements to have better error messages
	// if (!(patternFile.toLowerCase().endsWith(".png") ||
	//       patternFile.toLowerCase().endsWith(".gif") ||
	//       patternFile.toLowerCase().endsWith(".jpg")) ||
	//     !(sourceFile.toLowerCase().endsWith(".png") ||
	//       sourceFile.toLowerCase().endsWith(".gif") ||
	//       sourceFile.toLowerCase().endsWith(".jpg"))) {
	//     System.err.println("ERROR! ERROR! WRONG FILE EXTENSION!!!");
	//     return;
	// }
	// BufferedReader patternFile = null;
	// BufferedReader sourceFile = null;

 //        //Multiple arguments given, check for '-r' flag
 //        else if (args.length >= 2) {
 //        	patternFile = args[1];
 //        }
 //        else {
 //        	System.err.println("Invalid command, one or more arguments required.");
 //        	return;
 //        }
 //        try{
	//     if (img1.width == img2.width && img1.height == img2.height){
	// 	//Check if any results, if not call compareScaleUp()
	// 	compareExact(sourceIntArray, patternIntArray);
	//     }
	//     else if ( ){

	//     }


	//     Pixel[][] patternPixelArray = ;
	//     Pixel[][] sourcePixelArray = ;



 //        	//Open FileReader to view file contents
 //                //FileReader fr = new FileReader(inputFile);
 //                //String inputString = new BufferedReader(fr).readLine();
 //                //Call parse function on the input and pass in the reduce flag bit
 //                //String prefix = Prefixer.parseInfix(inputString, reduce);
 //                //Print the input string before and after to the console
 //                System.out.println("result");
 //                //Close FileReader
 //                //fr.close();


	// }
 //        catch (Exception e){
 //                System.err.println("Error: " + e.getMessage());
 //        }

    patternFile = "../../../Desktop/test_2.png";
    sourceFile = "../../../Desktop/test_1.png";

    BufferedImage img = null;
    BufferedImage big = null;

    try{
      img = ImageIO.read(new File(sourceFile));
      big = ImageIO.read(new File(patternFile));
    } catch(IOException e){
      System.err.println(e);
    }

    Pixel[][] pattern = new Pixel[big.getWidth()][big.getHeight()];
    Pixel[][] source = new Pixel[img.getWidth()][img.getHeight()];

    for(int i = 0; i < pattern.length; i++){
      for(int j = 0; j < pattern[0].length; j++){
        pattern[i][j] = new Pixel(big.getRGB(i,j));
      }
    }

    for(int i = 0; i < source.length; i++){
      for(int j = 0; j < source[0].length; j++){
        source[i][j] = new Pixel(img.getRGB(i,j));
        System.out.print(img.getRGB(i,j) + ", ");
      }
      System.out.println("");
    }

    int[] match = compareScaleUp(pattern, source, 10);
    for(int i = 0; i < match.length; i++){
      System.out.println(match[i]);
    }
	}



	//TODO Implement
	//Method to check if pattern matches exactly the base image

	//Call this if same size arrays and arrays within are same length
        public void compareExact(int[][] pattern, int[][] source) {

	}

	//TODO Implement
	//Method to check if patternis a cropped version of source
    public void compareNoResize(int[][] pattern, int[][] source) {

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
          System.out.println("and the next pixel is similar! " + j + ", "  + i);
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
              fillingGap = false;   //but due to match so move on
            }
            else if(!Pixel.isSimilar(source[i][j], pattern[curPatternRow][curPatternCol], tolerance)){
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



    /**
     *THEORY: Like numbers should be near others.
     *30	     36	      20          50
     *30 31 31 36 35 21 20 21 20 49 50
     */















}
