/*
 * @author xuejiang
 * 
 * 
 * 
 */

package Utils;

public class Date {
	private int year;
	private int month;
	private int day;
	
	private int hour;
	private int minute;
	private int sec;

	public Date() {	
	}
	
	public Date(Date t) {
		this.year = t.getYear();
		this.month = t.getMonth();
		this.day = t.getDay();
		this.hour = t.getHour();
		this.minute = t.getMinute();
		this.sec = t.getSec();
	}
	
	public Date(int y, int m, int d) {
		this.year = y;
		this.month = m;
		this.day = d;		
	}
	
	public Date(int y, int m, int d, int h, int mi, int se) {
		this.year = y;
		this.month = m;
		this.day = d;
		this.hour = h;
		this.minute = mi;
		this.sec = se;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(year);
		s.append("-");
		s.append(month);
		s.append("-");
		s.append(day);
		s.append("-");
		s.append(hour);
		s.append("-");
		s.append(minute);
		s.append("-");
		s.append(sec);
		s.append("-");
		return s.toString();
	}
	
	public boolean equals(Date b) {
		if (this.year != b.getYear()) {
			return false;
		}
		else if (this.month != b.getMonth()) {
			return false;
		}
		else if (this.day != b.getDay()) {
			return false;
		}
		else {
			return true;
		}		
	}

	public int convertSeconds() {
		return (hour*3600 + minute*60 + sec);
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
		
	}
	
	public int getSec() {
		return sec;
	}

	public void setDate(int y, int m, int d, int h, int mi, int se) {
		this.year = y;
		this.month = m;
		this.day = d;
		this.hour = h;
		this.minute = mi;
		this.sec = se;
	}
	public void output() {
		System.out.print(this.year + "-" + this.month + "-" + this.day 
						+ " " + this.hour + ":" + this.minute + ":" + this.sec + " ");
	}
}
