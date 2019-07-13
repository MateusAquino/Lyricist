package me.mateusaquino.lyricist.elements;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import me.mateusaquino.lyricist.ImageUtils;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.effects.Effect;

public class ImageSolid implements Element {

	public static enum Mode {
		DEFAULT, FILL_SCREEN
	}
	
	private BufferedImage image;
	private Rectangle rect;
	private Effect[] effects;
	
	public ImageSolid(BufferedImage image, Effect... effects){
		this(image, Mode.DEFAULT, effects);
	}
	
	public ImageSolid(BufferedImage image, Mode fillMode, Effect... effects){
		this(image, 0, 0, (fillMode == Mode.DEFAULT) ? 0 : -1, (fillMode == Mode.DEFAULT) ? 0 : -1, effects);
	}
	
	public ImageSolid(BufferedImage image, int x, int y, Mode fillMode, Effect... effects){
		this(image, new Rectangle(x, y,(fillMode == Mode.DEFAULT) ? 0 : -1, (fillMode == Mode.DEFAULT) ? 0 : -1), effects);
	}
	
	public ImageSolid(BufferedImage image, int x, int y, int width, int height, Effect... effects){
		this(image, new Rectangle(x, y, width, height), effects);
	}
	
	public ImageSolid(BufferedImage image, Rectangle rect, Effect... effects){
		this.image = image;
		this.rect = rect;
		this.effects = effects;
	}
	
	@Override
	public Position getPosition() {
		return Position.TOP_LEFT;
	}

	@Override
	public Effect[] getEffects() {
		return effects;
	}
	
	@Override
	public void setEffects(Effect... effects) {
		this.effects = effects;
	}

	BufferedImage newImg = null;
	@Override
	public BufferedImage getRender(int screenWidth, int screenHeight, int start, int current, int end) {
		if (newImg == null) {
			double width = rect.getWidth();
			double height = rect.getHeight();
			if (width == 0 && height == 0){
				newImg = image;
			} else if (width == -1 && height == -1) {
				width = screenWidth;
				height = screenHeight;
				newImg = ImageUtils.resize(image, (int) width, (int) height);
			} else
				newImg = ImageUtils.resize(image, 
						(int) (width==0?image.getWidth():width), 
						(int) (height==0?image.getHeight():height));
			BufferedImage i = ImageUtils.blank(newImg.getWidth()+2*(int)rect.getX(), newImg.getHeight()+2*(int)rect.getY());
			newImg = ImageUtils.concat(i, newImg, (int) rect.getX(), (int) rect.getY());
		}
		return newImg;
	}
}
