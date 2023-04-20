package it.unimi.isa.model;

import java.util.List;

public class Bacchetta {

	private String lettera;
	private List<Vertex> vertexList;
	private int [] nodiScelti;
	private int indexTM;
	
	public Bacchetta(int [] nodiscelti){
		this.nodiScelti = nodiscelti;
	}
	
	public Bacchetta(String lettera, int [] nodiscelti){
		this.nodiScelti = nodiscelti;
		this.lettera = lettera;
	}
	
	public Bacchetta() {
		// TODO Auto-generated constructor stub
	}

	public String getLettera() {
		return lettera;
	}
	public void setLettera(String lettera) {
		this.lettera = lettera;
	}
	
	public List<Vertex> getVertexList() {
		return vertexList;
	}
	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList = vertexList;
	}
	@Override
	public String toString() {
//		String temp = "Bacchetta "+this.lettera+"=[";
//		for(Vertex v : this.vertexList){
//			temp += "{" + v.getN()+":"+v.isColored() + "},";
//		}
//		temp += "]";
//		return temp;
		
		String temp = "Bacchetta["+this.indexTM+"] "+this.lettera+"=[";
		int i = 1;
		for(int v : this.nodiScelti){
			temp += "{" +v + "}";
			if(i<nodiScelti.length){
				temp += ",";
			}
			i++;
		}
		temp += "]";
		return temp;
	}
	public void setNodiScelti(int [] nodiScelti) {
		this.nodiScelti = nodiScelti;
	}
	public int [] getNodiScelti() {
		return nodiScelti;
	}

	public void setIndexTM(int indexTM) {
		this.indexTM = indexTM;
	}

	public int getIndexTM() {
		return indexTM;
	}
	
	
}
