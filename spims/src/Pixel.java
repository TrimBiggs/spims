import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.DataBufferInt;
import java.lang.Math;;

public class Pixel {

    public int r;
    public int g;
    public int b;
    
  public Pixel(int p){
        b = p & 0x0000FF;
        g = (p & 0x00FF00) >> 8;
        r = (p & 0xFF0000) >> 16;
    }


    public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
        return (Math.abs(a.r - b.r) <= tolerance) &&
               (Math.abs(a.g - b.g) <= tolerance) &&
               (Math.abs(a.b - b.b) <= tolerance);
    }


}
