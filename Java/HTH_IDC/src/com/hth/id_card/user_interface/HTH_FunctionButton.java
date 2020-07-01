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

public class HTH_FunctionButton extends JButton {
  /**
   * Serial Version ID.
   */
  private static final long serialVersionUID = 2L;

  public HTH_FunctionButton(String text) {
    super(text);
    setContentAreaFilled(false);
    setFont(new Font("Arial",  Font.PLAIN, 15));
    setPreferredSize(new Dimension(200, 31));
    setMaximumSize(new Dimension(200, 31));
    setMinimumSize(new Dimension(200, 31));
    setForeground(Color.WHITE);
    setBorder(null);
    setRolloverEnabled(true);
    setFocusPainted(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    try {
      BufferedImage bgImg = null;

      if (getModel().isPressed()) {
        bgImg = ImageIO.read(HTH_Image.getImageURL("FunctionButton_Hover.png"));
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
      } else if (getModel().isRollover()) {
        bgImg = ImageIO.read(HTH_Image.getImageURL("FunctionButton_Hover.png"));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      } else {
        bgImg = ImageIO.read(HTH_Image.getImageURL("FunctionButton_On.png"));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }

      g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    super.paintComponent(g);
  }
}
