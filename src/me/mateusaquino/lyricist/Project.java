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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

/**
 * This is where the magic happens.
 * 
 * @author Mateus de Aquino Batista
 * @category Main Elements
 */
public final class Project {

	private LinkedList<Track> tracks = new LinkedList<Track>();
	private int width, height;
	private Color background;
	private int fps;
	
	public Project(int width, int height, File ffmpegFile){
		this(width, height, 60, ffmpegFile);
	}
	
	public Project(int width, int height, int fps, File ffmpegFile){
		this(Color.BLACK, width, height, fps, ffmpegFile);
	}
	
	public Project(Color background, int width, int height, File ffmpegFile){
		this(Color.BLACK, width, height, 60, ffmpegFile);
	}
	
	/** if background color = null, the frames will be rendered with opacity **/
	public Project(Color background, int width, int height, int fps, File ffmpegFile) {
		this.width = width;
		this.height = height;
		this.background = background;
		this.ffmpegFile = ffmpegFile;
		this.fps = (fps>60) ? 60 : fps;
		
		if (!lyricistFolder.exists())
			lyricistFolder.mkdirs();
		
		if (!renderFolder.exists())
			renderFolder.mkdirs();
	}
	
	public BufferedImage[] getFrames(int beginning, int end, boolean printNum){
		double step = 60.0/(double)fps;
		int qnt = (int)((end-beginning)/step);
		BufferedImage[] frames = new BufferedImage[qnt+1];
		int frameNumber = 0;
		int pcnt = 0;
		for (double frame = beginning; frame <= end; frame+=step){
			frames[frameNumber] = getFrame((int)frame, printNum);
			frameNumber++;
			
			if (frameNumber*100/qnt!=pcnt){ // %
				pcnt = frameNumber*100/qnt;
				System.out.println(pcnt+"%");
			}
		}
		return frames;
	}
	
	public BufferedImage getFrame(int frameNumber){
		return getFrame(frameNumber, false);
	}
	
	public void preview(int frameNumber){
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(this.getFrame(frameNumber, true))));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void preview(int beginning, int end){
		preview(beginning, end, fps);
	}
	
	/** Preview where:<br>
	 *  • <b>fps</b> = frames per second<br>
	 *  • and <b>previewFps</b> = speed (in frames per second) **/
	private int frameNumber;
	public void preview(int beginning, int end, int previewFps) {
		BufferedImage firstFrame = this.getFrame(beginning, true);
		System.out.println("Rendering frames...");
		BufferedImage[] frames = getFrames(beginning, end, true);
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		
		frameNumber = 0;
		JPanel pane = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (frameNumber>frames.length-2) // repeat
                	frameNumber = 0;
                g.drawImage(frames[frameNumber], 0, 0, firstFrame.getWidth(), firstFrame.getHeight(), this);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(firstFrame.getWidth(), firstFrame.getHeight());
            }
        };
        Timer timer = new Timer((int)(1000.0/(double)previewFps), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frameNumber++;
				pane.repaint();
				if (!frame.isVisible())
					((Timer)e.getSource()).stop();
			}
		});
        
        frame.getContentPane().add(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timer.start();	
	}

	
	File lyricistFolder = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath()
			+ "/Lyricist");
	File renderFolder = new File(lyricistFolder.getAbsolutePath()+"/render");
	File ffmpegFile;
	
	public void render(File output, int beginning, int end) throws IOException {
		render(output, beginning, end, fps, "", false);
	}
	
	public void render(File output, int beginning, int end, int speedFps) throws IOException {
		render(output, beginning, end, speedFps, "",  false);
	}

	public void render(File output, int beginning, int end, boolean encodeOnly) throws IOException {
		render(output, beginning, end, fps, "", encodeOnly);
	}
	
	public void render(File output, int beginning, int end, String ffmpegArgs) throws IOException {
		render(output, beginning, end, 60, ffmpegArgs, false);
	}
	
	public void render(File output, int beginning, int end, String ffmpegArgs, boolean encodeOnly) throws IOException {
		render(output, beginning, end, 60, ffmpegArgs, encodeOnly);
	}
	
	public void render(File output, int beginning, int end, int speedFps, String ffmpegArgs, boolean encodeOnly) throws IOException {
		double step = 60/(double)fps;
		
		if (!encodeOnly) {
			System.out.println("Cleaning last render's garbage...");
			
			for(File file: renderFolder.listFiles()) 
			    if (!file.isDirectory()) 
			        file.delete();
			
			renderFolder.mkdir();
		}
		
		System.out.println("Started rendering...");
		
		int oldP = 0;
		int frame = 0;
		
		if (!encodeOnly) {
			for (double i = 0; i < end-beginning; i+=step) {
				String frameNum = String.format("%6s", frame++).replace(" ", "0");
				File outputfile = new File(renderFolder.getAbsolutePath()+"/image_" + frameNum + ".png");
				ImageIO.write(this.getFrame(beginning+(int)i), "png", outputfile);
				
				int newP = (int)(i*100/(end-beginning));
				if (oldP!=newP)
					System.out.println((oldP = newP)+"%");
			}
			System.out.println("Rendering completed");
		}
		
		System.out.println("Encoding file...");
		
		String pts = (""+(60.0/(double)speedFps)).replace(",", ".");
		String commands;
		if (ffmpegArgs.isEmpty())
			commands = "\"" + ffmpegFile + "\" -f image2 -i \""
		        + renderFolder + "\\image_%6d.png\" -vcodec libx264 -filter:v \"setpts="+pts+"*PTS,fps=60\" \"" 
				+ output + "\"";
		else
			commands = "\"" + ffmpegFile + "\" -f image2 -i \"" + renderFolder + "\\image_%6d.png\" " + ffmpegArgs
			+ " \""+ output + "\"";
		System.out.println(commands);
		
		boolean failed = false;
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.start().waitFor();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Rendering failed! Make sure you have ffmpeg installed.");
			failed = true;
		}
		
		if (!failed) {
			System.out.println("Video rendered at: '" + output.getAbsolutePath()+"' successfully!");
			Desktop.getDesktop().open(output);
		}
	}
	
	public BufferedImage getFrame(int frameNumber, boolean printNum){
		BufferedImage frame = ImageUtils.blank(width, height);
		Graphics2D g2d = frame.createGraphics();
		if (background!=null){
			g2d.setColor(background);
			g2d.fillRect(0, 0, width, height);
		}
		for (Track track : tracks)
			g2d.drawImage(track.getFrame(width, height, frameNumber, track.getMargin()), 0, 0, null);

		if (printNum) 
			g2d.drawImage(
				ImageUtils.drawText(ImageUtils.blank(width, height),
					"Frame: " + frameNumber,
					Center.LEFT,
					new Font("Helvetica", Font.PLAIN, 16),
					Color.RED),
			0, 0, null);
		
		g2d.dispose();
		return frame;
	}
	
	public Track newTrack() {
		return this.newTrack(new Margin(0, 0, 0, 0));
	}
	
	public Track newTrack(Margin margin) {
		Track track = new Track(margin);
		tracks.add(track);
		return track;
	}
	
	public void html5AlphaEncode() throws IOException {
		File[] files = renderFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println(i+"/"+files.length);
			File f = files[i];
			BufferedImage frame = ImageIO.read(f);
			frame = ImageUtils.makeCopy(frame);
			BufferedImage mask = ImageUtils.filter(frame, (r,g,b,a)->{return new Color(a,a,a);});
			BufferedImage newFrame = ImageUtils.concat(ImageUtils.blank(width, 2*height), frame);
			newFrame = ImageUtils.concat(newFrame, mask, 0, height);
			ImageIO.write(newFrame, "png", f);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}