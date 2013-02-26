import java.io.FileFilter;
import java.io.File;

public class ImageFilter implements FileFilter{
  private String[] imageTypes;

  public ImageFilter(){
    imageTypes = new String[]{"jpg", "png", "gif"};
  }

  public boolean accept(File f){
    for(int i = 0; i < imageTypes.length; i++){
      if(f.getName().toLowerCase().endsWith(imageTypes[i])){ return true; }
    }
    System.err.println("Found invalid input for file " + f.getName());
    System.exit(0);
    return false;
  }
}