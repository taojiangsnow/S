package Utils;

import java.util.ArrayList;

import Context.Context;

public class Sequence {
	private ArrayList<Context> sq;
	
	public Sequence() {
		sq = new ArrayList<Context>();
	}
	
	public void addContext(Context c) {
		sq.add(c);
	}
	
	public int getLength() {
		return sq.size();
	}
	
	/*get context at i*/
	public Context getContext(int i) {
		if (sq.size() == 0) {
			return null;
		} else {
			return sq.get(i);
		}
			
	}
	
	public Context getLastContext() {
		return sq.get(sq.size() -1);
	}
	
	/*get the max index of context, it equals the dimension of transition matrix*/
	public int getMaxIndex() {
		int max = 0;
		for (int i = 1; i < sq.size(); i++) {
			if (sq.get(i).getIndex() > max) {
				max = sq.get(i).getIndex();
			}
		}
		return max;
	}
	
	public void output() {
		for (int i = 0; i < sq.size(); i++) {
			sq.get(i).output();
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public  void outputS(ArrayList<Context> s) {
		for (int i = 0; i < s.size(); i++) {
			s.get(i).output();
			System.out.print(" ");
		}
	}
    /* find the context with timestamp of t
     * if there is the object, return it
     * else return the nearest one
     * 
     * */
	public int findT(int t) {
		int index = sq.size() - 1;
		for (int i = 0; i < sq.size(); i++) {
			//System.out.println(sq.size());
			if (sq.get(i).getTimetmp().convertSeconds() == t) {
				//System.out.println("t is found");
				index = i;
				break;
			} else {
					
					if (sq.get(i).getTimetmp().convertSeconds() > t) {
						index = i - 1;
						break;
					}
					
				//index = -1;
				//break;
			}
		}
		//System.out.print(index + " ");
		return index;
	}
	
	/*find context between start and end timestamp after index*/
	public int[] findBetweenT(int start, int end, int p) {
		int[] index = new int[2];
		int t = 1;
		int last = 0;
		for (int i = p + 1; i < sq.size(); i++) {
			if (sq.get(i).getTimetmp().convertSeconds() > end) {
				index[1] = last;
				break;
			}
			if (sq.get(i).getTimetmp().convertSeconds() >= start) {
				if (t == 1) {
					index[0] = i; 
				}
				t += 1;	
				last = i;
			}
		}
		return index;
	}
	
	public int[] findEndpointBetweenT(int start, int end) {
		int[] index = new int[2];
		int t = 1;
		int last = 0;
		
		for (int i = 0; i < sq.size(); i++) {
			if (sq.get(i).getTimetmp().convertSeconds() > end) {
				index[1] = last;
				break;
			}
			if (sq.get(i).getTimetmp().convertSeconds() >= start) {
				if (t == 1) {
					index[0] = i; 
				}
				t += 1;	
				last = i;
			}
		}
		return index;
	}
	
	public int findFirstAfterT(int t, int index) {
		if (t > getLastContext().getTimetmp().convertSeconds()) {
			return -1;
		} else {
			for (int i = index + 1; i < sq.size(); i++) {
				if (sq.get(i).getTimetmp().convertSeconds() > t) {
					return i;
				}
			}
			return 0;
		}
	}
	
	public int findFirstAfterT(int t) {
		if (t > getLastContext().getTimetmp().convertSeconds()) {
			return -1;
		} else {
			for (int i = 0; i < sq.size(); i++) {
				if (sq.get(i).getTimetmp().convertSeconds() > t) {
					return i;
				}
			}
			return 0;
		}		
	}
	
	public void clear() {
		for (int i= 0; i < sq.size(); i++) {
			sq.get(i).clear();
			sq.set(i, null);
		}
	}
	
	public void remove(int i) {
		sq.get(i).clear();
		sq.set(i, null);
	}
}
