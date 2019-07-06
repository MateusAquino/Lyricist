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

import me.mateusaquino.lyricist.effects.Effect;

/**
 * Sine Ease using Cubic-Bezier
 * 
 * @author Mateus de Aquino Batista
 * @category Ease
 */
public final class SinEase extends Ease {
	
	public SinEase(Effect effect) {
		super(effect);
	}

	@Override
	public CubicBezier in() {
		return cubicBezier(0.68, -0.55, 0.265, 1.55);
	}

	@Override
	public CubicBezier out() {
		return cubicBezier(0.68, -0.55, 0.265, 1.55);
	}

	@Override
	public CubicBezier both() {
		return cubicBezier(.87,-0.08,.3,1.2); // TODO: fix values 'cuz i was lazy, it's 00:43 already omg
	}
}