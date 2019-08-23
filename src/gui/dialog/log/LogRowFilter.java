package gui.dialog.log;

public interface LogRowFilter {

	/**
	 * Filters rows depending on row data
	 * @param rowData - row data
	 * @return <code>true</code> if row is accepted.
	 */
	public boolean filter(String [] rowData);
	
}
