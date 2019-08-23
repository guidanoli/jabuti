package gui.dialog.main;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.*;

import gui.error.FatalError;
import svn.BranchManager;
import vars.Language;
import vars.LocalResources;

/**
 * @author guidanoli
 *
 */
public class BranchTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3381824531233680142L;
	
	protected static final int BRANCH_NAME = 0;
	protected static final int LAST_SETUP = 1;
	protected static final int SETUP = 2;
	protected static final int MAKE = 3;
	
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_LAUNCH = 1;
	
	/* Table Status */
	int status = STATUS_IDLE;
	
	/* Language */
	private Language lang = Language.getInstance();
	
	/* Branch Manager */
	protected BranchManager manager;
	
	/* Column fields */
	protected String[] branchNames;
	protected String[] lastSetupDate;
	
	// Setup and Make fields when status = STATUS_IDLE
	protected boolean[] setupToggle;
	protected boolean[] makeToggle;
	
	// Setup and Make fields when status = STATUS_LAUNCH
	protected int[] setupStatus;
	protected int[] makeStatus;
	
	/* Launch Progress Icons */
	protected Icon[] icons;
	protected String[] icon_paths = {
			LocalResources.empty ,
			LocalResources.pause ,
			LocalResources.unlock ,
			LocalResources.more ,
			LocalResources.success ,
			LocalResources.warning ,
			LocalResources.error ,
	};
	
	/* Meta fields */
	protected String[] columnNames = new String[]{
			lang.get("gui_branchtable_columns_branch") ,
			lang.get("gui_branchtable_columns_lastsetup") ,
			lang.get("gui_branchtable_columns_setup") ,
			lang.get("gui_branchtable_columns_make")
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
	
	/**
	 * Constructs the branch table model
	 * @param manager - branch manager
	 */
	public BranchTableModel( BranchManager manager ) {
		this.manager = manager;
		icons = new Icon[icon_paths.length];
		int i = 0;
		for( String path : icon_paths )
		{
			InputStream is = LocalResources.getStream(path);
			try {
				icons[i] = new ImageIcon(ImageIO.read(is));
			} catch (IOException e) {
				FatalError.show(e);
			}
			i++;
		}
		updateAllColumns();
	}
	
	/**
	 * Updates specific table columns
	 * @param names - update Branch Name column
	 * @param dates - update Last Setup Date column
	 * @param setup - update Setup column (idle)
	 * @param make - update Make column (idle)
	 * @param setup_s - update Setup column (launch)
	 * @param make_s - update Make column (launch)
	 */
	public void updateColumns(boolean names, boolean dates, boolean setup, boolean make, boolean setup_s, boolean make_s)
	{
		if(names) branchNames = manager.getBranchNames();
		if(dates) lastSetupDate = manager.getLastSetupDates();
		if(setup) setupToggle = manager.getBoolSetup();
		if(make) makeToggle = manager.getBoolMake();
		if(setup_s) setupStatus = manager.getStatusSetup();
		if(make_s) makeStatus = manager.getStatusMake();
	}
	
	/**
	 * Updates all columns
	 * @see #updateColumns(boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public void updateAllColumns() { updateColumns(true,true,true,true,true,true); }

	/**
	 * Updates status with a valid identifier, either:
	 * <ul><li>{@link #STATUS_IDLE}</li>
	 * <li>{@link #STATUS_LAUNCH}</li></ul>
	 * @param status - new status
	 */
	public void setStatus(int status) {
		if( status != STATUS_LAUNCH && status != STATUS_IDLE ) return;
		this.status = status;
	}
	
	/**
	 * @return table status, which is either:
	 * <ul><li>{@link #STATUS_IDLE}</li>
	 * <li>{@link #STATUS_LAUNCH}</li></ul>
	 */
	public int getStatus() {
		return status;
	}
	
	/* Overwritten methods */
	
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
				return icons[setupStatus[rowIndex]];
		case MAKE:
			if( status == STATUS_IDLE )
				return makeToggle[rowIndex] ? Boolean.TRUE : Boolean.FALSE ;
			else if( status == STATUS_LAUNCH )
				return icons[makeStatus[rowIndex]];
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
		
}
