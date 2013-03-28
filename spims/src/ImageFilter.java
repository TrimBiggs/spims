import java.io.FileFilter;
import java.io.File;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

/**
* Class ImageFilter is FileFilter used to determine if input files
* are of valid types.
*/
public class ImageFilter implements FileFilter{
  private final String[] IMAGE_TYPES = new String[]{"jpg", "png", "gif", "jpeg"};

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

          //Check if format type is in the given array.
          for(int i = 0; i < IMAGE_TYPES.length; i++){
            if(ext.toLowerCase().equals(IMAGE_TYPES[i])) { return true; }
          }
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