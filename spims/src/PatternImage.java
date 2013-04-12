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
public class PatternImage {

	/**
	*	Constructor for class PatternImage.
	*
	*	@param _image The array of Pixel RGB values that make up the image
	* 	@return PatternImage. Return the newly created PatternImage object
	*/
	public PatternImage(Pixel[][] _pixels){
		pixels = _pixels;
		offset = 0;
	}

	public Pixel[][] pixels;
	public int offset;
}