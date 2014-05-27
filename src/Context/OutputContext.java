package Context;

public class OutputContext extends Context{
	private boolean suppressed;
	
	public OutputContext(Context s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	public boolean getSuppressed() {
		return suppressed;
	}
	
	public void setSuppressed(boolean b) {
		suppressed = b;
	}
}
