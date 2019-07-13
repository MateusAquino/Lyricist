package me.mateusaquino.lyricist.effects;

import me.mateusaquino.lyricist.ApplyEffectEvent;
import me.mateusaquino.lyricist.metaeffects.SupportsMetaEffects;

public class LineReveal implements Effect {

	public LineReveal(boolean horizontal, int duration) {
		this(horizontal, duration, -1);
	}
	public LineReveal(boolean horizontal, int duration, int fromLine) {
		this.horizontal = horizontal;
		this.time = duration;
		this.line = fromLine;
	}
	
	int line = -1;
	double time;
	boolean horizontal;
	
	int maxDist = -1;
	@SupportsMetaEffects
	@Override
	public void apply(ApplyEffectEvent event) {
		if (maxDist == -1)
			if (horizontal){
				if (line == -1)
					line = event.projectHeight()/2;
				maxDist = Math.max(event.projectHeight()-line, line);
			} else {
				if (line == -1)
					line = event.projectWidth()/2;
				maxDist = Math.max(event.projectWidth(), line);
			}		
		double progress = (event.current()-event.start())/time;
		progress = (progress>1) ? 1 : event.calcProgress(progress);
		int r = (int) Math.ceil(maxDist*progress);
		if (horizontal)
			new RenderArea(0, line-r, event.projectWidth(), line+2*r).apply(event);
		else
			new RenderArea(line-r, 0, line+2*r, event.projectHeight()).apply(event);
	}
}