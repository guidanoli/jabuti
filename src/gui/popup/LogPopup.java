package gui.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.maindlg.MenuPopup;
import svn.BranchManager;
import svn.LauncherLogManager;
import vars.Language;

/**
 * <p>Displays the program usage history in a fancy dialog with options to query certain
 * parameters, e.g. specific branches or specific actions.
 * <p>It reads data from the Log file and outputs in a hypertext style, mainly using tables.
 * <p>If the log is too large, a scroll pane will be shown to allow the user to scroll down
 * the text.
 * @see svn.LauncherLogManager LauncherLogManager
 * @author guidanoli
 *
 */
public class LogPopup implements MenuPopup {

	/* Managers */
	private BranchManager branchManager = BranchManager.getInstance();
	private Language lang = Language.getInstance();
	private ArrayList<String []> data = LauncherLogManager.readLog();
	
	/* Components */
	private JDialog dlg;
	private JComboBox<String> branchCombo, actionCombo;
	JEditorPane jep = new JEditorPane();
	
	public void open(JFrame parent) {
		dlg = new JDialog(parent,lang.get("gui_popup_log_title"),true);
		buildDialog();
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(dlg.getOwner());
		dlg.setVisible(true);
	}
	
	private void buildDialog() {
		int margin = 10;
		JPanel panel = new JPanel(new BorderLayout(margin,margin));
		panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		
		String [] branchNames = branchManager.getBranchNames();
		String [] branchOptions = new String [branchNames.length+1];
		for(int i = 0 ; i < branchNames.length; i++)
			branchOptions[i+1] = branchNames[i];
		branchOptions[0] = "All branches";
		branchCombo = new JComboBox<String>(branchOptions);
		
		String [] actions = { "All actions", lang.get("gui_popup_log_action_setup"), lang.get("gui_popup_log_action_make") };
		actionCombo = new JComboBox<String>(actions);
				
		JLabel branchLabel = new JLabel("Branch: ");
		JLabel actionLabel = new JLabel("Action: ");
		Font f = new Font(Font.DIALOG, Font.BOLD, 12);
		branchLabel.setFont(f);
		actionLabel.setFont(f);
		
	    jep.setContentType("text/html");
	    jep.setText(getTableHTML());
	    jep.setEditable(false);
	    jep.setOpaque(false);
	    JScrollPane scrollPane = new JScrollPane(jep);

		panel.add(branchCombo, BorderLayout.LINE_START);
		panel.add(actionCombo, BorderLayout.LINE_END);
		panel.add(scrollPane, BorderLayout.PAGE_END);
		
		dlg.getContentPane().add(panel);
	}
	
	private String getTableHTML() {
		if(data==null) return "";
		Iterator<String []> iterator = data.iterator();
		StringBuilder sb = new StringBuilder();
		String [] headers = {"Date", "Branch", "Description"};
		sb.append("<table><tr>");
		for( String header : headers ) {
			sb.append(String.format("<th>%s</th>", header));	
		}
		sb.append("</tr>");
		while(iterator.hasNext()) {
			String [] registry = iterator.next();
			sb.append("<tr>");
			sb.append(getRowHTML(registry));
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	private String getRowHTML(String [] data) {
		StringBuilder sb = new StringBuilder();
		long date = Long.parseLong(data[1]);
		String dateStr = branchManager.getDateString(date);
		sb.append("<td>"+dateStr+"</td>"); // date
		sb.append("<td>"+data[0]+"</td>"); // branch
		String actionStr = "";
		switch(data[2]) {
		case "setup":
			actionStr = String.format("Setup %s \u2192 %s", data[3], data[4]);
			break;
		case "make":
			int seconds = Integer.parseInt(data[3]) / 1000;
			int minutes = seconds / 60;
			int hours = minutes / 60;
			minutes %= 60;
			seconds %= 60;
			if( hours == 0 )
				actionStr = String.format("Compiled in %02d min %02d s", minutes, seconds);
			else
				actionStr = String.format("Compiled in %02d hours %02d min %02d s", hours, minutes, seconds);
			break;
		}
		sb.append("<td>"+actionStr+"</td>"); // date
		return sb.toString();
	}
	
}
