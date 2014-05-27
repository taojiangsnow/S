package DataInput.Converters;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;


public abstract class DataConverter {
	protected String filename;
	
	protected abstract void convert() throws MatlabConnectionException, MatlabInvocationException;
}
