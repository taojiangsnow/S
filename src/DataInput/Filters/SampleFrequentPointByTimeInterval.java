package DataInput.Filters;

import java.util.ArrayList;

import Context.Context;
import Main.Maskit;
import Utils.Cycle;
import Utils.Sequence;

public class SampleFrequentPointByTimeInterval extends SampleByTimeInterval{
	public SampleFrequentPointByTimeInterval(int t,int s,int e) {
		super(t,s,e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Sequence filter(Sequence sq) {
		int T = Cycle.SECOFDAY / interval;
		int time, belongT;
		ArrayList<ArrayList<Context>> index_interval = new ArrayList<ArrayList<Context>>();
		for (int i= 0; i < T; i++) {
			index_interval.add(new ArrayList<Context>());
		}
		for (int i = 0; i < sq.getLength(); i++) {
			time = sq.getContext(i).getTimetmp().convertSeconds();
			belongT = findBelongT(time,T);
			sq.getContext(i).setT(belongT);
			//System.out.println("id: "+ sq.getContext(i).getId()+" belongT: "+sq.getContext(i).getT());
			index_interval.get(belongT).add(sq.getContext(i));
		}
		/************
		for (int i = 0; i < T; i++) {
			System.out.println("T: "+i+" size: "+index_interval.get(i).size());
			if (index_interval.get(i).size() > 1) {
			    for (int j = 0; j < index_interval.get(i).size(); j++) {
			    	System.out.print(index_interval.get(i).get(j)+" ");
			    }
			    System.out.println();
			} else {
				if (index_interval.get(i).size() > 0) {
					System.out.println(index_interval.get(i).get(0));
				}
			}
		}
		*************/
		/*construct new output sequence*/
		Sequence fs = new Sequence();
		ArrayList<Context> temp_index_interval;
		for (int i = 0; i < T; i++) {
			if (index_interval.get(i).size()!= 0) {
				temp_index_interval = index_interval.get(i);
				fs.addContext((Context)temp_index_interval.get(findFrequentCId(temp_index_interval)).clone());
			}
		}
		
		for(int i = 0; i < index_interval.size(); i++) {
			index_interval.set(i, null);
		}
		index_interval = null;		
		return fs;
	}

	private int findBelongT(int time, int length) {
		int start = 0;
		int end = interval;
		for (int i = 0; i < length; i++) {
			if ((time >= start) && (time <end)) {
				return i;
			} else {
				start += interval;
				end += interval;
			}
		}
		return length-1;
	}
	
	/*find the frequent item of each T*/
	private int findFrequentCId(ArrayList<Context> l) {
		int frequent_context_id = 0;
		int frequent = 0;
		for (int i = 0; i < l.size(); i++) {
			if (frequent == 0) {
				frequent_context_id = i;
				frequent = 1;
			}else if (l.get(frequent_context_id).getId() != l.get(i).getId()) {
				frequent--;
				if (frequent == 0) {
					frequent_context_id = 0;
				}
			} else {
				frequent++;
			}
		}
		return frequent_context_id;
	}
}
