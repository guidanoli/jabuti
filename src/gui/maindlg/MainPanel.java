package gui.maindlg;
import javax.swing.*;
import javax.swing.table.*;

import com.sun.media.sound.ModelAbstractChannelMixer;

import io.GlobalProperties;
import vis.BranchManager;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	protected GlobalProperties gp;
	protected JTable table;
	protected BranchManager branchManager;
	protected BranchTableModel tablemodel;
	
	public MainPanel(GlobalProperties gp) {
		this.gp = gp;
		this.branchManager = new BranchManager(gp);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buildTable();
	}
		
	private void buildTable()
	{
		tablemodel = new BranchTableModel(branchManager);
		table = new JTable(tablemodel);
		// Disable column dragging
		table.getTableHeader().setReorderingAllowed(false);
		// Set toggle's width
		for( int i = 0 ; i < table.getColumnCount() ; i++ )
		{
			Class<?> type = table.getColumnClass(i);
			if( type == Boolean.class )
				table.getColumnModel().getColumn(i).setPreferredWidth(1);
			else
				table.getColumnModel().getColumn(i).setPreferredWidth(200);
		}
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
	
	public void updateTable() {
		System.out.println("Updating...");
		// TODO: Make it update the table!
		tablemodel.updateAllColumns();
	}
	
	// nothing much yet...
	
}
