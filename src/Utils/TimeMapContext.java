package Utils;

import Main.Maskit;

public class TimeMapContext {
	public static int length;
	public static int[][] mapping;
	
	public TimeMapContext(int l) {
		length = l; 
		mapping = new int[l][];
		for (int i = 0; i < l; i++) {
			mapping[i] = new int[Maskit.locids.size()];
		}
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < Maskit.locids.size(); j++) {
				mapping[i][j] = 0;
			}
		}
	}
	
	public void add(int T, int indexofc) {
		System.out.println("T " + T + " indexofc "+indexofc);
		if ((T >= length) || (indexofc >= mapping[0].length)) {
			System.out.println("Exceeds dimention of mapping!");
		} else {
			mapping[T][indexofc] = 1;
		}
	}
	
	public int getCol() {
		return mapping[0].length;
	}
	
	public int getLength() {
		return length;
	}

	public int[] getRow(int t) {
		if (t < length) {
			return mapping[t];
		}
		return null;
	}

	public int get(int t, int indexofsc) {
		return mapping[t][indexofsc];
	}
	
	public void output() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < mapping[0].length; j++) {
				System.out.print(mapping[i][j]);
			}
			System.out.println();
		}
	}
}
