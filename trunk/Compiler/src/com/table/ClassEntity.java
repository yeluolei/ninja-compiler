package com.table;

import java.util.Hashtable;

public class ClassEntity{
	private String className;
	private String parentClassName;
	private String classReturnType;
	private Hashtable<String, Variability>fieldTable;
	private Hashtable<String, MethodEntity>methodTable;
	
	
	public ClassEntity()
	{
		fieldTable= new Hashtable<String, Variability>();
		methodTable = new Hashtable<String, MethodEntity>();
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

	public Hashtable<String, MethodEntity> getMethodTable() {
		return methodTable;
	}

	public void setMethodTable(Hashtable<String, MethodEntity> methodTable) {
		this.methodTable = methodTable;
	}	
}
