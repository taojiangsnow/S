package Check;

import Main.Maskit;
import Utils.Sequence;
import Context.*;
import Utils.PreferredSettings;

public class SimulatableCheck {
	public static boolean okayToRelease(int start, int index1, int end, int index2) {
		double prior,posterior = 0;
		//boolean flag = false;

		for (int t = 1; t < Maskit.T; t++) {
			for (int i = 0; i < Maskit.sensitive_context_set.length; i++) {
				System.out.println("t "+t+" si "+Maskit.sensitive_context_set[i]);
				if (Maskit.mapping.get(t, Maskit.sensitive_context_set[i]) == 0) {
					continue;
				}
				System.out.println("mapping");
				prior = PreferredSettings.model.getPriorProb(t,Maskit.sensitive_context_set[i]);
				if (t > start && t < end) {
					posterior = PreferredSettings.model.getPosterior(t, Maskit.sensitive_context_set[i], start, index1, end, index2);
				} else if (t == start) {
					posterior = PreferredSettings.model.getProb(t, Maskit.sensitive_context_set[i], start, index1);
					/*
					if (Maskit.sensitive_context_set[i] != index1) {
						posterior = 0.0;
					} else {
						//flag = true;
						posterior = 1.0;
					}
					*/
				} else if (t == end) {
					posterior = PreferredSettings.model.getProb(t, Maskit.sensitive_context_set[i], end, index2);
					/*
					if (index2 != Maskit.sensitive_context_set[i]) {
						posterior = 0.0;
					} else {
						//flag = true;
						posterior = 1.0;
					}
					*/
				} else if (t < start) {
					System.out.println("t < start");
					posterior = PreferredSettings.model.getPosterior(t, Maskit.sensitive_context_set[i], t, Maskit.sensitive_context_set[i], start, index1);
				} else {
					posterior = PreferredSettings.model.getPosterior(t, Maskit.sensitive_context_set[i], end, index2, Maskit.T, Maskit.locids.size()-1);
				}
				
				System.out.println("prior: "+prior+" posterior: "+posterior);
				if ((double)Math.round(Math.abs(posterior - prior)*100/100.0) > PreferredSettings.delta) {
					return false;
				}
			}	
		}
		return true;
	}
	
	public static boolean okayToRelease(int time, Sequence pos) {
		double prior,posterior;
		@SuppressWarnings("unused")
		int index_of_last_released = -1;
		@SuppressWarnings("unused")
		int index_of_first_released = Maskit.mapping.getLength();
		
		/*find the last released context in output sequence*/
		int index_of_last_released_pos = -1; //indices "start" state
		int time_of_last_released_pos = -1;  //indices "start"'s time
		for (int i = pos.getLength(); i >= 0; i--) {
			if (!((OutputContext)pos.getContext(i)).getSuppressed()) {
				index_of_last_released_pos = pos.getContext(i).getIndex();
				time_of_last_released_pos = i;
			}
		}
		
		/*possible state j at time t*/
		int[] c_j = PreferredSettings.model.Reached(time_of_last_released_pos, index_of_last_released_pos, time-time_of_last_released_pos);
		
		/*checking each possible sensitive context at time t*/
		for (int cj = 0; cj < c_j.length; cj++) {
			for (int t = 0; t < Maskit.mapping.getLength(); t++) {
				for (int c = 0; c < Maskit.sensitive_context_set.length; c++) {
					/*find the released context's index nearest t*/
					if (t <= time) {
						index_of_last_released = -1; //if there's not such one among the output sequence, set it time of start(-1)
						for (int i = t; i >=0; i--) {
							if (!((OutputContext)pos.getContext(i)).getSuppressed()) {
									index_of_last_released = i;
							}						 
						}	
					} else {
						index_of_last_released = time;
					}
					
					if (t <= pos.getLength()) {
						index_of_first_released = time;  //if there's not such one among the output sequence, set it time of start(T)
						for (int i = t; i <= pos.getLength(); i++) {
							if (!((OutputContext)pos.getContext(i)).getSuppressed()) {
								index_of_first_released = i;
							}
						}	
					} else {
						index_of_first_released = Maskit.mapping.getLength();
					}
					
					prior = PreferredSettings.model.getPriorProb(t,c);
					posterior = PreferredSettings.model.getPosterior(c, t, pos, cj);
					
					if (posterior - prior > PreferredSettings.delta) {
						return false;
					}
				}
			}			
		}
		return true;
	}
}
