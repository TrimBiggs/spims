import java.io.*;

/**
*    Class Pixel holds an rgb value for a pixel in an image.
* It has comparison functions to use with other pixels.
*
*   @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*	@version 0.4
*	@since 1/30/2012/
*
*/
public class Pixel {
	/**
	*	Constructor for class Pixel.
	*
	*	@param p The single int value representing the composite RGB value of the pixel.
	* 	@return Pixel. This is a constructor, it builds a Pixel object
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
	* 	@param red R value for the pixel
	* 	@param green G value for the pixel
	* 	@param blue B value for the pixel
	*	@return Pixel. Return a Pixel object with the given RGB value
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
	* @param tolerance.	An int representing a discrepancy that can exists between two pixels
	* 					and still be considered 'similar' enough to be a match of eachother. This is to account
	* 					for differences between format types and transformations an image may have undergone.
	* @return boolean. 	Function returns true is the two Pixel objects have differences less
	*					then the tolerance number. Returns false otherwise.
	*/
	public static int isSimilar(Pixel a, Pixel b, int tolerance){
		int rDiff = Math.abs(a.r - b.r);
		int gDiff = Math.abs(a.g - b.g);
		int bDiff = Math.abs(a.b - b.b);
		if ((rDiff <= tolerance) && (gDiff <= tolerance) && (bDiff <= tolerance))
			return (rDiff + gDiff + bDiff);
		else
			return -1;
	}


	/**
	*
	* @param a	The given Pixel object that we getting the quotient for
	* @param scale The integer values used to get the quotient.
	* @return Pixel.	Function returns a pixel project by scaling down the RGB value
	*					of the given pixel
	*
	*/
	public static Pixel getQuotient(Pixel a, int scale) {
		return new Pixel(a.r / scale, a.g / scale, a.b / scale);
	}


	/**
	*
	* @param a1	First given Pixel object, which is used as the lower bound.
	* @param a2	Second given Pixel object, which is used as the upper bound.
	* @param b 	Given Pixel object. This is the one we are trying to see if it's between
	*			of a1 and a 2
	* @param tolerance	A given integer. We make sure the differences for all RBG colors
	*					between b and a1/a2 are less than the tolerances
	* @return boolean 	Return true if Pixel b is in between Pixel a1 and a2. And the
	*					The differences between the pixels are less than the tolerance.
	*					Return false otherwise.
	*/
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
