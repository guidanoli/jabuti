package gui.dialog.preferences;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringJoiner;

import javax.imageio.ImageIO;
import javax.swing.*;

import gui.defaults.DefaultCardLayout;
import gui.dialog.main.MainFrame;
import gui.error.FatalError;
import gui.error.LightError;
import vars.Language;
import vars.LocalResources;
import vars.properties.GlobalProperties;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel implements ActionListener {

	// Language
	Language lang = Language.getInstance();

	// Components
	JComboBox<String> combo;
	JPanel value_panel = new JPanel(new DefaultCardLayout());
	JLabel help_icon;
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
	int hgap = 10;
	int vgap = 5;

	public PreferencesPanel(JDialog dlg, MainFrame parent) {
		this.dlg = dlg;
		this.parent = parent;
		setBorder(BorderFactory.createEmptyBorder(vborder, hborder, vborder, hborder));
		// Panels
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel buttons = new JPanel(new FlowLayout());
		// Combo box
		model = new PreferencesComboModel();
		combo = new JComboBox<String>(model);
		// Help button
		InputStream is = LocalResources.getStream(LocalResources.info);
		try {
			help_icon = new JLabel(new ImageIcon(ImageIO.read(is)));
		} catch (IOException e) {
			FatalError.show(e);
		}
		// Components setup
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
		comboPanel.setAlignmentX(CENTER_ALIGNMENT);
		combo.addActionListener(this);
		combo.setSelectedItem(combo.getItemAt(0));
		cancel_btn.addActionListener(this);
		cancel_btn.setAlignmentX(CENTER_ALIGNMENT);
		apply_btn.addActionListener(this);
		apply_btn.setAlignmentX(CENTER_ALIGNMENT);
		default_btn.addActionListener(this);
		default_btn.setAlignmentX(CENTER_ALIGNMENT);
		// Add components to panel
		comboPanel.add(combo);
		comboPanel.add(Box.createHorizontalStrut(hgap));
		comboPanel.add(help_icon);
		add(comboPanel);
		add(Box.createVerticalStrut(vgap));
		add(value_panel);
		add(Box.createVerticalStrut(vgap));
		buttons.add(apply_btn);
		buttons.add(default_btn);
		buttons.add(cancel_btn);
		add(buttons);
	}

	public void updateTextBox() {
		String key = model.getSelectedItemKey();
		String value = gp.get(key);
		type = model.getSelectedItemType();
		JPanel typePanel = type.getPanel(lang);
		if (typePanel.getParent() != value_panel) // if not added...
			value_panel.add(typePanel, key); // add with key as unique identifier
		((CardLayout) value_panel.getLayout()).show(value_panel, key);
		type.setState(value);
		String helperToolTip = lang.get("gui_popup_preferences_prophelp_"+key);
		if(helperToolTip != null) helperToolTip = breakTextLine(helperToolTip, 64);
		help_icon.setToolTipText(helperToolTip);
		updateDialog();
	}

	private String breakTextLine(String string, int maxCharCount) {
		StringJoiner joiner = new StringJoiner("<br />");
		String [] words = string.split(" ");
		int currentLineCharCount = 0;
		int baseIndex = 0, currentIndex = 0;
		for(String word : words) {
			currentLineCharCount += word.length();
			if( currentLineCharCount > maxCharCount ) {
				StringJoiner lineJoiner = new StringJoiner(" ");
				for(int i = baseIndex; i < currentIndex; i++) {
					lineJoiner.add(words[i]);
				}
				joiner.add(lineJoiner.toString());
				baseIndex = currentIndex + 1;
				currentLineCharCount = 0;
			}
			currentLineCharCount++; // accounts for space to be added
			currentIndex++;
		}
		if( baseIndex != currentIndex ) {
			StringJoiner lineJoiner = new StringJoiner(" ");
			for(int i = baseIndex; i < currentIndex; i++) {
				lineJoiner.add(words[i]);
			}
			joiner.add(lineJoiner.toString());
		}
		return "<html>" + joiner.toString() + "</html>";
	}
	
	public void updateDialog() {
		dlg.pack();
		dlg.setLocationRelativeTo(parent);
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == cancel_btn) {
			dlg.setVisible(false);
		} else if (source == apply_btn) {
			if (type.validateValue(type.getState())) {
				String key = model.getSelectedItemKey();
				String value = model.getSelectedItemType().getState();
				gp.setProperty(key, value);
				applyPreference();
				return;
			}
			JOptionPane.showMessageDialog(	dlg, lang.get("gui_popup_preferences_applymsg_invalid"),
											lang.get("name"), JOptionPane.ERROR_MESSAGE	);
		} else if (source == default_btn) {
			String key = model.getSelectedItemKey();
			String default_value = gp.resetPropertyToDefault(key);
			if( default_value == null ) LightError.show(lang.get("gui_errmsg_gp_default_resetfailed"), dlg);
			else type.setState(default_value);
			applyPreference();
		} else if (source == combo) {
			if (event.getActionCommand() == "comboBoxChanged") {
				updateTextBox();
			}
		}
	}

	private void applyPreference() {
		if (gp.save()) {
			LightError.show(lang.get("gui_popup_preferences_applymsg_ok"), dlg);
			if (model.getSelectedItemReset())
				LightError.show(lang.get("gui_popup_preferences_applymsg_reset"), dlg);
			parent.panel.updateTable();
			return;
		}
		LightError.show(lang.get("gui_popup_preferences_applymsg_error"), dlg);
	}

}
