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

package me.mateusaquino.test;

import static me.mateusaquino.lyricist.Position.MID_CENTER;
import static me.mateusaquino.lyricist.Position.TOP_CENTER;
import static me.mateusaquino.lyricist.elements.TextBuilder.AESTHETIC;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import me.mateusaquino.lyricist.Margin;
import me.mateusaquino.lyricist.Position;
import me.mateusaquino.lyricist.Project;
import me.mateusaquino.lyricist.Track;
import me.mateusaquino.lyricist.effects.FadeIn;
import me.mateusaquino.lyricist.effects.FadeOut;
import me.mateusaquino.lyricist.effects.Outline;
import me.mateusaquino.lyricist.effects.Translate;
import me.mateusaquino.lyricist.effects.Wave;
import me.mateusaquino.lyricist.elements.Text;
import me.mateusaquino.lyricist.elements.TextBuilder;
import me.mateusaquino.lyricist.metaeffects.SinEase;

/**
 * This is a class for testing purposes
 * 
 * @author Mateus de Aquino Batista
 */
public class Test {

	public static void main(String args[]) throws IOException {
		// Project
		File ffmpeg = new File("C:\\ffmpeg\\ffmpeg.exe");
		Project proj = new Project(Color.PINK, 1020, 720, 30, ffmpeg);
		
		Track track = proj.newTrack(new Margin(10,0,0,0));
		int SEGUNDO = 60;
		
		
		// Texts
		Font font = new Font("Helvetica", Font.BOLD, 90);
		Font fontsmall = new Font("Helvetica", Font.BOLD, 20);
		TextBuilder voice = new TextBuilder(font, MID_CENTER, new FadeIn(SEGUNDO/2), new FadeOut(SEGUNDO/2));
		TextBuilder rhythm = new TextBuilder(fontsmall, TOP_CENTER, new FadeOut(5));
		
		// texto "txt"
		track.addElement(100, 3*SEGUNDO, new Text("txt", font, Position.PROPORTIONAL(1, 0), 
				new Outline(), new FadeIn(SEGUNDO/2), new Wave(), new FadeOut(SEGUNDO/2)));
		
		// texto central
		track.addSequence(100)
				.then(SEGUNDO*2, voice.txt("Mafios", new Wave()))
				.then(SEGUNDO*2/3, voice.txt("É"))
				.then(SEGUNDO, voice.txt("topper"))
				.done();
		
		// texto d cima
		track.addSequence(100).then(SEGUNDO/4, rhythm.txt("SNARE"))
				.wait(SEGUNDO/4)
				.then(SEGUNDO/4, rhythm.txt("KICK"))
				.wait(SEGUNDO/4)
				.repeat(20)
				.done();
		
		// legenda
		track.addElement(95, SEGUNDO*3, AESTHETIC.txt("Teste de legendas topzera", new SinEase(
					new Translate(Position.MID_CENTER, SEGUNDO/2, SEGUNDO)
				).BOTH));
		
		proj.preview(60, SEGUNDO*4);

		//proj.render(new File("C:\\Users\\Home\\Desktop\\Developer Folders\\Java\\IMAGERY LIB\\vid.mp4"),
				//60, SEGUNDO*13/2);
	}
}