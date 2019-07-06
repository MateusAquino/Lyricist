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

package me.mateusaquino.lyricist.effects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.mateusaquino.lyricist.ApplyEffectEvent;
import me.mateusaquino.lyricist.ImageUtils;

/**
 * This effect simulates a shadow
 * 
 * @author Mateus de Aquino Batista
 * @category Effects
 */
public class Shadow implements Effect {

	private Color color;
	private double opacity;
	private int radius;
	private int offsetY;
	private int offsetX;
	
	public Shadow(){
		this(Color.BLACK, 5, .8, 3, 3);
	}
	
	public Shadow(Color shadowColor){
		this(shadowColor, 5, .8, 3, 3);
	}
	
	public Shadow(Color shadowColor, int radius, double opacity){
		this(shadowColor, radius, opacity, 3, 3);
	}
	
	public Shadow(Color shadowColor, int radius, double opacity, int offsetX, int offsetY){
		this.color = shadowColor;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.radius = radius;
		this.opacity = opacity;
	} 
	
	@Override
	public void apply(ApplyEffectEvent event) {
		event.setFrame(dropShadow(event.frame()));
	}
	
	private BufferedImage dropShadow(BufferedImage img) {
		BufferedImage shadow = ImageUtils.filter(img, (r,g,b,a)->{
			return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
		});
		
	    // Result
	    BufferedImage result = ImageUtils.blank(img.getWidth()+offsetX+radius, img.getHeight()+offsetY+radius);
	    Graphics g = result.getGraphics();

	    // Shadow
	    g.drawImage(shadow, offsetX, offsetY, null);
	    g.dispose();

	    // Shadow's blur
	    result = ImageUtils.blur(result, radius);
	    
	    // Shadow's opacity
	    result = ImageUtils.opacity(result, (float) opacity);
	    
	    // Draw original image
	    g = result.getGraphics();
	    g.drawImage(img, 0, 0, null);
	    g.dispose();
	    
	    return result;
	}	
}