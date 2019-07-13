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
import java.awt.Font;
import java.awt.image.BufferedImage;

import me.mateusaquino.lyricist.Center;
import me.mateusaquino.lyricist.ImageUtils;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.effects.Effect;

/**
 * Text Element
 * 
 * @author Mateus de Aquino Batista
 * @category Texts
 * @see TextBuilder
 */
public class Text implements Element {
	private Position position;
	private Effect[] effects;
	private BufferedImage render;
	private Color color;
	private Font font;
	private String text;
	private Center horizontalAlign;
	
	public Text(String text, int size, Position position, Effect... effects) {
		this(text, new Font("Helvetica", Font.PLAIN, size), position, effects);
	}
	
	public Text(String text, int size, Color color, Position position, Effect... effects) {
		this(text, new Font("Helvetica", Font.PLAIN, size), Center.MIDDLE, color, position, effects);
	}
	
	public Text(String text, Font font, Position position, Effect... effects){
		this(text, font, Center.MIDDLE, Color.WHITE, position, effects);
	}
	
	public Text(String text, Font font, Center horizontalAlign, Color color, Position position, Effect... effects){
		BufferedImage img = ImageUtils.blank(1, 1);
		img = ImageUtils.drawText(img, text, horizontalAlign, font, color);
        
        this.render = img;
        this.position = position;
        this.effects = effects;
        this.color = color;
        this.font = font;
        this.text = text;
        this.horizontalAlign = horizontalAlign;
	}

	public String getText(){
		return text;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Font getFont(){
		return font;
	}
	
	public Center getHorizontalAlign(){
		return horizontalAlign;
	}

	@Override
	public Position getPosition() {
		return position;
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
		return render;
	}
}