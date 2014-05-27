package SuppressionVectorTrain;

import SuppressionVector.SupVec;
import SuppressionVector.SupVec.PartialOutSeq;

public class PassedSV {
	public int time;
	public int sc;
	public PartialOutSeq o;
	public SupVec p;
	
	public PassedSV(int i, int j, PartialOutSeq q, SupVec u) {
		time = i;
		sc = j;
		o = q;
		p = u;			
	}
}
