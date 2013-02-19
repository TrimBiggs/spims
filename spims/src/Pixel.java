import java.io.*;

public class Pixel {
	public Pixel(int p){
		b = p & 0x0000FF;
		g = (p & 0x00FF00) >> 8;
		r = (p & 0xFF0000) >> 16;
		val = p;
	}
	public Pixel(int red, int green, int blue){
		r = red;
		g = green;
		b = blue;
		val = 0;
	}

	public static boolean isSimilar(Pixel a, Pixel b, int tolerance){
		return (Math.abs(a.r - b.r) <= tolerance) &&
		(Math.abs(a.g - b.g) <= tolerance) &&
		(Math.abs(a.b - b.b) <= tolerance);
	}

	public static Pixel getQuotient(Pixel a, int scale) {
		return new Pixel(a.r / scale, a.g / scale, a.b / scale);
	}

	public static boolean getBetween(Pixel a1, Pixel a2, Pixel b, int tolerance) {
		return (((a1.r <= b.r && b.r <= a2.r) || (a1.r >= b.r && b.r >= a2.r)) ||
				((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance))) &&
			   (((a1.g <= b.g && b.g <= a2.g) || (a1.g >= b.g && b.g >= a2.g)) ||
			    ((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance))) &&
			   (((a1.b <= b.b && b.b <= a2.b) || (a1.b >= b.b && b.b >= a2.b)) ||
			   	((Math.abs(a1.r - b.r) <= tolerance) || (Math.abs(a2.r - b.r) <= tolerance)));
	}

	public int r;
	public int g;
	public int b;
	public int val;
}