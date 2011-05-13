package com.table;

import java.util.Hashtable;
import java.util.Vector;

public class MethodEntity {
	private String methodName;
	private String returnType; // ∑µªÿ¿‡–Õ
	private Vector<Variability> parameters;
	private Hashtable<String, Variability>localTable;
	public MethodEntity() {
		parameters = new Vector<Variability>();
		localTable = new Hashtable<String, Variability>();
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Vector<Variability> getParameters() {
		return parameters;
	}

	public void setParameters(Vector<Variability> parameters) {
		this.parameters = parameters;
	}

	public Hashtable<String, Variability> getLocalTable() {
		return localTable;
	}
	public void setLocalTable(Hashtable<String, Variability> localTable) {
		this.localTable = localTable;
	}
	
}
