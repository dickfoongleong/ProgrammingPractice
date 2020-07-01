package com.hth.id_card.user_interface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import com.hth.images.HTH_Image;

public class HTH_ControlButton extends JButton {
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 3L;

	public HTH_ControlButton(String text) {
		super(text);
		setContentAreaFilled(false);
		setFont(new Font("Arial",  Font.PLAIN, 15));
		setPreferredSize(new Dimension(95, 25));
		setMaximumSize(new Dimension(95, 25));
		setMinimumSize(new Dimension(95, 25));
		setForeground(new Color(79, 145, 200));
		setBorder(null);
		setRolloverEnabled(true);
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			BufferedImage bgImg = ImageIO.read(HTH_Image.getImageURL("PanelButton.png"));

			if (getModel().isPressed()) {
				setFont(getFont().deriveFont(Font.BOLD));
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
			} else if (getModel().isRollover()) {
				setFont(getFont().deriveFont(Font.BOLD));
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				setFont(getFont().deriveFont(Font.PLAIN));
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.paintComponent(g);
	}
}
