/*
 * @author xuejiang
 * @date 2014-4-20
 */

package Context;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;

import DataInput.Loaders.SensitiveContext.SensitiveContextSetLoader;
import DataInput.Loaders.SensitiveContext.TxtSensitiveContextSetLoader;

public class SensitiveContextSet {
	protected HashSet<String> set;
	private SensitiveContextSetLoader loader;
	
	public SensitiveContextSet() {
		set = new HashSet<String>();
	}
	
	public SensitiveContextSet(SensitiveContextSetLoader l) {
		loader = l;
	}
	
	public HashSet<String> getSet() {
		return set;
	}
	
	public int getSize() {
		return set.size();
	}
	
	public void add(String s) {
		set.add(s);
	}
	
	public void setLoader(SensitiveContextSetLoader l) {
		loader = l;
	}
	
	public void getSensitiveSet() throws FileNotFoundException {
		if (loader instanceof TxtSensitiveContextSetLoader) {
			set = loader.getSensitiveContextSet();
		}			
	}
	
	public void output() {
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			System.out.print(it.toString()+" ");
		}
	}
}
