import java.io.*; //??? not sure

public class Pixel {
	public Pixel(int p){
		b = p & 0x0000FF;
		g = (p & 0x00FF00) >> 8;
		r = (p & 0xFF0000) >> 16;
	}

	public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
		return (abs(a.r - b.r) <= tolerance) &&
		(abs(a.g - b.g) <= tolerance) &&
		(abs(a.b - b.b) <= tolerance);
	}

	public int r;
	public int g;
	public int b;
}

public class Spims {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	String patternFile = "";
    	String sourceFile = "";

		//Less than apprpriate number of arguments given                                                                                                                                                                                                    
    	if (args.length < 4) {
    		System.err.println("Error: Expected at least 4 inputs.\n"+
    			"Got " + args.length + ": " + args);
    		return;
    	}
        //TODO Modify to take in more arguments and check validity/flags
    	if (args[0].equals("-p")) {

    	}
    	//Do a search on the '-p' and '-s' flags
    	if (args.length == 4) {
	    //args[0] should be -p
    		patternFile = args[1];
	    //args[2] should be -s
    		sourceFile = args[3];
    	}
		//TODO Separate input and output into two if statements to have better error messages 
    	if (!(patternFile.toLowerCase().endsWith(".png") ||
    		  patternFile.toLowerCase().endsWith(".gif") ||
    		  patternFile.toLowerCase().endsWith(".jpg"))) {
    		System.err.println("Error: Expected pattern input with extension .png, .jpg, or .gif.\n"+
    			"Got " + patternFile);
    		return;
    	}
    	if (!(sourceFile.toLowerCase().endsWith(".png") ||
    		  sourceFile.toLowerCase().endsWith(".gif") ||
    		  sourceFile.toLowerCase().endsWith(".jpg"))) {
    		System.err.println("Error: Expected source input with extension .png, .jpg, or .gif.\n"+
    			"Got " + sourceFile);
    		return;
    	}
    
    	BufferedReader patternImg = null;
    	BufferedReader sourceImg = null;

    	try{
    		patternImg = ImageIO.read(new File(patternFile));
    		sourceImg = ImageIO.read(new File(sourceFile));
    		//TODO: Modify this to be Array of Arrays
    		Pixel[][] patternPixels = new Pixel[patternImg.getWidth()*(new Pixel[patternImg.getHeight])];
    		Pixel[][] sourcePixels = new Pixel[sourceImg.getWidth()*(new Pixel[sourceImg.getHeight])];

    	// System.out.println(pixels1.length);
   			patternImg.getRGB(0, 0, img2.getWidth(), patternFile.getHeight(), 
    			pixels1, 0, patternFile.getWidth());
    		sourceImg.getRGB(0, 0, sourceImg.getWidth(), sourceImg.getHeight(),
    			sourcePixels, 0, sourceImg.getWidth());

    	for(int i = 0; i < pixels1.length; i++){
    		if(i % img2.getWidth() == 0){
    			System.out.println();
    		}
    		System.out.print(pixels1[i]+"  ");
    	}
    	} catch(IOException e){
    		System.err.println(e);
    	}

    // dont think this works for png but it's supposedly fast..
    // int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

    	

    	try{
    		if (img1.width == img2.width && img1.height == img2.height){
		//Check if any results, if not call compareScaleUp()
    			compareExact(sourceIntArray, patternIntArray);
    		}
    		else if ( ){

    		}

    		Pixel[][] patternPixelArray = ;
    		Pixel[][] sourcePixelArray = ;

    		System.out.println("result");

    	}
    	catch (Exception e){
    		System.err.println("Error: " + e.getMessage());
    	}
    }



	//TODO Implement
	//Method to check if pattern matches exactly the base image

	//Call this if same size arrays and arrays within are same length
    public void compareExact(int[][] pattern, int[][] source) {

    }

	//TODO Implement
	//Method to check if patternis a cropped version of source
    public void compareNoResize(int[][] pattern, int[][] source) {	

    }

	//TODO Implement
	//Else - LOOOOONG check
    public void compareScaleUp(int[][] pattern, int[][] source, int tolerance) {

    }

    public void compareScaleDown(Pixel[][] pattern, Pixel[][] source, int tolerance) {

    }



    /**
     *THEORY: Like numbers should be near others. 
     *30	 36	  20          50
     *30 31 31 36 35 21 20 21 20 49 50
     */















}
