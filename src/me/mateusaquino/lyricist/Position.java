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

/**
 * Used to define the position of the element
 * on the screen.
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public final class Position {
	private Position(){}
	
	public static Position TOP_LEFT = new Position(), TOP_CENTER = new Position(), TOP_RIGHT = new Position(),
						   MID_LEFT = new Position(), MID_CENTER = new Position(), MID_RIGHT = new Position(),
	                       BOT_LEFT = new Position(), BOT_CENTER = new Position(), BOT_RIGHT = new Position();
	
	/** Note: ABSOLUTE values don't obey margins. **/
	public static Position ABSOLUTE(int x, int y){
		Position pos = new Position();
		pos.type = "ABSOLUTE";
		pos.setX(x);
		pos.setY(y);
		return pos;
	}
	
	/** Note: PROPORTIONAL values don't obey margins. **/
	public static Position PROPORTIONAL(double x, double y){
		Position pos = new Position();
		pos.type = "PROPORTIONAL";
		pos.setX(x);
		pos.setY(y);
		return pos;
	}
	
	private String type = "OTHER";
	private double x=-1;
	private double y=-1;

	public final double getX(){
		validate(true);
		return this.x;
	}
	
	public double getY(){
		validate(true);
		return this.y;
	}
	
	public String getType(){
		if (type.equals("OTHER"))
			return this.toString();
		else
			return type;
	}
	
	public void setX(double x){
		validate(false);
		this.x = x;
	}
	
	public void setY(double y){
		validate(false);
		this.y = y;
	}

	private static boolean outRange = false;
	// Used for ABSOLUTE values
	private void validate(boolean validateValue){
		if (this.type.equals("OTHER"))
			throw new InvalidPositionTypeException("Couldn't retrieve coordinates of a NON-ABSOLUTE position.");
		if (validateValue) // tested when rendering frame
			if (!outRange && this.type == "PROPORTIONAL" && (this.x<0||this.x>1||this.y<0||this.y>1)) 
				System.err.println((outRange = true)?"WARNING: Proportional values are out of the range [0..1]":"");
	}
	
	private class InvalidPositionTypeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private InvalidPositionTypeException(String message) {
			super(message);
		}
	}
}