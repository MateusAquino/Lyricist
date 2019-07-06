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

import java.util.LinkedList;

import me.mateusaquino.lyricist.elements.Element;

/**
 * This class is used to reduce the amount of
 * coding and also create linear sequences of
 * elements. (synchronized)
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public final class Sequence {
	private LinkedList<Track.TimedElement> sequence;
	public int time = 0;
	private LinkedList<Track.TimedElement> elements;
	
	protected Sequence(int initialTime, LinkedList<Track.TimedElement> toAdd, LinkedList<Track.TimedElement> elements){
		time = initialTime;
		sequence = toAdd;
		this.elements = elements;
	}
	
	public Sequence then(int duration, Element element){
		sequence.add(Track.build(time, time+duration, element));
		time += duration;
		return this;
	}
	
	public Sequence wait(int duration){
		time += duration;
		return this;
	}
	
	public Sequence repeat(int times){
		LinkedList<Track.TimedElement> copy = new LinkedList<Track.TimedElement>();
		int start = Integer.MAX_VALUE, end = -1;
		for (Track.TimedElement e : sequence){
			copy.add(e);
			start = Math.min(start, e.start());
			end = Math.max(end, e.end());
		}
		int timespan = end-start;
		
		for (int i = 1; i <= times; i++)
			for (Track.TimedElement element : copy)
				sequence.add(Track.build(element.start()+timespan*i, element.end()+timespan*i, element.element()));
		
		return this;
	}
	
	public void done(){
		elements.addAll(sequence);
	}
}