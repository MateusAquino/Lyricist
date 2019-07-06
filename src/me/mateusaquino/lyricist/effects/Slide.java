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
import me.mateusaquino.lyricist.Direction;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.metaeffects.SupportsMetaEffects;

/**
 * This effect is a extension of Translate (SUPPORTS META)
 * 
 * @author Mateus de Aquino Batista
 * @category Effects
 * @see Direction
 * @see Translate
 */

public class Slide implements Effect {
	private Translate equivalent;
	private Direction i, e;
	private int d;
	
	/** Uses initial position */
	public Slide(Direction finalLocation){
		this(null, finalLocation, 0);
	}
	
	public Slide(Direction finalLocation, int delayInFrames){
		this(null, finalLocation, delayInFrames);
	}
	
	public Slide(Direction initialLocation, Direction finalLocation){
		this(initialLocation, finalLocation, 0);
	}
	
	public Slide(Direction initialLocation, Direction finalLocation, int delayInFrames){
		i = initialLocation;
		e = finalLocation;
		d = delayInFrames;
	}
	
	@SupportsMetaEffects
	@Override
	public void apply(ApplyEffectEvent event) {
		if (equivalent == null){
			Position i, e;
			int pW = event.projectWidth();
			int pH = event.projectHeight();
			int nW = event.frame().getWidth();
			int nH = event.frame().getHeight();
			
			if (this.i == null)
				i = event.element().getPosition();
			else
				i = Position.ABSOLUTE(f(this.i.x(), pW, nW),
									 f(this.i.y(), pH, nH));
			e = Position.ABSOLUTE(f(this.e.x(), pW, nW),
								  f(this.e.y(), pH, nH));
			
			equivalent = new Translate(i, e, d);	
		}
		equivalent.apply(event);
	}
	
	int f(int x, int p, int n){
		return ((p+n)*(x+1))/2-n;
	} 
}