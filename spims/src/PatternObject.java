import java.io.*;

/**
*    Class PatternObject holds the int array of arrays that represent
*    a pattern image, as well as the offset between the pattern and source.
* It has comparison functions to determine if RGB int values match.
*
* @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*	@version 0.4
*	@since 1/30/2012/
*
*/
public class PatternObject {

  /**
	* Public variables to be accessed by the program
	*
	* @param pixels The RGB int values that make up an image
	* @param offset The sum of all difference in RGB values between the pattern
	*               image and source image. 
	*/
	public int[][] pixels;
	public int offset;

	/**
	*	Constructor for class PatternObject.
	*
	*	@param _pixels The array of RGB int values that make up the image
	* @return PatternObject. Return the newly created PatternObject object
	*/
	public PatternObject(int[][] _pixels) {
		pixels = _pixels;
		offset = 0;
	}

	/**
	*	Compares two RGB int values to see if they are the exact same value
	*
	*	@param p The RGB int value of a pixel from the pattern image 
	* @param s The RGB int value of a pixel from the source image
	* @return boolean. Return true if the RGB values match exactly
	*/
	public static boolean isExactlySimilar(int p, int s) {
		return (p == s);
	}

	/**
	*	Compares two RGB int values to see if they are the exact same value
	*
	*	@param p The RGB int value of a pixel from the pattern image 
	* @param s The RGB int value of a pixel from the source image
	* @param tol The acceptable difference between the two RGB int values p and s
	* @return int. Return the positive difference between p and s if within the tolerance. 
	*              Otherwise return -1.
	*/
	public static int isSimilar(int p, int s, int tol) {
		int bp =  p & 0x0000FF;
		int gp = (p & 0x00FF00) >> 8;
		int rp = (p & 0xFF0000) >> 16;

		int bs =  s & 0x0000FF;
		int gs = (s & 0x00FF00) >> 8;
		int rs = (s & 0xFF0000) >> 16;

		int rDiff = Math.abs(rp - rs);
		int gDiff = Math.abs(gp - gs);
		int bDiff = Math.abs(bp - bs);

		if ((rDiff <= tol) && (gDiff <= tol) && (bDiff <= tol))
			return (rDiff + gDiff + bDiff);
		else
			return -1;
	}
}