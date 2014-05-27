package DataInput.Loaders.SensitiveContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class TxtSensitiveContextSetLoader extends SensitiveContextSetLoader{
	private String filename;
	
	public TxtSensitiveContextSetLoader(String f) {
		filename = f; 
	}
	
	public HashSet<String> getSensitiveContextSet() throws FileNotFoundException {
		HashSet<String>set = new HashSet<String>();
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		
		String c = scanner.next();
		set.add(c);
		while (scanner.hasNext()) {
			c = scanner.next();
			set.add(c);
		}
		scanner.close();
		
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			System.out.print(it.next()+" ");
		}
		
		return set;
	}
}
