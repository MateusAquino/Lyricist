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

package me.mateusaquino.lyricist.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import me.mateusaquino.lyricist.ImageUtils;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.effects.Effect;

/**
 * Simple solid-render element
 * 
 * @author Mateus de Aquino Batista
 * @category Solids
 */
public final class Solid implements Element {

	private Color color;
	private Rectangle rect;
	private Effect[] effects;
	
	public Solid(Effect... effects){
		this(Color.BLACK, effects);
	}
	
	public Solid(Color color, Effect... effects){
		this(color, 0, 0, 0, 0, effects);
	}
	
	public Solid(Color color, int x, int y, int width, int height, Effect... effects){
		this(color, new Rectangle(x, y, width, height), effects);
	}
	
	public Solid(Color color, Rectangle rect, Effect... effects){
		this.color = color;
		this.rect = rect;
		this.effects = effects;
	}
	
	@Override
	public Position getPosition() {
		return Position.TOP_LEFT;
	}

	@Override
	public Effect[] getEffects() {
		return effects;
	}
	
	@Override
	public void setEffects(Effect... effects) {
		this.effects = effects;
	}

	@Override
	public BufferedImage getRender(int screenWidth, int screenHeight, int start, int current, int end) {
		double x = rect.getX();
		double y = rect.getY();
		double width = rect.getWidth();
		double height= rect.getHeight();
		if (width==0)
			width = screenWidth;
		if (height==0)
			height = screenHeight;		
		rect.setRect(x, y, width, height);
		BufferedImage shape = ImageUtils.blank(screenWidth, screenHeight);
		Graphics2D g2d = (Graphics2D) shape.getGraphics();
		g2d.setColor(color);
		g2d.fill(this.rect);
		g2d.dispose();
		return shape;
	}

}