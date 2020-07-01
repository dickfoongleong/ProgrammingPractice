package com.hth.id_card.user_interface;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class HTH_CardCover extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Rectangle[] rectangleList;
	
	public HTH_CardCover(Rectangle[] rectangleList) {
		this.rectangleList = rectangleList;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int idx = 0; idx < rectangleList.length; idx++) {
			g.setColor(Color.WHITE);
			g.fillRect(rectangleList[idx].x, rectangleList[idx].y, rectangleList[idx].width, rectangleList[idx].height);
		}
	}
}
