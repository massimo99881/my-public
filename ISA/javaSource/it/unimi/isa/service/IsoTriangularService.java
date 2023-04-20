package it.unimi.isa.service;

import it.unimi.isa.model.Sigma;

public interface IsoTriangularService {

	public void isodiagonalizza();
	public void obtainFgoVx(Sigma sigma, String[][] m);
	public void obtainFgoRSn(Sigma sigma, String[][] m);
	
}
