/*
 * @author xuejiang
 */
package Context;

import Utils.Date;

public class Context implements Cloneable{
	protected String context;
	protected Date timestamp;
	protected int id;
	protected int index; /*the index in dif_ids*/
	protected int T;
	
	/*for complex structure of contexts*/
	private int parent;
	
	public Context(int i) {
		index = i;
	}
	
	public Context(int id, Date t) {
		this.id = id;
		this.timestamp = new Date(t);
	}
	
	public Context(String s, Date t, int id, int index) {
		this.context = s;
		this.timestamp = new Date(t.getYear(), t.getMonth(), t.getDay(), t.getHour(), t.getMinute(), t.getSec());
		this.id = id;
		this.index = index;
	}
	
	public Context(Context s) {
		this.context = s.getContext();
		this.timestamp = new Date(s.getTimetmp().getYear(), s.getTimetmp().getMonth(), s.getTimetmp().getDay(), s.getTimetmp().getHour(), s.getTimetmp().getMinute(), s.getTimetmp().getSec());
		this.id = s.getId();
		this.index = s.getIndex();
	}
	
	public Context(String s, int y, int m, int d, int h, int mi, int se, int id, int index) {
		this.context = s;
		this.timestamp = new Date(y, m, d, h, mi, se);
		this.id = id;
		this.index = index;
	}
	
	public Context(String s, int y, int m, int d, int h, int mi, int se, int id) {
		this.context = s;
		this.timestamp = new Date(y, m, d, h, mi, se);
		this.id = id;
	}
	
	public Object clone() {
		Context t = null;
		try {
			t = (Context)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Date getTimetmp() {
		return this.timestamp;
	}
	
	public String getContext() {
		return this.context;
	}
	
	public int getParent() {
		return this.parent;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getT() {
		return T;
	}
	
	public void setIndex(int i) {
		this.index = i;
	}
	
	public void setT(int i) {
		this.T = i;
	}
	
	public void output() {
		this.timestamp.output();
		StringBuilder builder = new StringBuilder();
		//builder.append(this.context);
		//builder.append(" ");
		builder.append(this.T);
		builder.append(" ");
		builder.append(this.id);
		builder.append(" ");
		builder.append(this.index);
		builder.append(" ");
		System.out.print(builder);
	}

	public void clear() {
		this.context = null;
		this.timestamp = null;
	}
}
 