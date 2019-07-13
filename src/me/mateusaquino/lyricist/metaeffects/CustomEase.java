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
 * Custom Bezier Ease class
 * <br>If you need help creating your own ease, use 
 * <a href="https://www.desmos.com/calculator/8radcpsprz">this</a> demo and
 * <br>insert the points coords directly into the function. Example:
 * <br><pre><code>{@literal new CustomEase(0.797, 0, 0.98, 0.18, 
 *			new Translate(...))}</code></pre>
 * 
 * @author Mateus de Aquino Batista
 * @category Meta Effect
 * @see <a href="https://www.desmos.com/calculator/8radcpsprz">
 * 				 https://www.desmos.com/calculator/8radcpsprz</a>
 */
public final class CustomEase extends Ease {

	private final CubicBezier cb;
	/** 
	 * Custom Bezier Ease class
	 * <br>If you need help creating your own ease, use 
	 * <a href="https://www.desmos.com/calculator/8radcpsprz">this</a> demo and
	 * <br>insert the points coords directly into the function. Example:
	 * <br><pre><code>{@literal new CustomEase(0.797, 0, 0.98, 0.18, 
	 *			new Translate(...))}</code></pre>
	 * 
	 * @author Mateus de Aquino Batista
	 * @category Meta Effect
	 * @see <a href="https://www.desmos.com/calculator/8radcpsprz">
	 * 				 https://www.desmos.com/calculator/8radcpsprz</a>
	 */
	public CustomEase(double x1, double y1, double x2, double y2, Effect... effects){
		super(effects);
		this.in = true;
		this.out = true;
		cb = cubicBezier(x1, y1, x2, y2);
	}
	
	public static boolean warn = false;
	@Override
	public CubicBezier in() {
		if (!warn) {
			System.err.println("WARNING: With custom ease you dont need to specify #.IN");
			warn = true;
		}
		return both();
	}

	@Override
	public CubicBezier out() {
		if (!warn) {
			System.err.println("WARNING: With custom ease you dont need to specify #.OUT");
			warn = true;
		}
		return both();
	}

	@Override
	public CubicBezier both() {
		return this.cb;
	}
}