package it.unimi.isa.beans;

import java.util.HashMap;
import java.util.List;

public class IndepMatrixBean {
	
	private List<Integer> lastComputedKList;
	private List<Integer> rowSumList;
	private List<HashMap <Integer,List<Integer>>> independentMatrix;
	
	public List<Integer> getLastComputedKList() {
		return lastComputedKList;
	}
	public void setLastComputedKList(List<Integer> lastComputedKList) {
		this.lastComputedKList = lastComputedKList;
	}
	public List<Integer> getRowSumList() {
		return rowSumList;
	}
	public void setRowSumList(List<Integer> rowSumList) {
		this.rowSumList = rowSumList;
	}
	public List<HashMap<Integer, List<Integer>>> getIndependentMatrix() {
		return independentMatrix;
	}
	public void setIndependentMatrix(
			List<HashMap<Integer, List<Integer>>> independentMatrix) {
		this.independentMatrix = independentMatrix;
	}
	
	

}
