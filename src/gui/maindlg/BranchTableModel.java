package gui.maindlg;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.*;


import vis.BranchManager;

@SuppressWarnings("serial")
public class BranchTableModel extends AbstractTableModel {

	protected static final int BRANCH_NAME = 0;
	protected static final int LAST_SETUP = 1;
	protected static final int SETUP = 2;
	protected static final int MAKE = 3;
	
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_LAUNCH = 1;
	
	// status
	int status = STATUS_IDLE;
	
	// manager
	protected BranchManager manager;
	
	// fields
	protected String[] branchNames;
	protected String[] lastSetupDate;
	protected boolean[] setupToggle;
	protected boolean[] makeToggle;
	
	// launch fields
	protected int[] setupStatus;
	protected int[] makeStatus;
	
	// icons
	protected Icon[] icons;
	protected String[] icon_paths = {};
	
	// meta fields
	protected String[] columnNames = new String[]{
			vars.Language.get("gui_branchtable_columns_branch") ,
			vars.Language.get("gui_branchtable_columns_lastsetup") ,
			vars.Language.get("gui_branchtable_columns_setup") ,
			vars.Language.get("gui_branchtable_columns_make")
	};
	protected Class<?>[] idleColumnClasses = new Class[]{
			String.class ,
			String.class ,
			Boolean.class ,
			Boolean.class
	};
	protected Class<?>[] launchColumnClasses = new Class[]{
			String.class ,
			String.class ,
			Icon.class ,
			Icon.class
	};
	
	public BranchTableModel( BranchManager manager ) {
		this.manager = manager;
		icons = new Icon[icon_paths.length];
		int i = 0;
		for( String path : icon_paths )
		{
			icons[i] = new ImageIcon(path);
			i++;
		}
		updateAllColumns();
	}
	
	public void updateColumns(boolean names, boolean dates, boolean setup, boolean make)
	{
		if(names) branchNames = manager.getBranchNames();
		if(dates) lastSetupDate = manager.getLastSetupDates();
		if(setup) setupToggle = manager.getBoolSetup();
		if(make) makeToggle = manager.getBoolMake();
	}
	
	public void updateAllColumns() { updateColumns(true,true,true,true); }
	public String getColumnName(int col) { return columnNames[col]; }
	public Class<?> getColumnClass(int col) {
		if( status == STATUS_IDLE )
			return idleColumnClasses[col];
		else if( status == STATUS_LAUNCH )
			return launchColumnClasses[col];
		else
			return null;
	}
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
			if( status == STATUS_IDLE )
				return setupToggle[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
			else if( status == STATUS_LAUNCH )
				return setupStatus[rowIndex];
		case MAKE:
			if( status == STATUS_IDLE )
				return makeToggle[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
			else if( status == STATUS_LAUNCH )
				return makeStatus[rowIndex];
		default:
			return null;
		}
	}
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return status != STATUS_LAUNCH && (columnIndex == SETUP || columnIndex == MAKE);			
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
			if( status == STATUS_IDLE )
			{
				setupToggle[rowIndex] = (Boolean) aValue;
				manager.setBoolSetup(branchName,(boolean) aValue);
			}
			else if( status == STATUS_LAUNCH )
			{
				setupStatus[rowIndex] = (int) aValue;
			}
			break;
		case MAKE:
			if( status == STATUS_IDLE )
			{
				makeToggle[rowIndex] = (Boolean) aValue;
				manager.setBoolMake(branchName,(boolean) aValue);
			}
			else if( status == STATUS_LAUNCH )
			{
				makeStatus[rowIndex] = (int) aValue;
			}
			break;
		default:
			break;
		}
	}
	public void setStatus(int status) {
		if( status == STATUS_LAUNCH || status == STATUS_IDLE )
			this.status = status;
	}
	
	
}
