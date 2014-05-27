package Check;

import Context.Context;
import Model.MarkovModel.MarkovModel;
import SuppressionVector.SupVec;

public class ProbabilisticPC extends PrivacyCheck{

	public ProbabilisticPC(double i, MarkovModel m) {
		super(i, m);
		// TODO Auto-generated constructor stub
	}

	public static boolean okayToRelease(Context c, SupVec p) {
		double sp = p.getP(c.getT(), c.getIndex());
		
		double flipping = Math.random();
		if (flipping < sp) {
			return false;
		} else {
			return true;
		}
	}
	
}
