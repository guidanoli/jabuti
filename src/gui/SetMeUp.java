package gui;

import gui.maindlg.MainFrame;

public class SetMeUp {

	public static String version = "1.0";
	
	public static void main(String[] args) {
		MainFrame painel;
		try {
			painel = new MainFrame("SetMeUp v."+version);
			painel.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
