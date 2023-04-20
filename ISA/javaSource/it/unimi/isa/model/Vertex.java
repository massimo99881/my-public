package it.unimi.isa.model;

public class Vertex {

	private int n;
	private int r;
	private int c;
	private boolean colored;
	
	public Vertex(){
		
	}
	
	public Vertex(int n, int r, int c) {
		this.n=n;
		this.r=r;
		this.c=c;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public boolean isColored() {
		return colored;
	}
	public void setColored(boolean colored) {
		this.colored = colored;
	}
	
	
}
