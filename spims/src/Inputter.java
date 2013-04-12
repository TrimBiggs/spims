import java.util.HashSet;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
* Class Inputter takes the arguments passed to the main method and validates
* them. Upon successful validation, they are processed into BufferedImage lists.
*
* @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
* @version 0.4
* @since 1/30/2012/
*/
public class Inputter{
    private String p_flag;
    private String s_flag;
    private String p_loc;
    private String s_loc;

    private HashMap<String, BufferedImage> patternImages =
        new HashMap<String, BufferedImage>();
    private HashMap<String, BufferedImage> sourceImages =
        new HashMap<String, BufferedImage>();

    private final HashSet<String> VALID_P_FLAGS = new HashSet<String>(
        Arrays.asList(new String[] {"-p", "-pdir", "--pdir"})
    );
    private final HashSet<String> VALID_S_FLAGS = new HashSet<String>(
        Arrays.asList(new String[] {"-s", "-sdir", "--sdir"})
    );
    private final ImageFilter FILTER = new ImageFilter();

    /**
    *   Constructor for class Inputter.
    *
    *   @param args Array of arguments passed to the main method of Spims
    *       that will be processed for validity.
    */
    public Inputter(String[] args){
        validNumberArgs(args);

        p_flag = args[0];
        s_flag = args[2];
        p_loc = args[1];
        s_loc = args[3];
    }

    /**
    *   Validate flags are correct.
    *   Validate file types are correct.
    *   Populate collections of data.
    */
    public void processInputs(){
        validatePatternFlag();
        validateSourceFlags();

        gatherPatterns();
        gatherSources();

        validateArrays();
    }

    /**
    *   @return The map of BufferedImages to be used as patterns
    */
    public HashMap<String, BufferedImage> getPatterns(){
        return patternImages;
    }

    /**
    *   @return The map of BufferedImages to be used as sources
    */
    public HashMap<String, BufferedImage> getSources(){
        return sourceImages;
    }

    //Private Methods

    /**
    *   Determine if the given flag arguments are valid
    *
    *   @return Whether the pattern flag is one of -p, -pdir, or --pdir and the
    *       source flag is one of -s, -sdir, or --sdir.
    */
    private void validatePatternFlag(){
        if(!VALID_P_FLAGS.contains(p_flag)){
            System.err.println("Invalid flag provided: " + p_flag);
            System.exit(1);
        }
    }

    /**
    *   Validates pattern input and processes it. The class's patternImages
    *   list will be populated during this process.
    */
    private void gatherPatterns(){
        File pattern = new File(p_loc);
        File[] patterns;

        if(!p_flag.equals("-p") && pattern.isDirectory()){
            patterns = pattern.listFiles(FILTER);
            for(File f : patterns){
                addToPatterns(f);
            }
        } else if(!pattern.isDirectory()){
            if(FILTER.accept(pattern)){
                addToPatterns(pattern);
            } else {
                handleInvalidFiles("pattern");
            }
        } else {
            System.err.println(
                "Pattern input does not match pattern flag. Program terminating"
            );
            System.exit(1);
        }
    }

    /**
    *   Helper method for adding images to the pattern collection
    */
    private void addToPatterns(File f){
        String name = f.getName();
        BufferedImage b;

        try{
            b = ImageIO.read(f);
        }catch(Exception e){
            System.err.println("There was an error reading your image.");
            System.exit(1);
            return; //tricking java. program will terminate before it gets here
        }

        patternImages.put( name.substring(name.lastIndexOf("/") + 1), b);
    }

    /**
    *   Helper method for adding images to the source collection
    */
    private void addToSources(File f){
        String name = f.getName();
        BufferedImage b;

        try{
            b = ImageIO.read(f);
        }catch(Exception e){
            System.err.println("There was an error reading your image.");
            System.exit(1);
            return; //tricking java. program will terminate before it gets here
        }

        sourceImages.put( name.substring(name.lastIndexOf("/") + 1), b);
    }

    /**
    *   Validates source input and processes it. The class's soruceImages
    *   list will be populated during this process.
    */
    private void gatherSources(){
        File source = new File(s_loc);
        File[] sources;

        if(!s_flag.equals("-s") && source.isDirectory()){
            sources = source.listFiles(FILTER);
            for(File f : sources){
                addToSources(f);
            }
        } else if(!source.isDirectory()){
            if(FILTER.accept(source)){
                addToSources(source);
            } else {
                handleInvalidFiles("source");
            }
        } else {
            System.err.println(
                "Source input does not match source flag. Program terminating."
            );
            System.exit(1);
        }
    }

    /**
    *   Ensure that the populated arrays are populated
    */
    private void validateArrays(){
        if(patternImages.size() == 0){
            handleInvalidFiles("pattern");
        }

        if(sourceImages.size() == 0){
            handleInvalidFiles("source");
        }
    }

    /**
    *   Determine if the given flag arguments are valid
    *
    *   @return Whether the pattern flag is one of -p, -pdir, or --pdir and the
    *       source flag is one of -s, -sdir, or --sdir.
    */
    private void validateSourceFlags(){
        if(!VALID_S_FLAGS.contains(s_flag)){
            System.err.println("Invalid flag provided: " + s_flag);
            System.exit(1);
        }
    }

    /**
    *   Ensure that there are 4 arguments passed to the class
    *
    *   @param args The args passed to the class
    */
    private void validNumberArgs(String[] args){
        if(args.length != 4) {
            System.err.print(
                "Error: Expected exactly 4 inputs. Got " + args.length
            );
            System.exit(1);
        }
    }

    /**
    *   Gracefully tells the user that there are invalid files and terminates
    *
    *   @param source The input that caused the failure.
    */
    private void handleInvalidFiles(String source){
        System.err.println(
            "Error: Expected " + source + " input(s) of type .png, .jpeg, or .gif."
        );
        System.exit(1);
    }
}