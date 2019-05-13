package gui.maindlg;
import javax.swing.table.*;


import vis.BranchManager;

@SuppressWarnings("serial")
public class BranchTableModel extends AbstractTableModel {

	protected static final int BRANCH_NAME = 0;
	protected static final int LAST_SETUP = 1;
	protected static final int SETUP = 2;
	protected static final int MAKE = 3;
	
	// manager
	protected BranchManager manager;
	
	// fields
	protected String[] branchNames;
	protected String[] lastSetupDate;
	protected boolean[] boolSetup;
	protected boolean[] boolMake;
	
	// meta fields
	protected String[] columnNames = new String[]{
			vars.Language.get().gui_branchtable_columns_branch ,
			vars.Language.get().gui_branchtable_columns_lastsetup ,
			vars.Language.get().gui_branchtable_columns_setup ,
			vars.Language.get().gui_branchtable_columns_make
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
		updateAllColumns();
	}
	
	public void updateColumns(boolean names, boolean dates, boolean setup, boolean make)
	{
		if(names) branchNames = manager.getBranchNames();
		if(dates) lastSetupDate = manager.getLastSetupDates();
		if(setup) boolSetup = manager.getBoolSetup();
		if(make) boolMake = manager.getBoolMake();
	}
	
	public void updateAllColumns() { updateColumns(true,true,true,true); }
	public String getColumnName(int col) { return columnNames[col]; }
	public Class<?> getColumnClass(int col) { return columnClasses[col]; }
	public int getRowCount() {
		if( branchNames == null )
			return 0;
		return branchNames.length;
	}
	public int getColumnCount() { return 4; }
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case BRANCH_NAME:
			return branchNames[rowIndex];
		case LAST_SETUP:
			return lastSetupDate[rowIndex];
		case SETUP:
			return boolSetup[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
		case MAKE:
			return boolMake[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
		default:
			return null;
		}
	}
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == SETUP || columnIndex == MAKE;			
	}
	public void setValueAt(	Object aValue,
						    int rowIndex,
						    int columnIndex) {
		String branchName = (String) getValueAt(rowIndex,BRANCH_NAME);
		switch(columnIndex) {
//		
//		NOT EDITABLE
//		
//		case BRANCH_NAME:
//			break;
//		case LAST_SETUP:
//			break;
//		
		case SETUP:
			boolSetup[rowIndex] = (Boolean) aValue;
			manager.setBoolSetup(branchName,(boolean) aValue);
			break;
		case MAKE:
			boolMake[rowIndex] = (Boolean) aValue;
			manager.setBoolMake(branchName,(boolean) aValue);
			break;
		default:
			break;
		}
	}
	
}
