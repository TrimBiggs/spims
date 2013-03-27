import java.io.FileFilter;
import java.io.File;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

public class ImageFilter implements FileFilter{
  private String[] imageTypes;

  public ImageFilter(){
    imageTypes = new String[]{"jpg", "png", "gif"};
  }

  public boolean accept(File f){
    //ignore hidden files that might be automatically generated
    if(f.getName().toLowerCase().startsWith(".") ||
       f.getName().endsWith("~")){ return false; }

    Iterator<ImageReader> irs;
    try{
      ImageInputStream iis = ImageIO.createImageInputStream(f);
      irs = ImageIO.getImageReaders(iis);

      while(irs.hasNext()){
        try{
          String ext = irs.next().getFormatName();
          for(int i = 0; i < imageTypes.length; i++){
            if(ext.equals(imageTypes[i])){ return true; }
          }
        }catch(Exception e){ System.err.println(e); }
      }
    }catch (Exception e){
      System.err.println(e);
      e.printStackTrace();
    }

    //if notthing matches exit the program
    System.err.println("Found invalid input for file " + f.getName());
    System.exit(1);
    return false;
  }
}