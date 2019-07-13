/* Lyricist - Lyrics video maker

MIT License

Copyright (c) 2019 Mateus de Aquino Batista

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package me.mateusaquino.lyricist;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import com.jhlabs.image.GaussianFilter;

/**
 * This is a huge pack of methods to modify the image
 * and also reduce the amount of lines in other classes :p
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public final class ImageUtils {
	private ImageUtils(){}
	
	// #-----#--#--#-----# TEXTS #-----#--#--#-----# //
	
	public static BufferedImage drawText(BufferedImage img, String text, Font font, Color color){
		return drawText(img, text, Center.MIDDLE, font, color);
	}
	
	public static BufferedImage drawText(BufferedImage img, String text, Center center, Font font, Color color){
		String[] lines = text.split("\n");
		Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = 0;
        int height = (fm.getHeight()+2)*lines.length;
		for (String txt : lines)
			width = Math.max(width, fm.stringWidth(txt+txt.length()));
		g2d.dispose();

        img = blank(width, height);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(color);
        int s = fm.stringWidth(" ");
        for (int line = 0; line < lines.length; line++){
        	if (center == Center.LEFT)
        		g2d.drawString(lines[line], 0, (fm.getAscent()-2)*(line+1));
        	else if (center == Center.MIDDLE)
        		g2d.drawString(lines[line], (width-fm.stringWidth(lines[line])+s/2)/2, (fm.getAscent()-2)*(line+1));
        	else // Center.RIGHT
        		g2d.drawString(lines[line], width-fm.stringWidth(lines[line]), (fm.getAscent()-2)*(line+1));
        }
        g2d.dispose();
        return img;
	}
	
	// #-----#--#--#-----# IMAGE PROPERTIES #-----#--#--#-----# //
	
	public static BufferedImage setBG(BufferedImage img, Color color) {
		BufferedImage newImg = blank(img.getWidth(), img.getHeight());
		Graphics2D g2d = (Graphics2D) newImg.getGraphics();
		g2d.setColor(color);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		return newImg;
	}
	
	
	/** Replaces all transparent pixels with new color<br>
	 *  If invert is specified, replaces all <b>non</b>-transparent pixels with new color*/
	public static BufferedImage alpha2Color(BufferedImage img, boolean invert, Color newColor) {
		return filter(img, (r,g,b,a) -> {
			if((a == 0 && !invert) || (a != 0 && invert)){
	            r = newColor.getRed();
	            g = newColor.getGreen();
	            b = newColor.getBlue();
	        }
			return new Color(r,g,b,a);
		});
	}
	
	public static BufferedImage blank(int width, int height){
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage concat(BufferedImage img1, BufferedImage img2){
		return concat(img1, img2, 0, 0);
	}
	
	public static BufferedImage concat(BufferedImage img1, BufferedImage img2, int x, int y){
		int w = Math.max(img1.getWidth(), img2.getWidth());
		int h = Math.max(img1.getHeight(), img2.getHeight());
		BufferedImage img = blank(w, h);
		Graphics g = img.getGraphics();
		g.drawImage(img1, 0, 0, null);
		g.drawImage(img2, x, y, null);
		g.dispose();
		return img;
	}
	
	public static BufferedImage makeCopy(BufferedImage img){
		return concat(img, blank(img.getWidth(), img.getHeight()));
	}
	
	// #-----#--#--#-----# EFFECTS #-----#--#--#-----# //
	
	public static BufferedImage opacity(BufferedImage img, float alpha){
		float filteredAlpha = (alpha>1) ? 1 : (alpha<0) ? 0 : alpha;
	    return filter(img, (r,g,b,a)->{
	    	return new Color(r,g,b,(int)(a*filteredAlpha));
	    });
	}
	
	public static BufferedImage blur(BufferedImage image, int radius) {
		if (radius == 0)
			return image;
		
		GaussianFilter filter = new GaussianFilter(radius);
		BufferedImage newFrame = blank(image.getWidth()+radius, image.getHeight()+radius);
		filter.filter(image, newFrame);
		return newFrame;
	}
	
	/** Dilate image (Based on opacity) */
	public static BufferedImage dilate(BufferedImage img, Color strokeColor, int stroke) {
		BufferedImage newImg = blank(img.getWidth(), img.getHeight());
		Graphics2D g2d = (Graphics2D) newImg.getGraphics();
		g2d.setColor(strokeColor);
		
		filter(img, (x, y, r, g, b, a)->{
			if(a > 40)
	            g2d.drawOval(x, y, stroke, stroke);
		});
		
	    g2d.dispose();
	    return newImg; 
	}
	
	/** Outline Image */
	public static BufferedImage outline(BufferedImage frame, Color outlineColor, int stroke){
		BufferedImage newFrame = blank(frame.getWidth()+stroke*2, frame.getHeight()+stroke*2);
		
		Graphics2D g2d = (Graphics2D) newFrame.getGraphics();
		g2d.drawImage(frame, 0, 0, null);
		g2d.dispose();
		
		newFrame = alpha2Color(newFrame, true, Color.BLACK);
		newFrame = dilate(newFrame, outlineColor, stroke);
		
		g2d = (Graphics2D) newFrame.getGraphics();
		if (stroke==1)
			stroke = 2;
		g2d.drawImage(frame, stroke/2, stroke/2, null);
		g2d.dispose();
		return newFrame;
	}

	/** Makes wavy-like image **/
	public static BufferedImage wavy(BufferedImage img, double i, double xMult, double yMult, 
																		double speed, double bend) {
		BufferedImage newImg = blank(img.getWidth(), img.getHeight());
		int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		int[] newPixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		for (double y = 0; y < img.getHeight(); y++)
			for (double x = 0; x < img.getWidth(); x++){
				int color = pixels[(int)y*img.getWidth()+(int)x];
				int newX = (int) x + (int)(Math.sin((bend*y/90.0 + speed*i)/10)*xMult);
				int newY = (int) y + (int)(Math.sin(Math.PI/2.0+(bend*x/90.0 + speed*i)/10)*yMult);
		        
		        try{ 
		        	newPixels[newY*img.getWidth()+newX] = color;
		        } catch(Exception e){
		        }
			}
		
		newImg.setRGB(0, 0, img.getWidth(), img.getHeight(), newPixels, 0, img.getWidth());
	    return newImg; 
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    return dimg;
	}  
	
	public static BufferedImage reposition(BufferedImage img, int newX, int newY) { 
	    BufferedImage dimg = new BufferedImage(img.getWidth()+2*newX, img.getHeight()+2*newY, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(img, newX, newY, null);
	    g2d.dispose();
	    return dimg;
	}
	
	public static BufferedImage resizepos(BufferedImage img, int newX, int newY, int newW, int newH){
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW+2*newX, newH+2*newY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, newX, newY, null);
		g2d.dispose();
		return dimg;
	}
	
	public static BufferedImage subtract(BufferedImage img1, BufferedImage img2) {
		int[][] alpha = new int[img2.getWidth()][img2.getHeight()];
		ImageUtils.filter(img2, (x, y, r, g, b, a) -> {alpha[x][y] = a;});
		return filter(img1, (x, y, r, g, b, a) -> {
			Color cor = Color.BLACK;
			try {
				int alfasub = a - alpha[x][y];
				alfasub = (alfasub<0) ? 0 : alfasub;
				cor = new Color(r,g,b,alfasub);
			} catch(Exception e){
				cor = new Color(r,g,b,a);
			}
			return cor;
		});
	}
	
	// #-----#--#--#-----# CALCULATIONS #-----#--#--#-----# //
	
	/** Calculates X value of Position **/
	public static int calcX(Position pos, int containerWidth, Margin margin, int nodeWidth){
		double x;
		int left = margin.left();
		int right = margin.right();
		int range = containerWidth -nodeWidth -left -right;

		if      (pos == Position.TOP_LEFT   || pos == Position.MID_LEFT   || pos == Position.BOT_LEFT)
			x = left + range * 0.00;
		else if (pos == Position.TOP_CENTER || pos == Position.MID_CENTER || pos == Position.BOT_CENTER)
			x = left + range * 0.50;
		else if (pos == Position.TOP_RIGHT  || pos == Position.MID_RIGHT  || pos == Position.BOT_RIGHT) 
			x = left + range * 1.00;
		else if (pos.getType().equals("ABSOLUTE"))
			x = pos.getX();
		else if (pos.getType().equals("PROPORTIONAL"))
			x = left + range * pos.getX();
		else
			x = 0;
		
		return (int) x;
	}
	
	/** Calculates Y value of Position **/
	public static int calcY(Position pos, int containerHeight, Margin margin, int nodeHeight){
		double y;
		int top = margin.top();
		int bottom = margin.bottom();
		int range = containerHeight -nodeHeight -top -bottom;

		if      (pos == Position.TOP_LEFT || pos == Position.TOP_CENTER || pos == Position.TOP_RIGHT)
			y = top + range * 0.00;
		else if (pos == Position.MID_LEFT || pos == Position.MID_CENTER || pos == Position.MID_RIGHT)
			y = top + range * 0.50;
		else if (pos == Position.BOT_LEFT || pos == Position.BOT_CENTER || pos == Position.BOT_RIGHT) 
			y = top + range * 1.00;
		else if (pos.getType().equals("ABSOLUTE"))
			y = pos.getY();
		else if (pos.getType().equals("PROPORTIONAL"))
			y = top + range * pos.getY();
		else
			y = 0;
		
		return (int) y;
	}
	

	// #-----#--#--#-----# FILTERS #-----#--#--#-----# //
	
	/** Accepts (R, G, B) | no return = no change in color **/
	@FunctionalInterface public interface PixelRGB        { void accept(int r, int g, int b); }
	/** Accepts (R, G, B, A) | no return = no change in color **/
	@FunctionalInterface public interface PixelRGBA       { void accept(int r, int g, int b, int a); }
	/** Accepts (X, Y, R, G, B, A) | no return = no change in color **/
	@FunctionalInterface public interface Pixel           { void accept(int x, int y, int r, int g, int b, int a); }
	/** Accepts (R, G, B) | return = change in color **/
	@FunctionalInterface public interface PixelFilterRGB  { Color accept(int r, int g, int b); }
	/** Accepts (R, G, B, A) | return = change in color **/
	@FunctionalInterface public interface PixelFilterRGBA { Color accept(int r, int g, int b, int a); }
	/** Accepts (X, Y, R, G, B, A) | return = change in color **/
	@FunctionalInterface public interface PixelFilter     { Color accept(int x, int y, int r, int g, int b, int a); }
	
	/** Accepts (X, Y, R, G, B, A) | return = change in color **/
	public static BufferedImage filter(BufferedImage img, PixelFilter filter){
		WritableRaster raster = img.getRaster();
		ColorModel colorModel = img.getColorModel();
		int yoff = 0;
        int off;
        int nbands = raster.getNumBands();
        int[] data = new int[nbands];
        
        int[] pixels = new int[img.getWidth()*img.getHeight()];

        for (int y = 0; y < img.getHeight(); y++, yoff+=img.getWidth()) {
            off = yoff;
            for (int x = 0; x < img.getWidth(); x++) {
            	int rgba[] = argb(colorModel.getRGB(raster.getDataElements(x, y, data)));
                pixels[off++] = filter.accept(x, y, rgba[1], rgba[2], rgba[3], rgba[0]).getRGB();
            }
        }
		
		BufferedImage newImg = blank(img.getWidth(), img.getHeight());
	    newImg.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		return newImg;
	}
	
	public static int[] argb(int rgba){
		int a = (rgba>>24)&255;
        int r = (rgba>>16)&255;
        int g = (rgba>>8)&255;
        int b = (rgba)&255;
        return new int[]{a, r, g, b};
	} 
	
	/** Accepts (R, G, B, A) | return = change in color **/
	public static BufferedImage filter(BufferedImage img, PixelFilterRGB filter){
		return filter(img, (x,y,r,g,b,a)->{return filter.accept(r, g, b);});
	}
	
	/** Accepts (R, G, B, A) | return = change in color **/
	public static BufferedImage filter(BufferedImage img, PixelFilterRGBA filter){
		return filter(img, (x,y,r,g,b,a)->{return filter.accept(r, g, b, a);});
	}
	
	/** Accepts (X, Y, R, G, B, A) | no return = no change in color **/
	public static void filter(BufferedImage img, Pixel filter){
		filter(img, (x,y,r,g,b,a)->{filter.accept(x, y, r, g, b, a); return Color.BLACK;});
	}
	
	/** Accepts (R, G, B, A) | no return = no change in color **/
	public static void filter(BufferedImage img, PixelRGBA filter){
		filter(img, (x,y,r,g,b,a)->{filter.accept(r, g, b, a); return Color.BLACK;});
	}
	
	/** Accepts (R, G, B) | no return = no change in color **/
	public static void filter(BufferedImage img, PixelRGB filter){
		filter(img, (x,y,r,g,b,a)->{filter.accept(r, g, b); return Color.BLACK;});
	}

}