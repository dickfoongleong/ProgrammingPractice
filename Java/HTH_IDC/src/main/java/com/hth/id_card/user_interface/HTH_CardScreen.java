package com.hth.id_card.user_interface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.hth.backend.beans.IDCARD;
import com.hth.id_card.HTH_IDC;
import com.hth.util.IDCard;
import com.hth.util.IDMask;

public class HTH_CardScreen extends JLayeredPane {
  private static final int OFFSET = 40;

  /**
   * Serial Version ID.
   */
  private static final long serialVersionUID = 6L;

  /**
   * ID card width showed on monitor.
   */
  private static final int WIDTH = 715;

  /**
   * ID card height showed on monitor.
   */
  private static final int HEIGHT = 490;

  /**
   * ID texts layer.
   */
  private static final Integer LAYER_TEXT = JLayeredPane.MODAL_LAYER;

  /**
   * ID logo layer.
   */
  private static final Integer LAYER_LOGO = JLayeredPane.PALETTE_LAYER;

  /**
   * ID cover layer
   */
  private static final Integer LAYER_COVER = JLayeredPane.POPUP_LAYER;

  /**
   * Default font for all texts.
   */
  private static final Font DEFAULT_FONT = new Font("AvantGarde", Font.PLAIN, 15);

  /**
   * Card panel.
   */
  private JLayeredPane cardContentPanel;

  private IDCard card;
  private boolean isFront;

  public HTH_CardScreen(IDCard card, boolean isFront) {
    this.card = card;
    this.isFront = isFront;

    setOpaque(true);
    setBackground(Color.WHITE);

    setCard();
    add(cardContentPanel, JLayeredPane.PALETTE_LAYER);
  }

  public JLayeredPane getCardContentPanel() {
    return cardContentPanel;
  }

  private void setCard() {
    cardContentPanel = new JLayeredPane();
    cardContentPanel.setOpaque(true);
    cardContentPanel.setBackground(Color.WHITE);

    JPanel logoPanel, textPanel;
    if (isFront) {
      if (card.getFrontLarge().isEmpty()) {
        logoPanel = setLogoView(card.getFrontLogo());
      } else {
        logoPanel = setLargeLogoView(card.getFrontLogo(), card.getFrontLarge());
      }
      textPanel = setLineView(card.getFrontTexts(), card.getFrontTextModifiers());
    } else {
      if (card.getBackLarge().isEmpty()) {
        logoPanel = setLogoView(card.getBackLogo());
      } else {
        logoPanel = setLargeLogoView(card.getBackLogo(), card.getBackLarge());
      }
      textPanel = setLineView(card.getBackTexts(), card.getBackTextModifiers());
    }

    cardContentPanel.add(logoPanel, LAYER_LOGO);
    cardContentPanel.add(textPanel, LAYER_TEXT);
    applyMask();
  }

  /**
   * Set the 9 logo views.
   * 
   * @param logoList
   *        Array of the 9 logo images.
   *        
   * @return
   *        Panel contains all the 9 logo images.
   */
  private JPanel setLogoView(Image[] logoList) {
    JPanel imgPanel;
    JLabel imgLabel;

    imgPanel = new JPanel();
    imgPanel.setLayout(new GridLayout(3, 3, 0, 0));
    imgPanel.setBounds(0, 0, WIDTH, HEIGHT);
    imgPanel.setOpaque(false);

    for (int pos = 0; pos < logoList.length; pos++) {
      imgLabel = new JLabel();

      if (logoList[pos] != null) {
        BufferedImage logo = toBufferedImage(logoList[pos]);
        int orgWidth = logoList[pos].getWidth(null);
        int orgHeight = logoList[pos].getHeight(null);
        int newWidth = orgWidth;
        int newHeight = orgHeight;

        if (orgWidth != WIDTH / 3) {
          newWidth = WIDTH / 3;
          newHeight = (newWidth * orgHeight) / orgWidth;
        }

        if (newHeight > HEIGHT / 3) {
          newHeight = HEIGHT / 3;
          newWidth = (newHeight * orgWidth) / orgHeight;
        }

        Image logoScaled = logo.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(logoScaled));

        if (pos % 3 == 0) {
          imgLabel.setHorizontalAlignment(JLabel.LEFT);
        } else if (pos % 3 == 1) {
          imgLabel.setHorizontalAlignment(JLabel.CENTER);
        } else if (pos % 3 == 2) {
          imgLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
      }

      imgPanel.add(imgLabel);
    }

    return imgPanel;
  }

  /**
   * Set the 3 large logo views.
   * 
   * @param logoList
   *      Array of logo images.
   * @param direction
   *      Direction of the image spans. (H or V)
   * 
   * @return
   *      Panel contains all the 3 logo images.
   */
  private JPanel setLargeLogoView(Image[] logoList, String direction) {
    JPanel imgPanel;
    JLabel imgLabel;

    JLayeredPane pane1 = new JLayeredPane();
    pane1.setOpaque(false);

    JLayeredPane pane2 = new JLayeredPane();
    pane2.setOpaque(false);

    JLayeredPane pane3 = new JLayeredPane();
    pane3.setOpaque(false);

    imgPanel = new JPanel();
    imgPanel.setBounds(0, 0, WIDTH, HEIGHT);
    imgPanel.setOpaque(false);

    if (direction.equalsIgnoreCase("V")) {
      imgPanel.setLayout(new GridLayout(1, 3, 0, 0));

      for (int pos = 0; pos < logoList.length; pos++) {
        imgLabel = new JLabel();
        imgLabel.setBounds(0, 0, WIDTH / 3, HEIGHT);

        if (logoList[pos] != null) {
          BufferedImage logo = toBufferedImage(logoList[pos]);
          int orgWidth = logoList[pos].getWidth(null);
          int orgHeight = logoList[pos].getHeight(null);
          int newWidth = orgWidth;
          int newHeight = orgHeight;

          if (orgWidth > WIDTH / 3) {
            newWidth = WIDTH / 3;
            newHeight = (newWidth * orgHeight) / orgWidth;
          }

          if (newHeight > HEIGHT) {
            newHeight = HEIGHT;
            newWidth = (newHeight * orgWidth) / orgHeight;
          }

          Image logoScaled = logo.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
          imgLabel.setIcon(new ImageIcon(logoScaled));

          if (pos / 3 == 0) {
            imgLabel.setVerticalAlignment(JLabel.TOP);
          } else if (pos / 3 == 1) {
            imgLabel.setVerticalAlignment(JLabel.CENTER);
          } else if (pos / 3 == 2) {
            imgLabel.setVerticalAlignment(JLabel.BOTTOM);
          }

          if (pos % 3 == 0) {
            pane1.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          } else if (pos % 3 == 1) {
            pane2.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          } else if (pos % 3 == 2) {
            pane3.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          }
        }
      }
    } else {
      imgPanel.setLayout(new GridLayout(3, 1, 0, 0));

      for (int pos = 0; pos < logoList.length; pos++) {
        imgLabel = new JLabel();
        imgLabel.setBounds(0, 0, WIDTH, HEIGHT / 3);

        if (logoList[pos] != null) {
          BufferedImage logo = toBufferedImage(logoList[pos]);
          int orgWidth = logoList[pos].getWidth(null);
          int orgHeight = logoList[pos].getHeight(null);
          int newWidth = orgWidth;
          int newHeight = orgHeight;

          if (orgWidth > WIDTH) {
            newWidth = WIDTH;
            newHeight = (newWidth * orgHeight) / orgWidth;
          }

          if (newHeight > HEIGHT / 3) {
            newHeight = HEIGHT / 3;
            newWidth = (newHeight * orgWidth) / orgHeight;
          }

          Image logoScaled = logo.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
          imgLabel.setIcon(new ImageIcon(logoScaled));

          if (pos % 3 == 0) {
            imgLabel.setHorizontalAlignment(JLabel.LEFT);
          } else if (pos % 3 == 1) {
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
          } else if (pos % 3 == 2) {
            imgLabel.setHorizontalAlignment(JLabel.RIGHT);
          }

          if (pos / 3 == 0) {
            pane1.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          } else if (pos / 3 == 1) {
            pane2.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          } else if (pos / 3 == 2) {
            pane3.add(imgLabel, JLayeredPane.DEFAULT_LAYER + pos);
          }
        }
      }
    }

    imgPanel.add(pane1);
    imgPanel.add(pane2);
    imgPanel.add(pane3);
    return imgPanel;
  }

  /**
   * Set the 18 text views.
   * 
   * @param lineTexts
   *        Array of 18 lines.
   * @param lineModifiers
   *        Array of 18 modifiers.
   *        
   * @return
   *        Panel contains all the 18 lines of texts.
   */
  private JPanel setLineView(String[] lineTexts, String[] lineModifiers) {
    JPanel textPanel, linePanel;
    String text, modifier;

    textPanel = new JPanel();
    //TODO: Remove hard code....
    if (HTH_IDC.member.equals("AR1")) {
      textPanel.setBounds(OFFSET, 0, WIDTH - OFFSET, HEIGHT);
    } else {
      textPanel.setBounds(0, 0, WIDTH, HEIGHT);
    }
    textPanel.setLayout(new GridLayout(18, 0, 0, 0));
    textPanel.setOpaque(false);

    for (int line = 0; line < lineTexts.length; line++) {
      text = lineTexts[line];
      if (text.contains("@@")) {
        text = IDCARD.mergeVar(text);
      }
      modifier = lineModifiers[line];

      linePanel = new JPanel();
      linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
      linePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 18));
      linePanel.setOpaque(false);

      if (text.contains("||")) {
        String[] sections = text.split("\\|\\|");
        //              modifier += ",center";

        for (String section : sections) {
          setLineStyle(linePanel, section, modifier);
        }
      } else {
        setLineStyle(linePanel, text, modifier);
      }

      textPanel.add(linePanel);
    }

    return textPanel;
  }

  /**
   * Apply masks on the ID card.
   * <br><br>
   * Masks are extra modifiers for partial texts on each line.
   */
  private void applyMask() {
    JPanel textPanel = (JPanel) cardContentPanel.getComponentsInLayer(LAYER_TEXT)[0];
    Component[] lineList = textPanel.getComponents();

    JPanel linePanel;
    JTextPane textField;
    IDMask[] maskList;
    int lineIdx, col, start, length;
    String[] modList;

    if (isFront) {
      maskList = card.getFrontMask();
    } else {
      maskList = card.getBackMask();
    }

    for (IDMask mask : maskList) {
      lineIdx = mask.getLine() - 1;
      col = mask.getCol();
      start = mask.getStartIdx();
      length = mask.getEndIdx() - start + 1;
      modList = mask.getModifierList();

      if (col == -1) {
        linePanel = (JPanel) lineList[lineIdx + 1];
        String colorHex = modList[0].replaceFirst("BG#", "");
        Color bgColor = new Color(Integer.valueOf(colorHex.substring(0, 2), 16),Integer.valueOf(colorHex.substring(2, 4), 16), Integer.valueOf(colorHex.substring(4), 16));
        linePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 0, 0, 0), 
            BorderFactory.createMatteBorder(2, 0, 0, 0, bgColor)));
      } else {
        try {
          linePanel = (JPanel) lineList[lineIdx];
          textField = (JTextPane) linePanel.getComponent(col - 1);

          StyledDocument doc = textField.getStyledDocument();
          SimpleAttributeSet attrs = new SimpleAttributeSet();
          for (String mod : modList) {
            if (mod.equals("b")) {
              StyleConstants.setBold(attrs, true);
            } else if (mod.equals("i")) {
              StyleConstants.setItalic(attrs, true);
            } else if (mod.equals("u")) {
              StyleConstants.setUnderline(attrs, true);
            } else if (mod.startsWith("#") && mod.length() == 7) {
              String colorHex = mod.replaceFirst("#", "");
              Color color = new Color(Integer.valueOf(colorHex.substring(0, 2), 16),Integer.valueOf(colorHex.substring(2, 4), 16), Integer.valueOf(colorHex.substring(4), 16));
              StyleConstants.setForeground(attrs, color);
            }
          }
          doc.setCharacterAttributes(start, length, attrs, false);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Set the formatting styles on the give line.
   * 
   * @param panel
   *        Line panel.
   * @param text
   *        Text on the line.
   * @param style
   *        Styles of the line.
   */
  private void setLineStyle(JPanel panel, String text, String style) {
    Pattern pattern = Pattern.compile("#!\\(.+?\\)|#[1-9]?@\\(.+?\\)|##\\(.{9,}?\\)");
    Matcher matcher = pattern.matcher(text);

    String activeTxt;
    while (matcher.find()) {
      activeTxt = matcher.group().replaceAll("#[1-9]?[!@#]\\(|\\)$", "");
      text = text.replace(matcher.group(), activeTxt);
    }

    panel.add(setupTextField(text, style));
  }

  /**
   * Set up text field for a line.
   * 
   * @param text
   *        Text showed on the field.
   * @param styleList
   *        String of modifiers separated by commas(,).
   *        
   * @return
   *        Text field with the given text formatted by the given styles.
   */
  private JTextPane setupTextField(String text, String styleList) {
    // Create the StyleContext, document and the pane.
    StyledDocument doc;
    JTextPane textField = new JTextPane();

    textField.setPreferredSize(new Dimension(textField.getSize().width, HEIGHT / 18));
    textField.setMinimumSize(new Dimension(textField.getSize().width, HEIGHT / 18));
    textField.setFont(DEFAULT_FONT);
    textField.setEditable(false);
    textField.setOpaque(false);
    textField.setBorder(null);
    textField.setForeground(Color.BLACK);

    doc = textField.getStyledDocument();
    SimpleAttributeSet attrSet = new SimpleAttributeSet();
    StyleConstants.setAlignment(attrSet, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, text.length(), attrSet, false);

    if (!styleList.isEmpty()) {
      String[] keys = styleList.split(",");
      String value;

      for (String key : keys) {
        value = key.trim();
        switch (value) {
          case "center":
            StyleConstants.setAlignment(attrSet, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, text.length(), attrSet, false);
            break;
          case "left":
            StyleConstants.setAlignment(attrSet, StyleConstants.ALIGN_LEFT);
            doc.setParagraphAttributes(0, text.length(), attrSet, false);
            break;
          case "right":
            StyleConstants.setAlignment(attrSet, StyleConstants.ALIGN_RIGHT);
            doc.setParagraphAttributes(0, text.length(), attrSet, false);
            break;
          case "b":
            textField.setFont(textField.getFont().deriveFont(Font.BOLD));
            break;
          case "i":
            textField.setFont(textField.getFont().deriveFont(Font.ITALIC));
            break;
          case "u":
            StyleConstants.setUnderline(attrSet, true);
            doc.setParagraphAttributes(0, text.length(), attrSet, false);
            break;
          case "WHITE":
            textField.setForeground(new Color(255, 255, 255));
            break;
          case "RED":
            textField.setForeground(new Color(255, 0, 0));
            break;
          case "GREEN":
            textField.setForeground(new Color(0, 255, 0));
            break;
          case "BLUE":
            textField.setForeground(new Color(0, 0, 255));
            break;
          case "small":
            textField.setFont(textField.getFont().deriveFont(14.0f));
            break;
          case "large":
            textField.setFont(textField.getFont().deriveFont(18.0f));
            break;
          default:
            if (value.contains("#") && (value.length() == 4 || value.length() == 7)) {
              String colorHex = value;

              if (value.length() == 4) {
                colorHex = "#" + value.charAt(1) + value.charAt(1) + value.charAt(2) + value.charAt(2) + value.charAt(3) + value.charAt(3);
              }

              textField.setForeground(new Color(
                  Integer.valueOf(colorHex.substring(1,3), 16), 
                  Integer.valueOf(colorHex.substring(3,5), 16),
                  Integer.valueOf(colorHex.substring(5,7), 16)));
            }
        }
      }
    }

    if (isFront) {
      textField.setFont(textField.getFont().deriveFont(textField.getFont().getSize2D() + 2.0f));
    }

    try {
      doc.insertString(0, text, attrSet);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return textField;
  }

  public void applyCover() {
    if (HTH_IDC.member.equals("AR1")) {
      // TODO: Need to read new file.
      List<Rectangle> rectangleList = new ArrayList<Rectangle>();
      int x = 0;
      int y = 0;
      if (isFront) {
        JPanel logoPanel = (JPanel) cardContentPanel.getComponentsInLayer(LAYER_LOGO)[0];
        Component[] logoList = logoPanel.getComponents();

        rectangleList.add(new Rectangle(x, y, logoList[0].getSize().width, logoList[0].getSize().height));

        x += logoList[0].getSize().width + logoList[1].getSize().width;
        rectangleList.add(new Rectangle(x, y, logoList[2].getSize().width, logoList[0].getSize().height));

        x = 0;
        y = 0;
        JPanel txtPanel = (JPanel) cardContentPanel.getComponentsInLayer(LAYER_TEXT)[0];
        Component[] txtList = txtPanel.getComponents();
        for (int i = 0; i < txtList.length; i++) {
          if (i == 6 || i == 7) {
            rectangleList.add(new Rectangle(x, y, txtList[i].getSize().width + OFFSET, txtList[i].getSize().height));
          }
          y += txtList[i].getSize().height;
        }
      } else {
        rectangleList.add(new Rectangle(x, y, cardContentPanel.getSize().width, cardContentPanel.getSize().height));
      }

      HTH_CardCover coverPanel = new HTH_CardCover(rectangleList.toArray(new Rectangle[0]));
      coverPanel.setBounds(0, 0, WIDTH, HEIGHT);
      coverPanel.setOpaque(false);
      cardContentPanel.add(coverPanel, LAYER_COVER);
    }
  }

  /**
   * Convert Image instance to BufferedImage instance.
   * 
   * @param img
   *        Original image.
   *    
   * @return
   *        Buffered image of the original image.
   */
  private BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }

    BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    Graphics2D graphic = bImage.createGraphics();
    graphic.drawImage(img, 0, 0, null);
    graphic.dispose();

    return bImage;
  }
}
