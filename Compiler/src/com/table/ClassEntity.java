package com.table;

import java.util.Hashtable;
import java.util.Vector;

public class ClassEntity{
	private String className;
	private String parentClassName;
	private String classReturnType;
	private Hashtable<String, Variability>fieldTable;
	private Vector<MethodEntity>methodTable;
	
	
	public ClassEntity()
	{
		fieldTable= new Hashtable<String, Variability>();
		methodTable = new Vector<MethodEntity>();
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	

	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public String getClassReturnType() {
		return classReturnType;
	}

	public void setClassReturnType(String classReturnType) {
		this.classReturnType = classReturnType;
	}

	public Hashtable<String, Variability> getFieldTable() {
		return fieldTable;
	}

	public void setFieldTable(Hashtable<String, Variability> fieldTable) {
		this.fieldTable = fieldTable;
	}

	public Vector<MethodEntity> getMethodTable() {
		return methodTable;
	}
}
