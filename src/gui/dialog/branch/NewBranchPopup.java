package gui.dialog.branch;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ext.TextPrompt;
import gui.component.Console;
import gui.defaults.DefaultPopup;
import gui.dialog.MenuPopup;
import gui.error.LightError;
import net.miginfocom.swing.MigLayout;
import svn.BranchManager;
import vars.properties.GlobalProperties;

/**
 * The "New Branch" dialog allows the user to initialize a new branch
 * on its working directory by making use of an initialization script.
 * 
 * @author guidanoli
 *
 */
public class NewBranchPopup implements MenuPopup, Consumer<Boolean> {

	private DefaultPopup dlg;
	private BranchManager manager = BranchManager.getInstance();
	private GlobalProperties gp = GlobalProperties.getInstance();
	private String [] branchList;
	private JFrame parent;
	private String dlgTitle = "New Branch";
	
	/**
	 * Implementing {@link MenuPopup}
	 */
	@Override
	public void open(JFrame parent) {
		this.parent = parent;
		
		/* Updates branch list */
		branchList = manager.getBranchNames();
		if(branchList.length == 0){
			invalidate("There are no branches to base upon.");
			return;
		}
		
		/* Constructs dialog */
		dlg = new DefaultPopup(parent, dlgTitle);
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout(
				"fill, wrap 2",
				"10[right]rel[grow,fill]10"
		));
		buildDialog(panel);
		dlg.getContentPane().add(panel);
		
		/* Sets up to display */
		dlg.pack();
		dlg.setResizable(false);
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);
	}

	/**
	 * Builds the dialog itself
	 * @param panel - the panel in which the components will be laid on
	 */
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

		Console console = new Console(this);
		JScrollPane jsp = new JScrollPane(console);
		jsp.setMinimumSize(new Dimension(700, 300));
		
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		
		/* Adding validation functionality to OK button */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String branchName = branchNameTextComponent.getText();
				String folderName = folderNameTextComponent.getText();
				String makeSettingSource = (String) makeSettingsCombo.getSelectedItem();
				String prjSettingSource = (String) prjSettingsCombo.getSelectedItem();
				
				/* Validates fields */
				if (folderName.equals("")) folderName = branchName;
				if(branchName.equals("")) {
					invalidate("Branch name cannot be empty!");
					return;
				}
				String basePath = gp.get("path");
				Path newFolderPath = null;
				try {
					newFolderPath = Paths.get(basePath, folderName);
				} catch(InvalidPathException e) {
					invalidate("Folder name isn't valid!");
					return;
				}
				if(Files.exists(newFolderPath)) {
					invalidate("Folder name already exists!");
					return;
				}
				ProcessBuilder initProcessBuilder = new ProcessBuilder("cmd", "/C", "test.bat", folderName)
						.directory(new File(basePath));
				console.clearScreen();
				console.run(initProcessBuilder);
				// does initxp.bat <folderName> -> console
				// copies configprojects from <prjSettingSource>/src to <folderName>/src
				// copies configvismake from <makeSettingSource>/src to <folderName/src and changes branch name to <branchName>
			}
		});
		
		/* Adding closing functionality to Cancel button */
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
		panel.add(jsp, "span, grow");
		panel.add(okButton, "skip 1, split 2, gapbefore push, sizegroup btns");
		panel.add(cancelButton, "sizegroup btns");
	}
		
	/**
	 * Implementing {@link Consumer}
	 * Used by the console to tell whether a command was successful or not
	 * @param successful - <code>true</code>
	 */
	public void accept(Boolean successful) {
		if (successful) {
			// now copy config files to new branch
		} else {
			invalidate("An IO error occurred and the program was terminated.");
		}
	}
	
	/**
	 * Present an error message to the screen
	 * @param message
	 */
	private void invalidate(String message) {
		LightError.show(message, dlgTitle, parent);
	}
	
}
