import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

/**
*
*   @author Corey Hanson, Tim Briggs, Reed Lockwood, Wen Cao
*   @version 0.4
*   @Since 1/30/2012/
*
*/

class MatchData{
    public String pattern;
    public String source;
    public int width;
    public int height;
    public int x;
    public int y;
    public int size;
    public int x2;
    public int y2;

    public MatchData(String _pat, String _src, int _w, int _h, int _x, int _y){
        pattern = _pat;
        source = _src;
        width = _w;
        height = _h;
        x = _x;
        y = _y;
        //FENCEPOSTING?
        size = (width + 1) * (height + 1);
        x2 = x + width;
        y2 = y + height;
    }

    public String toString(){
        return new String("" + pattern + " matches " + source +
                      " at " + width + "x" + height + "+" + x + "+" + y);
    }
}

public class Outputter{
    private ArrayList<MatchData> myMatches;

    public Outputter(){
        myMatches = new ArrayList<MatchData>();
    }

/**
* This function adds needed return information for the new match onto the output object
* @param _pat The patten image name
* @param _src The source image name
* @param _w   The width in pixels of the matched area
* @param _h   The height in pixels of the matched area
* @param _x   The x-axis value of where the images match
* @param _y   The y-axis value of where the images match
* @return Null. Function returns nothing, it just add the new matching to outputter object
*/    
    public void add(String _pat, String _src, int _w, int _h, int _x, int _y){
        myMatches.add(new MatchData(_pat, _src, _w, _h, _x, _y));
        //System.out.println("adding");
    }

/**
* This funciton checks the total number of atches we have in the output object
* @return integer Returns the number of matches in the result outputter object
*/
    public int size() {
        return myMatches.size();
    }

 
/**
* This function removes duplicated matches from the outputter object
* @return Null
*/    
    public void filter(){
        //compare each match to every match that comes after it
        ArrayList badIndexes = new ArrayList();
        for(int i = 0; i < myMatches.size(); i++) {
            MatchData first = myMatches.get(i);
            if (!badIndexes.contains(i)){
                for(int j = i+1; j < myMatches.size(); j++) {
                    MatchData second = myMatches.get(j);
                    if(first.pattern.equals(second.pattern) && first.source.equals(second.source)){

                        //find the (possibly non-existant) rectangle where the two matches overlap
                        int width = Math.min(first.x2, second.x2) - Math.max(first.x, second.x) + 1;
                        int height = Math.min(first.y2, second.y2) - Math.max(first.y, second.y) + 1;
                        if(width > 0 && height > 0){
                            float crossover = (float)(width * height);
                            //is it greater than half the size of the first match?
                            if(crossover > .5 * ((float) first.size)){
                                //get rid of the first match
                                if (!badIndexes.contains(j))
                                    badIndexes.add(j);
                            }
                        }
                    }
                }
            }
        }
        Set set = new HashSet(badIndexes);
        for(int i = myMatches.size() - 1; i >= 0; i--){
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
* This function goes through each match in the outputter object. It outputs each match
* to a new line.w
* @return Null
*/
    public void output(){
        //boolean firstline = true;
        filter();
        int i = 0;
        while(i < myMatches.size()){
            System.out.println(myMatches.get(i).toString());
            i++;
        }
/*        for(Iterator<MatchData> iter = myMatches.iterator(); iter.hasNext();){
            if(!firstline){
                System.out.println();
            }
            System.out.println(iter.next().toString());
        }*/
    }
}
