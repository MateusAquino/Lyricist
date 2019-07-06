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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import me.mateusaquino.lyricist.effects.Effect;
import me.mateusaquino.lyricist.elements.Audio;
import me.mateusaquino.lyricist.elements.Element;

/**
 * Track used to add elements
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public final class Track {
	private LinkedList<TimedElement> elements = new LinkedList<TimedElement>();
	private LinkedList<TimedAudio> audios = new LinkedList<TimedAudio>();
	
	private Margin margin;
	private float opacity = 1;
	private boolean debugRenders = false;
	
	protected Track(Margin margin){
		this.margin = margin;
	}

	public Sequence addSequence(int initialTime){
		LinkedList<TimedElement> toAdd = new LinkedList<TimedElement>();
		return new Sequence(initialTime, toAdd, elements);
	}
	
	public Track addElement(int start, int duration, Element element) {
		elements.add(build(start, start+duration, element));
		return this;
	}
	
	public Track addAudio(Audio audio){
		return addAudio(0, audio);
	}
	
	public Track addAudio(int start, Audio audio){
		audios.add(build(start, audio));
		return this;
	}
	
	public void setOpacity(float opacity){
		this.opacity = opacity;
	}
	
	public float getOpacity(){
		return opacity;
	}
	
	/** Test transparent images by defining a blue background **/
	public void debug(boolean debugRenders){
		this.debugRenders = debugRenders;
	}
	
	public BufferedImage getFrame(int width, int height, int frameNumber, Margin margin){
		BufferedImage frame = ImageUtils.blank(width, height);
		Graphics2D frame_g2d = frame.createGraphics();
		for (TimedElement elem : elements){
			if (frameNumber>elem.start() && frameNumber<=elem.end()) {
				Element e = elem.element();
				BufferedImage render = e.getRender(width, height, elem.start(), frameNumber, elem.end());

				// Define positions
				int x = ImageUtils.calcX(e.getPosition(), width, margin, render.getWidth());
				int y = ImageUtils.calcY(e.getPosition(), height, margin, render.getHeight());
				
				// Create event values
				ApplyEffectEvent values = 
						ApplyEffectEvent.initiate(render, x, y, elem, frameNumber, width, height, margin);
				
				// Apply Effects
				for (Effect filter : e.getEffects())
						filter.apply(values);
				
				
				render = values.frame();
				x = values.x();
				y = values.y();
				
				if (debugRenders)
					render = ImageUtils.setBG(render, Color.BLUE);
				frame_g2d.drawImage(render, x, y, null);
			}
		}
		frame_g2d.dispose();
		return (opacity==1) ? frame : ImageUtils.opacity(frame, opacity);
	}
	
	
	public Margin getMargin() {
		return margin;
	}
	
	public void setMargin(Margin margin){
		this.margin = margin;
	}
	
	
	/************ UTILITIES ************/
	protected static TimedElement build(int start, int end, Element element){
		return new TimedElement.TimedElementBuilder(start, end, element);
	}
	
	protected static TimedAudio build(int start, Audio audio){
		return new TimedAudio.TimedAudioBuilder(start, audio);
	}
	
	protected static abstract class TimedElement {
		private int start, end;
		private Element element;
		
		private static class TimedElementBuilder extends TimedElement {
			TimedElementBuilder(int start, int end, Element element) {
				super(start, end, element);
		}}
		
		private TimedElement(int start, int end, Element element){
			this.start = start;
			this.end = end;
			this.element = element;
		}
		
		protected int start(){
			return start;
		}
		
		protected int end(){
			return end;
		}
		
		// Clone element
		protected Element element(){
			return element;
		}
	}
	
	protected static abstract class TimedAudio {
		private int start;
		private Audio audio;
		
		private static class TimedAudioBuilder extends TimedAudio {
			TimedAudioBuilder(int start, Audio element) {
				super(start, element);
		}}
		
		private TimedAudio(int start, Audio audio){
			this.start = start;
			this.audio = audio;
		}
		
		protected int start(){
			return start;
		}
		
		protected Audio element(){
			return audio;
		}
	}
}