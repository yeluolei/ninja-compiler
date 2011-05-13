package com.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessage {
	private ArrayList<String> errorsList = new ArrayList<String>();
	public boolean hasError;
	
	public List<String> getErrorsList(){
		return this.errorsList;
	}
	
	public int getNumErrors(){
		return errorsList.isEmpty() ? 0 : errorsList.size();
	}
	
	public void addError(int line , String errormessage)
	{
		hasError = true;
		this.errorsList.add("Line " + line +" : " + errormessage);
	}
}
