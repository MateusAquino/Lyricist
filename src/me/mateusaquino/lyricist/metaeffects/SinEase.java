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
	
	public SinEase(Effect... effects) {
		super(effects);
	}
	
	@Override
	public CubicBezier in() {
		return cubicBezier(0.47, 0, 0.745, 0.715);
	}

	@Override
	public CubicBezier out() {
		return cubicBezier(0.39, 0.575, 0.565, 1);
	}

	@Override
	public CubicBezier both() {
		return cubicBezier(0.445, 0.05, 0.55, 0.95);
	}
}