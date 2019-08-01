package gui.dialog.preferences.types;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;

import gui.dialog.preferences.PreferenceType;
import vars.Language;

public class SliderPreferenceType implements PreferenceType {

	protected JPanel panel = new JPanel(new GridLayout(1,1));
	protected JSlider slider;
	public SliderPreferenceType(int min, int max) {
		slider = new JSlider(min, max);
		panel.add(slider);
	}
	public SliderPreferenceType(int min, int max, int minTicks, int majTicks) {
		this(min, max);
		slider.setMinorTickSpacing(minTicks);
		slider.setMajorTickSpacing(majTicks);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	public JPanel getPanel(Language lang) { return panel; }
	public void setState(String value) {
		int num = Integer.parseInt(value);
		slider.setValue(num);
	}
	public String getState() {
		return Integer.toString(slider.getValue());
	}
	public boolean validateValue(String value) {
		boolean isNumeric = value.matches("\\d+");
		if(!isNumeric) return false;
		int num = Integer.parseInt(value);
		return num >= slider.getMinimum() && num <= slider.getMaximum();
	}

}
