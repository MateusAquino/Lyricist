package me.mateusaquino.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jhlabs.image.SphereFilter;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.Project;
import me.mateusaquino.lyricist.Track;
import me.mateusaquino.lyricist.effects.Filter;
import me.mateusaquino.lyricist.effects.LineReveal;
import me.mateusaquino.lyricist.effects.RenderArea;
import me.mateusaquino.lyricist.effects.Translate;
import me.mateusaquino.lyricist.elements.ImageSolid;
import me.mateusaquino.lyricist.metaeffects.CubicEase;
import me.mateusaquino.lyricist.metaeffects.CustomEase;

public class Test3 {
	public static void main(String[] args) throws IOException {
		// Setup
		BufferedImage aro = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\Aro.png"));
		BufferedImage mapa = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\Mapa.png"));
		BufferedImage planeta = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\Planeta.png"));
		BufferedImage uni = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\uni.png"));
		BufferedImage nu = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\nu.png"));
		BufferedImage louro = ImageIO.read(new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\louro.png"));
		
		File ffmpeg = new File("C:\\ffmpeg\\ffmpeg.exe");
		
		int w = 1612, h = 525;
		Project proj = new Project(null, w, h, 60, ffmpeg);
		
		Track finalAnim = proj.newTrack();
		Track back = proj.newTrack();
		Track middle = new Track();
		Track front = proj.newTrack();
		
		double k = 0.73, inv = 1-k;
		double mapOffset = -0.3;
		int SEGUNDO = 60;
		
		// Effects
		SphereFilter sf = new SphereFilter();
		sf.setRadius(165);
		sf.setRefractionIndex(1.3f);
		sf.setCentreY(0.48f);
		Filter bojo = new Filter(sf);
		middle.addGlobalEffects(bojo);
		back.subtractAlpha(middle);
		
		Translate mapTranslate = new Translate(Position.PROPORTIONAL(-0.02 + mapOffset, .5), 
											   Position.PROPORTIONAL(1.3   + mapOffset, .5), 0, 70);
		
		CustomEase easedEnd = new CustomEase(.14, .24, .6, .97, mapTranslate);
		
		
		// Elements
		front.addElement(0, SEGUNDO*5, new ImageSolid(aro));
		
		middle.addSequence(0).then(70, new ImageSolid(mapa, (int)(w*inv),
															(int)(h*inv),
															(int)(w*k),
															(int)(h*k),
															mapTranslate))
		 			  .repeat(1)
					   
					  .then(SEGUNDO*4, new ImageSolid(mapa, (int)(w*inv),
												  	  		(int)(h*inv),
												  	  		(int)(w*k),
												  	  		(int)(h*k),
												  	  		easedEnd)).done();
		
		back.addElement(0, SEGUNDO*5, new ImageSolid(planeta));
		
		finalAnim.addElement(70+15, SEGUNDO*5, new ImageSolid(uni,
				new CubicEase(new Translate(
						Position.PROPORTIONAL(.8, .4), 
						Position.PROPORTIONAL(.18, .4), 0, SEGUNDO*2)).BOTH,
				new RenderArea(new Rectangle(w/2, 0, w, h), true)
								.sum(new Circle(w/2, h/2, 166))
				));
		
		finalAnim.addElement(70+15, SEGUNDO*5, new ImageSolid(nu,
				new CubicEase(new Translate(
						Position.PROPORTIONAL(.3, .4), 
						Position.PROPORTIONAL(.82, .4), 0, SEGUNDO*2)).BOTH,
				new RenderArea(new Rectangle(0, 0, w/2, h), true)
							 	.sum(new Circle(w/2, h/2, 166))
				));
		finalAnim.addElement(70+65, SEGUNDO*5, new ImageSolid(louro, 
				new CustomEase(.04, .73, .79, 1, new LineReveal(true, 110, h)),
				new CustomEase(.03, .6, .8, .9, new LineReveal(false, 130))									
				));
		
		File renderMp4 = new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\loader.mp4");
		File renderWebm = new File("C:\\Users\\Home\\Desktop\\Unionu\\Anim\\loader.webm");
		
		//proj.html5AlphaEncode();
		proj.render(renderMp4, 0, 220, "-vcodec libx264 -acodec libmp3lame -preset slow -crf 22 -threads 0", true);
		proj.render(renderWebm, 0, 220, "-vcodec libvpx -acodec libvorbis -threads 4", true);
		//proj.preview(0, 220, 30);
	}
}