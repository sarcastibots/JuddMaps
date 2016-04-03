package com.sarcastibots.JuddMaps.Editor;

import com.sarcastibots.JuddMaps.Map.Sprite;

/**
 *TODOL Fix this class properly.
 */
public class Camera
{
	final static float floatiness = 0.002f;
	
	public int viewWidth, viewHeight;
	
	public float viewx;

	public float viewy;
	float speedx, speedy;
	float trackingx, trackingy;
	Sprite trackSprite = null;
	
	public void trackSprite(Sprite s) {
		trackSprite = s;
	}
	
	public void setViewSize(int width, int height) {
		viewWidth = width;
		viewHeight = height;
	}
	
	/**
	 * override this method to update AI. Only use this method,
	 * as the render method may be called even when the game is
	 * paused.
	 */
	public void logic() {
		
		if(trackSprite != null) {
			trackingx = trackSprite.getX();
			trackingy = trackSprite.getY();
		}
		
		speedx += (trackingx - viewx) * floatiness;
		speedy += (trackingy - viewy) * floatiness;
		
		speedx = speedx * .94f;
		speedy = speedy * .94f;
		
		viewx += speedx;
		viewy += speedy;
		
		
		
	}
}