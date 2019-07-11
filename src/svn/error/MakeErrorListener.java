package svn.error;

public class MakeErrorListener implements ErrorListener {
	
	private boolean isHandling = true;
	
	public boolean handleErrorOutput(String output) { return true; }

	public boolean isHandling() { return isHandling; }
	
	public void setHandling(boolean isHandling) { this.isHandling = isHandling; }

}
