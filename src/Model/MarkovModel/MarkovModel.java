package Model.MarkovModel;

import java.io.IOException;
import java.util.ArrayList;

import Model.Model;
import Utils.Sequence;

public abstract class MarkovModel extends Model{
	protected int M; 
	protected int N; 
	protected int T;
	protected double[][][] transition; 
	protected double[][][] emission;
	
	protected double[][] alpha;
	protected double[][] beta; 

	protected ArrayList<Sequence> observation;
	protected Sequence longesttrace;
	
	public MarkovModel(ArrayList<Sequence> s, int cycle, int t) {
		super(cycle);
		observation = s;
		T = t;
	}
	
	public abstract int getT();
	public abstract int getM();

	public abstract void output();
	public abstract void outputTransition(String f) throws IOException;

	public abstract void train();
	public abstract double getTransition(int t, int i, int j);

	public void outputTransition(String f, int i) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
