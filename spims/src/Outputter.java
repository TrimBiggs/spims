import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;


/**
*   Class MatchData is used solely by the Outputter class to hold specific
*   details about a subimage match including names, dimnesions, size, and locations.
*
*   @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*   @version 0.4
*   @since 1/30/2012/
*/
class MatchData{
    public String pattern;
    public String source;
    public int width;
    public int height;
    public int x;
    public int y;
    public int offset;
    public int area;
    public int x2;
    public int y2;

    /**
    *   Constructor for class MatchData. Creates a new MatchData
    *   object with appropriate parameters
    *
    *   @param _pat The name of the pattern image
    *   @param _src The name of the source image
    *   @param _w The width of the match
    *   @param _h The height of the match
    *   @param _x The x location of the upper left pixel in the source image where the match occurs
    *   @param _y The y location of the upper left pixel in the source image where the match occurs
    */
    public MatchData(String _pat, String _src, int _w, int _h, int _x, int _y, int _offset){
        pattern = _pat;
        source = _src;
        width = _w;
        height = _h;
        x = _x;
        y = _y;
        offset = _offset;
        area = width * height;
        x2 = x + width;
        y2 = y + height;
    }

    /**
    *   Returns a string representation of the MatchData object
    *   and the information it contains.
    *
    *   @return A string telling the user what the object represents.
    */
    public String toString(){
        return new String("" + pattern + " matches " + source +
                      " at " + width + "x" + height + "+" + x + "+" + y);
    }
}

/**
*   Public class Outtputer holds MatchData objects representing each
*   match found by the program. It also outputs all the information
*   contained in the MatchData objects when requested to do so.
*/
public class Outputter{
    private ArrayList<MatchData> myMatches;
    private int maxAllowableTolerance = 50;

    /**
    *   Constructor for class Outputter. Takes no arguments, but
    *   initializes an empty ArrayList of type MatchData.
    *
    *   @return void; Creates a new ArrayList of MatchData
    */
    public Outputter(){
        myMatches = new ArrayList<MatchData>();
    }

    /**
    *   Adds a new MatchData object to the class's collection of MatchData.
    *
    *   @param _pat The name of the pattern image
    *   @param _src The name of the source image
    *   @param _w The width of the match
    *   @param _h The height of the match
    *   @param _x The x location of the upper left pixel in the source image where the match occurs
    *   @param _y The y location of the upper left pixel in the source image where the match occurs
    */
    public void add(String _pat, String _src, int _w, int _h, int _x, int _y){
        myMatches.add(new MatchData(_pat, _src, _w, _h, _x, _y, 0));
    }

    /**
    *   Adds a new MatchData object to the class's collection of MatchData.
    *
    *   @param _pat The name of the pattern image
    *   @param _src The name of the source image
    *   @param _w The width of the match
    *   @param _h The height of the match
    *   @param _x The x location of the upper left pixel in the source image where the match occurs
    *   @param _y The y location of the upper left pixel in the source image where the match occurs
    *   @param _offset The sum of all differences between the RGB int values of the pattern and source images
    */
    public void add(String _pat, String _src, int _w, int _h, int _x, int _y, int _offset){
        myMatches.add(new MatchData(_pat, _src, _w, _h, _x, _y, _offset));
    }

    /**
    *   Computes the size of the Outputters ArrayList of MatchData
    *
    *   @return The number of MatchData objects held by this Outputter object
    */
    public int size() {
        return myMatches.size();
    }

    //THIS FUNCTION ASSUMES BOTH FOUND PATTERNS ARE THE SAME SIZE. UPDATE LATER
    /**
    *   Remove matches that are outside of the tolerance.
    *   Remove duplicate matches that overlap one another by at least 50%.
    *
    *   @return void; Bad image matches will be removed from the output array
    */
    public void filter(){
        //compare each match to every following match that has been saved
        ArrayList<Integer> badIndexes = new ArrayList<Integer>();
        for(int matchIndex = 0; matchIndex < myMatches.size(); matchIndex++) {
            MatchData first = myMatches.get(matchIndex);
            first.offset = (first.offset / first.area);
            //Remove match if it is not within the proper allowed tolerance
            if (first.offset > maxAllowableTolerance) {
                badIndexes.add(matchIndex);
            }
            else if (!badIndexes.contains(matchIndex)) {
                for (int nextMatchIndex = matchIndex+1; nextMatchIndex < myMatches.size(); nextMatchIndex++) {
                    MatchData second = myMatches.get(nextMatchIndex);
                    if (first.pattern.equals(second.pattern) && first.source.equals(second.source)) {
                        //find the possible rectangle where the two matches overlap
                        int width = Math.min(first.x2, second.x2) - Math.max(first.x, second.x) + 1;
                        int height = Math.min(first.y2, second.y2) - Math.max(first.y, second.y) + 1;
                        if (width > 0 && height > 0){
                            float crossover = (float)(width * height);
                            second.offset = second.offset / second.area;
                            //is it greater than half the area of the first match?
                            if (crossover > .5 * ((float) first.area)) {
                                //get rid of the first match
                                if (first.offset < second.offset) {
                                    if (!badIndexes.contains(matchIndex)) {
                                        badIndexes.add(matchIndex);
                                        break;
                                    }
                                }
                                else if (!badIndexes.contains(nextMatchIndex)) {
                                    badIndexes.add(nextMatchIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
        Set<Integer> set = new HashSet<Integer>(badIndexes);
        for(int i = myMatches.size() - 1; i >= 0; i--) {
            if (badIndexes.contains(i))
                myMatches.remove(i);
        }
        /*for(Iterator<MatchData> iter = myMatches.iterator(); iter.hasNext();){
            MatchData first = iter.next();
            for(Iterator<MatchData> secondIter = (Iterator<MatchData>) iter.clone(); secondIter.hasNext();){
                MatchData second = secondIter.next();
                if(first.pattern.equals(second.pattern) && first.source.equals(second.source)){
                    //find the (possibly non-existant) rectangle where the two matches overlap
                    int width = min(first.x2, second.x2) - max(first.x, second.x) + 1;
                    int height = min(first.y2, second.y2) - max(first.y, second.y) + 1;
                    if(width > 0 && height > 0){
                        float crossover = (float)(width * height);
                        //is it greater than half the size of the first match?
                        if(crossover > .5 * ((float) first.size)){
                            //get rid of the first match
                            iter.remove();
                            break;
                        }
                    }
                }
            }
        }*/
    }

    /**
    *   Print information held by each MatchData object collected by this Outputter to the console.
    */
    public void output(){
        //boolean firstline = true;
        filter();
        int i = 0;
        while(i < myMatches.size()){
            System.out.println(myMatches.get(i).toString());
            i++;
            //System.out.println(myMatches.get(i).offset);
        }
/*        for(Iterator<MatchData> iter = myMatches.iterator(); iter.hasNext();){
            if(!firstline){
                System.out.println();
            }
            System.out.println(iter.next().toString());
        }*/
    }
}
