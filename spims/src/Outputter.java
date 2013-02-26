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
        return String("" + pattern + " matches " + source +
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
    }

    public void output(){
        boolean firstline = true;
        for(Iterator<MatchData> iter = myMatches.iterator; iter.hasNext();){
            if(!firstline){
                print("\n");
            }
            print(iter.next().toString());
        }
    }
}
