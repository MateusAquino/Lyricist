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

/**
 * This effect simulates waves
 * 
 * @author Mateus de Aquino Batista
 * @category Effects
 */
public class Wave implements Effect {

	private double intensity, speed, angle, bend;
	
	public Wave(){
		this(4);
	}
	
	public Wave(double intensity){
		this(intensity, 4);
	}
	
	public Wave(double intensity, double speed){
		this(intensity, speed, 0);
	}
	
	public Wave(double intensity, double speed, double angle){
		this(intensity, speed, angle, 12);
	}
	
	public Wave(double intensity, double speed, double angle, double bend){
		this.intensity = intensity;
		this.speed = speed;
		this.angle = angle;
		this.bend = bend*10;
	}
	
	@Override
	public void apply(ApplyEffectEvent event) {
		double i = intensity;
		double g = (i+i*Math.sin((angle+45)/90*Math.PI))/2;
		double xMult = g;
		double yMult = i - g;
		
		event.setFrame(ImageUtils.wavy(event.frame(), event.current(), xMult, yMult, speed, bend));
	}

}