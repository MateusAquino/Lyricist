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
import java.util.Arrays;

import me.mateusaquino.lyricist.Center;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.effects.Effect;
import me.mateusaquino.lyricist.effects.Outline;
import me.mateusaquino.lyricist.effects.Shadow;

/**
 * This class is used to recycle Text styles/effects
 * 
 * @author Mateus de Aquino Batista
 * @category Texts
 * @see Text
 */

public class TextBuilder {
	public static final TextBuilder AESTHETIC = new TextBuilder(36, new Color(252, 252, 75), Position.BOT_CENTER,
			new Outline(), new Shadow());

	public TextBuilder(Font font, Position position, Effect... effects) {
		this(font, Center.MIDDLE, Color.WHITE, position, effects);
	}
	
	public TextBuilder(int size, Position position, Effect... effects) {
		this(new Font("Helvetica", Font.PLAIN, size), Center.MIDDLE, Color.WHITE, position, effects);
	}
	
	public TextBuilder(int size, Color color, Position position, Effect... effects) {
		 this(new Font("Helvetica", Font.PLAIN, size), Center.MIDDLE, color, position, effects);
	}
	
	public TextBuilder(Font font, Center horizontalAlign, Color color, Position position, Effect... effects) {
		this.font = font;
		this.color = color;
		this.position = position;
		this.effects = effects;
		this.horizontalAlign = horizontalAlign;
	}
	
	private Font font;
	private Color color;
	private Position position;
	private Effect[] effects;
	private Center horizontalAlign;
	
	public TextBuilder setHorizontalAlign(Center align) {
		horizontalAlign = align;
		return this;
	}
	
	public TextBuilder setPosition(Position position) {
		this.position = position;
		return this;
	}
	
	public TextBuilder setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public TextBuilder setEffects(Effect... effects) {
		this.effects = effects;
		return this;
	}
	
	public TextBuilder setFont(Font font) {
		this.font = font;
		return this;
	}
	
	public Text txt(String text, Effect... effects){
		return new Text(text, font, horizontalAlign, color, position, concat(this.effects, effects));
	}
	
	public Text txt(String text, int size, Effect... effects){
		return new Text(text, new Font(font.getFontName(), font.getStyle(), size), horizontalAlign, color, 
				position, concat(this.effects, effects));
	}
	
	public static Effect[] concat(Effect[] first, Effect[] second) {
		Effect[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}