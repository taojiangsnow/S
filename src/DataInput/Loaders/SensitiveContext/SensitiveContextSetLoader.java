package DataInput.Loaders.SensitiveContext;

import java.io.FileNotFoundException;
import java.util.HashSet;

public abstract class SensitiveContextSetLoader {
	public abstract  HashSet<String> getSensitiveContextSet() throws FileNotFoundException;
}
