package gui.preferencesdlg;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

import gui.maindlg.MainFrame;
import io.GlobalProperties;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel implements ActionListener {

	// Components
	JComboBox<String> combo;
	JPanel value_panel = new JPanel(new CardLayout());
	JButton apply_btn = new JButton("Apply");
	JButton default_btn = new JButton("Restore Default");
	JButton cancel_btn = new JButton("Cancel");
	
	// Current preference
	PreferenceType type;
	
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
		cancel_btn.addActionListener(this);
		cancel_btn.setAlignmentX(CENTER_ALIGNMENT);
		apply_btn.addActionListener(this);
		apply_btn.setAlignmentX(CENTER_ALIGNMENT);
		default_btn.addActionListener(this);
		default_btn.setAlignmentX(CENTER_ALIGNMENT);
		// Add components to panel
		add(combo);
		add(Box.createVerticalStrut(vgap));
		add(value_panel);
		add(Box.createVerticalStrut(vgap));
		buttons.add(apply_btn);
		buttons.add(default_btn);
		buttons.add(cancel_btn);
		add(buttons);
	}

	public void updateTextBox() {
		String key = model.getSelectedItemLabel();
		String value = gp.getProperty(key);
		type = model.getSelectedItemType();
		if( type.getPanel().getParent() != value_panel ) //if not added
			value_panel.add(type.getPanel(), key); //key as unique identifier
		((CardLayout) value_panel.getLayout()).show(value_panel, key);
		type.setState(value);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if( source == cancel_btn )
		{
			frame.setVisible(false);
		}
		else if( source == apply_btn )
		{
			if( type.validateState() )
			{
				String label = model.getSelectedItemLabel();
				String value = model.getSelectedItemType().getState();
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
