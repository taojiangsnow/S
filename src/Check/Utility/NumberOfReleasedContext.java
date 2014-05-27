package Check.Utility;

import Check.SimulatableCheck;
import Check.Utility.Utility;
import Main.Maskit;
import SuppressionVector.SupVec;
import Utils.Matric;
import Utils.PreferredSettings;

public class NumberOfReleasedContext extends Utility{
	@Override
	public double getUtility(SupVec s) {
		double sum = 0.0;
		for (int i = 1; i < s.getLength()-1; i++) {
			for (int j = 1; j < s.getNumC()-1; j++) {
				sum += (1 - s.getP(i, j)) * PreferredSettings.model.getPriorProb(i,j);
			}
		}
		return sum;
	}

	@Override
	public double getUtility() {
		int nr = Maskit.mapping.getLength();
		int nc = Maskit.locids.size();
		int[][] supp = new int[nr][nc];
		System.out.println("nr "+nr+" nc "+nc);
		int num_of_suppressed_symbol = 0,index_of_cj = 0;
		
		/*compute supp*/
		for (int t_ci = 1; t_ci < nr - 1; t_ci++) {			
			for (int j = 1; j <= nc-2; j++) {
				/*only do for possible output sequence o1,...ot-1,j*/
				if ((Maskit.mapping.get(t_ci, j) != 0) && (PreferredSettings.model.getPriorProb(t_ci,j) > 0)) {
					System.out.println("**************************************t_ci "+t_ci+" ci "+j);
					num_of_suppressed_symbol = 0;
					
					/*find the number of suppressed symbol after Xt = cj*/
					for (int t = t_ci + 1; t < nr-1; t++) {						
						/*possible state j at time t*/
						int[] cj= PreferredSettings.model.Reached(t_ci, j,  t-t_ci);
						
						System.out.print("cj: ");
						for (int q = 0; q < cj.length; q++) {
							System.out.print(cj[q]+" ");
						}
						System.out.println();
						int p;
						for (p = 0; p < cj.length; p++) {
							int q;
							for (q= 0; q < Maskit.sensitive_context_set.length; q++) {
								if (cj[p] == Maskit.sensitive_context_set[q]) {
									System.out.println("***has sensitive");
									break;
								}
							}
							if (q < Maskit.sensitive_context_set.length) {
								break;
							}
						}
						if (p < cj.length) {
							num_of_suppressed_symbol++;
						} else {
							for (index_of_cj = 0; index_of_cj < cj.length; index_of_cj++) {
								//if (Maskit.mapping.get(t, index_of_cj) != 0) {
								if (SimulatableCheck.okayToRelease(t_ci, j, t, cj[index_of_cj])) {
									break;
								}
							}
							if (index_of_cj == cj.length) {
								num_of_suppressed_symbol++;
							} else {
								break;
							}
						}
					}
					supp[t_ci][j] = num_of_suppressed_symbol;	
					System.out.println("**************************************t_ci "+t_ci+" j "+j+" supp "+num_of_suppressed_symbol);
				}
			}
		}
		
		Matric gammar = new Matric(nr,nc);
		double expected_num_suppressed;
		for (int i = nr - 2; i >= 0; i--) {
			for (int j = 0; j < nc; j++) {
				if (Maskit.mapping.get(i, j) == 0) {
					continue;
				}
				expected_num_suppressed = supp[i][j];
				for (int t = 0; t < nc; t++) {
					expected_num_suppressed += PreferredSettings.model.getProb(i+supp[i][j]+1, t, i, j)*gammar.get(i+supp[i][j]+1,t);
					System.out.println("prob "+PreferredSettings.model.getProb(i+supp[i][j]+1, t, i, j)+"gamma.get("+i+supp[i][j]+1+","+t+") "+gammar.get(i+supp[i][j]+1,t)
							+"expected: "+expected_num_suppressed);
				}
				gammar.set(i, j, expected_num_suppressed);
				System.out.println("***************************gamma["+i+"]["+j+"]"+" "+expected_num_suppressed);
			}
		}

		return Maskit.T-1-gammar.get(0, 0);
	}
}
