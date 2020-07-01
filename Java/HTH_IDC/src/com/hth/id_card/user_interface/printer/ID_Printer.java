package com.hth.id_card.user_interface.printer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.hth.id_card.user_interface.HTH_CardScreen;
import com.hth.id_card.user_interface.HTH_Frame;
import com.hth.id_card.user_interface.HTH_FunctionButton;
import com.hth.util.IDCard;

/**
 * HTH ID printer window.
 * <br><br>
 * This window contains all the functionalities in HTH ID printer program. Modify this class for Printer portion of ID Process.
 * 
 * @author dickfoong
 */
public class ID_Printer extends HTH_Frame implements WindowListener, Printable, ActionListener {
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 102L;

	/**
	 * Program title shown on application window.
	 */
	private static final String PRINTER_NAME = "ID Printer";

	/**
	 * ID card width on PDF page.
	 */
	private static final float ID_WIDTH = 287;

	/**
	 * ID card height on PDF page.
	 */
	private static final float ID_HEIGHT = 180;

	/**
	 * ID card X-location offset on PDF page.
	 */
	private static final float ID_X_OFFSET = ID_WIDTH / 2;

	/**
	 * Front of ID card Y-location offset on PDF page.
	 */
	private static final float ID_FRONT_Y_OFFSET = 25;

	/**
	 * Back of ID card Y-location offset on PDF page.
	 */
	private static final float ID_BACK_Y_OFFSET = ID_FRONT_Y_OFFSET + ID_HEIGHT;

	/**
	 * ID card width showed on monitor.
	 */
	private static final int WIDTH = 715;

	/**
	 * ID card height showed on monitor.
	 */
	private static final int HEIGHT = 490;

	/**
	 * Default font for all texts.
	 */
	private static final Font DEFAULT_FONT = new Font("AvantGarde", Font.PLAIN, 15);

	/**
	 * Card panel.
	 */
	private HTH_CardScreen cardFramePanel;

	/**
	 * All parent panels used in this printer screen.
	 */
	private JPanel contentPanel, controlPanel;

	/**
	 * All information labels shown in this printer screen.
	 */
	private JLabel loadingLabel, cardCountLabel;

	/**
	 * Function keys in this printer screen.
	 */
	private HTH_FunctionButton prevBtn, nextBtn, turnBtn, pdfBtn, printBtn;

	/**
	 * Boolean indicates the face of the ID card.
	 */
	private boolean isFront;

	/**
	 * Curring index of the ID card.
	 */
	private int currIdx;

	/**
	 * Array of cards to be printed.
	 */
	private IDCard[] cardList;

	/**
	 * Constructor to create ID printer screen with loading screen set up.
	 */
	public ID_Printer() {
		super(PRINTER_NAME);
		addWindowListener(this);

		initComponents();
		setFunctionKeys();

		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(controlPanel, BorderLayout.SOUTH);

		controlPanel.setLayout(new BorderLayout());
		controlPanel.setOpaque(false);

		cardCountLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		controlPanel.add(cardCountLabel, BorderLayout.EAST);

		add(contentPanel, BorderLayout.CENTER);

		setEnable(false);
		loadingLabel.setFont(DEFAULT_FONT);
		loadingLabel.setHorizontalAlignment(JLabel.CENTER);
		loadingLabel.setVerticalAlignment(JLabel.CENTER);
		contentPanel.add(loadingLabel, BorderLayout.CENTER);

		setVisible(true);
	}

	/**
	 * Initialize all the components in the programs.
	 */
	private void initComponents() {
		contentPanel = new JPanel();
		controlPanel = new JPanel();
		loadingLabel = new JLabel("ID card(s) loading... Please wait.");
		cardCountLabel = new JLabel("Card: ");
		turnBtn = new HTH_FunctionButton("Back");
		pdfBtn = new HTH_FunctionButton("Save all ID cards");
		printBtn = new HTH_FunctionButton("Print all");
		prevBtn = new HTH_FunctionButton("Previous ID card");
		nextBtn = new HTH_FunctionButton("Next ID card");
	}

	/**
	 * Set the function keys of the programs.
	 */
	private void setFunctionKeys() {
		turnBtn.addActionListener(this);
		pdfBtn.addActionListener(this);
		printBtn.addActionListener(this);
		prevBtn.addActionListener(this);
		nextBtn.addActionListener(this);

		addFunctionKeys(turnBtn, pdfBtn, printBtn);
	}

	/**
	 * Exit the program.
	 */
	private void exitProgram() {
		System.exit(0);
	}

	/**
	 * Begin printer session with given list of cards.
	 * @param cardList
	 */
	public void begin(IDCard[] cardList) {
		this.cardList = cardList;
		currIdx = 0;

		if (cardList.length > 1) {
			resetFunctionKeys();
			addFunctionKeys(prevBtn, nextBtn, turnBtn, pdfBtn, printBtn);
		}

		isFront = true;

		showID();
	}

	/**
	 * Draw and show the current ID.
	 */
	private void showID() {
		if (cardFramePanel != null) {
			contentPanel.remove(cardFramePanel); 
		}

		if (cardList.length == 0) {
			setEnable(false);
			loadingLabel.setText("There is no ID cards in the queue.");
			contentPanel.add(loadingLabel, BorderLayout.CENTER);
			revalidate();
			repaint();
		} else {
			cardCountLabel.setText("Card: " + (currIdx + 1) + " / " + cardList.length);

			while (!cardList[currIdx].isReady()) {
				try {
					setEnable(false);
					contentPanel.add(loadingLabel, BorderLayout.CENTER);
					revalidate();
					repaint();

					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			contentPanel.remove(loadingLabel);
			setEnable(true);

			if (isFront) {
				turnBtn.setText("Back");
			} else {
				turnBtn.setText("Front");
			}

			cardFramePanel = new HTH_CardScreen(cardList[currIdx], isFront);
			JLayeredPane cardContentPanel = cardFramePanel.getCardContentPanel();
			cardContentPanel.setBounds(getCardBound());

			cardFramePanel.add(setCardBorder(), JLayeredPane.DEFAULT_LAYER);
			cardFramePanel.add(cardContentPanel, JLayeredPane.PALETTE_LAYER);

			contentPanel.add(cardFramePanel, BorderLayout.CENTER);
			revalidate();
			repaint();

			isFront = !isFront;
		}
	}

	/**
	 * Switch on/off on all function keys.
	 * 
	 * @param isEnable
	 *        on/off of the function keys.
	 */
	private void setEnable(boolean isEnable) {
		if (cardList != null && cardList.length > 1) {
			if (currIdx == 0) {
				prevBtn.setEnabled(false);
			} else {
				prevBtn.setEnabled(isEnable);
			}

			if (currIdx == cardList.length - 1) {
				nextBtn.setEnabled(false);
			} else {
				nextBtn.setEnabled(isEnable);
			}
		}

		turnBtn.setEnabled(isEnable);
		pdfBtn.setEnabled(isEnable);
		printBtn.setEnabled(isEnable);
	}

	/**
	 * Show next ID card.
	 */
	private void showNext() {
		currIdx++;
		isFront = true;
		showID();
	}

	/**
	 * Show previous ID card.
	 */
	private void showPrev() {
		currIdx--;
		isFront = true;
		showID();
	}

	/**
	 * Save ID cards as PDF document with 1 page per card.
	 */
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
				currIdx = 0;
				isFront = true;

				while (currIdx < cardList.length) {
					page = new PDPage();

					showID();
					front = generateIDImg("Front");

					showID();
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

					currIdx++;
				}

				doc.save(output);
				doc.close();
				Desktop.getDesktop().open(output);
			} catch (FileNotFoundException fnfe) {
				currIdx = 0; // Reset to last page.
				showID();

				String errMsg = "A file name can't contain any of the following characters:\n:\"<>|";
				JOptionPane.showMessageDialog(this, errMsg, "Error on saving ID cards", JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			currIdx = cardList.length - 1; // Reset to last page.
		}
	}

	/**
	 * Generate temporary file for ID card images.
	 * 
	 * @param name
	 *        Name of the file.
	 * @return
	 *        File instance of the output temporary file.
	 *        
	 * @throws Exception
	 *        Exception thrown if file is not created.
	 */
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

	/**
	 * Prepare printer image to print onto physical papers/cards.
	 */
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		// We have 2 pages for each id, and 'page' is zero-based.
		if ((currIdx = page / 2) >= cardList.length) {
			currIdx = cardList.length - 1;
			showID();
			
			String msg = "Printer is printing the ID cards. You may close the current window.";
			JOptionPane.showMessageDialog(this, msg, "Printing ID cards", JOptionPane.INFORMATION_MESSAGE);
			
			return NO_SUCH_PAGE;
		}

		if (page % 2 == 0) {
			isFront = true;
		} else {
			isFront = false;
		}

		showID();
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

	/**
	 * Handles button click actions for all function keys.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == printBtn) {
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
				} catch (PrinterException ex) {
					/* The job did not successfully complete */
				}
			}	
		} else if (e.getSource() == turnBtn) {
			showID();
		} else if (e.getSource() == pdfBtn) {
			savePDF();
		} else if (e.getSource() == nextBtn) {
			showNext();
		} else if (e.getSource() == prevBtn) {
			showPrev();
		}

	}

	/**
	 * Set the borders surround the ID card.
	 * 
	 * @return
	 *        Empty panel with borders surrounding.
	 */
	private JPanel setCardBorder() {
		Rectangle cardBound = getCardBound();
		int padding = 3;

		JPanel borderPanel = new JPanel();
		borderPanel.setOpaque(false);
		borderPanel.setBounds(cardBound.x - padding, cardBound.y - padding, cardBound.width + (2 * padding), cardBound.height + (2 * padding));
		borderPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

		return borderPanel;
	}

	/**
	 * Get the card boundaries.
	 * 
	 * @return
	 *        Rectangle boundaries.
	 */
	private Rectangle getCardBound() {
		int contentWidth = contentPanel.getSize().width;
		int contentHeight = contentPanel.getSize().height - controlPanel.getSize().height;
		int x = (contentWidth / 2) - (WIDTH / 2);
		int y = (contentHeight / 2) - (HEIGHT / 2);

		return new Rectangle(x, y, WIDTH, HEIGHT);
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
}
