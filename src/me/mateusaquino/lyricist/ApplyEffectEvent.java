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

import java.awt.image.BufferedImage;

import me.mateusaquino.lyricist.Track.TimedElement;
import me.mateusaquino.lyricist.elements.Element;

/**
 * Event with properties needed to
 * customize/create an effect.
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements/Event
 */

public interface ApplyEffectEvent {
	Element element();
	TimedElement timed();
	BufferedImage frame();
	int x();
	int y();
	int initialX();
	int initialY();
	int start();
	int current();
	int end();
	int durationInFrames();
	void setX(int x);
	void setY(int y);
	void setFrame(BufferedImage frame);
	
	int projectWidth();
	int projectHeight();
	Margin projectMargin();
	
	// For meta-compatible effects
	
	/** Used by effects compatible with meta effects
	 * <br>the input is the original percentage,
	 *     and the result will be the new one after the maths **/
	double calcProgress(double percentInput);
	
	/** Used by effects compatible with meta effects 
	 * @see {@link ApplyEffectEvent#calcProgress(double)} 
	 * **/
	double calcProgress(int start, int current, int end);
	
	/** Used by effects compatible with meta effects 
	 *  <br> Takes the algorithm to convert the progress value 
	 *  @see ProgressAlgorithm **/
	void setProgressAlgorithm(ProgressAlgorithm alg);
	
	/** Used by effects compatible with meta effects 
	 * <br> re-defines the algorithm to <code>x -> x</code> (linear progress)**/
	void resetProgressAlgorithm();
	

	
	/**
	 * A functional interface used for meta-compatible effects
	 * to calculate the <b>new</b> progress value.
	 * <br>
	 * A linear algorithm, for example, can be expressed as
	 * (x -> x), or you can use {@link ApplyEffectEvent#resetProgressAlgorithm()}
	 * 
	 * @author Mateus de Aquino Batista
	 * @category Main Elements/Event
	 */
	@FunctionalInterface
	public interface ProgressAlgorithm {
		public double calcProgress(double percentInput);
	}
	
	static ApplyEffectEvent initiate(BufferedImage iFrame, int iX, int iY, TimedElement el,
									int current, int projW, int projH, Margin projM){
		return new ApplyEffectEvent() {
			BufferedImage frame = iFrame;
			int y = iY, x = iX;
			ProgressAlgorithm progressAlgorithm = x -> x;
			
			public void setY(int y) {
				this.y = y;
			}
			public void setX(int x) {
				this.x = x;
			}
			public void setFrame(BufferedImage frame) {
				this.frame = frame;
			}
			public int y() {
				return y;
			}
			public int x() {
				return x;
			}
			public int initialX(){
				return iX;
			}
			public int initialY(){
				return iY;
			}
			public int start() {
				return el.start();
			}
			public int projectWidth() {
				return projW;
			}
			public Margin projectMargin() {
				return projM;
			}
			public int projectHeight() {
				return projH;
			}
			public BufferedImage frame() {
				return frame;
			}
			public int end() {
				return el.end();
			}
			public TimedElement timed(){
				return el;
			}
			public Element element() {
				return el.element();
			}
			public int durationInFrames() {
				return el.end()-el.start();
			}
			public int current() {
				return current;
			}
			public double calcProgress(double percentInput){
				return progressAlgorithm.calcProgress(percentInput);
			}
			public double calcProgress(int start, int current, int end){
				return progressAlgorithm.calcProgress((current-start)/(double)(end-start));
			}
			public void setProgressAlgorithm(ProgressAlgorithm alg){
				progressAlgorithm = alg;
			}
			public void resetProgressAlgorithm(){
				progressAlgorithm = x -> x;
			}
		};
	}
}