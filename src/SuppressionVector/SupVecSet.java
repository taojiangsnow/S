/*
 * @author xuejiang 
 */

package SuppressionVector;

import java.util.ArrayList;

public class SupVecSet {
	private ArrayList<SupVec> set;
	
	public SupVecSet() {
		set = new ArrayList<SupVec>();
	}
	
	public int getSize() {
		return set.size();
	}
	
	public SupVec getSV(int i) {
		if (i < set.size()) {
			return set.get(i);
		} else {
			return null;
		}
	}
	
	public void add(SupVec v) {
		set.add(v);
	}
	
	public void remove(int i) {
		set.get(i).clear();
		set.remove(0);
	}

	public void output() {
		for (SupVec p: set) {
			p.output();
			System.out.println();
		}
	}
	public void clear() {
		if (set.size() == 0) {
			return;
		} else {
			for (int i = 0; i < set.size(); i++) {
				set.get(i).clear();
			}
		}		
	}
	
	public Object clone() {
		SupVecSet temp = new SupVecSet();
		for (int i = 0; i < set.size(); i++) {
			temp.add((SupVec)set.get(i).clone());
		}
		return temp;
	}
}
