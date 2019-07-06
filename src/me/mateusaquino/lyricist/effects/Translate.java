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

import me.mateusaquino.lyricist.ApplyEffectEvent;
import me.mateusaquino.lyricist.ImageUtils;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.metaeffects.SupportsMetaEffects;

/**
 * Translate effect (SUPPORTS META)
 * 
 * @author Mateus de Aquino Batista
 * @category Effects
 * @see Slide
 */

public class Translate implements Effect {

	private Position from, to;
	private int delay;
	private int duration;
	
	public Translate(Position to){
		this(null, to, 0, -1);
	}
	
	public Translate(Position from, Position to){
		this(from, to, 0, -1);
	}
	
	public Translate(Position to, int delayInFrames){
		this(null, to, delayInFrames, -1);
	}
	
	public Translate(Position from, Position to, int delayInFrames){
		this(from, to, delayInFrames, -1);
	}
			
	public Translate(Position to, int delayInFrames, int durationInFrames) {
		this(null, to, delayInFrames, durationInFrames);
	}
	
	public Translate(Position from, Position to, int delayInFrames, int durationInFrames){
		this.from = from;
		this.to = to;
		this.delay = delayInFrames;
		this.duration = durationInFrames;
	}

	double xi = Float.NEGATIVE_INFINITY, yi, xe, ye;
	
	@SupportsMetaEffects
	@Override
	public void apply(ApplyEffectEvent event) {
		int time = event.current()-event.start();
		if (duration == -1)
			duration = event.durationInFrames()-delay;
		
		double percent = (time<=delay) ? 0 : (time>delay+duration) ? 1 : 
						(time - delay)/(double)duration;
		percent = event.calcProgress(percent);
		
		if (xi == Float.NEGATIVE_INFINITY) {
			if (from==null)
				from = event.element().getPosition();
		
			xi = ImageUtils.calcX(from, event.projectWidth(), event.projectMargin(), event.frame().getWidth());
			yi = ImageUtils.calcY(from, event.projectHeight(), event.projectMargin(), event.frame().getHeight());
		
			xe = ImageUtils.calcX(to, event.projectWidth(), event.projectMargin(), event.frame().getWidth());
			ye = ImageUtils.calcY(to, event.projectHeight(), event.projectMargin(), event.frame().getHeight()); 
		}
		event.setX((int)(xi+percent*(xe-xi)));
		event.setY((int)(yi+percent*(ye-yi)));
	}
}