package gui.dialog.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.defaults.DefaultPopup;
import gui.dialog.MenuPopup;
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
public class LogPopup implements MenuPopup, ItemListener {

	/* Managers */
	private BranchManager branchManager = BranchManager.getInstance();
	private Language lang = Language.getInstance();
	private ArrayList<String []> data;
	
	/* Components */
	private DefaultPopup dlg;
	JPanel panel;
	private JComboBox<String> branchCombo, actionCombo;
	private JEditorPane jep = new JEditorPane();
	
	/* Row filter */
	private String allBrances = lang.get("gui_popup_log_allbranches");
	private String allActions = lang.get("gui_popup_log_allactions");
	private LogRowFilter rowFilter = new LogRowFilter() {
		public boolean filter(String[] rowData) {
			String branch = rowData[0];
			String action = rowData[2].toLowerCase();
			String selectedBranch = (String) branchCombo.getSelectedItem();
			String selectedAction = (String) actionCombo.getSelectedItem();
			boolean branchFilter = branch.equals(selectedBranch) ||
					selectedBranch.equals(allBrances);
			boolean actionFilter = action.equals(selectedAction.toLowerCase()) ||
					selectedAction.equals(allActions);
			return branchFilter && actionFilter;
		}
	};
	
	public void open(JFrame parent) {
		dlg = new DefaultPopup(parent,lang.get("gui_popup_log_title"));
		data = LauncherLogManager.readLog(); // updates data
		buildDialog();
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(dlg.getOwner());
		dlg.setVisible(true);
	}
		
	private void buildDialog() {
		int margin = 10;
		panel = new JPanel(new BorderLayout(margin,margin));
		panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		
		String [] branchNames = branchManager.getBranchNames();
		String [] branchOptions = new String [branchNames.length+1];
		for(int i = 0 ; i < branchNames.length; i++)
			branchOptions[i+1] = branchNames[i];
		branchOptions[0] = allBrances;
		branchCombo = new JComboBox<String>(branchOptions);
		branchCombo.addItemListener(this);
		
		String [] actions = { allActions, lang.get("gui_popup_log_action_setup"), lang.get("gui_popup_log_action_make") };
		actionCombo = new JComboBox<String>(actions);
		actionCombo.setPreferredSize(branchCombo.getPreferredSize());
		actionCombo.addItemListener(this);
		
	    jep.setContentType("text/html");
	    jep.setText(getTableHTML(new LogRowFilter() {
	    	public boolean filter(String[] r) { return true; }
    	})); // accepts everything
	    jep.setEditable(false);
	    jep.setOpaque(false);
	    JScrollPane scrollPane = new JScrollPane(jep);
	    scrollPane.setPreferredSize(new Dimension(500,300));
	    
		panel.add(branchCombo, BorderLayout.LINE_START);
		panel.add(actionCombo, BorderLayout.LINE_END);
		panel.add(scrollPane, BorderLayout.PAGE_END);
		
		dlg.getContentPane().add(panel);
	}
	
	private String getTableHTML(LogRowFilter rowFilter) {
		if(data==null) return "";
		Iterator<String []> iterator = data.iterator();
		StringBuilder sb = new StringBuilder();
		String [] headers = { 	lang.get("gui_popup_log_col_date") ,
								lang.get("gui_popup_log_col_branch"),
								lang.get("gui_popup_log_col_desc") };
		sb.append("<table width=\"100%\"><tr>");
		for( String header : headers ) {
			sb.append(String.format("<th>%s</th>", header));	
		}
		sb.append("</tr>");
		while(iterator.hasNext()) {
			String [] registry = iterator.next();
			if( !rowFilter.filter(registry) ) continue;
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
			int diff = Integer.parseInt(data[4]) - Integer.parseInt(data[3]);
			actionStr = lang.format("gui_popup_log_desc_setup", data[3], data[4], diff);
			break;
		case "make":
			int seconds = Integer.parseInt(data[3]) / 1000;
			int minutes = seconds / 60;
			int hours = minutes / 60;
			minutes %= 60;
			seconds %= 60;
			if( hours == 0 )
				actionStr = lang.format("gui_popup_log_desc_make_ms", minutes, seconds);
			else
				actionStr = lang.format("gui_popup_log_desc_make_hms", hours, minutes, seconds);
			break;
		}
		sb.append("<td>"+actionStr+"</td>"); // date
		return sb.toString();
	}

	private void updateLogTable( LogRowFilter filter )
	{
		jep.setText(getTableHTML(filter));
		panel.repaint();
	}
	
	public void itemStateChanged(ItemEvent ie) {
		updateLogTable(rowFilter);
	}
	
	
	
}
