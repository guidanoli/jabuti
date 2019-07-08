package gui.dialog.main;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.*;

import gui.defaults.DefaultFrame;
import svn.BranchManager;
import svn.LaunchProgressListener;
import svn.Launcher;
import vars.Language;
import vars.properties.GlobalProperties;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, LaunchProgressListener {

	// managers
	private Language lang = Language.getInstance();
	private Launcher launcher = null; // constructor will instantly launch
	protected GlobalProperties gp = GlobalProperties.getInstance();
	
	// JTable
	protected JTable table;
	protected BranchTableModel tablemodel;
	protected BranchManager branchManager = BranchManager.getInstance();
	JScrollPane scrollingBox = new JScrollPane(	JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	// buttons
	protected JButton stopBtn = new JButton(lang.get("gui_mainpanel_btnlabel_stop"));
	protected JButton launchBtn = new JButton(lang.get("gui_mainpanel_btnlabel_launch"));
	protected JPanel btnPanel = new JPanel(new CardLayout());
	protected JButton closeBtn = new JButton(lang.get("gui_mainpanel_btnlabel_close"));
	
	public MainPanel() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buildTable();
		/* tool tips */
		stopBtn.setToolTipText(lang.get("gui_mainpanel_btntip_stop"));
		launchBtn.setToolTipText(lang.get("gui_mainpanel_btntip_launch"));
		closeBtn.setToolTipText(lang.format("gui_mainpanel_btntip_close",lang.get("name")));
		/* action listeners */
		launchBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		closeBtn.addActionListener(this);
		/* launch/stop button panel */
		JPanel stopBtnPanel = new JPanel(new BorderLayout(0,0));
		JPanel launchBtnPanel = new JPanel(new BorderLayout(0,0));
		stopBtnPanel.add(stopBtn);
		launchBtnPanel.add(launchBtn);
		btnPanel.add(stopBtnPanel,stopBtn.getText());
		btnPanel.add(launchBtnPanel,launchBtn.getText());
		((CardLayout)btnPanel.getLayout()).show(btnPanel, launchBtn.getText());
		/* all buttons' panel */
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(btnPanel);
		buttons.add(closeBtn);
		add(buttons);
	}
		
	private void buildTable()
	{
		tablemodel = new BranchTableModel(branchManager);
		table = new JTable(tablemodel);
		// Disable column dragging
		table.getTableHeader().setReorderingAllowed(false);
		// Set columns width
		updateTableColumnsWidth();
		// Centralizes all string values in table
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(String.class, centerRenderer);
		scrollingBox.setViewportView(table);
		scrollingBox.setPreferredSize(new Dimension(600,300));
		add(scrollingBox);
	}
		
	/**
	 * <p>Updates the table columns' width according to their weights, which are determined by
	 * the {@link #getWeightedWidths()} method.
	 */
	private void updateTableColumnsWidth() {
		int weighted_widths[] = getWeightedWidths();
		for( int i = 0 ; i < table.getColumnCount() ; i++ )
			table.getColumnModel().getColumn(i).setPreferredWidth(weighted_widths[i]);
	}

	/**
	 * <p>Calculates each column's weight proportional to the length of the largest string
	 * contained in a given column (including the column name). This gives an extra degree
	 * of freedom for different idioms to have larger column names but also for larger
	 * folder names, avoiding string ellipsis at all costs.
	 * @return array of weighted widths with size equal to the number of columns in the table
	 */
	private int[] getWeightedWidths() {
		int numCol = table.getColumnCount();
		int numRow = table.getRowCount();
		int [] weights = new int[numCol];
		int sum = 0;
		for( int i = 0 ; i < numCol ; i++ )
		{
			int maxWidth = table.getColumnName(i).length();
			for( int j = 0 ; j < numRow ; j++ )
			{
				Object cellObj = table.getValueAt(j, i);
				if( cellObj instanceof String )
				{
					String cellStr = (String) cellObj;
					int cellStrLen = cellStr.length();
					if( cellStrLen > maxWidth ) maxWidth = cellStrLen;
				}
			}
			sum += maxWidth;
			weights[i] = maxWidth;
		}
		for( int i = 0 ; i < numCol ; i++ )
		{
			weights[i] *= 600.0f/((float)sum);
		}
		return weights;
	}

	public void updateTable() {
		tablemodel.updateAllColumns();
		updateTableColumnsWidth();
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
			launcher = new Launcher(branchManager,this);
		}
		else if( source == stopBtn )
		{
			launcher.interrupt();
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
		updateTable();
	}

	public void launchBegan() {
		System.out.println("Setting up...");
		setTableStatus(BranchTableModel.STATUS_LAUNCH);
		updateTable();
	}
	
	protected void setTableStatus(int status)
	{
		tablemodel.setStatus(status);
		MainFrame parent = (MainFrame) SwingUtilities.getWindowAncestor(this);
		if( status == BranchTableModel.STATUS_LAUNCH )
		{
			parent.setCloseOperation(MainFrame.TRAY);
			parent.setMenuEnabled(false);
			((CardLayout)btnPanel.getLayout()).show(btnPanel, stopBtn.getText());
		}
		else if( status == BranchTableModel.STATUS_IDLE )
		{
			parent.setCloseOperation(MainFrame.CLOSE);
			parent.setMenuEnabled(true);
			((CardLayout)btnPanel.getLayout()).show(btnPanel, launchBtn.getText());
		}
		table.repaint();
	}
	
	public void setValueToAllBranches(int column, Object value)
	{
		for(int i = 0 ; i < tablemodel.getRowCount(); i++)
			tablemodel.setValueAt(value, i, column);
		table.repaint();
	}
	
}
