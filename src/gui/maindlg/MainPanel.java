package gui.maindlg;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.*;

import gui.DefaultFrame;
import svn.BranchManager;
import svn.LaunchProgressListener;
import svn.Launcher;
import vars.GlobalProperties;
import vars.Language;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, LaunchProgressListener {

	private Language lang = Language.getInstance();
	protected GlobalProperties gp;
	protected JTable table;
	protected JButton launchBtn = new JButton(lang.get("gui_mainpanel_btnlabel_launch"));
	protected JButton closeBtn = new JButton(lang.get("gui_mainpanel_btnlabel_close"));
	protected BranchManager branchManager;
	protected BranchTableModel tablemodel;
	JScrollPane scrollingBox = new JScrollPane(	JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public MainPanel(GlobalProperties gp) {
		this.gp = gp;
		branchManager = new BranchManager();
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
		{
			MainFrame parent = (MainFrame) SwingUtilities.getWindowAncestor(this);
			DefaultFrame.forceClosing(parent);
		}
		else if( source == launchBtn )
		{
			new Launcher(branchManager,this);
		}
	}

	public void progressUpdate(int i, int setup, int make) {
		tablemodel.setValueAt(setup, i, BranchTableModel.SETUP);
		tablemodel.setValueAt(make, i, BranchTableModel.MAKE);
		tablemodel.fireTableCellUpdated(i, BranchTableModel.SETUP);
		tablemodel.fireTableCellUpdated(i, BranchTableModel.MAKE);
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
		setTableStatus(BranchTableModel.STATUS_IDLE);
	}

	public void launchBegan() {
		System.out.println("Setting up...");
		setTableStatus(BranchTableModel.STATUS_LAUNCH);
	}
	
	protected void setTableStatus(int status)
	{
		tablemodel.setStatus(status);
		MainFrame parent = (MainFrame) SwingUtilities.getWindowAncestor(this);
		if( status == BranchTableModel.STATUS_LAUNCH )
		{
			parent.setCloseOperation(MainFrame.TRAY);
		}
		else if( status == BranchTableModel.STATUS_IDLE )
		{
			parent.setCloseOperation(MainFrame.CLOSE);
		}
		table.repaint();
	}
	
	// nothing much yet...
	
}
