package com.view;

import javax.swing.JFrame;

import org.eclipse.swt.widgets.Display;

public class Main {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		CompilerFrame cFrame = new CompilerFrame();
		cFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cFrame.show();
		
		//CompilerShell shell = new CompilerShell();
		//shell.createSShell();
		//shell.open();
	}

}
