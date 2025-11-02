package aha.common;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public final class ImageUtil {
    private ImageUtil() { Guards.throwStaticClassInstantiateError(); }

    public static BufferedImage loadImage(String resourcePath) {
        try {
            var url = Objects.requireNonNull(
                ImageUtil.class.getResource(resourcePath),
                "Resource not found: " + resourcePath
            );
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not load image: " + resourcePath,
            	e);
        }
    }

    public final static int dpiScaled(int basePx) {
        var dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        return Math.max(16, (int) Math.round(basePx * dpi / 96.0));
    }

    public final static BufferedImage tintPreserveLuminance(BufferedImage src,
    	Color tint) {
    	
        int w = src.getWidth(), h = src.getHeight();
        var dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        final int tr = tint.getRed(), tg = tint.getGreen(), tb = tint.getBlue();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                int a = (argb >>> 24) & 0xFF;
                if (a == 0) {
                    dst.setRGB(x, y, 0);
                    continue;
                }
                int r = (argb >>> 16) & 0xFF;
                int g = (argb >>> 8) & 0xFF;
                int b = argb & 0xFF;

                int gray = (r + g + b) / 3;
                int nr = (gray * tr) / 255;
                int ng = (gray * tg) / 255;
                int nb = (gray * tb) / 255;

                dst.setRGB(x, y, (a << 24) | (nr << 16) | (ng << 8) | nb);
            }
        }
        return dst;
    }

    /**
     * Make (near-)white pixels transparent. Useful when a logo was exported on 
     * white.
     * @param src       source image (any type)
     * @param threshold 0..255: pixels with all RGB >= threshold become 
     *                  transparent (e.g., 245)
     * @return ARGB image with white knocked out
     */
    public final static BufferedImage knockOutWhite(BufferedImage src,
    	int threshold) {
    	
        int w = src.getWidth(), h = src.getHeight();
        var dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                int a = (argb >>> 24) & 0xFF;
                int r = (argb >>> 16) & 0xFF;
                int g = (argb >>> 8)  & 0xFF;
                int b =  argb         & 0xFF;

                // Hard knockout: near-white → fully transparent
                if (r >= threshold && g >= threshold && b >= threshold) {
                    a = 0;
                }
                dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return dst;
    }

    /**
     * Soft knockout that fades near-white to transparent to avoid hard halos.
     * Pixels lighter than low become fully transparent; darker than high stay
     * opaque; between them alpha is blended linearly.
     * @param src   source image
     * @param low   0..255 (start of fade, e.g., 235)
     * @param high  0..255 (end of fade, e.g., 255), must be >= low
     */
    public final static BufferedImage knockOutWhiteSoft(BufferedImage src, 
    	int low, int high) {
    	
        int w = src.getWidth(), h = src.getHeight();
        if (high < low) throw new IllegalArgumentException("high must be >= low");

        var dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final float span = Math.max(1, high - low); // prevent div-by-zero

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                int a = (argb >>> 24) & 0xFF;
                int r = (argb >>> 16) & 0xFF;
                int g = (argb >>> 8)  & 0xFF;
                int b =  argb         & 0xFF;

                // Use perceived luminance for nicer edges
                int luminance = (int)Math.round(0.2126*r + 0.7152*g + 0.0722*b);

                if (luminance >= high) {
                    a = 0; // fully transparent
                } else if (luminance > low) {
                    float t = (luminance - low) / span; // 0..1
                    int newA = (int)Math.round(a * (1.0 - t)); // fade out
                    a = Math.max(0, Math.min(255, newA));
                }
                dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return dst;
    }

    /**
     * Generic "knock out near color" (e.g., kill a solid background that isn't
     * white).
     * @param src        source image
     * @param bg         background color to remove
     * @param tolerance  0..255: max per-channel distance to treat as background
     *                   (e.g., 15)
     */
    public final static BufferedImage knockOutNearColor(BufferedImage src,
    	Color bg, int tolerance) {
    	
        int w = src.getWidth(), h = src.getHeight();
        var dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int br = bg.getRed(), bgG = bg.getGreen(), bb = bg.getBlue();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                int a = (argb >>> 24) & 0xFF;
                int r = (argb >>> 16) & 0xFF;
                int g = (argb >>> 8)  & 0xFF;
                int b =  argb         & 0xFF;

                if (Math.abs(r - br) <= tolerance &&
                    Math.abs(g - bgG) <= tolerance &&
                    Math.abs(b - bb) <= tolerance) {
                    a = 0;
                }
                dst.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return dst;
    }

    public final static BufferedImage scale(BufferedImage src, int s) { 
    	return scale(src, s, s); }

    public final static BufferedImage scale(BufferedImage src, int w, int h) {
        var out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        var g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        	RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        	RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }
    
}
