package SuppressionVectorTrain;

import Main.Maskit;
import SuppressionVector.SupVec;
import SuppressionVector.SupVecSet;
import Utils.PreferredSettings;

public class GreedyTrain extends SupVecTrain{
	@Override
	public void train() {
		// TODO Auto-generated method stub
		SupVecSet max_false = new SupVecSet();
		SupVecSet min_true = new SupVecSet();
		SupVecSet max_cand = new SupVecSet();
		
		SupVec sv = new SupVec(Maskit.T+1,Maskit.locids.size());
		System.out.println("suppression vector' size:"+ sv.getLength()+" "+sv.getNumC());;
		//sv.initialWithOne();
		sv.setP(3, 3, 1.0); 
		sv.setP(3, 6, 1.0); 
		sv.setP(3, 14, 1.0);
		sv.setP(3, 16, 1.0);
		sv.setP(4, 4, 1.0);
		sv.setP(4, 7, 1.0);
		max_cand.add(sv);
		
		SupVec p;
		while(max_cand.getSize() != 0) {
			p = max_cand.getSV(0);
			if (p.isPrivate()) {
				SupVec min_p = findMinTrue((SupVec)p.clone());
				min_true.add((SupVec)min_p.clone());
				System.out.println("min_true.add(min_p); min_true size "+min_true.getSize());
				//min_p.output();
				max_cand = updateCandidates(max_cand, min_p, min_true);
				System.out.println("****after_update****");
				for (int i = 0; i < max_cand.getSize(); i++) {
					max_cand.getSV(i).output();
				}
				System.out.println("updateCandidates(max_cand, min_p, min_true);");
			} else {
				max_false.add((SupVec)p.clone());
				max_cand.remove(0);
			}
		}
		
		System.out.println("**********Done Searching****************");
		double recall_max = min_true.getSV(0).getUtility(PreferredSettings.utility);
		double recall;
		int max_index = 0;
		System.out.println("min_true.size "+min_true.getSize());
		for (int i = 1; i < min_true.getSize(); i++) {
			recall = min_true.getSV(i).getUtility(PreferredSettings.utility);
			System.out.println("p ");
			min_true.getSV(i).output();
			System.out.println("utility " + recall);
			if (recall > recall_max) {
				recall_max = recall;
				max_index = i;
			}
		}
		
		SupVecTrain.setPbest(min_true.getSV(max_index));
		SupVecTrain.outputPbest();
	}

	private SupVecSet updateCandidates(SupVecSet set, SupVec pmin, SupVecSet mt) {
		SupVecSet new_set = new SupVecSet();
		SupVec new_supv;
		SupVec cp;
		int raw = pmin.getLength();
		int col = pmin.getNumC();
		
		System.out.println("****Max_cand_initial****");
		for (int i = 0; i < set.getSize(); i++) {
			set.getSV(i).output();
		}
		
		for (int i = 0; i < set.getSize(); i++) {
			cp = set.getSV(i);
			if (cp.isGreaterEq(pmin)) {
				System.out.println("MinTrue set");
				mt.output();
				new_supv = (SupVec)cp.clone();
				for (int s = 1; s < raw-1; s++) {
					for (int m = 1; m < col-1; m++) {	
						if (pmin.getP(s, m) < 1.0/PreferredSettings.k) {
							continue;
						}
						new_supv.setP(s, m, pmin.getP(s, m)-1.0/PreferredSettings.k);
						if (new_supv.isMaximal(mt)) {
							System.out.println("ismaximal");
							new_set.add(new_supv);
						}
					}
				}
			} else {
				new_set.add((SupVec)cp.clone());
			}
		}
		System.out.println("***new set***");
		new_set.output();

		System.out.println("****new cand****");
		set.output();
		
		return new_set;
	}

	private SupVec findMinTrue(SupVec p) {
		for (int i = 1; i < p.getLength()-1; i++) {
			for (int j = 1; j < p.getNumC()-1; j++) {
				if (Maskit.mapping.get(i, j) != 0) {
					findMinTrueInDim(i,j,p);
				}
			}
		}
		return p;
	}

	private void findMinTrueInDim(int i, int j, SupVec p) {
		int hi = (int) (p.getP(i, j)* PreferredSettings.k);
		int hi_initial = hi;
		double initial = p.getP(i, j);
		
		System.out.println("findMinTrueInDim: hi "+hi);
		p.setP(i, j, 0.0);
		if (p.isPrivate()) {
			System.out.println("findMintrueDim p.isprivate");
			p.output();
			return ;
		} else {
			System.out.println("findMintrueDim p.not__isprivate");
			int lo = 0;
			while (hi - lo >= 2) {
				int mid = (hi + lo)/2;
				p.setP(i, j, mid/PreferredSettings.k);
				if (p.isPrivate()) {
					hi = mid;
				} else {
					lo = mid;
				}
			}
			if (hi_initial == hi) {
				p.setP(i,j,initial);
			}	
		}
	}	
}
