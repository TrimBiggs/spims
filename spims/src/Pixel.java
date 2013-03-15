import java.io.*;

public class Pixel {
	public Pixel(int p){
		b = p & 0x0000FF;
		g = (p & 0x00FF00) >> 8;
		r = (p & 0xFF0000) >> 16;
		val = p;
	}
	public Pixel(int red, int green, int blue){
		r = red;
		g = green;
		b = blue;
		val = 0;
	}

    //ASSERT: left must be less than right, top must be less than bottom, and all must be within the dimensions of Pixel[][] other
    //INDEX OUT OF BOUNDS ERROR - if you're getting one in this function, it might be because a float was rounded into
    //an out-of-bounds integer.  I would advise subtracting a very small number from top and right if this happens
    public static boolean isScaledSimilar(Pixel a, float left, float top, float right, float bottom, Pixel[][] other, int tolerance){
       
        float red = 0;
        float green = 0;
        float blue = 0;

        //remember that "bottom" is a higher y value than "top"
        float pixelArea = (right - left) * (bottom - top);

        for(float y = Math.floor(top); y < bottom; y++){
            for(float x = Math.floor(left); x < right; x++){

                float width = Math.min(x + 1, right) - Math.max(x, left);
                float height = Math.min(y + 1, bottom) - Math.max(y, top);
                float overlap = width * height;
                float weight = overlap / pixelArea
                
                Pixel current = other[(int) x][(int) y];

                red += weight * current.r;
                green += weight * current.g;
                blue += weight * current.b;
            {
        }

        return isSimilar(a, Pixel((int) (red + .5), (int) (green + .5), (int) (blue + .5)), tolerance);
    }

	public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
		return (Math.abs(a.r - b.r) <= tolerance) &&
		(Math.abs(a.g - b.g) <= tolerance) &&
		(Math.abs(a.b - b.b) <= tolerance);
	}

	public static Pixel getQuotient(Pixel a, int scale) {
		return new Pixel(a.r / scale, a.g / scale, a.b / scale);
	}

	public static boolean getBetween(Pixel a1, Pixel a2, Pixel b, int tolerance) {
		return (((a1.r <= b.r && b.r <= a2.r) || (a1.r >= b.r && b.r >= a2.r)) ||
				((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance))) &&
			   (((a1.g <= b.g && b.g <= a2.g) || (a1.g >= b.g && b.g >= a2.g)) ||
			    ((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance))) &&
			   (((a1.b <= b.b && b.b <= a2.b) || (a1.b >= b.b && b.b >= a2.b)) ||
			   	((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance)));
	}

	public int r;
	public int g;
	public int b;
	public int val;
}
