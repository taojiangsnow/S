package SuppressionVectorTrain;

import java.util.ArrayList;

import SuppressionVector.SupVec;
import SuppressionVector.SupVec.PartialOutSeq;

public class PassedSVSet {
	public ArrayList<PassedSV> set;
	
	public PassedSVSet() {
		set = new ArrayList<PassedSV>();
	}
	
	public void add(PassedSV s) {
		set.add(s);
	}
	
	public int size() {
		return set.size();
	}
	/*parameters:
	 * t: time
	 * s: sensitive context's index
	 * */
	public boolean containsDominator(SupVec sv, int t, int s, PartialOutSeq q) {
		PassedSV temp;
		if (set.size() == 0) {
			return false;
		}
		for (int i = 0; i < set.size(); i++) {
			temp = set.get(i);
			//System.out.println("temp.p");
			//temp.p.output();
			if ((sv.isGreaterEq(temp.p)) && (temp.time == t) && (temp.sc == s) && (temp.o.equals(q))) {
				return true;
			}
		}
		return false;		
	}
}
