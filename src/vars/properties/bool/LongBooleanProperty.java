package vars.properties.bool;

import vars.Language;
import vars.properties.GlobalProperties;

/**
 * Stores up to 64 properties in an optimal secure way (binary representation).
 * 
 * @author guidanoli
 */
public class LongBooleanProperty implements BooleanProperty {

	private int count;
	private boolean [] defaultValues;
	private String labelsKeyPreffix;
		
	public LongBooleanProperty(int count, boolean defaultValue, String propertyLabelsKey) {
		assert count > 0;
		this.count = count;
		this.defaultValues = new boolean[count];
		for(int i = 0; i < count; i++) this.defaultValues[i] = defaultValue;
		this.labelsKeyPreffix = propertyLabelsKey;
	}
	
	public LongBooleanProperty(boolean [] defaultValues, String propertyLabelsKey) {
		if(defaultValues == null) count = 0;
		else count = defaultValues.length;
		this.defaultValues = defaultValues;
		this.labelsKeyPreffix = propertyLabelsKey;
	}

	public boolean[] toBooleanArray(String propertyString) {
		Long number = Long.parseLong(propertyString);
		String mask = String.format("%%%ds", count);
		String binStr = String.format(mask, Long.toBinaryString(number)).replace(' ', '0');
		boolean [] booleanArray = new boolean[count];
		for(int i = 0; i < count; i++) booleanArray[i] = binStr.charAt(i) == '1';
		return booleanArray;
	}

	public String toPropertyString(boolean[] booleanArray) {
		StringBuilder sb = new StringBuilder();
		for(boolean b : booleanArray) sb.append(b?"1":"0");
		Long binLong = Long.parseLong(sb.toString(), 2);
		return binLong.toString();
	}

	public boolean validateProperty(boolean[] booleanArray) {
		return booleanArray.length == count;
	}

	public String[] getPropertyLabels() {
		Language lang = Language.getInstance();
		String [] propertyLabels = new String[count];
		for(int i = 0; i < count; i++)
		{
			String langKey = String.format("meta_keylabel_%s_%d", labelsKeyPreffix, i);
			propertyLabels[i] = lang.get(langKey);
			if( propertyLabels[i] == null ) propertyLabels[i] = String.format("<%s>", langKey);
		}
		return propertyLabels;
	}
	
	public boolean isEnabled(int type) {
		GlobalProperties gp = GlobalProperties.getInstance();
		assert type >= 0 && type < count;
    	boolean [] values = toBooleanArray(gp.get(labelsKeyPreffix));
    	return values[type];
	}
	
	public String getDefaultPropertyValue() { return toPropertyString(defaultValues); }

}
