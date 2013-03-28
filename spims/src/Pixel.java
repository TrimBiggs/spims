import java.io.*;

/**
*	Class Pixel holds an rgb value for a pixel in an image.
* It has comparison functions to use with other pixels.
*/
public class Pixel {
	/**
	*	Constructor for class Pixel.
	*
	*	@param p The single int value representing the composite RGB value of the pixel.
	*/
	public Pixel(int p){
		b = p & 0x0000FF;
		g = (p & 0x00FF00) >> 8;
		r = (p & 0xFF0000) >> 16;
		val = p;
	}

	/**
	*	Constructor for class Pixel.
	*
	* @param red R value for the pixel
	* @param green G value for the pixel
	* @param blue B value for the pixel
	*/
	public Pixel(int red, int green, int blue){
		r = red;
		g = green;
		b = blue;
		val = 0;
	}

	/**
	*	Compares two Pixels to see if they are similar enough to be considered the same.
	*
	* @param a The first Pixel in the comparison
	* @param b The second Pixel in the comparison
	* @param tolerance An int representing a discrepancy that can exists between two pixels
	* and still be considered 'similar' enough to be a match of eachother. This is to account
	* for differences between format types and transformations an image may have undergone.
	*/
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