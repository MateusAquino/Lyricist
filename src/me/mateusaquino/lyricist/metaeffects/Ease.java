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
 * Cubic-Bezier Ease class skeleton
 * 
 * @author Mateus de Aquino Batista
 * @category Meta Effect
 * @see <a href="https://www.desmos.com/calculator/8radcpsprz">
 * 				 https://www.desmos.com/calculator/8radcpsprz</a>
 */
public strictfp abstract class Ease extends MetaEffect implements Cloneable {
	public final Ease IN, OUT, BOTH;
	private boolean in = false, out = false;
	
	public Ease(Effect... effects){
		this.effects = effects;
		try {
			IN = (Ease) this.clone();
			IN.in = true;
			OUT = (Ease) this.clone();
			OUT.out = true;
			BOTH = (Ease) this.clone();
			BOTH.in = true;
			BOTH.out = true;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Uh... basically unreachable code :d");
		}
	}
	
	@Override
	public final void metaApply(ApplyEffectEvent event) {
		event.setProgressAlgorithm(i->calculate(i, in, out));
		for (Effect effect : effects)
			effect.apply(event);
		event.resetProgressAlgorithm();
	}
	
	public abstract CubicBezier in();
	public abstract CubicBezier out();
	public abstract CubicBezier both();
	
	double k = Double.MAX_VALUE;
	
	private strictfp double calculate(double t, boolean in, boolean out){
		double y1, y2;
		CubicBezier c;
		if (in && out)
			c = both();
		else if (in == true)
			c = in();
		else if (out == true)
			c = out();
		else
			throw new IllegalArgumentException("AN EASE EFFECT WAS CREATED BUT NOT APPLIED! USE #.IN #.OUT or #.BOTH");
		
		double x1, x2;
			x1 = c.p1x;	
			x2 = c.p2x;

			double a0 = 1 - 3*x2 + 3*x1;
			double b0 = 3*x2 - 6*x1;
			double c0 = 3*x1;
			double roots[] = solve(a0, b0, c0, -t);
			k = Double.MAX_VALUE;
			for (double v : roots)
				k = (v == 0) ? k : v;
			if (k == Double.MAX_VALUE)
				k = 0;
		// Formula: https://www.desmos.com/calculator/8radcpsprz
		y1 = c.p1y;
		y2 = c.p2y;
		double a1 = 1 - 3*y2 + 3*y1;
		double b1 = 3*y2 - 6*y1;
		double c1 = 3*y1;
		double res = a1*Math.pow(k, 3) + b1*Math.pow(k, 2) + c1*k;
		return res;
	}
	
	protected strictfp class CubicBezier {
		double p1x, p1y, p2x, p2y;
		private CubicBezier(double p1x, double p1y, double p2x, double p2y){
			this.p1x = p1x; this.p1y = p1y; this.p2x = p2x; this.p2y = p2y;
		}
	}
	
	public strictfp CubicBezier cubicBezier(double p1x, double p1y, double p2x, double p2y){
		return new CubicBezier(p1x, p1y, p2x, p2y);
	} 
	
	private strictfp double[] solve(double a, double b, double c, double d){
		if (a == 0.0) {
			if (b == 0.0) {
				if (c == 0.0)
					return new double[] { 0 };
				
				// Solve for linear:
				return new double[]{ -d/c };
			}
							
			// Solve for quadratic:
			double delta = c*c - 4.0*b*d;
				if (delta < 0.0)
					return new double[]{ 0 };
				else {
					double x1 = (-c + Math.sqrt(delta))/2.0*b;
					double x2 = (-c - Math.sqrt(delta))/2.0*b;	
					return new double[] { x1, x2 };
				}
		}
		
		// Solve for cubic:
		double denom = a;
		a = b/denom;
		b = c/denom;
		c = d/denom;

		double adiv3 = a / 3.0;
		double Q = (3*b - a*a) / 9.0;
		double R = (9*a*b - 27*c - 2*a*a*a) / 54.0;
		double delta = Q*Q*Q + R*R;

		if (delta < 0.0) { // 3 R
			double theta = Math.acos (R / Math.sqrt (-(Q*Q*Q)));
			double SQRT_Q = Math.sqrt (-Q);
			double x1 = 2.0 * SQRT_Q * Math.cos (theta/3.0) - adiv3;
			double x2 = 2.0 * SQRT_Q * Math.cos ((theta+2.0*Math.PI)/3.0) - adiv3;
			double x3 = 2.0 * SQRT_Q * Math.cos ((theta+4.0*Math.PI)/3.0) - adiv3;
			return new double[] { x1, x2, x3 };
		} else if (delta > 0.0) { // 1 R
			double SQRT_D = Math.sqrt (delta);
			double S = Math.cbrt (R + SQRT_D);
			double T = Math.cbrt (R - SQRT_D);
			double x1 = (S + T) - adiv3;
			return new double[]{ x1 };
		} else { // 3 R (at least two are equal)
			double CBRT_R = Math.cbrt (R);
			double x1 = 2*CBRT_R - adiv3;
			double x2_3 = CBRT_R - adiv3;
			return new double[] { x1, x2_3, x2_3 };
		}
	}
}