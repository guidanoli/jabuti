package gui.preferences;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

import gui.error.LightError;
import gui.maindlg.MainFrame;
import vars.Language;
import vars.properties.GlobalProperties;


@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel implements ActionListener {

	// Language
	Language lang = Language.getInstance();
	
	// Components
	JComboBox<String> combo;
	JPanel value_panel = new JPanel(new CardLayout());
	JButton apply_btn = new JButton(lang.get("gui_popup_preferences_btn_apply"));
	JButton default_btn = new JButton(lang.get("gui_popup_preferences_btn_restore"));
	JButton cancel_btn = new JButton(lang.get("gui_popup_preferences_btn_cancel"));
	
	// Current preference
	PreferenceType type;
	
	// Meta components
	PreferencesComboModel model;
	GlobalProperties gp = GlobalProperties.getInstance();
	JDialog dlg;
	MainFrame parent;
	
	// Layout
	int hborder = 10;
	int vborder = 10;
	int vgap = 5;
	
	public PreferencesPanel(JDialog dlg, MainFrame parent) {
		this.dlg = dlg;
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
		String key = (String) model.getSelectedItemProperty(PreferencesComboModel.KEY);
		String value = gp.get(key);
		type = (PreferenceType) model.getSelectedItemProperty(PreferencesComboModel.TYPE);
		if( type.getPanel().getParent() != value_panel ) //if not added
			value_panel.add(type.getPanel(), key); //key as unique identifier
		((CardLayout) value_panel.getLayout()).show(value_panel, key);
		type.setState(value);
		updateDialog();
	}
	
	public void updateDialog() {
		dlg.pack();
		dlg.setLocationRelativeTo(parent);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if( source == cancel_btn )
		{
			dlg.setVisible(false);
		}
		else if( source == apply_btn )
		{
			if( type.validateState() )
			{
				String key = (String) model.getSelectedItemProperty(PreferencesComboModel.KEY);
				String value = ((PreferenceType) model.getSelectedItemProperty(PreferencesComboModel.TYPE)).getState();
				gp.setProperty(key,value);
				applyPreference();
				return;
			}
			JOptionPane.showMessageDialog(	dlg, lang.get("gui_popup_preferences_applymsg_invalid"),
											lang.get("name"), JOptionPane.ERROR_MESSAGE	);
		}
		else if( source == default_btn )
		{
			String key = (String) model.getSelectedItemProperty(PreferencesComboModel.KEY);
			String default_value = GlobalProperties.getInstance().get(key);
			gp.setProperty(key,default_value);
			type.setState(default_value);
			applyPreference();
		}
		else if( source == combo )
		{
			if( arg0.getActionCommand() == "comboBoxChanged" )
			{
				updateTextBox();
			}
		}
	}
	
	private void applyPreference() {
		if( gp.save() )
		{
			LightError.show(lang.get("gui_popup_preferences_applymsg_ok"),dlg);
			if( (boolean) model.getSelectedItemProperty(PreferencesComboModel.RESET) )
				LightError.show(lang.get("gui_popup_preferences_applymsg_reset"),dlg);
			parent.panel.updateTable();
			return;
		}
		LightError.show(lang.get("gui_popup_preferences_applymsg_error"),dlg);
	}
	
	
}
