package com.auk.utilities;

import java.util.HashMap;
import java.util.Hashtable;

import org.testng.annotations.DataProvider;

import com.auk.base.Base;




public class DataProviders extends Base{
	
	@DataProvider
	public static Object[][] getTestDataUsingMap(Xls_Reader xls,String testCaseName) {

		Object[][] data = null;
		//Xls_Reader xls = new Xls_Reader(excelPath);

		HashMap<String, String> table = null;

		// Find number of columns in xls
		int numofcols = 0;
		while (!xls.getCellData(testCaseName, numofcols, 1).equals("")) {

			numofcols++;

		}

		System.out.println("Number of Columns are : " + numofcols);
		// Find number of rows in xls
		int numofrows = 0;
		while (!xls.getCellData(testCaseName, 0, numofrows + 2).equals("")) {
			// numofrow+2 is given as we need to start from 2nd row.First row is
			// column Heading
			// System.out.println(xls.getCellData("TestData", 0, numofrows));
			numofrows++;

		}

		System.out.println("Number of rows are : " + numofrows);
		// initialising data object with number of rows and one column.The data
		// of one row will be put in one column of Hashtable

		data = new Object[numofrows][1]; // The column will be 1 only as we will
											// put data in hash table.

		// Putting data in data Object
		for (int row = 2; row < numofrows + 2; row++) {
			table = new HashMap<String, String>();
			for (int col = 0; col < numofcols; col++) {
				table.put(xls.getCellData(testCaseName, col, 1), xls.getCellData(testCaseName, col, row));
			}
			data[row - 2][0] = table;
		}

		return data;
	}
	
	@DataProvider
	public Object[][] getTestdataUsingTable(String excelPath,String testCaseName){
	    
	    Object[][] data=null;
	    Xls_Reader xls = new Xls_Reader(excelPath);
	    
	    Hashtable<String,String> table =null;
	    
	    //Find number of columns in xls
	    int numofcols=0;
	    while(!xls.getCellData(testCaseName, numofcols, 1).equals("")){
	        
	        numofcols++;
	        
	    }
	    
	    System.out.println("Number of Columns are : " + numofcols);
	    //Find number of rows in xls
	    int numofrows=0;
	    while(!xls.getCellData(testCaseName, 0, numofrows+2).equals("")){      
	    	//numofrow+2 is given as we need to start from 2nd row.First row is column Heading
	       //System.out.println(xls.getCellData("TestData", 0, numofrows));  
	        numofrows++;
	        
	    }
	    
	    System.out.println("Number of rows are : " + numofrows) ;
	    // initialising data object with number of rows and one column.The data of one row will be put in one column of Hashtable
	    
	    data= new Object[numofrows][1];    //The column will be 1 only as we will put data in hash table.
	        
	    // Putting data in data Object        
	    for(int row=2;row<numofrows+2;row++ ){
	        table=new Hashtable<String,String>();
	        for(int col=0;col<numofcols;col++){
	            
	            table.put(xls.getCellData(testCaseName, col, 1),xls.getCellData(testCaseName, col, row) );
	            
	        }
	        
	        data[row-2][0]= table;
	        
	    }
	    
	    
	    return data;
	}
	
}
