package us.myles_selim.alchemical_brews.utils;

public class ColorUtils {

	public static int invIntsToRGB(int r, int g, int b) {
		int ret = r;
		ret = (ret << 8) + g;
		ret = (ret << 8) + b;
		return ret;
	}

	public static int[] rgbToIndInts(int rgb) {
		int[] ret = new int[3];
		ret[0] = (rgb >> 16) & 0xFF;
		ret[1] = (rgb >> 8) & 0xFF;
		ret[2] = rgb & 0xFF;
		return ret;
	}

	public static float[] rgbToIndFloats(int rgb) {
		float[] ret = new float[3];
		ret[0] = ((rgb >> 16) & 0xFF) / 255.0f;
		ret[1] = ((rgb >> 8) & 0xFF) / 255.0f;
		ret[2] = (rgb & 0xFF) / 255.0f;
		return ret;
	}

}
