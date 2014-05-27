package DataInput.Filters;

import Context.Context;
import Utils.Sequence;

public class SampleEndpointByTimeInterval extends SampleByTimeInterval{
	public SampleEndpointByTimeInterval(int t, int s, int e) {
		super(t,s,e);
	}

	@Override
	/*sample the start and end context of trace according to time interval*/
	public Sequence filter(Sequence sq){
		Sequence fs = new Sequence();
		int[] index;
		int start = sq.getContext(0).getTimetmp().convertSeconds();	
		
		int end = start + interval;
		int lastt = sq.getLastContext().getTimetmp().convertSeconds();
		
		index = sq.findEndpointBetweenT(start,end);
		//System.out.println(index[0] + " " + index[1]);
		/*if index[0]==index[1] so between start and end there's only one context*/
		Context ctemp;
		int T_of_index0;
		if (index[0] != index[1]) {
			ctemp = (Context)sq.getContext(index[0]).clone();
			T_of_index0 = convertIndexOfT(ctemp.getTimetmp().convertSeconds());
			ctemp.setT(T_of_index0); //set index of T
			fs.addContext(ctemp);
			ctemp = (Context)sq.getContext(index[1]).clone();
			ctemp.setT(T_of_index0 + 1); //set index of T
			fs.addContext(ctemp);
		} else {
			ctemp = (Context)sq.getContext(index[0]).clone();			
			T_of_index0 = convertIndexOfT(ctemp.getTimetmp().convertSeconds());
			ctemp.setT(T_of_index0); //set index of T
			fs.addContext(ctemp);
			fs.addContext(ctemp);  //each interval we sample two points
			fs.getLastContext().setT(T_of_index0 + 1);
		}		
		start = end + 1;
		end = start + interval;
		while (end < lastt) {
			index = sq.findBetweenT(start,end,index[1]);
			//System.out.println(index[0] + " " + index[1]);
			if (index[0] != index[1]) {
				ctemp = (Context)sq.getContext(index[0]).clone();
				T_of_index0 = convertIndexOfT(ctemp.getTimetmp().convertSeconds());
				ctemp.setT(T_of_index0); //set index of T
				fs.addContext(ctemp);
				ctemp = (Context)sq.getContext(index[1]).clone();
				ctemp.setT(T_of_index0 + 1); //set index of T
				fs.addContext(ctemp);
			} else {
				ctemp = (Context)sq.getContext(index[0]).clone();			
				T_of_index0 = convertIndexOfT(ctemp.getTimetmp().convertSeconds());
				ctemp.setT(T_of_index0);
				fs.addContext(ctemp);
				fs.addContext(ctemp);
				fs.getLastContext().setT(T_of_index0 + 1);				
			}
			
			start = end + 1;
			end = start + interval;
		}
		
		/*end exceeds time of last context*/
		if (start < lastt) {
			int ls = sq.findFirstAfterT(start, index[1]);
			ctemp = (Context)sq.getContext(ls).clone();
			T_of_index0 = convertIndexOfT(ctemp.getTimetmp().convertSeconds());
			ctemp.setT(T_of_index0);
			fs.addContext(ctemp);
			if (ls != (sq.getLength() - 1)) {
				ctemp = (Context)sq.getLastContext().clone();
				ctemp.setT(T_of_index0 + 1);
				fs.addContext(ctemp);				
			} else {
				fs.addContext(ctemp);
				fs.getLastContext().setT(T_of_index0 + 1);
			}
		}				
		return fs;
	}
	
	public int convertIndexOfT(int sec) {
		int index_of_T = sec/interval;
		if (index_of_T == Math.floor(index_of_T)) {
			return 2*index_of_T;
		} else {
			return 2*index_of_T+1;
		}
	}
}
