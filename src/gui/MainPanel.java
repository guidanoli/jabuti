package gui;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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
		
	}
	
	private void buildTable()
	{
		AbstractTableModel model = new BranchTableModel(branchManager);
		table = new JTable(model);
		JScrollPane scrollingBox = new JScrollPane(table);
		this.add(scrollingBox);
	}
	
	// nothing much yet...
	
}
