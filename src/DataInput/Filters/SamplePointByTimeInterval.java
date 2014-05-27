package DataInput.Filters;

import Context.Context;
import Utils.Sequence;

public class SamplePointByTimeInterval extends SampleByTimeInterval{
	public SamplePointByTimeInterval(int t,int s,int e) {
		super(t,s,e);
	}

	@Override
	/*sample the point context of trace according to time interval*/
	public Sequence filter(Sequence sq){
		Sequence fs = new Sequence();
		int t = sq.getContext(0).getTimetmp().convertSeconds();
		fs.addContext((Context) sq.getContext(0).clone());
		t += interval;
		
		while (t < sq.getLastContext().getTimetmp().convertSeconds()) {
			fs.addContext((Context)(sq.getContext(sq.findT(t)).clone()));			
			t += interval;
		}
		
		fs.addContext((Context)sq.getLastContext().clone());		
		return fs;
	}
}
