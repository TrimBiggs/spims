import java.util.ArrayList;
import java.util.Iterator;

class MatchData{
    private String pattern;
    private String source;
    private int width;
    private int height;
    private int x;
    private int y;

    public MatchData(String _pat, String _src, int _w, int _h, int _x, int _y){
        pattern = _pat;
        source = _src;
        width = _w;
        height = _h;
        x = _x;
        y = _y;
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

    public void output(){
        //boolean firstline = true;
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
