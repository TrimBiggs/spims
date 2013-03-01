import java.util.ArrayList;
import java.util.Iterator;

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

    public void add(String _pat, String _src, int _w, int _h, int _x, int _y){
        myMatches.add(new MatchData(_pat, _src, _w, _h, _x, _y));
        //System.out.println("adding");
    }

    public int size() {
        return myMatches.size();
    }

    //THIS FUNCTION ASSUMES BOTH FOUND PATTERNS ARE THE SAME SIZE.  UPDATE LATER
    //FENCEPOSTING on width and height?
    public void filter(){
        //compare each match to every match that comes after it
       for(int i = 0; i < myMatches.size();){
           MatchData first = myMatches.get(i);
           for(int j = i + 1; j < myMatches.size(); j++){
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
                           myMatches.remove(i);
                           i--;
                           break;
                       }
                   }
               }
           }
       }
    }

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
