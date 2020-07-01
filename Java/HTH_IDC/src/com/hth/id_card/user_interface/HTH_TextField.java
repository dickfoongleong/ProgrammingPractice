package com.hth.id_card.user_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class HTH_TextField extends JTextField {

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 4L;
	
	private int limit;
	
	public HTH_TextField(int limit, Font font) {
		super();
		
		this.limit = limit;
		
		setBackground(Color.WHITE);
		setForeground(new Color(0, 0, 150));
		setHorizontalAlignment(LEFT);
		setFont(font);
		setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addMouseListener(new HTH_TextFieldMouseListener(this));
		getDocument().addDocumentListener(new HTH_DocumentListener(this));
		AbstractDocument doc = (AbstractDocument) getDocument();
		doc.setDocumentFilter(new UppercaseDocumentFilter());
		
		Dimension size = getTextSize();
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	public HTH_TextField(int limit, Font font, Border border) {
	  super();
      
      this.limit = limit;
      
      setBackground(Color.WHITE);
      setForeground(new Color(0, 0, 150));
      setHorizontalAlignment(LEFT);
      setFont(font);
      setBorder(border);
      getDocument().addDocumentListener(new HTH_DocumentListener(this));
      AbstractDocument doc = (AbstractDocument) getDocument();
      doc.setDocumentFilter(new UppercaseDocumentFilter());
      
      Dimension size = getTextSize();
      setPreferredSize(size);
      setMaximumSize(size);
      setMinimumSize(size);
	}
	
	private Dimension getTextSize() {
		String txt = "";
		for (int c = 0; c <= limit; c++) {
			txt += "M";
		}
		
		int width =  getFontMetrics(getFont()).stringWidth(txt);
		int height = getFontMetrics(getFont()).getHeight() + 10;
		
		return new Dimension(width, height);
	}
	
	private class HTH_DocumentListener implements DocumentListener {
		private String value;
		private HTH_TextField field;
		
		public HTH_DocumentListener(HTH_TextField field) {
			this.field = field;
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
						value = field.getText().toUpperCase();
					}
				}
			});
		}
	}
	
	private class UppercaseDocumentFilter extends DocumentFilter {
		public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, text.toUpperCase(), attr);
		}
		
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			fb.replace(offset, length, text.toUpperCase(), attrs);
		}
	}
	
	private class HTH_TextFieldMouseListener implements MouseListener {
		private HTH_TextField field;
		
		public HTH_TextFieldMouseListener(HTH_TextField field) {
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
}
