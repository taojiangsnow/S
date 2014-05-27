package Utils;

public class MDouble {
	public int moleculor;	
	public int dominator;
	
	public MDouble() {
			
	}
		
	public MDouble(int i,int j) {
		moleculor = i;
		dominator = j;
	}
		
	public static MDouble sub(MDouble p, MDouble q) {
	   	MDouble result = new MDouble();
	   	
	   	result.dominator = getMinMultiple(p.dominator, q.dominator);
	   	result.moleculor = p.moleculor * result.dominator / p.dominator -
		          		   q.moleculor * result.dominator / q.dominator;
	   	System.out.println(result.moleculor);
	   	int divisor = getMaxDivisor(result.moleculor,result.dominator);
	   	result.moleculor /= divisor;
	   	result.dominator /= divisor;
	    	
	   	return result;
	}
	
	public static MDouble absSub(MDouble p, MDouble q) {
	   	MDouble result = new MDouble();
	   	
	   	result.dominator = getMinMultiple(p.dominator, q.dominator);
	   	result.moleculor = Math.abs(p.moleculor * result.dominator / p.dominator -
		          		   q.moleculor * result.dominator / q.dominator);
	   	System.out.println(result.moleculor);
	   	int divisor = getMaxDivisor(result.moleculor,result.dominator);
	   	result.moleculor /= divisor;
	   	result.dominator /= divisor;
	    	
	   	return result;		
	}
	
    public static int getMaxDivisor(int i, int j) {
    	int temp ;
    	while (i % j != 0) {
	    	temp = i % j;
	    	i = j;
	    	j = temp;
	    }
	    return j;
    }
	    
	public static int getMinMultiple(int i, int j) {
	   	return i * j / getMaxDivisor(i, j);
	}

	public boolean isNegative() {
		return (moleculor < 0);
	}
	
	public boolean isSmallerThen(MDouble i) {
		MDouble temp = sub(this, i);
		return (temp.moleculor < 0);
	}
	
	public double getDoubleValue() {
		return (moleculor+0.0)/dominator;
	}
	
	public void output() {
		System.out.println(moleculor + "/" + dominator);
	}	
}

