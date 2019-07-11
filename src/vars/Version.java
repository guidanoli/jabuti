package vars;

/**
 * As described by <a href="https://semver.org/">Semantic Versioning 2.0.0</a>,
 * there are three parts of a version string:
 * 
 * <ol>
 * <li>MAJOR version when you make incompatible API changes</li>
 * <li>MINOR version when you add functionality in a backwards-compatible manner</li>
 * <li>PATCH version when you make backwards-compatible bug fixes</li>
 * </ol>
 * 
 * Joining as <b>MAJOR.MINOR.PATCH</b>
 * 
 * @author guidanoli
 *
 */
public class Version {

	String versionString;
	
	/**
	 * Constructs version object
	 * @param version - version string
	 */
	public Version(String version) {
		assert version != null;
		versionString = version;
	}
	
	/**
	 * @return major version number
	 */
	public int getMajor() {
		String pattern = "v(\\d+).*";
		String majorStr = versionString.replaceAll(pattern, "$1");
		if( majorStr == null ) return 0;
		try {
			int major = Integer.parseInt(majorStr);
			return major;
		} catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * @return minor version number
	 */
	public int getMinor() {
		String pattern = "v\\d+\\.(\\d+).*";
		String minorStr = versionString.replaceAll(pattern, "$1");
		if( minorStr == null ) return 0;
		try {
			int minor = Integer.parseInt(minorStr);
			return minor;
		} catch(Exception e) {
			return 0;
		}
	}

	/**
	 * @return patch string (can contain letters)
	 */
	public String getPatch() {
		String pattern = "v\\d+\\.\\d+\\.(.*)";
		return versionString.replaceAll(pattern, "$1");
	}
	
	public boolean earlierThan(Version anotherVersion) {
		
		int majorComp = getMajor() - anotherVersion.getMajor();
		int minorComp = getMinor() - anotherVersion.getMinor();
		int patchComp = getPatch().compareToIgnoreCase(anotherVersion.getPatch());
		
		if( majorComp < 0 ) {
			return true;
		}
		else if( majorComp > 0 ) {
			return false;
		}
		else
		{
			if( minorComp < 0 ) {
				return true;
			}
			else if( minorComp > 0 ) {
				return false;
			}
			else
			{
				return patchComp < 0;
			}
		}
	}
	
}
