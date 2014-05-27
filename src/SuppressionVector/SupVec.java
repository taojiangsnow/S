/*
 * @author xuejiang
 * 
 */

package SuppressionVector;

import java.util.ArrayList;

import Check.Utility.*;
import DataInput.Filters.*;
import Main.Maskit;
import Model.MarkovModel.MarkovModel;
import SuppressionVectorTrain.PassedSV;
import SuppressionVectorTrain.SupVecTrain;
import Utils.Cycle;
import Utils.PreferredSettings;

public class SupVec implements Cloneable{
	private double[][] p; //suppression probability of each context c at each time t
	private int num_c;  // number of contexts
	private int length; // t
	
	public SupVec(int l, int n) {
		length = l;
		num_c = n;
		p = new double[length][num_c];
	}
	
	public SupVec(SupVec s) {
		p = new double[s.getLength()][s.getNumC()];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.num_c; j++) {
				p[i][j] = s.p[i][j];
			}
		}
		length = s.length;
		num_c = s.num_c;
	}
	
	public void initialWithOne() {
		for (int i = 1; i < length-1; i++) {
			for (int j = 1; j < num_c-1; j++) {
				if (Maskit.mapping.get(i, j) != 0) {
					p[i][j] = 1.0;
				}
			}
		}
	}
	
	public Object clone() {
		SupVec temp = new SupVec(this);
		return temp;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getNumC() {
		return num_c;
	}
	
	public void setP(int i, int j, double q) {
		p[i][j] = q;
	}
	
	public double[][] getP() {
		return p;
	}
	
	public double getP(int i, int j) {
		return p[i][j];
	}
	
	public static void train(SupVecTrain t) {
		t.train();
	}

	public boolean isGreaterEq(SupVec pmin) {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < p[i].length; j++) {
				if (p[i][j] < pmin.getP(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isGreater(SupVec pmin,double[][] p2) {
		pmin.output();
		
		for (int i = 0; i < p2.length; i++) {
			for (int j = 0; j < p2[0].length; j++) {
				System.out.print(p2[i][j]+ " ");
			}
		}
		for (int i = 1; i < pmin.length-1; i++) {
			for (int j = 1; j < pmin.num_c-1; j++) {
				if (pmin.getP(i,j) <= p2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isMaximal(SupVecSet mt) {
		System.out.println("111");
		for (int i = 0; i < mt.getSize(); i++) {
			if (!isGreater(mt.getSV(i),p)) {
				return false;
			}
		}
		return true;
	}
	
	public void clear() {
		p = null;
		length = 0;
		num_c = 0;
	}
	
	public void output() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < num_c; j++) {
				System.out.print(p[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public boolean isPrivate() {
		int indexofsc; 
		double prior,posterior;
		int t1,t2,t,j;
		boolean flag = false;
		for (t = 0; t < length; t++) {
			t1 = t; //partial output sequence's start time
			t2 = t; //partial output sequence's end time
			for (j = 0; j < Maskit.sensitive_context_set.length; j++) {
				indexofsc = Maskit.sensitive_context_set[j];	
				
				if (Maskit.mapping.get(t,indexofsc) == 0) {
					continue;
				}
				
				System.out.println("t "+t+" c "+indexofsc);
				prior = PreferredSettings.model.getPriorProb(t,indexofsc);	
				System.out.println("Suppression Vector");
				this.output();
				System.out.println(" prior "+prior);
				
				/*get partial output sequence set*/
				PartialOutSeqSet pos = new PartialOutSeqSet();
				for (int ps = 0; ps <= t1; ps++) {
					for (int c1 = 0; c1 < Maskit.locids.size(); c1++) {
						//System.out.print("ps "+ps+" c1 "+c1);
						if ((Maskit.mapping.get(ps, c1) != 0) && (p[ps][c1] < 1.0) && PreferredSettings.model.getPriorProb(ps, c1) > 0) {
							for (int pe = t2; pe < length; pe++) {
								for (int c2 = 0; c2 < Maskit.locids.size(); c2++) {
									//System.out.print(" pe "+pe+" c2 "+c2);
									if (ps == pe) {
										//System.out.println("ps == pe ps "+ps+" c1 "+c2+" pe "+pe);
										if ((indexofsc == c1) && (c1 == c2)) {
											pos.add(new PartialOutSeq(ps,pe,c1,c1));
										}
									} else {
										//System.out.println("ps != pe ps "+ps+" c1 "+c2+" pe "+pe);
										if ((ps == t) && (indexofsc == c1) && (Maskit.mapping.get(pe, c2) != 0) && (p[pe][c2] < 1.0)) {
											pos.add(new PartialOutSeq(ps,pe,c1,c2));
										}
										if ((pe == t) && (indexofsc == c2) && (Maskit.mapping.get(pe, c2) != 0) && (p[pe][c2] < 1.0)) {
											//System.out.println("pe == t j == c2");
											pos.add(new PartialOutSeq(ps,pe,c1,c2));
										} 
										if ((ps != t) && (pe != t) && (Maskit.mapping.get(pe, c2) != 0) && (p[pe][c2] < 1.0)) {
											pos.add(new PartialOutSeq(ps,pe,c1,c2));
										}
									}
								}
							}
						}
					}
				}
				
				System.out.println("*******check p for each output sequence pos.size: "+pos.size());
				pos.output();
				System.out.println();
				/*check p for each output sequence*/
				for (int i = 0; i < pos.size(); i++) {
					pos.get(i).output();
					if (pos.get(i).isProbZero()) {
						System.out.println("pos.get(i).isProbZero()");
						continue;
					}
					if (SupVecTrain.psvs.containsDominator(this, t, indexofsc, pos.get(i))) {
						System.out.println("current p");
						this.output();
						System.out.println("SupVecTrain.psvs.containsDominated(this, t, indexofsc");
						continue;	
					}
					System.out.println("Computing posterior probability");
					posterior = PreferredSettings.model.getPosterior(indexofsc,t,pos.get(i),this);
					System.out.println("posterior "+posterior+" prior "+prior+" "+(double)(Math.round(Math.abs(posterior - prior)*100)/100.0));
					if ((double)(Math.round(Math.abs(posterior - prior)*100)/100.0) > PreferredSettings.delta) {
						flag = false;
						return false;
					} else {
						System.out.println("Add PassedSV");
						PassedSV psv = new PassedSV(t,indexofsc,pos.get(i),(SupVec)this.clone());
						SupVecTrain.psvs.add(psv);
						psv.p.output();
						flag = true;
						System.out.println("PassedSV SIZE: "+SupVecTrain.psvs.size());
					}
				}
			} 
		}
		return flag;
	}
	
	public class PartialOutSeq {
		public int t1;
		public int t2;		
		public int index1;
		public int index2;
		
		public PartialOutSeq(int t, int s, int i, int j) {
			t1 = t;
			t2 = s;
			index1 = i;
			index2 = j;
		}

		public int getTimeOfOutSeq(int i) {
			return t1+i;
		}
		
		public boolean equals(PartialOutSeq s) {
			if ((t1 == s.t1) && (t2 == s.t2) && (index1 == s.index1) && (index2 == s.index2)) {
				return true;
			} else {
				return false;
			}
	}
		public void output() {
			System.out.println("***t1 "+ t1 + " index1 "+index1+" t2 "+t2+" index2 "+index2);
		}
		
		public boolean isProbZero() {	
			int step = t2 - t1;
			if (step == 0) {
				if (PreferredSettings.model.getPriorProb(t1,index1) > 0) {
					return false;
				} else {
					return true;
				}
			} else {
				int next_c = Maskit.locids.size();
				boolean[][] next = new boolean[step][];
		    	for (int q = 0; q < step; q++) {
					next[q] = new boolean[next_c];
				}
		    	for (int i = 0; i < step; i++) {
		    		for (int j = 0; j < next_c; j++) {
		    			next[i][j] = false;
		    		}
		    	}
		    	
				int s = 1;
				double prob;
				boolean proceed;
					
				while (s <= step) {
					proceed = false;
					if (s == 1) {
						for (int end = 0; end < next_c; end++) {
							if (Maskit.mapping.get(t1+s, end) != 0) {
								prob = ((MarkovModel)PreferredSettings.model).getTransition(t1,index1,end);
								System.out.println("s=1 prob: "+prob);
								if ((p[t1+s][end] > 0.0) && (prob > 0.0)) {
									next[s-1][end] = true;
									proceed = true;
								}
							}
						}
					} else {
						for (int begin = 0; begin < next_c; begin++) {
							if (next[s-2][begin]) {
								if (s == step) {
									prob = ((MarkovModel)PreferredSettings.model).getTransition(t1+s-1,begin, index2);
									if (prob > 0.0) {
										next[s-1][index2] = true;
										proceed = true;
									}
								} else {
									for (int end = 0; end < next_c; end++) {
										if (Maskit.mapping.get(t1+s, end) != 0) {
											prob = ((MarkovModel)PreferredSettings.model).getTransition(t1+s-1,begin, end);
											System.out.println("s "+s+" prob "+prob);
											if ((p[t1+s][end] > 0.0) && (prob > 0.0)) {
												next[s-1][end] = true;
												proceed = true;
											}
										}
									}
								}
							}		
						}
					}
					System.out.println("proceed "+proceed);
					if (proceed) {
						s++;
					} else {
						break;
					}
				}
				if (s > step) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	public class PartialOutSeqSet {
		public ArrayList<PartialOutSeq> set;
		
		public PartialOutSeqSet() {
			set = new ArrayList<PartialOutSeq>();
		}
		
		public void add(PartialOutSeq s) {
			if (!isContain(s)) {
				set.add(s);
			}
		}
		
		public boolean isContain(PartialOutSeq s) {
			for (PartialOutSeq q: set) {
				if (q.equals(s)) {
					return true;
				}
			}
			return false;
		}
		
		public int size() {
			return set.size();
		}
		
		public void output() {
			for (int i = 0; i <set.size(); i++) {
				set.get(i).output();
			}
		}
		
		public PartialOutSeq get(int i) {
			if (i < set.size()) {
				return set.get(i);
			} else {
				return null;
			} 
		}
	}

	public double getUtility(Utility utility) {
		// TODO Auto-generated method stub
		return utility.getUtility(this);
	}
}
