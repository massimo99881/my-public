package it.unimi.isa.service.impl;

import it.unimi.isa.service.MathematicaService;

import org.springframework.stereotype.Service;

@Service
public class MathematicaServiceImpl implements MathematicaService {

	final public static String SCRIPT_FIBONACCI = "Clear[FIB] \n FIB[0]:=1; \n FIB[1]:=1; \n FIB[n_Integer]:= FIB[n-1] + FIB[n-2]; \n ";
	final public static String SCRIPT_EXPONENTIAL = "Clear[EXP] \n EXP[0] := 1; \n EXP[n_Integer] := 2^n; \n ";
	
	//private static final Logger logger = Logger.getLogger(MathematicaServiceImpl.class);
	
	/**
	 * formatta la Transfer Matrix 
	 * @param xTM
	 * @return
	 */
	public String formatTransferMatrix(String[][] xTM) 
	{
		String tmp = "xTM = ";
		tmp += "{ ";
		
		for (int row = 0; row < xTM.length; row++) 
		{
			tmp +=  "{ ";
			for (int col = 0; col < xTM[row].length; col++) 
			{
				
				if(col==(xTM.length-1))
					tmp += xTM[row][col];
				else
					tmp += xTM[row][col] + ", ";
			}
			
			if(row==(xTM.length-1))
				tmp += "} ";
			else
				tmp += "}, ";
			
		}// end for
		
		tmp += "} ";
		return tmp;
	}


	public String formatIdentityTM(int length) 
	{
		String tmp = "Iden = ";
		tmp += "{ ";
		
		for (int row = 0; row < length; row++) 
		{
			tmp +=  "{ ";
			for (int col = 0; col < length; col++) 
			{
				String val = "0";
				if(row==col)
					val = "1";
				
				if(col==(length-1))
					tmp += val;
				else
					tmp += val + ", ";
			}
			
			if(row==(length-1))
				tmp += "} ";
			else
				tmp += "}, ";
			
		}// end for
		
		tmp += "} ";
		return tmp;
	}
	
}
