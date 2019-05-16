package gui.maindlg;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.*;

import gui.DefaultFrame;
import vars.GlobalProperties;

import vis.BranchManager;
import vis.LaunchProgressListener;
import vis.Launcher;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, LaunchProgressListener {
	
	protected GlobalProperties gp;
	protected JTable table;
	protected JButton launchBtn = new JButton(vars.Language.get("gui_mainpanel_btnlabel_launch"));
	protected JButton closeBtn = new JButton(vars.Language.get("gui_mainpanel_btnlabel_close"));
	protected BranchManager branchManager;
	protected BranchTableModel tablemodel;
	JScrollPane scrollingBox = new JScrollPane(	JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public MainPanel(GlobalProperties gp) {
		this.gp = gp;
		branchManager = new BranchManager(gp);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buildTable();
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		launchBtn.addActionListener(this);
		closeBtn.addActionListener(this);
		buttons.add(launchBtn);
		buttons.add(closeBtn);
		add(buttons);
	}
		
	private void buildTable()
	{
		tablemodel = new BranchTableModel(branchManager);
		table = new JTable(tablemodel);
		// Disable column dragging
		table.getTableHeader().setReorderingAllowed(false);
		// Set toggle's width
		int weighted_widths[] = { 300, 100, 50, 50 };
		for( int i = 0 ; i < table.getColumnCount() ; i++ )
			table.getColumnModel().getColumn(i).setPreferredWidth(weighted_widths[i]);
		// Centralizes all string values in table
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(String.class, centerRenderer);
		scrollingBox.setViewportView(table);
		add(scrollingBox);
	}
	
	public void updateTable() {
		tablemodel.updateAllColumns();
		table.revalidate();
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if( source == closeBtn )
			DefaultFrame.forceClosing((JFrame) SwingUtilities.getWindowAncestor(this));
		else if( source == launchBtn )
		{
			new Launcher(branchManager,this);
		}
	}

	public void progressUpdate(int i, int setup, int make) {
		System.out.printf("[%s]%s%s\n",branchManager.getBranchNames()[i],idtos(setup,'s'),idtos(make,'m'));
	}
	
	private String idtos(int i, char c) {
		switch(i) {
		case LaunchProgressListener.OFF:
			return " o";
		case LaunchProgressListener.WAITING:
			return " w";
		case LaunchProgressListener.RUNNING:
			return " r";
		case LaunchProgressListener.ENDED:
			return " e";
		case LaunchProgressListener.FAILED:
			return " f";
		default:
			return "";
		}
	}
	
	public void launchEnded() {
		System.out.println("All branches are set up!");
	}
	
	// nothing much yet...
	
}
