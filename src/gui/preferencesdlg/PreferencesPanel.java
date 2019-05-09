package gui.preferencesdlg;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

import gui.maindlg.MainFrame;
import io.GlobalProperties;

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
	
	GlobalProperties gp;
	JFrame frame;
	MainFrame parent;
	
	// Layout
	int hborder = 40;
	int vborder = 20;
	int vgap = 10;
	
	public PreferencesPanel(GlobalProperties gp, JFrame frame, MainFrame parent) {
		this.gp = gp;
		this.frame = frame;
		this.parent = parent;
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
		value = gp.getProperty(key);
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
				gp.setProperty(label,value);
				if( gp.save() )
				{
					JOptionPane.showMessageDialog(	frame, "Preference successfully saved.",
													"SetMeUp", JOptionPane.INFORMATION_MESSAGE	);
					parent.panel.updateTable();
					return;
				}
			}
			JOptionPane.showMessageDialog(	frame, "Could not save preference properly.",
											"SetMeUp", JOptionPane.ERROR_MESSAGE	);
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
