package vars.properties.bool;

import vars.properties.GlobalProperties;

/**
 * <p>This facade is intended to be used whenever working with
 * a complex property string that represents a set of boolean
 * values. It was first created for the {@link
 * gui.dialog.preferences.types.TogglePreferenceType TogglePreferenceType}
 * class, in order to maintain a stable property value state, delegating
 * it this class, but still, manipulating it in a more abstract way,
 * with operations on a boolean array.
 * <p>A boolean property has to provide not only a boolean array but also
 * a string array to be displayed in the GUI. Both have to be in the same
 * order. That is, the i-th term of one has to correspond to the i-th
 * term of the other. Consequently, both must have the same size.
 * 
 * @author guidanoli
 *
 */
public interface BooleanProperty {

	/**
	 * Transforms the property String as-is from the GlobalProperties
	 * class to an (ordered) boolean array. This and the properties
	 * labels array must be in order!
	 * @param propertyString - property string as got from GP
	 * @return array of booleans, whether the i-th sub-property is
	 * true or not
	 * @see {@link #toPropertyString(boolean[])}
	 * @see {@link GlobalProperties}
	 */
	public boolean [] toBooleanArray(String propertyString);
	
	/**
	 * The reverse function of {@link #toBooleanArray(String)}.
	 * @param booleanArray - array of boolean values for each
	 * sub-property
	 * @return property String, as to be set to GP. It can
	 * only return {@code null} if the state is invalid.
	 * @see {@link #toBooleanArray(String)}
	 * @see {@link GlobalProperties}
	 */
	public String toPropertyString(boolean [] booleanArray);
	
	/**
	 * Validates the current state of the boolean array.
	 * @param booleanArray - array of booleans, whether the i-th
	 * sub-property is true or not
	 * @return {@code true} if the state is valid and can be
	 * set to GP
	 * @see {@link #toBooleanArray(String)}
	 * @see {@link GlobalProperties}
	 */
	public boolean validateProperty(boolean [] booleanArray);
	
	/**
	 * @return array of string where the i-th item is a description
	 * of the i-th value on the boolean array. This array may <b>NOT</b>
	 * have null values.
	 */
	public String [] getPropertyLabels();
	
	/**
	 * @return default value of property, formatted as it would be set
	 * in GlobalProperties.
	 * @see {@link GlobalProperties}
	 */
	public String getDefaultPropertyValue();
	
}
