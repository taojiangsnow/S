package DataInput.Converters;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

public class MatConverter extends DataConverter{
	public MatConverter(String f) {
		filename = f;
	}
	
	@Override
	public void convert() throws MatlabConnectionException, MatlabInvocationException {
		MatlabProxyFactoryOptions option = new MatlabProxyFactoryOptions.Builder()
			 									.setMatlabLocation("D:\\software\\MATLAB_R2009A\\bin\\matlab.exe")
			 									.build();

		MatlabProxyFactory factory = new MatlabProxyFactory(option);
		MatlabProxy proxy = factory.getProxy();
		proxy.eval("ReadData");		
	}
}
