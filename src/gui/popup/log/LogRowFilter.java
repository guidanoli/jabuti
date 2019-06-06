package gui.popup.log;

public interface LogRowFilter {

	/**
	 * Filters rows depending on row data
	 * @param rowData - row data
	 * @return {@code true} if row is accepted.
	 */
	public boolean filter(String [] rowData);
	
}
