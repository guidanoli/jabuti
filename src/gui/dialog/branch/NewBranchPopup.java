package gui.dialog.branch;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ext.TextPrompt;
import ext.TextPrompt.Show;
import gui.defaults.DefaultPopup;
import gui.dialog.MenuPopup;
import svn.BranchManager;

public class NewBranchPopup implements MenuPopup {

	private DefaultPopup dlg;
	
	public NewBranchPopup(JFrame parent) {
		dlg = new DefaultPopup(parent, "New Branch");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		buildDialog(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dlg.getContentPane().add(panel);
	}

	private void buildDialog(JPanel panel) {
		ArrayList<Component> componentList = new ArrayList<Component>();
		
		JPanel namesPanel = new JPanel();
		GridLayout namesPanelLayout = new GridLayout(2,2);
		namesPanelLayout.setHgap(5);
		namesPanelLayout.setVgap(5);
		namesPanel.setLayout(namesPanelLayout);
		componentList.add(namesPanel);
		
		/* Branch name text field */
		JLabel branchNameLabel = new JLabel("Branch:");
		JTextField branchNameTextComponent = new JTextField();
		TextPrompt branchNameHint = new TextPrompt("Name of your branch", branchNameTextComponent, Show.FOCUS_LOST);
		branchNameHint.changeAlpha(0.5f);
		namesPanel.add(branchNameLabel);
		namesPanel.add(branchNameTextComponent);
				
		/* Folder name text field */
		JLabel folderNameLabel = new JLabel("Folder:");
		JTextField folderNameTextComponent = new JTextField();
		TextPrompt folderNameHint = new TextPrompt("Name of your folder", folderNameTextComponent, Show.FOCUS_LOST);
		folderNameHint.changeAlpha(0.5f);
		namesPanel.add(folderNameLabel);
		namesPanel.add(folderNameTextComponent);
		
		/* Add copying functionality to names text fields */
		branchNameTextComponent.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent arg0) { copyValue(); }
			public void insertUpdate(DocumentEvent arg0)  { copyValue(); }
			public void changedUpdate(DocumentEvent arg0) { copyValue(); }
			
			public void copyValue() {
				String value = branchNameTextComponent.getText();
				folderNameHint.setText(value);	
			}
			
		});
		
		JPanel settingsPanel = new JPanel();
		GridLayout settingsPanelLayout = new GridLayout(2,2);
		settingsPanelLayout.setHgap(5);
		settingsPanelLayout.setVgap(5);
		settingsPanel.setLayout(settingsPanelLayout);
		componentList.add(settingsPanel);
		
		/* Branches list */
		BranchManager manager = BranchManager.getInstance();
		String [] branchList = manager.getBranchNames();
		
		/* Make settings combo box */
		JLabel makeSettingsLabel = new JLabel("Make settings:");
		JComboBox<String> makeSettingsCombo = new JComboBox<String>(branchList);
		settingsPanel.add(makeSettingsLabel);
		settingsPanel.add(makeSettingsCombo);
				
		/* Projects settings combo box */
		JLabel prjSettingsLabel = new JLabel("Projects settings:");
		JComboBox<String> prjSettingsCombo = new JComboBox<String>(branchList);
		settingsPanel.add(prjSettingsLabel);
		settingsPanel.add(prjSettingsCombo);
		
		/* Adding copying functionality to settings combo boxes */
		makeSettingsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String value = (String) makeSettingsCombo.getSelectedItem();
				prjSettingsCombo.setSelectedItem(value);
			}
		});
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				TODO Validate and take action
			}
		});
		buttonsPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dlg.dispose();
			}
		});
		buttonsPanel.add(cancelButton);
		
		componentList.add(Box.createRigidArea(new Dimension(0,5)));
		componentList.add(new JSeparator(JSeparator.HORIZONTAL));
		componentList.add(buttonsPanel);
		
		/* Adds components with gaps */
		boolean first = true;
		for(Component component : componentList) {
			if (first) first = false;
			else panel.add(Box.createRigidArea(new Dimension(0,5)));
			panel.add(component);
		}
	}
		
	@Override
	public void open(JFrame parent) {
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);
	}

}
