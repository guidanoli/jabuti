package gui.popup;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import gui.DefaultPopup;
import gui.error.FatalError;
import gui.maindlg.MenuPopup;
import vars.Language;
import vars.LocalResources;

/**
 * The 'About' dialog shows information about software itself and
 * the ones that made it possible.
 * @author guidanoli
 *
 */
public class AboutPopup implements MenuPopup {
	
	private Language lang = Language.getInstance();
	
	private DefaultPopup dlg;
	private JFrame parent;

	/**
	 * {@inheritDoc}
	 */
	public void open(JFrame parent) {
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);
	}
	
	/**
	 * Constructs About Pop Up but does not open
	 * @param parent - parent frame
	 */
	public AboutPopup(JFrame parent) {
		this.parent = parent;
		dlg = new DefaultPopup(parent,lang.get("gui_popup_about_title"));
		JPanel panel = new JPanel(new GridBagLayout());
		buildDialog(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dlg.getContentPane().add(panel);
	}

	private void buildDialog(JPanel panel) {
		JLabel iconLabel = buildIcon();
		JEditorPane jep = buildHyperText();
		GridBagConstraints c = new GridBagConstraints();
		
		/*
		 * Icon Label
		 * **********/
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(iconLabel, c);
		
		/*
		 * Hyper Text
		 * **********/
		c.gridy = 1;
		c.insets = new Insets(10,0,0,0);
		panel.add(jep, c);
	}
	
	/**
	 * @return Software Icon Component
	 */
	private JLabel buildIcon() {
		BufferedImage myPicture = null;
		try { myPicture = ImageIO.read(LocalResources.getStream(LocalResources.icon_bw)); }
		catch (IOException e) { FatalError.show(e,parent,false); }
		JLabel picLabel;
		if( myPicture != null ) picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(64, 64, Image.SCALE_FAST)));
		else picLabel = new JLabel("<Missing image>");
		return picLabel;
	}
	
	/**
	 * @return HTML Component
	 */
	private JEditorPane buildHyperText() {
		JEditorPane jep = new JEditorPane();
	    jep.setContentType("text/html");
	    jep.setText(getHyperText());
	    jep.setEditable(false);
	    jep.setOpaque(false);
	    jep.addHyperlinkListener(e -> {
		    if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
		    	Desktop desktop = Desktop.getDesktop();
		        try {
		        	desktop.browse(e.getURL().toURI());
		        } catch (Exception ex) {
		        	FatalError.show(ex,parent);
		        }
		    }
	    });
	    return jep;
	}
		
	/**
	 * Joins all HTML lines to one single HTML text
	 * @return HTML
	 */
	private String getHyperText() {
		
		String header = String.format("%s v.%s", lang.get("name"), lang.get("version"));
		String authors = lang.get("authors");
		String repolink = getRepositoryLinkHTML("GitHub Repository");
		String iconscredits = getIconsAuthorHTML(); 
		
		String [] lines = {
			header,
			authors,
			repolink,
			"",
			iconscredits
		};
		
		String joinedLines = String.join("<br>", lines); 
		
		return String.format("<div align='center'>%s</div>", joinedLines);
	}
	
	/**
	 * @param label - name of repository link label
	 * @return HTML of hyperlink to repository
	 */
	private String getRepositoryLinkHTML(String label) {
		return String.format("<a href=\"%s\">%s</a>", lang.get("repository"), label);
	}
	
	/**
	 * @return HTML of credits to icons authors
	 */
	private String getIconsAuthorHTML() {
		String [] authorsName = {"Smashicons","Freepik"};
		String [] authorsURL = {"https:/www.flaticon.com/authors/smashicons/","https://www.freepik.com/"};
		String [] licenseName = {"CC 3.0 BY","CC 3.0 BY"};
		String [] licenseLink = {"http://creativecommons.org/licenses/by/3.0/","http://creativecommons.org/licenses/by/3.0/"};
		
		StringBuilder sb = new StringBuilder();
		sb.append("Icons made by:");
		for(int i = 0 ; i < authorsName.length; i++)
		{
			sb.append(String.format("<br><a href=\"%s\">%s</a>", authorsURL[i], authorsName[i]));
			sb.append(String.format(" (<a href=\"%s\">%s</a>)", licenseLink[i], licenseName[i]));
		}
		return sb.toString();
	}
	
}
