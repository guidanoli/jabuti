package gui;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class BranchTableModelListener implements TableModelListener{

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO - Process tableChanged info from BranchTable
		int type = e.getType();
		int firstrow = e.getFirstRow();
		int lastrow = e.getLastRow();
		int column = e.getColumn();
		System.out.printf("type = %d fr = %d lr = %d col = %d",type,firstrow,lastrow,column);
	}

}
