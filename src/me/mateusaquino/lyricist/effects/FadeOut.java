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
import me.mateusaquino.lyricist.metaeffects.SupportsMetaEffects;

/**
 * Simple fade out effect (SUPPORTS META)
 * 
 * @author Mateus de Aquino Batista
 * @category Effects
 * @see FadeIn
 */
public class FadeOut implements Effect {

	private int duration;  
	public FadeOut(int duration){
		this.duration = duration;
	}
	
	@SupportsMetaEffects
	@Override
	public void apply(ApplyEffectEvent event) {
		int time = event.current()-event.start();
		if (time < event.end()-event.start()-duration)
			return;
		
		double opacity = (event.end()-event.start()-time)/(double) duration;
		opacity = event.calcProgress(opacity);
		
		event.setFrame(ImageUtils.opacity(event.frame(), (float) opacity));
	}
}