package gui.popup;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import gui.error.FatalError;
import gui.maindlg.MenuPopup;


public class AboutPopup implements MenuPopup {
	
	protected JFrame frame = new JFrame("About");
	private final int margin = 20;
	private final int img_size = 50;
	String [] lines = {	"<div align='center'>" +
						vars.Language.format("%s %s", "name", "version"),
						vars.Language.get("authors"),
						getRepositoryLinkHTML("GitHub Repository"),
						"<br>" + getIconsAuthorHTML() + "</div>" };
	
	public AboutPopup(JFrame parent) {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel panel = new JPanel(layout);
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File(vars.LocalRessources.icon));
		} catch (IOException e) {
			FatalError.show(e,parent,false);
		}
		JLabel picLabel;
		if( myPicture != null )
			picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(img_size, img_size, Image.SCALE_FAST)));
		else
			picLabel = new JLabel("<Missing image>");
		JEditorPane jep = new JEditorPane();
	    jep.setContentType("text/html");
	    jep.setText(getHyperText());
	    jep.setEditable(false);
	    jep.setOpaque(false);
	    jep.addHyperlinkListener( e -> {
		    if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
		    	Desktop desktop = Desktop.getDesktop();
		        try {
		        	desktop.browse(e.getURL().toURI());
		        } catch (Exception ex) {
		        	FatalError.show(ex);
		        }
		    }
	    });
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(picLabel, c);
		c.gridy = 1;
		c.insets = new Insets(10,0,0,0);
		panel.add(jep, c);
		panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		frame.getContentPane().add(panel);
		frame.setResizable(false);
		gui.DefaultFrame.set(frame);
	}

	public void open(JFrame parent) { frame.setVisible(true); }
	protected String getHyperText() { return String.join("<br>", lines); }
	protected String getRepositoryLinkHTML(String label) {
		return String.format("<a href=\"%s\">%s</a>", vars.Language.get("repository"), label);
	}
	private String getIconsAuthorHTML() {
		return 	"<div>Icons made by <a href=\"https://www.flaticon.com/authors/pixel-buddha\" title=\"Pixel Buddha\">"+
				"Pixel Buddha</a><br> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">FlatIcon</a> "+
				"is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\"+"
				+ " target=\"_blank\">CC 3.0 BY</a></div>";
	}
	
}
