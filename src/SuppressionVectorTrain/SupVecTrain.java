package SuppressionVectorTrain;

import SuppressionVector.SupVec;
import SuppressionVectorTrain.PassedSV;
import SuppressionVectorTrain.PassedSVSet;

public abstract class SupVecTrain {
	public static PassedSVSet psvs = new PassedSVSet(); 
	public static SupVec p_best;
	
	public static void addPSV(PassedSV s) {
		psvs.add(s);
	}
	
	public abstract void train();
	
	public static void setPbest(SupVec p) {
		p_best = p;
	}

	public static void outputPbest() {
		for (int i = 0; i < p_best.getLength(); i++) {
			for (int j = 0; j < p_best.getNumC(); j++) {
				System.out.print(p_best.getP(i, j)+" ");
			}
			System.out.println();
		}
	}
	
}
