package gui;
import javax.swing.table.*;
import vis.BranchManager;

@SuppressWarnings("serial")
public class BranchTableModel extends AbstractTableModel {

	protected BranchManager manager;
	
	// fields
	protected String[] branchNames;
	protected String[] lastSetupDate;
	protected boolean[] boolSetup;
	protected boolean[] boolMake;
	
	// meta fields
	protected String[] columnNames = new String[]{
			"Branch name" ,
			"Last Setup" ,
			"Setup" ,
			"Make"
	};
	@SuppressWarnings("rawtypes")
	protected Class[] columnClasses = new Class[]{
			String.class ,
			String.class ,
			Boolean.class ,
			Boolean.class
	};
	
	public BranchTableModel( BranchManager manager ) {
		this.manager = manager;
		updateColumns(true,true,true,true);
	}
	
	public void updateColumns(boolean names, boolean dates, boolean setup, boolean make)
	{
		if(names) branchNames = manager.getBranchNames();
		if(dates) lastSetupDate = manager.getLastSetupDates();
		if(setup) boolSetup = manager.getBoolSetup();
		if(make) boolMake = manager.getBoolMake();
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return ( columnIndex == 2 || columnIndex == 3 );
	}
	public String getColumnName(int col) { return columnNames[col]; }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) { return columnClasses[col]; }
	public int getRowCount() { return branchNames.length; }
	public int getColumnCount() { return 4; }
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return branchNames[rowIndex];
		case 1:
			return lastSetupDate[rowIndex];
		case 2:
			return boolSetup[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
		case 3:
			return boolMake[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
		default:
			return null;
		}
	}
	
}