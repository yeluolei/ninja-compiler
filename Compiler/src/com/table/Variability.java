package com.table;

public class Variability {

	private String varName; // ������
	private String typeName; // ��������
	private Object value; // ����ֵ
	private int declearLine; // ����ʱ������


	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getDeclearLine() {
		return declearLine;
	}

	public void setDeclearLine(int declearLine) {
		this.declearLine = declearLine;
	}
	
	

}
