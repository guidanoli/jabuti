package vis;

import gui.error.FatalError;

public class Launcher {
	
	public Launcher(BranchManager manager) {
		String [] branchNames = manager.getBranchNames();
		boolean [] setup = manager.getBoolSetup();
		boolean [] make = manager.getBoolMake();
		if( branchNames == null || setup == null || make == null )
			FatalError.show("Could not gather informations about branches.");
		for( int i = 0 ; i < branchNames.length ; i++ )
			launch(branchNames[i],setup[i],make[i]);
	}
	
	protected void launch(String branch, boolean setup, boolean make) {
		new Thread() {
			public void run() {
				//TODO: hook up to vis
				if(setup) System.out.println("Setting up "+branch+"...");
				if(make) System.out.println("Making "+branch+"...");
			}
		}.start();
	}
	
}
