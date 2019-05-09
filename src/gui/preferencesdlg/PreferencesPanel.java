package gui.preferencesdlg;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.*;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel implements ActionListener {

	// Components
	JComboBox<String> combo;
	JTextField txt = new JTextField();
	JButton apply_btn = new JButton("Apply");
	JButton close_btn = new JButton("Close");
	
	// Current preference
	PreferenceType type;
	String value;
	
	// Meta components
	PreferencesComboModel model;
	
	Properties prop;
	JFrame frame;
	
	// Layout
	int hborder = 40;
	int vborder = 20;
	int vgap = 10;
	
	public PreferencesPanel(Properties prop, JFrame frame) {
		this.prop = prop;
		this.frame = frame;
		setBorder(BorderFactory.createEmptyBorder(vborder, hborder, vborder, hborder));
		// Panels
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		JPanel buttons = new JPanel(new FlowLayout());
		// Combo box
		model = new PreferencesComboModel();
		combo = new JComboBox<String>(model);
		// Components setup
		combo.addActionListener(this);
		combo.setSelectedItem(combo.getItemAt(0));
		combo.setAlignmentX(CENTER_ALIGNMENT);
		txt.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				value = type.openDialog(value);
				txt.setText(value);
			}
		});
		close_btn.addActionListener(this);
		close_btn.setAlignmentX(CENTER_ALIGNMENT);
		apply_btn.addActionListener(this);
		apply_btn.setAlignmentX(CENTER_ALIGNMENT);
		// Add components to panel
		add(combo);
		add(Box.createVerticalStrut(vgap));
		add(txt);
		add(Box.createVerticalStrut(vgap));
		buttons.add(apply_btn);
		buttons.add(close_btn);
		add(buttons);
	}

	public void updateTextBox() {
		String key = model.getSelectedItemLabel();
		value = prop.getProperty(key);
		type = model.getSelectedItemType();
		txt.setText((String) value);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if( source == close_btn )
		{
			frame.setVisible(false);
		}
		else if( source == apply_btn )
		{
			if( type.validateNewValue(txt.getText()) )
			{
				String label = model.getSelectedItemLabel();
				prop.setProperty(label,value);
				JOptionPane.showMessageDialog(	frame, "Preference successfully saved.",
												"SetMeUp", JOptionPane.INFORMATION_MESSAGE	);
			}
			else
			{
				JOptionPane.showMessageDialog(	frame, "Could not save preference properly.",
												"SetMeUp", JOptionPane.ERROR_MESSAGE	);
			}
		}
		else if( source == combo )
		{
			if( arg0.getActionCommand() == "comboBoxChanged" )
			{
				updateTextBox();
			}
		}
	}
		
	
	
}
