package gui;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import vis.BranchManager;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	JTable table;
	BranchManager branchManager = new BranchManager();
	
	public MainPanel() {
		// constructor
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBackground(Color.WHITE);
		buildTable();
	}
	
	public void paintComponent( Graphics g )
	{
		// loop
	}
	
	private void buildTable()
	{
		AbstractTableModel model = new BranchTableModel(branchManager);
		table = new JTable(model);
		// Centralizes all string values in table
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(String.class, centerRenderer);
		//
		JScrollPane scrollingBox = new JScrollPane(	table,
													JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollingBox);
	}
	
	// nothing much yet...
	
}
