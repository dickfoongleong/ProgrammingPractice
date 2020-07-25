package com.hth.id_card.user_interface.decorator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.hth.backend.iSeries;
import com.hth.backend.beans.DIVISN;
import com.hth.backend.beans.IDCARD;
import com.hth.backend.beans.IDCFLD;
import com.hth.backend.beans.IDCIMG;
import com.hth.id_card.HTH_IDC;
import com.hth.id_card.user_interface.HTH_CardScreen;
import com.hth.id_card.user_interface.HTH_ControlButton;
import com.hth.id_card.user_interface.HTH_Frame;
import com.hth.id_card.user_interface.HTH_FunctionButton;
import com.hth.id_card.user_interface.HTH_PromptButton;
import com.hth.id_card.user_interface.HTH_TextField;
import com.hth.util.Division;
import com.hth.util.GroupMaster;
import com.hth.util.IDCard;
import com.hth.util.IDHeader;
import com.hth.util.IDLogo;

public class ID_Decorator extends HTH_Frame implements WindowListener, Printable {
  private static final long serialVersionUID = 101L;
  private static final String DECORATOR_NAME = "Process ID Card";
  private static final Font HTH_FONT = new Font("Arial", Font.PLAIN, 18);
  private static final float ID_WIDTH = 287;
  private static final float ID_HEIGHT = 180;
  private static final float ID_X_OFFSET = ID_WIDTH / 2;
  private static final float ID_FRONT_Y_OFFSET = 25;
  private static final float ID_BACK_Y_OFFSET = ID_FRONT_Y_OFFSET + ID_HEIGHT;
  private static final int WIDTH = 715;
  private static final int HEIGHT = 490;
  private static final Font DEFAULT_FONT = new Font("AvantGarde", Font.PLAIN, 14);

  private HTH_CardScreen cardFramePanel;

  private boolean isFront;
  private boolean isLogoEditor;
  private int focusedIdx;

  private IDLogo[] selLogoList;
  private IDHeader[] selHeaderList;
  private GroupMaster[] groupList, selGroupList;
  private Division[] divisionList;
  private IDCard[] cardList;
  private IDCard selectedCard;
  private JPanel controlPanel;
  private JLayeredPane contentScreen, promptScreen, cardContentPane;
  private JTextPane[] linePanes;
  private JButton[] logoBtns;
  private JTextPane focusedTextPane;
  private JCheckBox bold, italic, underline;
  private JRadioButton left, right, center, small, normal, large;
  private JRadioButton hLargeLogo,vLargeLogo,smallLogo;
  private HTH_TextField grpField, divField, crdField;
  private HTH_TextField fromGrpField, fromDivField, fromCrdField;
  private HTH_TextField toGrpField, toDivField, toCrdField;
  private HTH_FunctionButton[] functionKeys;
  private HTH_ControlButton[] controlKeys;

  public ID_Decorator() {
    super(DECORATOR_NAME);
    addWindowListener(this);

    initComponents();

    JPanel contentPanel = new JPanel();
    contentPanel.setOpaque(true);
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setLayout(new BorderLayout());
    contentPanel.add(contentScreen, BorderLayout.CENTER);
    contentPanel.add(controlPanel, BorderLayout.SOUTH);
    add(contentPanel, BorderLayout.CENTER);

    setVisible(true);
  }

  public void begin(GroupMaster[] groupList) {
    this.groupList = groupList;

    promptScreen.setBounds(0, 0, contentScreen.getSize().width, contentScreen.getSize().height);
    setSelectionScreen();
  }

  private void initComponents() {
    focusedIdx = 0;

    promptScreen = new JLayeredPane();
    promptScreen.setOpaque(true);
    promptScreen.setBackground(Color.WHITE);

    cardContentPane = new JLayeredPane();
    cardContentPane.setOpaque(true);
    cardContentPane.setBackground(Color.WHITE);

    contentScreen = new JLayeredPane();
    contentScreen.setOpaque(false);

    controlPanel = new JPanel();
    controlPanel.setOpaque(false);
    controlPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

    linePanes = new JTextPane[18];

    logoBtns = new JButton[9];

    grpField = new HTH_TextField(10, HTH_FONT);
    divField = new HTH_TextField(3, HTH_FONT);
    crdField = new HTH_TextField(2, HTH_FONT);
    crdField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startEditor();
      }
    });

    fromGrpField = new HTH_TextField(10, HTH_FONT);
    fromDivField = new HTH_TextField(3, HTH_FONT);
    fromCrdField = new HTH_TextField(2, HTH_FONT);

    toGrpField = new HTH_TextField(10, HTH_FONT);
    toDivField = new HTH_TextField(3, HTH_FONT);
    toCrdField = new HTH_TextField(2, HTH_FONT);
    toCrdField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        copyCard();
      }
    });

    bold = new JCheckBox("Bold");
    bold.setOpaque(false);
    bold.setFont(HTH_FONT.deriveFont(14.0f));
    bold.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Font f = focusedTextPane.getFont();

        if (bold.isSelected()) {
          focusedTextPane.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
          addModifier("b");
        } else {
          focusedTextPane.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
          rmvModifier("b");
        }
        
        focusedTextPane.requestFocus();
      }
    });

    italic = new JCheckBox("Italic");
    italic.setOpaque(false);
    italic.setFont(HTH_FONT.deriveFont(14.0f));
    italic.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Font f = focusedTextPane.getFont();

        if (italic.isSelected()) {
          focusedTextPane.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
          addModifier("i");
        } else {
          focusedTextPane.setFont(f.deriveFont(f.getStyle() & ~Font.ITALIC));
          rmvModifier("i");
        }
        
        focusedTextPane.requestFocus();
      }
    });

    underline = new JCheckBox("Underline");
    underline.setOpaque(false);
    underline.setFont(HTH_FONT.deriveFont(14.0f));
    underline.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StyledDocument doc = focusedTextPane.getStyledDocument();
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        if (underline.isSelected()) {
          StyleConstants.setUnderline(attrSet, true);
          addModifier("u");
        } else {
          StyleConstants.setUnderline(attrSet, false);
          rmvModifier("u");
        }
        doc.setParagraphAttributes(0, focusedTextPane.getText().length(), attrSet, false);
        
        focusedTextPane.requestFocus();
      }
    });

    left = new JRadioButton("Left");
    left.setOpaque(false);
    left.setFont(HTH_FONT.deriveFont(14.0f));

    center = new JRadioButton("Center");
    center.setOpaque(false);
    center.setFont(HTH_FONT.deriveFont(14.0f));

    right = new JRadioButton("Right");
    right.setOpaque(false);
    right.setFont(HTH_FONT.deriveFont(14.0f));

    ButtonGroup alignments = new ButtonGroup();
    alignments.add(left);
    alignments.add(center);
    alignments.add(right);

    small = new JRadioButton("Small");
    small.setOpaque(false);
    small.setFont(HTH_FONT.deriveFont(14.0f));
    small.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        focusedTextPane.setFont(focusedTextPane.getFont().deriveFont(12.0f));
        rmvModifier("large");
        addModifier("small");
        
        focusedTextPane.requestFocus();
      }
    });

    normal = new JRadioButton("Medium");
    normal.setOpaque(false);
    normal.setFont(HTH_FONT.deriveFont(14.0f));
    normal.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        focusedTextPane.setFont(focusedTextPane.getFont().deriveFont(14.0f));
        rmvModifier("large", "small");
        
        focusedTextPane.requestFocus();
      }
    });

    large = new JRadioButton("Large");
    large.setOpaque(false);
    large.setFont(HTH_FONT.deriveFont(14.0f));
    large.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        focusedTextPane.setFont(focusedTextPane.getFont().deriveFont(16.0f));
        rmvModifier("small");
        addModifier("large");
        
        focusedTextPane.requestFocus();
      }
    });

    ButtonGroup sizes = new ButtonGroup();
    sizes.add(small);
    sizes.add(normal);
    sizes.add(large);

    smallLogo = new JRadioButton("Do Not Span");
    smallLogo.setOpaque(false);
    smallLogo.setFont(HTH_FONT.deriveFont(14.0f));
    smallLogo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isFront) {
          selectedCard.setFrontLarge("");
        } else {
          selectedCard.setBackLarge("");
        }
      }
    });

    hLargeLogo = new JRadioButton("Span Horizontally");
    hLargeLogo.setOpaque(false);
    hLargeLogo.setFont(HTH_FONT.deriveFont(14.0f));
    hLargeLogo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isFront) {
          selectedCard.setFrontLarge("H");
        } else {
          selectedCard.setBackLarge("H");
        }
      }
    });

    vLargeLogo = new JRadioButton("Span Verically");
    vLargeLogo.setOpaque(false);
    vLargeLogo.setFont(HTH_FONT.deriveFont(14.0f));
    vLargeLogo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isFront) {
          selectedCard.setFrontLarge("V");
        } else {
          selectedCard.setBackLarge("V");
        }
      }
    });

    ButtonGroup logoSize = new ButtonGroup();
    logoSize.add(smallLogo);
    logoSize.add(hLargeLogo);
    logoSize.add(vLargeLogo);
  }

  private void setSelectionScreen() {
    resetContent();
    setSelectionFunctionKeys();
    setSelectionControlKeys();
    setSelectionLabels();
    setSelectionInputFields();
    revalidate();
    repaint();
  }

  private void removePromptScreen() {
    promptScreen.removeAll();
    contentScreen.remove(promptScreen);

    restoreFunctionKeys();
    restoreControlKeys();
    revalidate();
    repaint();
  }

  private void setSelectionLabels() {
    int endLineX = 200;

    JLabel insLabel = getLabel("Please Enter:");
    insLabel.setBounds(10, 100, insLabel.getPreferredSize().width, insLabel.getPreferredSize().height);
    contentScreen.add(insLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel grpLabel = getLabel("Block/Group ID");
    grpLabel.setBounds(endLineX - grpLabel.getPreferredSize().width, 160, grpLabel.getPreferredSize().width, grpLabel.getPreferredSize().height);
    contentScreen.add(grpLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel divLabel = getLabel("Division");
    divLabel.setBounds(endLineX - divLabel.getPreferredSize().width, 220, divLabel.getPreferredSize().width, divLabel.getPreferredSize().height);
    contentScreen.add(divLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel crdLabel = getLabel("Card #");
    crdLabel.setBounds(endLineX - crdLabel.getPreferredSize().width, 280, crdLabel.getPreferredSize().width, crdLabel.getPreferredSize().height);
    contentScreen.add(crdLabel, JLayeredPane.DEFAULT_LAYER);
  }

  private void setSelectionInputFields() {
    int strLineX = 250;

    grpField.setBounds(strLineX, 155, grpField.getPreferredSize().width, grpField.getPreferredSize().height);
    contentScreen.add(grpField, JLayeredPane.DEFAULT_LAYER);
    grpField.requestFocus();

    HTH_PromptButton grpPromptBtn = new HTH_PromptButton();
    grpPromptBtn.setBounds(strLineX + grpField.getPreferredSize().width + 10, 160, grpPromptBtn.getPreferredSize().width, grpPromptBtn.getPreferredSize().height);
    grpPromptBtn.addActionListener(new GroupPromptActionListener(grpField, divField, crdField));
    grpPromptBtn.addKeyListener(new GroupPromptKeyListener(grpField, divField, crdField));
    contentScreen.add(grpPromptBtn, JLayeredPane.DEFAULT_LAYER);

    divField.setBounds(strLineX, 215, divField.getPreferredSize().width, divField.getPreferredSize().height);
    contentScreen.add(divField, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton divPromptBtn = new HTH_PromptButton();
    divPromptBtn.setBounds(strLineX + divField.getPreferredSize().width + 10, 220, divPromptBtn.getPreferredSize().width, divPromptBtn.getPreferredSize().height);
    divPromptBtn.addActionListener(new DivisionPromptActionListener(grpField, divField, crdField));
    divPromptBtn.addKeyListener(new DivisionPromptKeyListener(grpField, divField, crdField));
    contentScreen.add(divPromptBtn, JLayeredPane.DEFAULT_LAYER);

    crdField.setBounds(strLineX, 275, crdField.getPreferredSize().width, crdField.getPreferredSize().height);
    contentScreen.add(crdField, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton crdPromptBtn = new HTH_PromptButton();
    crdPromptBtn.setBounds(strLineX + crdField.getPreferredSize().width + 10, 280, crdPromptBtn.getPreferredSize().width, crdPromptBtn.getPreferredSize().height);
    crdPromptBtn.addActionListener(new CardPromptActionListener(grpField, divField, crdField));
    crdPromptBtn.addKeyListener(new CardPromptKeyListener(grpField, divField, crdField));
    contentScreen.add(crdPromptBtn, JLayeredPane.DEFAULT_LAYER);

  }

  private void setSelectionControlKeys() {
    controlPanel.removeAll();

    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        exitProgram();
      }
    });
    controlPanel.add(exitBtn);

    controlPanel.add(getGhostPanel(new Dimension(25, 25)));

    HTH_ControlButton okBtn = new HTH_ControlButton("OK");
    okBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startEditor();
      }
    });
    controlPanel.add(okBtn);

    controlKeys = new HTH_ControlButton[] {exitBtn, okBtn};
  }

  private void restoreControlKeys() {
    controlPanel.removeAll();

    if (controlKeys.length >= 1) {
      controlPanel.add(controlKeys[0]);
      for (int idx = 1; idx < controlKeys.length; idx++) {
        controlPanel.add(getGhostPanel(new Dimension(25, 25)));
        controlPanel.add(controlKeys[idx]);
      }
    }
  }

  private void setSelectionFunctionKeys() {
    HTH_FunctionButton addBtn, cpyBtn, rmvBtn;
    addBtn = new HTH_FunctionButton("Add Card");
    addBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startAddProcess();
      }
    });

    cpyBtn = new HTH_FunctionButton("Block/Group/Div Copy");
    cpyBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startCopyProcess();
      }
    });

    rmvBtn = new HTH_FunctionButton("Delete");
    rmvBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startRemoveProcess();
      }
    });

    functionKeys = new HTH_FunctionButton[]{addBtn, cpyBtn, rmvBtn};
    setFunctionKeys(functionKeys);
  }

  private void restoreFunctionKeys() {
    setFunctionKeys(functionKeys);
  }

  private void startEditor() {
    final String group = grpField.getText();
    final String div = divField.getText();
    final String num = crdField.getText();

    boolean isExisted = IDCARD.isCardExisted(group, div, num);
    if (isExisted) {
      showLoading();
      Thread idLoader = new Thread(new Runnable() {
        @Override
        public void run() {
          selectedCard = IDCARD.getCard(group, div, num);
          editCard(true); 
        }
      });
      idLoader.start();
    } else {
      String msg = "Record not found.";
      JOptionPane.showMessageDialog(this, msg, "Record Not Found", JOptionPane.WARNING_MESSAGE);
    }
  }

  private void startAddProcess() {
    String group = grpField.getText();
    String div = divField.getText();
    String num = crdField.getText();

    if (num.isEmpty()) {
      crdField.setText("00");
      num = "00";
    }

    if (group.isEmpty()) {
      String msg = "Please select a group or type in block ID";
      JOptionPane.showMessageDialog(this, msg, "Group/Block ID Is Empty", JOptionPane.WARNING_MESSAGE);
    } else {
      boolean isExisted = IDCARD.isCardExisted(group, div, num);
      if (isExisted) {
        String msg = "ID card already exists";
        JOptionPane.showMessageDialog(this, msg, "Duplicate Record Found", JOptionPane.WARNING_MESSAGE);
      } else {
        selectedCard = new IDCard(group.toUpperCase(), div.toUpperCase(), num.toUpperCase(), "");
        editCard(true);
      }
    }
  }

  private void startCopyProcess() {
    setPromptFunctionKeys();
    functionKeys = new HTH_FunctionButton[0];
    fromGrpField.setText("");
    fromDivField.setText("");
    fromCrdField.setText("");
    toGrpField.setText("");
    toDivField.setText("");
    toCrdField.setText("");

    showCopyPrompt();

    revalidate();
    repaint();
  }

  private void startRemoveProcess() {
    String group = grpField.getText();
    String div = divField.getText();
    String num = crdField.getText();

    boolean isExisted = IDCARD.isCardExisted(group, div, num);
    if (isExisted) {
      String msg = "Are you sure?";
      int result = JOptionPane.showConfirmDialog(this, msg, "Confirmation", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        IDCARD.delCard(group, div, num);
        grpField.setText("");
        divField.setText("");
        crdField.setText("");
      }
    } else {
      String msg = "Record not found";
      JOptionPane.showMessageDialog(this, msg, "Record Not Found", JOptionPane.WARNING_MESSAGE);
    }
  }

  private void setFunctionKeys(HTH_FunctionButton[] buttons) {
    resetFunctionKeys();
    addFunctionKeys(buttons);
  }

  private void resetContent() {
    setHeaderTitle(null);
    controlPanel.removeAll();
    contentScreen.removeAll();
  }

  private JLabel getLabel(String txt) {
    JLabel label = new JLabel(txt);
    label.setFont(HTH_FONT);
    int txtW = label.getFontMetrics(HTH_FONT).stringWidth(txt);
    int txtH = label.getFontMetrics(HTH_FONT).getHeight();
    label.setPreferredSize(new Dimension(txtW, txtH));

    return label;
  }

  private JLabel getLabel(int charCount, Font font) {
    String str = "";
    for (int c = 0; c <= charCount; c++) {
      str += "M";
    }

    JLabel label = new JLabel();
    label.setFont(font);
    int txtW = label.getFontMetrics(font).stringWidth(str);
    int txtH = label.getFontMetrics(font).getHeight();
    label.setPreferredSize(new Dimension(txtW, txtH));
    label.addMouseListener(new LabelMouseListener(label));

    return label;
  }

  private JPanel getGhostPanel(Dimension size) {
    JPanel ghostPanel = new JPanel();
    ghostPanel.setPreferredSize(size);
    ghostPanel.setMaximumSize(size);
    ghostPanel.setMinimumSize(size);
    ghostPanel.setOpaque(false);

    return ghostPanel;
  }

  private void showCopyPrompt() {
    setHeaderTitle("Copy Card");

    JLayeredPane copyPane = new JLayeredPane();
    copyPane.setOpaque(true);
    copyPane.setBackground(Color.WHITE);
    copyPane.setBounds(0, 0, contentScreen.getSize().width, contentScreen.getSize().height);
    contentScreen.add(copyPane, JLayeredPane.PALETTE_LAYER);

    setCopyPromptLabels(copyPane);
    setCopyPromptFields(copyPane);
    setCopyControlBtn();
  }

  private void setCopyPromptLabels(JLayeredPane prompt) {
    JLabel insLabel = getLabel("Enter the Block/Group you wish to copy from and to.");
    insLabel.setBounds(10, 100, insLabel.getPreferredSize().width, insLabel.getPreferredSize().height);
    prompt.add(insLabel, JLayeredPane.DEFAULT_LAYER);

    int endLineX = 200;
    JLabel fromGrpLabel = getLabel("From Block/Group ID");
    fromGrpLabel.setBounds(endLineX - fromGrpLabel.getPreferredSize().width, 160, fromGrpLabel.getPreferredSize().width, fromGrpLabel.getPreferredSize().height);
    prompt.add(fromGrpLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel fromDivLabel = getLabel("From Division");
    fromDivLabel.setBounds(endLineX - fromDivLabel.getPreferredSize().width, 220, fromDivLabel.getPreferredSize().width, fromDivLabel.getPreferredSize().height);
    prompt.add(fromDivLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel fromCrdLabel = getLabel("From Card #");
    fromCrdLabel.setBounds(endLineX - fromCrdLabel.getPreferredSize().width, 280, fromCrdLabel.getPreferredSize().width, fromCrdLabel.getPreferredSize().height);
    prompt.add(fromCrdLabel, JLayeredPane.DEFAULT_LAYER);

    endLineX += contentScreen.getSize().width / 2;
    JLabel toGrpLabel = getLabel("To Block/Group ID");
    toGrpLabel.setBounds(endLineX - toGrpLabel.getPreferredSize().width, 160, toGrpLabel.getPreferredSize().width, toGrpLabel.getPreferredSize().height);
    prompt.add(toGrpLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel toDivLabel = getLabel("To Division");
    toDivLabel.setBounds(endLineX - toDivLabel.getPreferredSize().width, 220, toDivLabel.getPreferredSize().width, toDivLabel.getPreferredSize().height);
    prompt.add(toDivLabel, JLayeredPane.DEFAULT_LAYER);

    JLabel toCrdLabel = getLabel("To Card #");
    toCrdLabel.setBounds(endLineX - toCrdLabel.getPreferredSize().width, 280, toCrdLabel.getPreferredSize().width, toCrdLabel.getPreferredSize().height);
    prompt.add(toCrdLabel, JLayeredPane.DEFAULT_LAYER);
  }

  private void setCopyPromptFields(JLayeredPane prompt) {
    int strLineX = 250;
    fromGrpField.setBounds(strLineX, 155, fromGrpField.getPreferredSize().width, fromGrpField.getPreferredSize().height);
    prompt.add(fromGrpField, JLayeredPane.DEFAULT_LAYER);

    fromDivField.setBounds(strLineX, 215, fromDivField.getPreferredSize().width, fromDivField.getPreferredSize().height);
    prompt.add(fromDivField, JLayeredPane.DEFAULT_LAYER);

    fromCrdField.setBounds(strLineX, 275, fromCrdField.getPreferredSize().width, fromCrdField.getPreferredSize().height);
    prompt.add(fromCrdField, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton fromGrpPromptBtn = new HTH_PromptButton();
    fromGrpPromptBtn.setBounds(strLineX + fromGrpField.getPreferredSize().width + 10, 160, fromGrpPromptBtn.getPreferredSize().width, fromGrpPromptBtn.getPreferredSize().height);
    fromGrpPromptBtn.addActionListener(new GroupPromptActionListener(fromGrpField, fromDivField, fromCrdField));
    fromGrpPromptBtn.addKeyListener(new GroupPromptKeyListener(fromGrpField, fromDivField, fromCrdField));
    prompt.add(fromGrpPromptBtn, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton fromDivPromptBtn = new HTH_PromptButton();
    fromDivPromptBtn.setBounds(strLineX + fromDivField.getPreferredSize().width + 10, 220, fromDivPromptBtn.getPreferredSize().width, fromDivPromptBtn.getPreferredSize().height);
    fromDivPromptBtn.addActionListener(new DivisionPromptActionListener(fromGrpField, fromDivField, fromCrdField));
    fromDivPromptBtn.addKeyListener(new DivisionPromptKeyListener(fromGrpField, fromDivField, fromCrdField));
    prompt.add(fromDivPromptBtn, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton fromCrdPromptBtn = new HTH_PromptButton();
    fromCrdPromptBtn.setBounds(strLineX + fromCrdField.getPreferredSize().width + 10, 280, fromCrdPromptBtn.getPreferredSize().width, fromCrdPromptBtn.getPreferredSize().height);
    fromCrdPromptBtn.addActionListener(new CardPromptActionListener(fromGrpField, fromDivField, fromCrdField));
    fromCrdPromptBtn.addKeyListener(new CardPromptKeyListener(fromGrpField, fromDivField, fromCrdField));
    prompt.add(fromCrdPromptBtn, JLayeredPane.DEFAULT_LAYER);

    strLineX += contentScreen.getSize().width / 2;
    toGrpField.setBounds(strLineX, 155, toGrpField.getPreferredSize().width, toGrpField.getPreferredSize().height);
    prompt.add(toGrpField, JLayeredPane.DEFAULT_LAYER);

    toDivField.setBounds(strLineX, 215, toDivField.getPreferredSize().width, toDivField.getPreferredSize().height);
    prompt.add(toDivField, JLayeredPane.DEFAULT_LAYER);

    toCrdField.setBounds(strLineX, 275, toCrdField.getPreferredSize().width, toCrdField.getPreferredSize().height);
    prompt.add(toCrdField, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton toGrpPromptBtn = new HTH_PromptButton();
    toGrpPromptBtn.setBounds(strLineX + toGrpField.getPreferredSize().width + 10, 160, toGrpPromptBtn.getPreferredSize().width, toGrpPromptBtn.getPreferredSize().height);
    toGrpPromptBtn.addActionListener(new GroupPromptActionListener(toGrpField, toDivField, toCrdField));
    toGrpPromptBtn.addKeyListener(new GroupPromptKeyListener(toGrpField, toDivField, toCrdField));
    prompt.add(toGrpPromptBtn, JLayeredPane.DEFAULT_LAYER);

    HTH_PromptButton toDivPromptBtn = new HTH_PromptButton();
    toDivPromptBtn.setBounds(strLineX + toDivField.getPreferredSize().width + 10, 220, toDivPromptBtn.getPreferredSize().width, toDivPromptBtn.getPreferredSize().height);
    toDivPromptBtn.addActionListener(new DivisionPromptActionListener(toGrpField, toDivField, toCrdField));
    toDivPromptBtn.addKeyListener(new DivisionPromptKeyListener(toGrpField, toDivField, toCrdField));
    prompt.add(toDivPromptBtn, JLayeredPane.DEFAULT_LAYER);    
  }

  private void setCopyControlBtn() {
    controlPanel.removeAll();

    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setSelectionScreen();
      }
    });
    controlPanel.add(exitBtn);

    controlPanel.add(getGhostPanel(new Dimension(25, 25)));

    HTH_ControlButton okBtn = new HTH_ControlButton("OK");
    okBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        copyCard();
      }
    });
    controlPanel.add(okBtn);

    controlKeys = new HTH_ControlButton[] {exitBtn, okBtn};
  }

  private void showGroupPrompt(final HTH_TextField grpField, final HTH_TextField divField, final HTH_TextField cardField) {
    setPromptFunctionKeys();
    setPromptControlKeys();

    setHeaderTitle("Select Group");
    selGroupList = groupList;

    JPanel headerPanel = setGroupRowPanel(null, null, null, null);
    headerPanel.setBounds(15, 40, headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height);
    promptScreen.add(headerPanel, JLayeredPane.POPUP_LAYER);

    final JScrollPane informationPane = new JScrollPane(setGroupPrompt(grpField, divField, cardField));
    informationPane.setBounds(15, 40 + headerPanel.getPreferredSize().height, headerPanel.getPreferredSize().width + 15, contentScreen.getSize().height * 80 / 100);
    informationPane.getVerticalScrollBar().setPreferredSize(new Dimension(15,informationPane.getBounds().height));
    informationPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    promptScreen.add(informationPane, JLayeredPane.POPUP_LAYER);

    JLabel searchLabel = getLabel("Search");
    searchLabel.setBounds(20, 10, searchLabel.getPreferredSize().width, searchLabel.getPreferredSize().height);
    promptScreen.add(searchLabel, JLayeredPane.POPUP_LAYER);

    final HTH_TextField searchField = new HTH_TextField(30, HTH_FONT);
    searchField.setBounds(30 + searchLabel.getPreferredSize().width, 5, searchField.getPreferredSize().width, searchField.getPreferredSize().height);
    searchField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selGroupList = searchGroup(searchField.getText().trim());
        informationPane.setViewportView(setGroupPrompt(grpField, divField, cardField));
      }
    });
    promptScreen.add(searchField, JLayeredPane.POPUP_LAYER);

    contentScreen.add(promptScreen, JLayeredPane.POPUP_LAYER);

    revalidate();
    repaint();
  }

  private void showDivisionPrompt(HTH_TextField grpField, HTH_TextField divField, HTH_TextField cardField) {
    updateDivList(grpField);
    setPromptFunctionKeys();
    setPromptControlKeys();
    setHeaderTitle("Select Division");

    JPanel headerPanel = setDivisionRowPanel(null, null, null);
    headerPanel.setBounds(50, 40, headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height);
    promptScreen.add(headerPanel, JLayeredPane.POPUP_LAYER);

    JScrollPane informationPane = new JScrollPane(setDivisionPrompt(divField, cardField));
    informationPane.setBounds(50, 40 + headerPanel.getPreferredSize().height, headerPanel.getPreferredSize().width + 15, contentScreen.getSize().height * 70 / 100);
    informationPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, informationPane.getBounds().height));
    informationPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    promptScreen.add(informationPane, JLayeredPane.POPUP_LAYER);

    contentScreen.add(promptScreen, JLayeredPane.POPUP_LAYER);

    revalidate();
    repaint();
  }

  private void showHeaderChooser() {
    selHeaderList = searchHeader("");
    
    setPromptFunctionKeys();
    setPromptControlKeys();
    setHeaderTitle("Select Heading");

    JPanel headerPanel = setHeaderRowPanel(null);
    headerPanel.setBounds(15, 40, headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height);
    promptScreen.add(headerPanel, JLayeredPane.POPUP_LAYER);

    final JScrollPane informationPane = new JScrollPane(setHeaderPrompt());
    informationPane.setBounds(15, 40 + headerPanel.getPreferredSize().height, headerPanel.getPreferredSize().width + 15, contentScreen.getSize().height * 9 / 10);
    informationPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, informationPane.getBounds().height));
    informationPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    promptScreen.add(informationPane, JLayeredPane.POPUP_LAYER);

    JLabel searchLabel = getLabel("Search");
    searchLabel.setBounds(20, 10, searchLabel.getPreferredSize().width, searchLabel.getPreferredSize().height);
    promptScreen.add(searchLabel, JLayeredPane.POPUP_LAYER);

    final HTH_TextField searchField = new HTH_TextField(30, HTH_FONT);
    searchField.setBounds(30 + searchLabel.getPreferredSize().width, 5, searchField.getPreferredSize().width, searchField.getPreferredSize().height);
    searchField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selHeaderList = searchHeader(searchField.getText().trim());
        informationPane.setViewportView(setHeaderPrompt());
      }
    });
    promptScreen.add(searchField, JLayeredPane.POPUP_LAYER);
    
    contentScreen.add(promptScreen, JLayeredPane.POPUP_LAYER);

    revalidate();
    repaint();
  }

  private JPanel setGroupPrompt(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
    JPanel groupPanel = new JPanel();
    groupPanel.setBackground(Color.WHITE);

    if (selGroupList.length == 0) {
      groupPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      groupPanel.add(getLabel("No records found."));
    } else {
      groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
      for (GroupMaster group : selGroupList) {
        groupPanel.add(setGroupRowPanel(group, grpField, divField, crdField));
      }
    }

    return groupPanel;
  }

  private void setPromptFunctionKeys() {
    setFunctionKeys(new HTH_FunctionButton[0]);
  }

  private void setPromptControlKeys() {
    controlPanel.removeAll();

    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        removePromptScreen();
      }
    });
    controlPanel.add(exitBtn);
  }

  private JPanel setGroupRowPanel(final GroupMaster group, final HTH_TextField grpField, final HTH_TextField divField, final HTH_TextField crdField) {
    final JPanel rowPanel = new JPanel();
    rowPanel.setOpaque(true);
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    rowPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    Font font = HTH_FONT.deriveFont(15.0f);
    JLabel idField = getLabel(10, font);
    idField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel vipField = getLabel(3, font);
    vipField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel nameField = getLabel(50, font);
    nameField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel blockField = getLabel(10, font);
    blockField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel statusField = getLabel(10, font);
    statusField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));

    int rowW = idField.getPreferredSize().width + vipField.getPreferredSize().width + nameField.getPreferredSize().width + blockField.getPreferredSize().width + statusField.getPreferredSize().width;
    int rowH = idField.getPreferredSize().height;
    rowPanel.setPreferredSize(new Dimension(rowW, rowH));
    rowPanel.setMinimumSize(new Dimension(rowW, rowH));
    rowPanel.setMaximumSize(new Dimension(rowW, rowH));

    if (group == null) {
      idField.setText("Group ID");
      idField.setForeground(Color.BLACK);

      vipField.setText("VIP");
      vipField.setForeground(Color.BLACK);

      nameField.setText("Name");
      nameField.setForeground(Color.BLACK);

      blockField.setText("Block ID");
      blockField.setForeground(Color.BLACK);

      statusField.setText("Status");
      statusField.setForeground(Color.BLACK);
    } else {
      rowPanel.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          rowPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          rowPanel.setBackground(Color.WHITE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          grpField.setText(group.getID());
          divField.setText("");
          crdField.setText("");

          removePromptScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });

      idField.setText(group.getID());
      idField.setForeground(new Color(0, 0, 150));

      if (group.isVIP()) {
        vipField.setText("VIP");
        vipField.setForeground(new Color(0, 0, 150));
      }

      nameField.setText(group.getName());
      nameField.setForeground(new Color(0, 0, 150));

      blockField.setText(group.getCarrier());
      blockField.setForeground(new Color(0, 0, 150));

      switch(group.getStatus()) {
        case TERMINATED:
          statusField.setText("TERMINATED");
          break;
        case RUN_OUT:
          statusField.setText("RUN OUT");
          break;
        default:
          statusField.setText("ACTIVE");
      }
      statusField.setForeground(new Color(0, 0, 150));
    }

    rowPanel.add(idField);
    rowPanel.add(vipField);
    rowPanel.add(nameField);
    rowPanel.add(blockField);
    rowPanel.add(statusField);

    return rowPanel;
  }

  private GroupMaster[] searchGroup(String key) {
    if (key.isEmpty()) {
      return groupList;
    } else {
      List<GroupMaster> matchedGroup = new ArrayList<GroupMaster>();
      for (GroupMaster group : groupList) {
        if (group.getName().contains(key) || group.getID().contains(key)) {
          matchedGroup.add(group);
        }
      }

      return matchedGroup.toArray(new GroupMaster[0]);
    }
  }
  
  private IDLogo[] searchLogo(String key) {
    if (key.isEmpty()) {
      return IDCIMG.getLogoList();
    } else {
      List<IDLogo> matchedLogo = new ArrayList<IDLogo>();
      for (IDLogo logo : IDCIMG.getLogoList()) {
        if (logo.getName().contains(key) || logo.getDescription().contains(key)) {
          matchedLogo.add(logo);
        }
      }
      
      return matchedLogo.toArray(new IDLogo[0]);
    }
  }
  
  private IDHeader[] searchHeader(String key) {
    if (key.isEmpty()) {
      return IDCFLD.getIDHeaderList();
    } else {
      String lowKey = key.toLowerCase();
      
      List<IDHeader> matchedHeader = new ArrayList<IDHeader>();
      for (IDHeader header : IDCFLD.getIDHeaderList()) {
        String lowDesc = header.getDesc().toLowerCase();
        String lowHeader = header.getHeader().toLowerCase();
        if (lowDesc.contains(lowKey) || lowHeader.contains(lowKey)) {
          matchedHeader.add(header);
        }
      }
      
      return matchedHeader.toArray(new IDHeader[0]);
    }
  }

  private void updateDivList(HTH_TextField grpField) {
    String selectedGroup = grpField.getText().trim();

    if (selectedGroup.isEmpty() ) {
      divisionList = new Division[0];
    } else {
      divisionList = DIVISN.getDivList(selectedGroup);	
    }
  }

  private JPanel setDivisionRowPanel(final Division div, final HTH_TextField divField, final HTH_TextField crdField) {
    final JPanel rowPanel = new JPanel();
    rowPanel.setOpaque(true);
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    rowPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    Font font = HTH_FONT;
    JLabel idField = getLabel(8, font);
    idField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel nameField = getLabel(50, font);
    nameField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));

    int rowW = idField.getPreferredSize().width + nameField.getPreferredSize().width;
    int rowH = idField.getPreferredSize().height;
    rowPanel.setPreferredSize(new Dimension(rowW, rowH));
    rowPanel.setMinimumSize(new Dimension(rowW, rowH));
    rowPanel.setMaximumSize(new Dimension(rowW, rowH));


    if (div == null) {
      idField.setText("Division");
      idField.setForeground(Color.BLACK);

      nameField.setText("Name");
      nameField.setForeground(Color.BLACK);
    } else {
      rowPanel.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          rowPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          rowPanel.setBackground(Color.WHITE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          divField.setText(div.getID());
          crdField.setText("");

          removePromptScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });

      idField.setText(div.getID());
      idField.setForeground(new Color(0, 0, 150));

      nameField.setText(div.getName());
      nameField.setForeground(new Color(0, 0, 150));
    }

    rowPanel.add(idField);
    rowPanel.add(nameField);

    return rowPanel;
  }

  private JPanel setDivisionPrompt(HTH_TextField divField, HTH_TextField crdField) {
    JPanel divPanel = new JPanel();
    divPanel.setBackground(Color.WHITE);

    if (divisionList.length == 0) {
      divPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      divPanel.add(getLabel("No records found."));
    } else {
      divPanel.setLayout(new BoxLayout(divPanel, BoxLayout.Y_AXIS));

      for (Division div : divisionList) {
        divPanel.add(setDivisionRowPanel(div, divField, crdField));
      }
    }

    return divPanel;
  }

  private void updateCardList(HTH_TextField grpField, HTH_TextField divField) {
    String selectedGroup = grpField.getText().trim();
    String selectedDiv = divField.getText().trim();

    if (selectedGroup.isEmpty()) {
      cardList = new IDCard[0];
    } else {
      cardList = IDCARD.getCardList(selectedGroup, selectedDiv);
    }
  }

  private JPanel setHeaderRowPanel(IDHeader header) {
    final JPanel rowPanel = new JPanel();
    rowPanel.setOpaque(true);
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    rowPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    Font font = HTH_FONT.deriveFont(16.0f);
    final JLabel idField = getLabel(6, font);
    idField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel txtField = getLabel(25, font);
    txtField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel descField = getLabel(40, font);
    descField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));

    int rowW = idField.getPreferredSize().width + txtField.getPreferredSize().width + descField.getPreferredSize().width;
    int rowH = idField.getPreferredSize().height;
    rowPanel.setPreferredSize(new Dimension(rowW, rowH));
    rowPanel.setMinimumSize(new Dimension(rowW, rowH));
    rowPanel.setMaximumSize(new Dimension(rowW, rowH));


    if (header == null) {
      idField.setText("ID");
      idField.setForeground(Color.BLACK);
      
      txtField.setText("Heading");
      txtField.setForeground(Color.BLACK);

      descField.setText("Description");
      descField.setForeground(Color.BLACK);
    } else {
      rowPanel.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          rowPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          rowPanel.setBackground(Color.WHITE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          int selStr = focusedTextPane.getSelectionStart();
          int selEnd = focusedTextPane.getSelectionEnd();
          
          String txt = focusedTextPane.getText();
          String strTxt = txt.substring(0, selStr);
          String var = "@@" + idField.getText() + "@@";
          String endTxt = "";
          if (selEnd != txt.length()) {
            endTxt = txt.substring(selEnd);
          }
          focusedTextPane.setText(strTxt + var + endTxt);

          removePromptScreen();
          focusedTextPane.requestFocus();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });

      idField.setText(header.getID());
      idField.setForeground(new Color(0, 0, 150));

      txtField.setText(header.getHeader());
      txtField.setForeground(new Color(0, 0, 150));
      
      descField.setText(header.getDesc());
      descField.setForeground(new Color(0, 0, 150));
    }

    rowPanel.add(idField);
    rowPanel.add(txtField);
    rowPanel.add(descField);

    return rowPanel;
  }

  private JPanel setHeaderPrompt() {
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(Color.WHITE);

    if (selHeaderList.length == 0) {
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      headerPanel.add(getLabel("No records found."));
    } else {
      headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

      for (IDHeader header : selHeaderList) {
        headerPanel.add(setHeaderRowPanel(header));
      }
    }

    return headerPanel;
  }

  private void showLoading() {
    contentScreen.removeAll();
    setFunctionKeys(new HTH_FunctionButton[0]);

    final JLabel loadingLabel = getLabel("Loading ID images...");
    loadingLabel.setBounds(40, 40, loadingLabel.getPreferredSize().width, loadingLabel.getPreferredSize().height);
    contentScreen.add(loadingLabel, JLayeredPane.DEFAULT_LAYER);

    revalidate();
    repaint();
  }

  private void previewCard() {
    setHeaderTitle("Preview ID Card");
    while (!selectedCard.isReady()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
    showID(true);
  }

  private void editCard(boolean isStart) {
    contentScreen.removeAll();

    setHeaderTitle("Edit ID Card");
    if (isStart) {
      String msg = "Enter description of the card";
      Object input = JOptionPane.showInputDialog(this, msg, "Description of Card Type", JOptionPane.PLAIN_MESSAGE, null, null, selectedCard.getDescription());
      if (input != null) {
        selectedCard.setDescription(input.toString());
      }

    }
    isFront = true;
    if (isLogoEditor) {
      editLogos();
    } else {
      editTexts();
    }
  }

  private void confirmExit() {
    String msg = "Do you want to save the change?";
    int option = JOptionPane.showConfirmDialog(this, msg, "Confirm Save", JOptionPane.YES_NO_CANCEL_OPTION);
    if (option == JOptionPane.YES_OPTION) {
      saveCard();
      exitProgram();
    } else if (option == JOptionPane.NO_OPTION) {
      exitProgram();
    }
  }

  private void editTexts() {
    //TODO: Edit card texts.
    contentScreen.removeAll();

    HTH_FunctionButton previewBtn = new HTH_FunctionButton("Preview");
    previewBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        previewCard();
      }
    });

    HTH_FunctionButton turnBtn = isFront ? new HTH_FunctionButton("Back") : new HTH_FunctionButton("Front");
    turnBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        isFront = !isFront;
        editTexts();
      }
    });

    HTH_FunctionButton editLogoBtn = new HTH_FunctionButton("Logos");
    editLogoBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        editLogos();
      }
    });

    HTH_FunctionButton saveBtn = new HTH_FunctionButton("Save");
    saveBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        saveCard();
      }
    });
    
    HTH_FunctionButton headBtn = new HTH_FunctionButton("Headers");
    headBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        showHeaderChooser();
      }
    });
    functionKeys = new HTH_FunctionButton[] {previewBtn, turnBtn, editLogoBtn, saveBtn, headBtn};
    setFunctionKeys(functionKeys);

    controlPanel.removeAll();
    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
        confirmExit();
      }
    });
    controlPanel.add(exitBtn);

    controlPanel.add(getGhostPanel(new Dimension(25, 25)));

    HTH_ControlButton okBtn = new HTH_ControlButton("OK");
    okBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTxt();
      }
    });
    controlPanel.add(okBtn);
    controlKeys = new HTH_ControlButton[] {exitBtn, okBtn};

    showTxtModifiers();
    showTxtEditor();

    revalidate();
    repaint();

    linePanes[0].requestFocus();
    isLogoEditor = false;
  }

  private void editLogos() {
    //TODO: Edit card logos.
    contentScreen.removeAll();

    HTH_FunctionButton previewBtn = new HTH_FunctionButton("Preview");
    previewBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        previewCard();
      }
    });

    HTH_FunctionButton turnBtn = isFront ? new HTH_FunctionButton("Back") : new HTH_FunctionButton("Front");
    turnBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isFront = !isFront;
        editLogos();
      }
    });

    HTH_FunctionButton editTxtBtn = new HTH_FunctionButton("Texts");
    editTxtBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editTexts();
      }
    });

    HTH_FunctionButton saveBtn = new HTH_FunctionButton("Save");
    saveBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveCard();
      }
    });
    functionKeys = new HTH_FunctionButton[] {previewBtn, turnBtn, editTxtBtn, saveBtn};
    setFunctionKeys(functionKeys);

    controlPanel.removeAll();
    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        confirmExit();
      }
    });
    controlPanel.add(exitBtn);
    controlKeys = new HTH_ControlButton[] {exitBtn};

    showLogoModifiers();
    showLogoEditor();

    revalidate();
    repaint();

    logoBtns[focusedIdx].requestFocus();
    isLogoEditor = true;
  }

  private void updateTxt() {
    String[] lines = new String[18];

    for (int idx = 0; idx < lines.length; idx++) {
      JTextPane pane = linePanes[idx];
      lines[idx] = pane.getText();
    }

    if (isFront) {
      selectedCard.setFrontTexts(lines);
    } else {
      selectedCard.setBackTexts(lines);
    }
  }

  private void showTxtModifiers() {
    //TODO: INCOMPLETE....Needed color picker.
    JPanel modPanel = new JPanel();
    modPanel.setOpaque(false);
    modPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
    modPanel.setBounds(2, 2, contentScreen.getSize().width - 4, contentScreen.getSize().height * 15 / 100);
    contentScreen.add(modPanel, JLayeredPane.DEFAULT_LAYER);

    JPanel alignPanel = new JPanel();
    alignPanel.setOpaque(false);
    alignPanel.setLayout(new GridLayout(0, 1));
    alignPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Alignment", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, HTH_FONT.deriveFont(12.0f), Color.BLACK));
    alignPanel.setPreferredSize(new Dimension(contentScreen.getSize().width * 15 / 100, modPanel.getBounds().height));

    ActionListener[] listeners = left.getActionListeners(); 
    if (listeners.length == 1) {
      left.removeActionListener(listeners[0]);
    }
    left.addActionListener(new LeftActionListener());
    alignPanel.add(left);

    listeners = center.getActionListeners();
    if (listeners.length == 1) {
      center.removeActionListener(listeners[0]);
    }
    center.addActionListener(new CenterActionListener());
    alignPanel.add(center);

    listeners = right.getActionListeners();
    if (listeners.length == 1) {
      right.removeActionListener(listeners[0]);
    }
    right.addActionListener(new RightActionListener());
    alignPanel.add(right);
    modPanel.add(alignPanel);

    JPanel fontPanel = new JPanel();
    fontPanel.setOpaque(false);
    fontPanel.setLayout(new GridLayout(0, 1));
    fontPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Font", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, HTH_FONT.deriveFont(12.0f), Color.BLACK));
    fontPanel.setPreferredSize(new Dimension(contentScreen.getSize().width * 15 / 100, modPanel.getBounds().height));
    fontPanel.add(bold);
    fontPanel.add(italic);
    fontPanel.add(underline);
    modPanel.add(fontPanel);

    JPanel sizePanel = new JPanel();
    sizePanel.setOpaque(false);
    sizePanel.setLayout(new GridLayout(0, 1));
    sizePanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Size", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, HTH_FONT.deriveFont(12.0f), Color.BLACK));
    sizePanel.setPreferredSize(new Dimension(contentScreen.getSize().width * 15 / 100, modPanel.getBounds().height));
    sizePanel.add(small);
    sizePanel.add(normal);
    sizePanel.add(large);
    modPanel.add(sizePanel);
  }

  private void showTxtEditor() {
    JPanel linePanel = new JPanel();
    linePanel.setLayout(new GridLayout(18, 0, 0, 0));
    linePanel.setBackground(Color.WHITE);
    linePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    linePanel.setBounds(2, 120, contentScreen.getSize().width - 4, contentScreen.getSize().height * 8 / 10);
    contentScreen.add(linePanel, JLayeredPane.DEFAULT_LAYER);

    String[] lines = isFront ? selectedCard.getFrontTexts() : selectedCard.getBackTexts();
    String[] mods = isFront ? selectedCard.getFrontTextModifiers() : selectedCard.getBackTextModifiers();

    for (int idx = 0; idx < lines.length; idx++) {
      String text = lines[idx];
      String styleList = mods[idx];

      final JTextPane txtPane = new JTextPane();
      txtPane.setPreferredSize(new Dimension(linePanel.getBounds().width, linePanel.getBounds().height / 18));
      txtPane.setMinimumSize(new Dimension(linePanel.getBounds().width, linePanel.getBounds().height / 18));
      txtPane.setMaximumSize(new Dimension(linePanel.getBounds().width, linePanel.getBounds().height / 18));
      txtPane.setFont(DEFAULT_FONT);
      txtPane.setEditable(true);
      txtPane.setOpaque(false);
      txtPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      txtPane.setForeground(Color.BLACK);
      txtPane.getDocument().addDocumentListener(new HTH_DocumentListener(txtPane, 150));
      txtPane.addFocusListener(new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
          focusedTextPane = txtPane;
          updateTxtModifiers();
        }

        @Override
        public void focusLost(FocusEvent e) {
        }
      });

      StyledDocument doc = txtPane.getStyledDocument();
      SimpleAttributeSet alignment = new SimpleAttributeSet();
      StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_CENTER);
      doc.setParagraphAttributes(0, text.length(), alignment, false);

      if (!styleList.isEmpty()) {
        String[] keys = styleList.split(",");
        String value;

        for (String key : keys) {
          value = key.trim();
          switch (value) {
            case "center":
              StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_CENTER);
              doc.setParagraphAttributes(0, text.length(), alignment, false);
              break;
            case "left":
              StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_LEFT);
              doc.setParagraphAttributes(0, text.length(), alignment, false);
              break;
            case "right":
              StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_RIGHT);
              doc.setParagraphAttributes(0, text.length(), alignment, false);
              break;
            case "b":
              txtPane.setFont(txtPane.getFont().deriveFont(Font.BOLD));
              break;
            case "i":
              txtPane.setFont(txtPane.getFont().deriveFont(Font.ITALIC));
              break;
            case "u":
              SimpleAttributeSet attrs = new SimpleAttributeSet();
              StyleConstants.setUnderline(attrs, true);
              doc.setParagraphAttributes(0, text.length(), attrs, false);
              break;
            case "WHITE":
              txtPane.setForeground(new Color(255, 255, 255));
              break;
            case "RED":
              txtPane.setForeground(new Color(255, 0, 0));
              break;
            case "GREEN":
              txtPane.setForeground(new Color(0, 255, 0));
              break;
            case "BLUE":
              txtPane.setForeground(new Color(0, 0, 255));
              break;
            case "small":
              txtPane.setFont(txtPane.getFont().deriveFont(12.0f));
              break;
            case "large":
              txtPane.setFont(txtPane.getFont().deriveFont(16.0f));
              break;
            default:
              if (value.contains("#") && (value.length() == 4 || value.length() == 7)) {
                String colorHex = value;

                if (value.length() == 4) {
                  colorHex = "#" + value.charAt(1) + value.charAt(1) + value.charAt(2) + value.charAt(2) + value.charAt(3) + value.charAt(3);
                }

                txtPane.setForeground(new Color(
                    Integer.valueOf(colorHex.substring(1,3), 16), 
                    Integer.valueOf(colorHex.substring(3,5), 16),
                    Integer.valueOf(colorHex.substring(5,7), 16)));
              }
          }
        }
      }

      try {
        doc.insertString(0, text, alignment);
      } catch (Exception e) {
        e.printStackTrace();
      }

      linePanel.add(txtPane);
      linePanes[idx] = txtPane;
    }
  }

  private void updateTxtModifiers() {
    normal.setSelected(true);
    center.setSelected(true);
    left.setSelected(false);
    right.setSelected(false);
    bold.setSelected(false);;
    italic.setSelected(false);
    underline.setSelected(false);
    small.setSelected(false);
    large.setSelected(false);

    for (int idx = 0; idx < linePanes.length; idx++) {
      JTextPane pane = linePanes[idx];
      if (pane == focusedTextPane) {
        String[] modifiers = isFront ? selectedCard.getFrontTextModifiers() : selectedCard.getBackTextModifiers();
        String mod = modifiers[idx];
        String[] parts = mod.split(",");
        for (String part : parts) {
          String value = part.trim();
          switch (value) {
            case "center":
              center.setSelected(true);
              left.setSelected(false);
              right.setSelected(false);
              break;
            case "left":
              left.setSelected(true);
              center.setSelected(false);
              right.setSelected(false);
              break;
            case "right":
              right.setSelected(true);
              left.setSelected(false);
              center.setSelected(false);
              break;
            case "b":
              bold.setSelected(true);
              break;
            case "i":
              italic.setSelected(true);
              break;
            case "u":
              underline.setSelected(true);
              break;
            case "WHITE":
              break;
            case "RED":
              break;
            case "GREEN":
              break;
            case "BLUE":
              break;
            case "small":
              small.setSelected(true);
              normal.setSelected(false);
              large.setSelected(false);
              break;
            case "large":
              large.setSelected(true);
              small.setSelected(false);
              normal.setSelected(false);
              break;
            default:
              if (value.contains("#") && (value.length() == 4 || value.length() == 7)) {

              }
          }
        }
      }
    }
  }

  private void showCardPrompt(HTH_TextField grpField, HTH_TextField divField, HTH_TextField cardField) {
    updateCardList(grpField, divField);
    setPromptFunctionKeys();
    setPromptControlKeys();
    setHeaderTitle("Select ID Card");

    JPanel headerPanel = setCardRowPanel(null, null);
    headerPanel.setBounds(50, 40, headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height);
    promptScreen.add(headerPanel, JLayeredPane.POPUP_LAYER);

    JScrollPane informationPane = new JScrollPane(setCardPrompt(cardField));
    informationPane.setBounds(50, 40 + headerPanel.getPreferredSize().height, headerPanel.getPreferredSize().width + 15, contentScreen.getSize().height * 70 / 100);
    informationPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, informationPane.getBounds().height));
    informationPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    promptScreen.add(informationPane, JLayeredPane.POPUP_LAYER);

    contentScreen.add(promptScreen, JLayeredPane.POPUP_LAYER);

    revalidate();
    repaint();
  }

  private JPanel setCardRowPanel(final IDCard card, final HTH_TextField crdField) {
    final JPanel rowPanel = new JPanel();
    rowPanel.setOpaque(true);
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    rowPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    Font font = HTH_FONT.deriveFont(17.0f);
    JLabel numField = getLabel(4, font);
    numField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel descField = getLabel(60, font);
    descField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));

    int rowW = numField.getPreferredSize().width + descField.getPreferredSize().width;
    int rowH = numField.getPreferredSize().height;
    rowPanel.setPreferredSize(new Dimension(rowW, rowH));
    rowPanel.setMinimumSize(new Dimension(rowW, rowH));
    rowPanel.setMaximumSize(new Dimension(rowW, rowH));


    if (card == null) {
      numField.setText("Card");
      numField.setForeground(Color.BLACK);

      descField.setText("Description");
      descField.setForeground(Color.BLACK);
    } else {
      rowPanel.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          rowPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          rowPanel.setBackground(Color.WHITE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          crdField.setText(card.getCardNumber());
          crdField.requestFocus();
          removePromptScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });

      numField.setText(card.getCardNumber());
      numField.setForeground(new Color(0, 0, 150));

      descField.setText(card.getDescription());
      descField.setForeground(new Color(0, 0, 150));
    }

    rowPanel.add(numField);
    rowPanel.add(descField);

    return rowPanel;
  }

  private JPanel setCardPrompt(HTH_TextField crdField) {
    JPanel cardPanel = new JPanel();
    cardPanel.setBackground(Color.WHITE);

    if (cardList.length == 0) {
      cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      cardPanel.add(getLabel("No records found."));
    } else {
      cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

      for (IDCard card : cardList) {
        cardPanel.add(setCardRowPanel(card, crdField));
      }
    }

    return cardPanel;
  }
  
  private void showLogoModifiers() {
    JPanel modPanel = new JPanel();
    modPanel.setOpaque(false);
    modPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
    modPanel.setBounds(2, 2, contentScreen.getSize().width - 4, contentScreen.getSize().height * 15 / 100);
    contentScreen.add(modPanel, JLayeredPane.PALETTE_LAYER);

    JPanel sizePanel = new JPanel();
    sizePanel.setOpaque(false);
    sizePanel.setLayout(new GridLayout(0, 1));
    sizePanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Size", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, HTH_FONT.deriveFont(12.0f), Color.BLACK));
    sizePanel.setPreferredSize(new Dimension(contentScreen.getSize().width * 15 / 100, modPanel.getBounds().height));
    sizePanel.add(smallLogo);
    sizePanel.add(hLargeLogo);
    sizePanel.add(vLargeLogo);
    modPanel.add(sizePanel);

    String isLargeFlag = isFront ? selectedCard.getFrontLarge() : selectedCard.getBackLarge();

    if (isLargeFlag.isEmpty()) {
      smallLogo.setSelected(true);
    } else if (isLargeFlag.equals("H")) {
      hLargeLogo.setSelected(true);
    } else if (isLargeFlag.equals("V")) {
      vLargeLogo.setSelected(true);
    }
  }

  private void showLogoEditor() {
    while (!selectedCard.isReady()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }

    JPanel logoPanel = new JPanel();
    logoPanel.setLayout(new GridLayout(3, 3, 0, 0));
    logoPanel.setBackground(Color.WHITE);
    logoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    logoPanel.setBounds(2, 120, contentScreen.getSize().width - 4, contentScreen.getSize().height * 8 / 10);
    contentScreen.add(logoPanel, JLayeredPane.DEFAULT_LAYER);

    Image[] logoList = isFront ? selectedCard.getFrontLogo() : selectedCard.getBackLogo();
    for (int pos = 0; pos < logoList.length; pos++) {
      final int position = pos;
      final JButton imgLabel = new JButton();
      imgLabel.setContentAreaFilled(false);
      imgLabel.setFocusPainted(false);
      imgLabel.setRolloverEnabled(true);
      imgLabel.setFont(HTH_FONT);
      imgLabel.setBorder(null);
      imgLabel.setOpaque(false);
      imgLabel.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          focusedIdx = position;
          showLogoChooser();
        }
      });
      imgLabel.addMouseListener(new MouseListener() {
        private Border border = null;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          border = imgLabel.getBorder();
          imgLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        @Override
        public void mouseExited(MouseEvent e) {
          imgLabel.setBorder(border);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });
      imgLabel.addKeyListener(new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            showLogoChooser();
          } else if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            imgLabel.setIcon(null);
            imgLabel.setText("Click to add logo");
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            imgLabel.setVerticalAlignment(JLabel.CENTER);

            String[] vars = isFront ? selectedCard.getFrontLogoVars() : selectedCard.getBackLogoVars();
            vars[focusedIdx] = "";
            Image[] logos = isFront ? selectedCard.getFrontLogo() : selectedCard.getBackLogo();
            logos[focusedIdx] = null;

            if (isFront) {
              selectedCard.setFrontLogo(logos);
              selectedCard.setFrontLogoVars(vars);
            } else {
              selectedCard.setBackLogo(logos);
              selectedCard.setBackLogoVars(vars);
            }
          }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

      });
      imgLabel.addFocusListener(new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
          focusedIdx = position;
          imgLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        @Override
        public void focusLost(FocusEvent e) {
          imgLabel.setBorder(null);
        }
      });

      if (logoList[pos] == null) {
        imgLabel.setText("Click to add logo");
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
      } else {
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

      logoPanel.add(imgLabel);
      logoBtns[pos] = imgLabel;
    }
  }

  private void showLogoChooser() {
    controlPanel.removeAll();
    HTH_ControlButton exitBtn = new HTH_ControlButton("Exit");
    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        removePromptScreen();
        editLogos();
      }
    });
    controlPanel.add(exitBtn);

    HTH_FunctionButton uploadBtn = new HTH_FunctionButton("Upload");
    uploadBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        uploadLogo();
      }
    });

    HTH_FunctionButton refreshBtn = new HTH_FunctionButton("Refresh");
    refreshBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        refreshLogoPrompt();
      }
    });
    setFunctionKeys(new HTH_FunctionButton[] {uploadBtn, refreshBtn});

    setHeaderTitle("Select ID Logo");
    showLogoList();
  }

  private void showLogoList() {
    selLogoList = IDCIMG.getLogoList();
    
    JPanel headerPanel = setLogoRowPanel(null);
    headerPanel.setBounds(20, 40, headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height);
    promptScreen.add(headerPanel, JLayeredPane.POPUP_LAYER);

    final JScrollPane informationPane = new JScrollPane(setLogoListPrompt());
    informationPane.setBounds(20, 40 + headerPanel.getPreferredSize().height, headerPanel.getPreferredSize().width + 15, contentScreen.getSize().height * 9 / 10);
    informationPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, informationPane.getBounds().height));
    informationPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    promptScreen.add(informationPane, JLayeredPane.POPUP_LAYER);

    JLabel searchLabel = getLabel("Search");
    searchLabel.setBounds(25, 10, searchLabel.getPreferredSize().width, searchLabel.getPreferredSize().height);
    promptScreen.add(searchLabel, JLayeredPane.POPUP_LAYER);
    
    final HTH_TextField searchField = new HTH_TextField(30, HTH_FONT);
    searchField.setBounds(30 + searchLabel.getPreferredSize().width, 5, searchField.getPreferredSize().width, searchField.getPreferredSize().height);
    searchField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selLogoList = searchLogo(searchField.getText().trim());
        informationPane.setViewportView(setLogoListPrompt());
      }
    });
    promptScreen.add(searchField, JLayeredPane.POPUP_LAYER);

    contentScreen.add(promptScreen, JLayeredPane.POPUP_LAYER);

    revalidate();
    repaint();
  }

  private void refreshLogoPrompt() {
    IDCIMG.refreshList();
    promptScreen.removeAll();
    contentScreen.remove(promptScreen);
    showLogoList();
  }
  
  private JPanel setLogoListPrompt() {
    JPanel listPanel = new JPanel();
    listPanel.setBackground(Color.WHITE);

    if (selLogoList.length == 0) {
      listPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      listPanel.add(getLabel("No records found."));
    } else {
      listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

      for (IDLogo logo : selLogoList) {
        listPanel.add(setLogoRowPanel(logo));
      }
    }

    return listPanel;
  }

  private JPanel setLogoRowPanel(final IDLogo logo) {
    final JPanel rowPanel = new JPanel();
    rowPanel.setOpaque(true);
    rowPanel.setBackground(Color.WHITE);
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    rowPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    Font font = HTH_FONT.deriveFont(17.0f);
    JLabel nameField = getLabel(15, font);
    nameField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    JLabel descField = getLabel(52, font);
    descField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
        BorderFactory.createEmptyBorder(3, 3, 3, 3)));

    int rowW = nameField.getPreferredSize().width + descField.getPreferredSize().width;
    int rowH = nameField.getPreferredSize().height;
    rowPanel.setPreferredSize(new Dimension(rowW, rowH));
    rowPanel.setMinimumSize(new Dimension(rowW, rowH));
    rowPanel.setMaximumSize(new Dimension(rowW, rowH));

    if (logo == null) {
      nameField.setText("Name");
      nameField.setForeground(Color.BLACK);

      descField.setText("Description");
      descField.setForeground(Color.BLACK);
    } else {
      rowPanel.addMouseListener(new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          rowPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          rowPanel.setBackground(Color.WHITE);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          String[] logoVars = isFront ? selectedCard.getFrontLogoVars() : selectedCard.getBackLogoVars();
          logoVars[focusedIdx] = logo.getName();

          Image[] logos = isFront ? selectedCard.getFrontLogo() : selectedCard.getBackLogo();
          logos[focusedIdx] = iSeries.downloadImages(new String[] {"/MobileApp/" + HTH_IDC.member + "/Logos/" + logo.getName().replace("&%", "") + ".PNG"})[0];

          if (isFront) {
            selectedCard.setFrontLogoVars(logoVars);
            selectedCard.setFrontLogo(logos);
          } else {
            selectedCard.setFrontLogoVars(logoVars);
            selectedCard.setFrontLogo(logos);
          }
          editLogos();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
      });

      nameField.setText(logo.getName());
      nameField.setForeground(new Color(0, 0, 150));

      descField.setText(logo.getDescription());
      descField.setForeground(new Color(0, 0, 150));
    }

    rowPanel.add(nameField);
    rowPanel.add(descField);

    return rowPanel;
  }

  private void uploadLogo() {
    JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));

    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File input = fileChooser.getSelectedFile();
// TODO: Remove Print Statement
      System.out.println(input.getAbsolutePath());

      JLabel nameLabel = getLabel(15, HTH_FONT.deriveFont(14.0f));
      nameLabel.setText("Name:");
      nameLabel.setHorizontalAlignment(JLabel.RIGHT);
      
      HTH_TextField nameField = new HTH_TextField(13, HTH_FONT.deriveFont(14.0f), BorderFactory.createEmptyBorder(5, 5, 5, 5));
      
      JPanel namePanel = new JPanel();
      namePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 0));
      namePanel.setOpaque(false);
      namePanel.add(nameLabel);
      namePanel.add(nameField);
      
      JLabel descLabel = getLabel(15, HTH_FONT.deriveFont(14.0f));
      descLabel.setText("Description:");
      descLabel.setHorizontalAlignment(JLabel.RIGHT);
      
      HTH_TextField descField = new HTH_TextField(60, HTH_FONT.deriveFont(14.0f), BorderFactory.createEmptyBorder(5, 5, 5, 5));
      
      JPanel descPanel = new JPanel();
      descPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 0));
      descPanel.setOpaque(false);
      descPanel.add(descLabel);
      descPanel.add(descField);

      JPanel dialogPanel = new JPanel();
      dialogPanel.setOpaque(false);
      dialogPanel.setLayout(new GridLayout(2, 1, 0, 5));
      dialogPanel.add(namePanel);
      dialogPanel.add(descPanel);
      
      int result = JOptionPane.showConfirmDialog(null, dialogPanel, 
               "Enter Name And Description", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
        String name = nameField.getText();
        String desc = descField.getText();
        IDCIMG.uploadFile(input, name, desc);
        refreshLogoPrompt();
      }
    }
  }

  private void showID(final boolean isFront) {
    contentScreen.removeAll();

    HTH_FunctionButton editBtn = new HTH_FunctionButton("Edit");
    editBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editCard(false);
      }
    });
    HTH_FunctionButton turnBtn = isFront ? new HTH_FunctionButton("Back") : new HTH_FunctionButton("Front");
    turnBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showID(!isFront);
      }
    });
    HTH_FunctionButton pdfBtn = new HTH_FunctionButton("Save all ID cards");
    pdfBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        savePDF();
      }
    });
    HTH_FunctionButton printBtn = new HTH_FunctionButton("Print all");
    printBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        printCard();
      }
    });
    functionKeys = new HTH_FunctionButton[] {editBtn, turnBtn, pdfBtn, printBtn};
    setFunctionKeys(functionKeys);

    cardFramePanel = new HTH_CardScreen(selectedCard, isFront);
    cardFramePanel.setBounds(0, 0, contentScreen.getSize().width, contentScreen.getSize().height);
    JLayeredPane cardContentPanel = cardFramePanel.getCardContentPanel();
    cardContentPanel.setBounds(getCardBound());

    cardFramePanel.add(setCardBorder(), JLayeredPane.DEFAULT_LAYER);
    cardFramePanel.add(cardContentPanel, JLayeredPane.PALETTE_LAYER);

    contentScreen.add(cardFramePanel, JLayeredPane.DEFAULT_LAYER);
    revalidate();
    repaint();
  }

  private void addModifier(String... modsToAdd) {
    for (int idx = 0; idx < linePanes.length; idx++) {
      if (linePanes[idx] == focusedTextPane) {
        String[] modifiers = isFront ? selectedCard.getFrontTextModifiers() : selectedCard.getBackTextModifiers();
        String modifier = modifiers[idx];
        String[] mods = modifier.split(",");
        for (String modToAdd : modsToAdd) {
          for (String mod : mods) {
            if (mod.equals(modToAdd)) {
              modToAdd = null;
              break;
            }
          }
          if (modToAdd != null) {
            modifier += "," + modToAdd;
          }
        }
        if (modifier.startsWith(",")) {
          modifiers[idx] = modifier.substring(1);
        } else {
          modifiers[idx] = modifier;
        }

        if (isFront) {
          selectedCard.setFrontTextModifiers(modifiers);
        } else {
          selectedCard.setBackTextModifiers(modifiers);
        }
        return;
      }
    }
  }

  private void rmvModifier(String... modsToRmv) {
    for (int idx = 0; idx < linePanes.length; idx++) {
      if (linePanes[idx] == focusedTextPane) {
        String[] modifiers = isFront ? selectedCard.getFrontTextModifiers() : selectedCard.getBackTextModifiers();
        String[] mods = modifiers[idx].split(",");
        String newMod = "";
        for (String mod : mods) {
          if (!mod.isEmpty()) {
            for (String modToRmv : modsToRmv) {
              if (mod.equals(modToRmv)) {
                mod = null;
                break;
              }
            }
            if (mod != null) {
              newMod += mod + ",";
            }
          }
        }

        if (newMod.endsWith(",")) {
          modifiers[idx] = newMod.substring(0, newMod.length() - 1);
        } else {
          modifiers[idx] = newMod;
        }

        if (isFront) {
          selectedCard.setFrontTextModifiers(modifiers);
        } else {
          selectedCard.setBackTextModifiers(modifiers);
        }
        return;
      }
    }
  }

  private void saveCard() {
    IDCARD.savCard(selectedCard);
  }

  private void copyCard() {
    String fromGrp = fromGrpField.getText().trim();
    String fromDiv = fromDivField.getText().trim();
    String fromCrd = fromCrdField.getText().trim();

    String toGrp = toGrpField.getText().trim();
    String toDiv = toDivField.getText().trim();
    String toCrd = toCrdField.getText().trim();

    if (toCrd.isEmpty()) {
      toCrdField.setText("00");
      toCrd = "00";
    }

    if (fromGrp.isEmpty() || fromCrd.isEmpty() || toGrp.isEmpty()) {
      String msg = "Group/Block ID and From card number cannot be empty.";
      JOptionPane.showMessageDialog(this, msg, "Mandatory Fields Are Empty", JOptionPane.WARNING_MESSAGE);
    } else {
      boolean isFromCardExisted = IDCARD.isCardExisted(fromGrp, fromDiv, fromCrd);
      boolean isToCardExisted = IDCARD.isCardExisted(toGrp, toDiv, toCrd);

      if (isFromCardExisted && !isToCardExisted) {
        IDCARD.cpyCard(fromGrp, fromDiv, fromCrd, toGrp, toDiv, toCrd);
        setSelectionScreen();
      } else if (isToCardExisted) {
        String msg = "To card exists. Are you sure to replace?";
        int option = JOptionPane.showConfirmDialog(this, msg, "Replace Card", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
          IDCARD.delCard(toGrp, toDiv, toCrd);
          IDCARD.cpyCard(fromGrp, fromDiv, fromCrd, toGrp, toDiv, toCrd);
          setSelectionScreen();
        }
      } else {
        String msg = "From card is not found.";
        JOptionPane.showMessageDialog(this, msg, "From Card Not Found", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  private void savePDF() {
    File front, back, output;

    JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Document", "pdf"));

    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      output = fileChooser.getSelectedFile();

      if (!output.getName().endsWith(".pdf") && !output.getName().endsWith(".PDF")) {
        output = new File(output.getParent(), output.getName() + ".pdf");
      }

      PDDocument doc = new PDDocument();
      PDPage page;
      try {
        page = new PDPage();

        showID(true);
        front = generateIDImg("Front");

        showID(false);
        back = generateIDImg("Back");

        float midHeight, midWidth;
        midHeight = page.getArtBox().getHeight() / 2; // Middle height of the page.
        midWidth = page.getArtBox().getWidth() / 2; // Middle width of the page.

        PDPageContentStream contents = new PDPageContentStream(doc, page); // Stream to write contents on page.
        PDImageXObject ftImg = PDImageXObject.createFromFile(front.getAbsolutePath(), doc);
        contents.drawImage(ftImg, midWidth - ID_X_OFFSET, midHeight + ID_FRONT_Y_OFFSET, ID_WIDTH, ID_HEIGHT);

        PDImageXObject bkImg = PDImageXObject.createFromFile(back.getAbsolutePath(), doc);
        contents.drawImage(bkImg, midWidth - ID_X_OFFSET, midHeight - ID_BACK_Y_OFFSET, ID_WIDTH, ID_HEIGHT);

        contents.close(); // Release the stream.
        doc.addPage(page);

        front.delete();
        back.delete();

        doc.save(output);
        doc.close();
        Desktop.getDesktop().open(output);
      } catch (FileNotFoundException fnfe) {
        showID(true);

        String errMsg = "A file name can't contain any of the following characters:\n:\"<>|";
        JOptionPane.showMessageDialog(this, errMsg, "Error on saving ID cards", JOptionPane.ERROR_MESSAGE);
        return;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void printCard() {
    PrinterJob job = PrinterJob.getPrinterJob();

    PageFormat pf = job.defaultPage();
    Paper p = pf.getPaper();

    p.setImageableArea(3, 3, p.getWidth() - 6, p.getHeight() - 6);
    pf.setPaper(p);

    PageFormat page = job.validatePage(pf);
    job.setPrintable(this, page);

    boolean ok = job.printDialog();
    if (ok) {
      try {
        job.print();
      } catch (PrinterException pe) {
        pe.printStackTrace();
      }
    } 
  }

  private void exitProgram() {
    System.exit(0);
  }

  private File generateIDImg(String name) throws Exception {
    File file = File.createTempFile(name, ".png");

    JLayeredPane cardContentPanel = cardFramePanel.getCardContentPanel();
    BufferedImage cardImg = new Robot().createScreenCapture(cardContentPanel.getBounds());
    Graphics2D cardGraphics = cardImg.createGraphics();
    cardContentPanel.paint(cardGraphics);

    BufferedImage img = new BufferedImage(WIDTH + 10, HEIGHT + 10, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D imgGraphics = img.createGraphics();
    imgGraphics.setColor(Color.WHITE);
    imgGraphics.fillRect(0, 0, WIDTH + 10, HEIGHT + 10); // Fill the color of the card.

    imgGraphics.setColor(Color.BLACK);
    imgGraphics.setStroke(new BasicStroke(1));
    imgGraphics.drawRoundRect(5, 5, WIDTH + 4, HEIGHT + 4, 10, 10); 
    imgGraphics.drawImage(cardImg, 7, 7, WIDTH, HEIGHT, null);
    ImageIO.write(img, "png", file);

    return file;
  }

  private Rectangle getCardBound() {
    int contentWidth = contentScreen.getSize().width;
    int contentHeight = contentScreen.getSize().height - controlPanel.getSize().height;
    int x = (contentWidth / 2) - (WIDTH / 2);
    int y = (contentHeight / 2) - (HEIGHT / 2);

    return new Rectangle(x, y, WIDTH, HEIGHT);
  }

  private JPanel setCardBorder() {
    Rectangle cardBound = getCardBound();
    int padding = 3;

    JPanel borderPanel = new JPanel();
    borderPanel.setOpaque(false);
    borderPanel.setBounds(cardBound.x - padding, cardBound.y - padding, cardBound.width + (2 * padding), cardBound.height + (2 * padding));
    borderPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

    return borderPanel;
  }

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

  public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
    // We have 2 pages for each id, and 'page' is zero-based.
    if (page >= 2) {
      showID(false);

      String msg = "Printer is printing the sample card.";
      JOptionPane.showMessageDialog(this, msg, "Printing ID card", JOptionPane.INFORMATION_MESSAGE);
      return NO_SUCH_PAGE;
    }

    boolean isFront;
    if (page % 2 == 0) {
      isFront = true;
    } else {
      isFront = false;
    }

    showID(isFront);
    isFront = !isFront;
    cardFramePanel.applyCover();

    JLayeredPane cardContentPanel = cardFramePanel.getCardContentPanel();
    Dimension dim;
    double cHeight, cWidth, pHeight, pWidth, pXStart, pYStart, xRatio, yRatio;
    dim = cardContentPanel.getSize();
    cHeight = dim.getHeight();
    cWidth = dim.getWidth();
    pHeight = pf.getImageableHeight();
    pWidth = pf.getImageableWidth();
    pXStart = pf.getImageableX();
    pYStart = pf.getImageableY();
    xRatio = pWidth / cWidth;
    yRatio = pHeight / cHeight;
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(pXStart, pYStart);
    g2d.scale(xRatio, yRatio);
    cardContentPanel.printAll(g2d);
    g2d.dispose();
    cardContentPanel.revalidate();

    // Tell the caller that this page is part of the printed document
    return PAGE_EXISTS;
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    exitProgram();
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  private class HTH_DocumentListener implements DocumentListener {
    private String value;
    private JTextPane field;
    private int limit;

    public HTH_DocumentListener(JTextPane field, int limit) {
      this.field = field;
      this.limit = limit;
      this.value = "";
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
      validateTxt();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
    }

    private void validateTxt() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if (field.getText().length() > limit) {
            int cursorPos = field.getCaretPosition() - 1;
            field.setText(value);
            field.setCaretPosition(cursorPos);
          } else {
            value = field.getText();
          }
        }
      });
    }
  }

  private class GroupPromptActionListener implements ActionListener {
    private HTH_TextField grpField, divField, crdField;

    public GroupPromptActionListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      showGroupPrompt(grpField, divField, crdField);
    }
  }

  private class GroupPromptKeyListener implements KeyListener {
    private HTH_TextField grpField, divField, crdField;

    public GroupPromptKeyListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        showGroupPrompt(grpField, divField, crdField);
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  }

  private class DivisionPromptActionListener implements ActionListener {
    private HTH_TextField grpField, divField, crdField;

    public DivisionPromptActionListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      showDivisionPrompt(grpField, divField, crdField);
    }
  }

  private class DivisionPromptKeyListener implements KeyListener {
    private HTH_TextField grpField, divField, crdField;

    public DivisionPromptKeyListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        showDivisionPrompt(grpField, divField, crdField);
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  }

  private class CardPromptActionListener implements ActionListener {
    private HTH_TextField grpField, divField, crdField;

    public CardPromptActionListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      showCardPrompt(grpField, divField, crdField);
    }
  }

  private class CardPromptKeyListener implements KeyListener {
    private HTH_TextField grpField, divField, crdField;

    public CardPromptKeyListener(HTH_TextField grpField, HTH_TextField divField, HTH_TextField crdField) {
      this.grpField = grpField;
      this.divField = divField;
      this.crdField = crdField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        showCardPrompt(grpField, divField, crdField);
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  }

  private class LabelMouseListener implements MouseListener {
    private JLabel field;

    public LabelMouseListener(JLabel field) {
      this.field = field;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (field.getParent() != null) {
        MouseListener[] listeners = field.getParent().getMouseListeners();
        for (MouseListener listener : listeners) {
          listener.mouseClicked(e);
        }
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      if (field.getParent() != null) {
        MouseListener[] listeners = field.getParent().getMouseListeners();
        for (MouseListener listener : listeners) {
          listener.mouseEntered(e);
        }
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      if (field.getParent() != null) {
        MouseListener[] listeners = field.getParent().getMouseListeners();
        for (MouseListener listener : listeners) {
          listener.mouseExited(e);
        }
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {
      if (field.getParent() != null) {
        MouseListener[] listeners = field.getParent().getMouseListeners();
        for (MouseListener listener : listeners) {
          listener.mousePressed(e);
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (field.getParent() != null) {
        MouseListener[] listeners = field.getParent().getMouseListeners();
        for (MouseListener listener : listeners) {
          listener.mouseReleased(e);
        }
      }
    }
  }

  private class LeftActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        StyledDocument doc = focusedTextPane.getStyledDocument();
        SimpleAttributeSet alignment = new SimpleAttributeSet();
        StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(0, focusedTextPane.getText().length(), alignment, false);
        rmvModifier("center", "right");
        addModifier("left");
        
        focusedTextPane.requestFocus();
    }
  }

  private class CenterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        StyledDocument doc = focusedTextPane.getStyledDocument();
        SimpleAttributeSet alignment = new SimpleAttributeSet();
        StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, focusedTextPane.getText().length(), alignment, false);
        rmvModifier("right", "left");
        addModifier("center");
        
        focusedTextPane.requestFocus();
    }
  }

  private class RightActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        StyledDocument doc = focusedTextPane.getStyledDocument();
        SimpleAttributeSet alignment = new SimpleAttributeSet();
        StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_RIGHT);
        doc.setParagraphAttributes(0, focusedTextPane.getText().length(), alignment, false);
        rmvModifier("left", "center");
        addModifier("right");
        
        focusedTextPane.requestFocus();
    }
  }
}
