package gui.defaults;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import gui.error.LightError;
import vars.Language;
import vars.LocalResources;
import vars.properties.GlobalProperties;
import vars.properties.bool.NotificationProperty;

public class DefaultNotificationPopup implements ActionListener, MouseListener {
	
	private Language lang = Language.getInstance();
	
	private final int openTimeout = 5;
	private final int closeTimeout = 1;
	private final float killedTimeout = 0.5f;
	private final String fontFace = "Verdana";
	private final int fontSize = 12;
	private final int charPerLine = 30;
	private boolean hovered = false;
	private boolean killed = false;

	static Semaphore buffer = new Semaphore(1);
	private final JDialog dlg = new JDialog();
	
	public DefaultNotificationPopup(NotificationProperty.Type notificationType, String message) {
		if( !GlobalProperties.notificationProperty.isEnabled(notificationType.getValue()) ) return;
		DefaultNotificationPopup self = this;
		new Thread() {
			public void run() { 
				try {
					buffer.acquire();
				} catch (InterruptedException e) {
					LightError.show(e);
				}
				dlg.setUndecorated(true);
				dlg.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
				dlg.setLayout(new GridBagLayout());
				dlg.addMouseListener(self);
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth = 2;
				c.weightx = 1.0f;
				c.weighty = 0f;
				c.insets = new Insets(5,5,5,5);
				c.fill = GridBagConstraints.BOTH;
				String headerString = lang.get(String.format("meta_keylabel_notify_%d",notificationType.getValue()));
				JLabel headerLabel = new JLabel(headerString);
				headerLabel.setFont(new Font(fontFace,Font.BOLD,fontSize));
				headerLabel.setOpaque(false);
				dlg.add(headerLabel,c);
				c.gridx = 2;
				c.weightx = 0f;
				c.weighty = 0f;
				c.fill = GridBagConstraints.NONE;
				c.anchor = GridBagConstraints.NORTHWEST;
				JButton closeBtn = new JButton("X");
				closeBtn.addActionListener(self);
				closeBtn.setMargin(new Insets(1,4,1,4));
				closeBtn.setFocusable(false);
				dlg.add(closeBtn, c);
				c.gridx = 0;
				c.gridy++;
				c.weightx = 1.0f;
				c.weighty = 1.0f;
				c.anchor = GridBagConstraints.LINE_START;
				c.insets = new Insets(5,10,10,10);
				c.fill = GridBagConstraints.BOTH;
				JLabel messageLabel = new JLabel("<HtMl>"+formatMessage(message));
				messageLabel.setIcon(getIcon());
				messageLabel.setIconTextGap(10);
				messageLabel.setFont(new Font(fontFace,Font.PLAIN,fontSize));
				dlg.add(messageLabel, c);
				dlg.pack();
				dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(dlg.getGraphicsConfiguration());
				dlg.setLocation(screenSize.width - dlg.getWidth() - 5, screenSize.height - toolHeight.bottom - dlg.getHeight());
				dlg.setVisible(true);
				new Thread() {
					public void run() {
						try {
							Thread.sleep(openTimeout*1000);
						} catch (InterruptedException e) {
							LightError.show(e);
						}
						if(killed) return;
						while(hovered);
						dlg.dispose();
						try {
							Thread.sleep(closeTimeout*1000);
						} catch (InterruptedException e) {
							LightError.show(e);
						}
						buffer.release();
					}
				}.start();
			}
		}.start();
	}	
		
	private ImageIcon getIcon() {
		BufferedImage myPicture = null;
		try { myPicture = ImageIO.read(LocalResources.getStream(LocalResources.icon_bw)); }
		catch (IOException e) { LightError.show(e); }
		return new ImageIcon(myPicture.getScaledInstance(32, 32, Image.SCALE_FAST));
	}
	
	public String formatMessage(String message) {
		StringJoiner lineJoiner = new StringJoiner("<br>");
		String [] wordsArray = message.split(" ");
		StringJoiner wordJoiner = new StringJoiner(" ");
		for( String w : wordsArray ) {
			wordJoiner.add(w);
			if( wordJoiner.length() > charPerLine ) {
				lineJoiner.add(wordJoiner.toString());
				wordJoiner = new StringJoiner(" ");
			}
		}
		lineJoiner.add(wordJoiner.toString()); // remains
		return lineJoiner.toString();
	}
	
	public void actionPerformed(ActionEvent e) {
		killed = true;
		dlg.dispose();
		try {
			Thread.sleep((int)(killedTimeout*1000));
		} catch (InterruptedException ie) {
			LightError.show(ie);
		}
		buffer.release();
	}

	public void mouseClicked(MouseEvent arg0) {
		actionPerformed(null);
	}

	public void mouseEntered(MouseEvent arg0) {
		hovered = true;
	}

	public void mouseExited(MouseEvent arg0) {
		hovered = false;
	}

	public void mousePressed(MouseEvent arg0) {
		// nothing...
	}

	public void mouseReleased(MouseEvent arg0) {
		// nothing...
	}

}
