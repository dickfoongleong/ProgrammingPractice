package com.hth.id_card.user_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hth.images.HTH_Image;

public abstract class HTH_Frame extends JFrame {
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	protected static final Dimension FRAME_SIZE = new Dimension(1220, 900);

	private String title;

	private JPanel headerPanel;
	private JPanel functionPanel;
	private JPanel functionKeyPanel;
	private JLabel titleLabel;

	protected HTH_Frame(String title) {
		this.title = title;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle(title);
		try {
			setIconImage(ImageIO.read(HTH_Image.getImageURL("hth_block.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		setContentPane(contentPanel);

		initHeaderPanel();
		initFunctionPanel();

		add(headerPanel, BorderLayout.NORTH);
		add(functionPanel, BorderLayout.WEST);

		revalidate();
		repaint();
		setPreferredSize(FRAME_SIZE);
		pack();
		relocateWindow();
	}

	protected void addFunctionKeys(HTH_FunctionButton... keyList) {
		for (HTH_FunctionButton btn : keyList) {
			functionKeyPanel.add(btn);
		}

	}

	protected void resetFunctionKeys() {
		functionKeyPanel.removeAll();
	}

	protected void setHeaderTitle(String heading) {
		if (heading == null || heading.isEmpty()) {
			titleLabel.setText(title);
		} else {
			titleLabel.setText(heading);
		}
	}

	private void initHeaderPanel() {
		headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setPreferredSize(new Dimension(0, 140));

		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		logoPanel.setPreferredSize(new Dimension(0, 105));
		logoPanel.setBackground(Color.WHITE);

		JLabel logoLabel = new JLabel(new ImageIcon(HTH_Image.getImageURL("hth_logo.png")));
		logoPanel.add(logoLabel);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(new Dimension(0, 35));
		titlePanel.setBackground(new Color(82, 144, 202));

		titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		titlePanel.add(titleLabel, BorderLayout.SOUTH);

		headerPanel.add(logoPanel, BorderLayout.NORTH);
		headerPanel.add(titlePanel, BorderLayout.SOUTH);
	}

	private void initFunctionPanel() {
		functionPanel = new JPanel();
		functionPanel.setLayout(new BorderLayout());
		functionPanel.setBackground(new Color(83, 89, 105));
		functionPanel.setPreferredSize(new Dimension(200, 0));

		JPanel dummyPanel = new JPanel();
		dummyPanel.setOpaque(false);
		dummyPanel.setPreferredSize(new Dimension(0, 80));
		functionPanel.add(dummyPanel, BorderLayout.NORTH);

		functionKeyPanel = new JPanel();
		functionKeyPanel.setOpaque(false);
		functionKeyPanel.setLayout(new BoxLayout(functionKeyPanel, BoxLayout.Y_AXIS));

		functionPanel.add(functionKeyPanel, BorderLayout.CENTER);
	}

	private void relocateWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int scMidWidth = screenSize.width / 2;
		int scMidHeight = screenSize.height / 2;
		int frameMidWidth = FRAME_SIZE.width / 2;
		int frameMidHeight = FRAME_SIZE.height / 2;

		setLocation(scMidWidth - frameMidWidth, scMidHeight - frameMidHeight);
	}
}
