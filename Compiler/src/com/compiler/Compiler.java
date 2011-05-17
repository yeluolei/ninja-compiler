package com.compiler;

import java.io.ByteArrayInputStream;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

public class Compiler {
	ByteArrayInputStream stream;
	SimpleNode root;

	public Compiler(String source) {
		stream = new ByteArrayInputStream(source.getBytes());
		root = null;
		new XYZ2(stream);
	}

	public String LexicalAnalyse() {
		String result = XYZ2.LexicalAnalyse();
		return result;
	}

	public boolean getLexicalState() {
		return XYZ2.getLexicalState();
	}

	public String getLexicalErrorMessage() {
		return XYZ2.getLexicalErrorMessage();
	}

	public int getsimpleStatCnt() {
		return XYZ2.getsimpleStatCnt();
	}

	public void ReInit(String source) {
		stream = new ByteArrayInputStream(source.getBytes());
		root = null;
		XYZ2.MyReInit(stream);
	}

	public int getWeigth() throws ParseException {
		if (root == null)
			root = XYZ2.Start();
		return weigth(root);
	}

	public int weigth(SimpleNode node) {
		int childrenWeight = 0;
		if (node.children != null) {
			for (int i = 0; i < node.children.length; ++i) {
				SimpleNode n = (SimpleNode) node.children[i];
				if (n != null) {
					childrenWeight += weigth(n);
				}
			}
		} else {
			if (node.id == 17)
				return 1;
			else
				return 0;
		}
		switch (node.id) {
		case 5:
		case 20:
			return 1 + childrenWeight;
		case 21:
			return 1 + childrenWeight;
		case 22:
			return childrenWeight * 2;
		case 23:
			return childrenWeight * 4;
		case 24:
			return 1 + childrenWeight;
		default:
			return childrenWeight;
		}
	}

	public String getSyntaxTreeString() throws ParseException {
		if (root == null)
			root = XYZ2.Start();
		String result = dump(root);
		return result;
	}

	public DefaultMutableTreeNode getSyntaxTree() throws ParseException {
		if (root == null)
			root = XYZ2.Start();
		return GenerateSyntaxTree(root);
	}

	public SimpleNode getRoot()
	{
		return root;
	}
	
	public String dump(SimpleNode node) {
		String name = node.toString();
		String temp = "";
		System.out.println(node.toString());
		if (node.children != null) {
			for (int i = 0; i < node.children.length; ++i) {
				SimpleNode n = (SimpleNode) node.children[i];
				if (n != null) {
					temp = temp + dump(n);
				}
			}
		}
		return name + '\n' + temp;
	}

	public DefaultMutableTreeNode GenerateSyntaxTree(SimpleNode node) {
		DefaultMutableTreeNode jTreenode = new DefaultMutableTreeNode(
				node.toString());
		if (node.children != null) {
			for (int i = 0; i < node.children.length; ++i) {
				SimpleNode n = (SimpleNode) node.children[i];
				if (n != null) {

					DefaultMutableTreeNode temp = GenerateSyntaxTree(n);
//					if (node.children.length > 1) {
						jTreenode.add(temp);
//					} else
//						jTreenode = temp;

				}
			}
		}
		return jTreenode;
	}

	public Map<String, Integer> getTokenRecord() {
		return XYZ2.getTokenRecord();
	}
}
