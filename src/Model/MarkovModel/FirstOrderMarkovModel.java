/**
 * @author xuejiang
 * date 2014-2-23
 */

package Model.MarkovModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Main.Maskit;
import SuppressionVector.SupVec;
import SuppressionVector.SupVec.PartialOutSeq;
import Utils.*;

public class FirstOrderMarkovModel extends MarkovModel{
	public FirstOrderMarkovModel(ArrayList<Sequence> s, int cycle, int t) {
		super(s,cycle,t);
		setLongesttrace();
	}
	
	private void setLongesttrace() {
		if (observation.size() <= 1) {
			longesttrace = observation.get(0);
		}
		
		int longest = 0;
		for (int i = 1; i < observation.size(); i++) {
			if (observation.get(i).getLength() > observation.get(longest).getLength()) {
				longest = i;
			}
		}
		//System.out.println("longest: "+longest);
		longesttrace = observation.get(longest);		
	}

	public void setMN(int m) {
		M = m;
		N = M;
	}
	
    public void allocateSpace() {
    	transition = new double[T][][];
    	for (int i = 0; i < T; i++) {
    		transition[i] = new double[M][M];
    	}
    }
	
	public void trainWithStatistics (ArrayList<Sequence> s) {
    	int start,end,time;
    	setMN(Maskit.locids.size());
    	allocateSpace();
    	System.out.println("model.T "+T+" M "+M);
    	
    	int startToN = 0;;
    	for (int i = 0; i < s.size(); i++) {
			transition[0][0][s.get(i).getContext(0).getIndex()] += 1;
			startToN += 1;
		}
    	transition[0][0][0] = 1.0;
		for (int i = 1; i < M; i++) {
			transition[0][0][i] /= startToN;
			transition[T-1][i][Maskit.end_index] = 1.0;
		}
		
		if (T > 2) {
	    	int[][] sum_each_line = new int[T-1][M-1];
	    	for (int j = 0; j < s.size(); j++) {
	    		for (int i = 0; i < s.get(j).getLength()-1; i++) {
	    			start = s.get(j).getContext(i).getIndex();
	    			end = s.get(j).getContext(i+1).getIndex();
	    			time = s.get(j).getContext(i).getT(); 
	    			System.out.println("time "+time+"start "+start+" end "+end);
	    			if (time < T-1) {
	    				transition[time][start][end] += 1;
		    			sum_each_line[time][start] += 1;	
	    			}
	    		}
	    	}
	    	
	    	for (int t = 1; t < transition.length-1; t++) {
	    		for (int i = 1; i< M-1; i++) {
	        		for (int j = 1; j < M-1; j++) {
	        			if (sum_each_line[t][i] != 0) {
	        				transition[t][i][j] /= sum_each_line[t][i];
	        			} else {
	        				transition[t][i][j] = 0;
	        			}
	        			
	        		}
	        	}	
	    	}			
		}
	}
	
	public void train() {
		trainWithStatistics(observation);
	}
	
	public int getM() {
		return M;
	}
	
	public int getT() {
		return T;
	}
		
    public void outputMatrix(double[][] p) {
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[1].length; j++) {
                System.out.print(p[i][j] + "\t");
            }
            System.out.println();
        }    	
    }
    
	public void output() {
		for (int i = 0; i < transition.length; i++) {
			 System.out.println("transition matrix "+i);
			 outputMatrix(transition[i]);
		}
	}

	@Override
	public void outputTransition(String f) throws IOException {
		// TODO Auto-generated method stub
    	FileWriter output = new FileWriter(f);
    	BufferedWriter writer = new BufferedWriter(output);
    	
    	StringBuffer bf;
		for (int t = 0; t < transition.length; t++) {
			bf = new StringBuffer("A"+t+"\n");
			writer.write(bf.toString());
			for (int i = 0; i < M; i++) {
				bf = new StringBuffer("");
	    		for (int j = 0; j < M; j++) {
	    			bf.append(transition[t][i][j]);
	    			if (j != M) {
	    				bf.append(" ");
	    			}
	    		}
	    		bf.append("\n");
	    		writer.write(bf.toString());
	    	} 	
		}	
    	writer.close();
	}

	@Override
	public double getPriorProb(int t, int index){
		Matric temp = new Matric(transition[0]);
		for (int n = 1;  n < t; n++) {
			Matric.multiply(temp, transition[n]);
		}
		
		double sum = temp.sumOfCol(index);
		return sum;
	}
	
	public double getPosterior(int index, int t, PartialOutSeq s, SupVec sv) {
		forward(s.t1, s.index1, s.t2, t, sv.getP());
		backward(s.t2, s.index2, t, sv.getP());	
		
		System.out.println("alpha");
		outputMatrix(alpha);
		System.out.println("beta");
		outputMatrix(beta);
		
		double sum = 0.0;
		for (int i = 0; i < M; i++) {
			sum += alpha[t][i] * beta[t][i];
		}
		System.out.println("sum "+sum);
		double p;
		if (sum != 0) {
			p = alpha[t][index] * beta[t][index];
			System.out.println("fenziof p : "+ p);
		    p /= sum;
		} else {
			p = 0;
		}
		System.out.println("p "+p);
		return p;
	}
	
	public double getPosterior(int t, int index, int start, int index1, int end, int index2) {
		System.out.println("t "+t+" index "+index+" start "+start+" index1 "+index1+" end "+end+" index2 "+index2);
		double molecular1 = getProb(t, index, start, index1);
		double molecular2 = getProb(end, index2, t, index);
		double dominator = getProb(end, index2, start, index1);
		
		if (dominator == 0) {
			System.out.println("FirstOrderMarkovModel: getPosterior : The dominator is zero!");
			return 0.0;
		}
		return molecular1 * molecular2 / dominator;
	}
	
	public void forward(int start, int index1, int end, int time, double[][] p) {
		/*initial alpha[start][index1]=1 0 for others*/
		alpha = new double[Maskit.T+1][M]; //alpha[0][] indicates alpha("start",-1)

		for (int i = 0; i < M; i++) {
        	if (i == index1) {
        		alpha[start][i] = 1;
        	}
        }
        for (int t = start+1; t <= time; t++) {
            for (int i = 0; i < alpha[0].length; i++) {
                alpha[t][i] = 0.0;
                if (t == start+1) {
                	alpha[t][i] = alpha[t-1][index1] * transition[t-1][index1][i] * (1-p[t-1][index1]);		
            	} else {
            		for (int j = 0; j < alpha[0].length; j++) {
                		alpha[t][i] += alpha[t-1][j] * transition[t-1][j][i] * p[t-1][j]; //p[t][i]:suppression probability                                       		
            		}
            	}               
            } 
        }		
	}

	public void backward(int end, int index2, int time, double[][] p) {
        beta = new double[Maskit.T+1][M];
        for (int i = 0; i < M; i++) {
        	if (i == index2) {
        		beta[end][index2] = 1.0 - p[end][index2];
        	}
        }
		
        for (int t = end - 1; t >= time; t--) {
            for (int i = 0; i < M; i++) {
            	beta[t][i] = 0.0;
            	if (t == end - 1) {
            		beta[t][i] = beta[t+1][index2] * transition[t][i][index2] * p[t][i];
            	} else {
            		for (int j = 0; j < M; j++) {
            			beta[t][i] += transition[t][i][j] * beta[t + 1][j] * p[t][i];
            		}
            	}               
            }
        }		
	}

	@Override
	public double getTransition(int t, int i, int j) {
			return transition[t][i][j];
	}

	@Override
	public double getPosterior(int index, int t, Sequence s, int cj) {
		forward(t, s);
		backward(t, s);	
		
		double sum = 0.0;
		for (int i = 0; i < M; i++) {
			sum += alpha[t][i] * beta[t][i];
		}
		
		double p = alpha[t][index] * beta[t][index] / sum;
	
		return p;
	}

	private void forward(int t, Sequence s) {
		// TODO Auto-generated method stub
		
	}

	private void backward(int t, Sequence s) {
		// TODO Auto-generated method stub
		
	}

	/*Pr[Xt = index|Xstart = index2]*/
	public double getProb(int t, int index, int start, int index2) {
		System.out.println("t "+t+" index "+index+" start "+start+" index2 "+index2);
		int step = t - start;
		
		if ((step == 0) && (index == index2)) {
			return 1.0;
		} else if (step == 0) {
			return 0.0;
		}
		if ((start == Maskit.T) || (start == Maskit.T+1)) {
			return 1.0;
		}
		Matric temp = new Matric(transition[start]);
		for (int i = 1; i < step; i++)  {
			Matric.multiply(temp, transition[start+i]);
		}

		return temp.get(index2, index);
	}
	
	@Override
	public int[] Reached( int t, int index1, int step) {
		ArrayList<Integer> tresult = new ArrayList<Integer>();
		FastVector vector = new FastVector(M);

		Matric temp = new Matric(transition[t]);
		
		vector.set(index1, 1.0);
		//vector.output();
		
		for (int n = 1;  n <= step; n++) {
			Matric.multiply(vector,temp);
		}
		
		for (int i = 0; i < M; i++) {
			if ((vector.get(i) > 0) && (Maskit.mapping.get(t+step, i) != 0)) {
				tresult.add(i);
			}
		}
		
		int[] result = new int[tresult.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = tresult.get(i);
		}
		return result;
	}

	@Override
	public int[] Reached(int t) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < Maskit.mapping.getCol(); i++) {
			if (Maskit.mapping.get(t, i) != 0) {
				result.add(i);
			}
		}
		int[] r = new int[result.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = result.get(i);
		}
		return r;
	}
}
