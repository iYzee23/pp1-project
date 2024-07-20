package rs.etf.pp1.mj.runtime;

public class Runn extends Run {

	static int peek() throws VMException {
		if (esp == 0) throw new VMException("expression stack underflow");
		return stack[esp - 1];
	}
	
}
