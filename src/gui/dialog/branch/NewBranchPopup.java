package gui.dialog.branch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ext.TextPrompt;
import gui.defaults.DefaultPopup;
import gui.dialog.MenuPopup;
import net.miginfocom.swing.MigLayout;
import svn.BranchManager;

public class NewBranchPopup implements MenuPopup {

	private DefaultPopup dlg;
	
	private void buildDialog(JPanel panel) {
		String branchDefaultHint = "Name of your branch";
		String folderDefaultHint = "Name of your folder";
		
		/* Branch name text field */
		JLabel branchNameLabel = new JLabel("Branch:");
		JTextField branchNameTextComponent = new JTextField();
		TextPrompt branchNameHint = new TextPrompt(branchDefaultHint, branchNameTextComponent);
		branchNameHint.changeAlpha(0.5f);
				
		/* Folder name text field */
		JLabel folderNameLabel = new JLabel("Folder:");
		JTextField folderNameTextComponent = new JTextField();
		TextPrompt folderNameHint = new TextPrompt(folderDefaultHint, folderNameTextComponent);
		folderNameHint.changeAlpha(0.5f);
		
		/* Add copying functionality to names text fields */
		branchNameTextComponent.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent arg0) { copyValue(); }
			public void insertUpdate(DocumentEvent arg0)  { copyValue(); }
			public void changedUpdate(DocumentEvent arg0) { copyValue(); }
			
			public void copyValue() {
				String value = branchNameTextComponent.getText();
				if (value.equals("")) value = folderDefaultHint;
				folderNameHint.setText(value);
			}
			
		});
		
		/* Add de-hint functionality to names text fields */
		folderNameTextComponent.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				String value = folderNameTextComponent.getText();
				String hint = folderNameHint.getText();
				if (value.equals(hint)) folderNameTextComponent.setText(null);
			}
			public void focusGained(FocusEvent arg0) {
				String value = folderNameTextComponent.getText();
				String hint = folderNameHint.getText();
				if (value.equals("") && !hint.equals(folderDefaultHint)) {
					folderNameTextComponent.setText(hint);
					folderNameTextComponent.setCaretPosition(hint.length());
				}
			}
		});
				
		/* Branches list */
		BranchManager manager = BranchManager.getInstance();
		String [] branchList = manager.getBranchNames();
		
		/* Make settings combo box */
		JLabel makeSettingsLabel = new JLabel("Make settings:");
		JComboBox<String> makeSettingsCombo = new JComboBox<String>(branchList);
				
		/* Projects settings combo box */
		JLabel prjSettingsLabel = new JLabel("Projects settings:");
		JComboBox<String> prjSettingsCombo = new JComboBox<String>(branchList);
		
		/* Adding copying functionality to settings combo boxes */
		makeSettingsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String value = (String) makeSettingsCombo.getSelectedItem();
				prjSettingsCombo.setSelectedItem(value);
			}
		});
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				TODO Validate and take action
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dlg.dispose();
			}
		});

		panel.add(branchNameLabel);
		panel.add(branchNameTextComponent);
		panel.add(folderNameLabel);
		panel.add(folderNameTextComponent);
		panel.add(makeSettingsLabel);
		panel.add(makeSettingsCombo);
		panel.add(prjSettingsLabel);
		panel.add(prjSettingsCombo);
		panel.add(okButton, "skip 1, split 2, gapbefore push, sizegroup btns");
		panel.add(cancelButton, "sizegroup btns");
	}
		
	@Override
	public void open(JFrame parent) {
		dlg = new DefaultPopup(parent, "New Branch");
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill, wrap 2", "10[right]rel[grow,fill]10"));
		buildDialog(panel);
		dlg.getContentPane().add(panel);
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);
	}

}
