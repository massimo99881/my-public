package it.unimi.isa.model;

public class SchemaObj {

	private int count = 0;
	private int schema [][] = null;

	public void setSchema(int schemax[][]) {
		schema = new int [schemax.length][schemax[0].length];
		for(int i=0;i<schemax.length;i++)
			for(int j=0;j<schemax[i].length;j++)
				schema[i][j] = schemax[i][j];
		
	}

	public int[][] getSchema() {
		return schema;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
	
	
}
