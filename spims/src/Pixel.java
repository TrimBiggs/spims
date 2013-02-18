public class Pixel {
    public Pixel(int p){
        b = p & 0x0000FF;
        g = (p & 0x00FF00) >> 8;
        r = (p & 0xFF0000) >> 16;
        val = p;
    }

    public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
        return (Math.abs(a.r - b.r) <= tolerance) &&
               (Math.abs(a.g - b.g) <= tolerance) &&
               (Math.abs(a.b - b.b) <= tolerance);
    }

    public int r;
    public int g;
    public int b;
    public int val;
}