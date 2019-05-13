package gui.maindlg;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.*;
import io.GlobalProperties;
import vis.BranchManager;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	protected GlobalProperties gp;
	protected JTable table;
	protected JButton launchBtn = new JButton("Launch");
	protected JButton closeBtn = new JButton("Close");
	protected BranchManager branchManager;
	protected BranchTableModel tablemodel;
	JScrollPane scrollingBox = new JScrollPane(	JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public MainPanel(GlobalProperties gp) {
		this.gp = gp;
		this.branchManager = new BranchManager(gp);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buildTable();
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
	
	// nothing much yet...
	
}
