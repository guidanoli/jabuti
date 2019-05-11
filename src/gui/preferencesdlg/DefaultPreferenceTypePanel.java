package gui.preferencesdlg;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DefaultPreferenceTypePanel implements PreferenceTypePanel {

	protected JPanel panel = new JPanel(new GridLayout(1,1));
	protected JTextField txt = new JTextField();
	
	public DefaultPreferenceTypePanel() { panel.add(txt); }
	public JPanel getPanel() { return panel; }
	public String getPanelName() { return "default"; }
	public void setState(String value) { txt.setText(value); }
	public String getValue() { return txt.getText(); }

}
