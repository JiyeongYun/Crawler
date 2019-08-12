package isel;

public class Period {
	private int start;
	private int end;
	
	public Period(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public void movePeriod(int period) {
		this.end = this.start;
		this.start = this.start - period;
	}
}
