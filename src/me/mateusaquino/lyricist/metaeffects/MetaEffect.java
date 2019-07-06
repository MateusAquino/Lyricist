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

package me.mateusaquino.lyricist.metaeffects;

import me.mateusaquino.lyricist.ApplyEffectEvent;
import me.mateusaquino.lyricist.effects.Effect;

/**
 * Skeleton class for meta effects
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public abstract class MetaEffect implements Effect {

	public abstract void metaApply(ApplyEffectEvent event);
	protected Effect[] effects;
	
	@Override
	public final void apply(ApplyEffectEvent event) {
		for (Effect effect : effects)
			try {
				if(!effect.getClass().getMethod("apply", ApplyEffectEvent.class).isAnnotationPresent(SupportsMetaEffects.class))
					throw new RuntimeException("Class '" + effect.getClass().getSimpleName() + "' doesn't support meta effects!");
			} catch (NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		metaApply(event);
	}
}