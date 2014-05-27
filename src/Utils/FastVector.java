package Utils;

public class FastVector {
	public double[] vector; 
	
	public FastVector(int length) {
		vector = new double[length];
	}
	
	public FastVector(double[] v) {
		vector = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			vector[i] = v[i];
		}
	}

	public double get(int i) {
		return vector[i];
	}
	
	public void set(int i, double j) {
		vector[i] = j;
	}
	
	public void clear() {
		vector = null;
	}
	
	public void output() {
		for (int i = 0; i < vector.length; i++) {
			System.out.print(vector[i]+" ");
		}
	}
	public int size() {
		return vector.length;
	}
}