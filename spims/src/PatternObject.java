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
public class PatternObject {

	/**
	*	Constructor for class PatternObject.
	*
	*	@param _image The array of Pixel RGB values that make up the image
	* 	@return PatternObject. Return the newly created PatternObject object
	*/
	public PatternObject(int[][] _pixels){
		pixels = _pixels;
		offset = 0;
	}

	public int[][] pixels;
	public int offset;

	public static int isSimilar(int x, int y, int tolerance){
		int bx = x & 0x0000FF;
		int gx = (x & 0x00FF00) >> 8;
		int rx = (x & 0xFF0000) >> 16;

		int by = y & 0x0000FF;
		int gy = (y & 0x00FF00) >> 8;
		int ry = (y & 0xFF0000) >> 16;

		int rDiff = Math.abs(rx - ry);
		int gDiff = Math.abs(gx - gy);
		int bDiff = Math.abs(bx - by);

		if ((rDiff <= tolerance) && (gDiff <= tolerance) && (bDiff <= tolerance))
			return (rDiff + gDiff + bDiff);
		else
			return -1;
	}
}