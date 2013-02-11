import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.DataBufferInt;

public class Imaging{
  public static void main(String[] args){
    String imageLoc = "../../../Desktop/test.png";
    BufferedImage img = null;

    try{
      img = ImageIO.read(new File(imageLoc));
    } catch(IOException e){
      System.err.println(e);
    }

    //dont think this works for png but it's supposedly fast..
    // int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

    int[] pixels1 = new int[img.getWidth()*img.getHeight()];
    // System.out.println(pixels1.length);
    img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels1, 0, img.getWidth());

    for(int i = 0; i < pixels1.length; i++){
      if(i % img.getWidth() == 0){
        System.out.println();
      }
      System.out.print(pixels1[i]);
    }
  }
}