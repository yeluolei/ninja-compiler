package com.table;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

public class ProgramTable{
	private Dictionary<String,ClassEntity> classTable;
	/**
	 *  The build in types in the language
	 */
	private Vector<String> types;
	private String mainClassName;
	public ProgramTable() {
		this.classTable = new Hashtable<String,ClassEntity>();
		this.types = new Vector<String>();
		this.types.add("Int");
		this.types.add("LongArray");
		this.types.add("Long");
		this.types.add("Boolean");
		this.types.add("IntArray");
	}

	public Dictionary<String, ClassEntity> getClassTable() {
		return classTable;
	}

	public String getMainClassName() {
		return mainClassName;
	}

	public void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}
	
	public boolean checkTypeExist(String type) {
		for (int i = 0 ; i < types.size() ; i++){
			if (types.get(i).equals(type))
				return true;
		}
		if (classTable.get(type)!=null){
			return true;
		}
		return false;
	}
	
}
