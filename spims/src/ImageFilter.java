import java.io.FileFilter;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Arrays;

/**
* Class ImageFilter is FileFilter used to determine if input files
* are of valid types.
*
*   @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*   @version 1.0
*   @since 1/30/2012/
*
*/

public class ImageFilter implements FileFilter{
  private final HashSet<String> IMAGE_TYPES = new HashSet<String>(
    Arrays.asList(new String[] {"png", "gif", "jpeg"})
  );

  /**
  * Contrusctor for class ImageFilter.
  */
  public ImageFilter(){
  }

  /**
  * Takes a file and gets the content type of that file to check for validity in the program.
  *
  * @param f The input File that is being checked for an appropraite type.
  * @return true or false, whether or not the File is of type jpeg, png, or gif.
  */
  public boolean accept(File f){
    //ensure that the file exists in the system
    if(!f.exists()){
      System.err.println("Given input " + f.getName() + " does not exist. Program will terminate.");
      System.exit(1);
      return false;
    }

    //ensure the file is not a subdirectory
    if(f.isDirectory()){
      System.err.println("Given input contained a subdirectory. Program will terminate.");
      System.exit(1);
      return false;
    }

    //ignore hidden files that might be automatically generated
    if(f.getName().toLowerCase().startsWith(".") ||
       f.getName().endsWith("~")){ return false; }

    Iterator<ImageReader> irs;
    try{
      //Create ImageReaders for the image file
      ImageInputStream iis = ImageIO.createImageInputStream(f);
      irs = ImageIO.getImageReaders(iis);

      while(irs.hasNext()){
        try{
          //Get the format of ImageInputStream
          String ext = irs.next().getFormatName();

          //Check if format type is in the given collection.
          if(IMAGE_TYPES.contains(ext.toLowerCase()) ) { return true; }
        }catch(Exception e){ System.err.println("There was an error retrieving the format of the file. Program will terminate."); }
      }
    }catch (Exception e){
      System.err.println("There was an error retrieving the format of the file. Program will terminate.");
    }

    //if notthing matches exit the program
    System.err.println("Found invalid input for file " + f.getName());
    System.exit(1);
    return false;
  }
}
