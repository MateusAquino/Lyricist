package me.mateusaquino.lyricist.effects;

import java.awt.Color;
import java.util.LinkedList;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import me.mateusaquino.lyricist.ApplyEffectEvent;
import me.mateusaquino.lyricist.ImageUtils;

public class RenderArea implements Effect {
	private enum Type{SUM, SUBTRACT, INTERCECT}
	private LinkedList<Shape> area = new LinkedList<Shape>();
	private LinkedList<Type> type = new LinkedList<Type>();
	public RenderArea(int x0, int y0, int x1, int y1) {
		this(x0, y0, x1-x0, y1-y0, false);
	}
	public RenderArea(Shape shape) {
		this(shape, false);
	}
	public RenderArea(int x0, int y0, int x1, int y1, boolean invertRender) {
		this(new Rectangle(x0, y0, x1-x0, y1-y0), invertRender);
	}
	public RenderArea(Shape shape, boolean invertRender) {
		this.area.add(shape);
		this.type.add(Type.SUM);
		this.invert = invertRender;
	}
	
	boolean invert = false;
	@Override
	public void apply(ApplyEffectEvent event) {
		event.setFrame(ImageUtils.filter(event.frame(), (x,y,r,g,b,a)->{
			if (calc(x+event.x(), y+event.y())^invert) 
				return new Color(r,g,b,a);
			else 
				return new Color(0,0,0,0);
		}));
	}
	
	private RenderArea op(Shape shape, Type type) {
		this.area.add(shape);
		this.type.add(type);
		return this;
	}
	
	public RenderArea sum(Shape shape) { return op(shape, Type.SUM); }
	public RenderArea subtract(Shape shape) { return op(shape, Type.SUBTRACT); }
	public RenderArea intercect(Shape shape) { return op(shape, Type.INTERCECT); }
	public RenderArea sum(int x0, int y0, int x1, int y1){ return sum(new Rectangle(x0,y0,x1,y1)); }
	public RenderArea subtract(int x0, int y0, int x1, int y1){ return subtract(new Rectangle(x0,y0,x1,y1)); }
	public RenderArea intercect(int x0, int y0, int x1, int y1){ return intercect(new Rectangle(x0,y0,x1,y1)); }
	
	private boolean calc(int x, int y) {
		boolean res = area.get(0).contains(x, y);
		boolean found = false;
		for (int i = 0; i < area.size(); i++, found = i < area.size() ? area.get(i).contains(x, y) : false)
			if (i==0)
				continue;
			else if (type.get(i)==Type.SUM)
				res |= found;
			else if (type.get(i)==Type.SUBTRACT)
				res &= found^true;
			else
				res &= found;
		return res;
	}
}