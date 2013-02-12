import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.DataBufferInt;

public class Imaging{
  public static void main(String[] args){
    String imageLoc = "../../../../Desktop/arnold.jpg";
    String imageLoc2= "../../../../Desktop/test.png";
    BufferedImage img = null;
    BufferedImage img2 = null;
    try{
      //img = ImageIO.read(new File(imageLoc));
      img = ImageIO.read(new File(imageLoc2));
      img2 = img.getSubimage(0, 0, 10, 10);
    } catch(IOException e){
      System.err.println(e);
    }

    //dont think this works for png but it's supposedly fast..
    // int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

    int[] pixels1 = new int[img2.getWidth()*img2.getHeight()];
    // System.out.println(pixels1.length);
    img2.getRGB(0, 0, img2.getWidth(), img2.getHeight(), pixels1, 0, img2.getWidth());

    for(int i = 0; i < pixels1.length; i++){
      if(i % img2.getWidth() == 0){
        System.out.println();
      }
      System.out.print(pixels1[i]+"  ");
    }
  }
}