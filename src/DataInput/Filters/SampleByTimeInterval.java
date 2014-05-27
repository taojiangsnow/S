/*
 * @author xuejiang
 */

package DataInput.Filters;

import DataInput.Filters.Filter;

public abstract class SampleByTimeInterval extends Filter{
	/*time interval of sampling*/
	protected int interval;
	protected int start;
	protected int end;
	
	public SampleByTimeInterval(int t, int s, int e) {
		interval = t;
		start = s;
		end = e;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public int getStartHour() {
		return start;
	}
	
	public int getEndHour() {
		return end;
	}

	public int getStartT() {
		return start-1;
	}
	
	public int getEndT() {
		return end-1;
	}
}