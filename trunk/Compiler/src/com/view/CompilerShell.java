package com.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.transform.Source;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.CTabItem;

public class CompilerShell {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Menu menuBar = null;
	private Menu submenu = null;
	private CTabFolder cTabFolder_Editor = null;
	private StyledText styledText = null;
	private CTabFolder cTabFolder_output = null;
	private CTabFolder cTabFolder = null;
	private StyledText styledText_output = null;
	private StyledText styledText_lexicalOutput = null;
	private StyledText styledText_syntaxOuput = null;
	public CompilerShell() {
		// TODO Auto-generated constructor stub
		createSShell();
	}
	
	public void open(){
		Display display = Display.getDefault();
		sShell.open();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell();
		sShell.setText("Compiler");
		sShell.setLayout(gridLayout);
		createCTabFolder_Editor();
		createCTabFolder();
		createCTabFolder_output();
		sShell.setSize(new Point(626, 402));
		menuBar = new Menu(sShell, SWT.BAR);
		MenuItem submenuItem1 = new MenuItem(menuBar, SWT.CASCADE);
		submenuItem1.setText("File");
		submenu = new Menu(submenuItem1);
		MenuItem push = new MenuItem(submenu, SWT.PUSH);
		push.setText("Open");
		push.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// TODO open file dialog.
				System.out.println("widgetSelected()"); 
				FileDialog fd = new FileDialog(sShell,SWT.OPEN);
				String filePath = fd.open();
				System.out.println(filePath);
				File f = new File(filePath);
				System.out.println(f.getPath());
				CTabItem cti = new CTabItem(cTabFolder_Editor, SWT.CLOSE);
				
				
				//cti.setControl(stext);
				ScrolledComposite sc = new ScrolledComposite(cTabFolder_Editor, SWT.BOLD|SWT.V_SCROLL|SWT.H_SCROLL);
				StyledText stext = new StyledText(sc, SWT.MULTI);
				sc.setContent(stext);
				cti.setControl(sc);
				byte[] b = new byte[(int)f.length()];
				String soureCode = "";
				try {
					FileInputStream fi = new FileInputStream(f);
					fi.read(b);
					soureCode = new String(b);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				cti.setText(f.getName());
				stext.setText(soureCode);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		MenuItem push_Exit = new MenuItem(submenu, SWT.PUSH);
		push_Exit.setText("Exit");
		submenuItem1.setMenu(submenu);
		//push_file.setText("File");
		//MenuItem push1 = new MenuItem(menuBar, SWT.PUSH);
		//push1.setText("help");
		sShell.setMenuBar(menuBar);
	}

	/**
	 * This method initializes cTabFolder_Editor	
	 *
	 */
	private void createCTabFolder_Editor() {
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		cTabFolder_Editor = new CTabFolder(sShell, SWT.BORDER);
		cTabFolder_Editor.setSimple(false);
		cTabFolder_Editor.setLayoutData(gd);
		cTabFolder_Editor.setTabHeight(18);
		CTabItem cTabItem = new CTabItem(cTabFolder_Editor, SWT.CLOSE);
		styledText = new StyledText(cTabFolder_Editor, SWT.NONE);
		cTabItem.setControl(styledText);
		
		ScrolledComposite sc = new ScrolledComposite(cTabFolder_Editor, SWT.BOLD|SWT.V_SCROLL|SWT.H_SCROLL);
	}

	/**
	 * This method initializes cTabFolder_output	
	 *
	 */
	private void createCTabFolder_output() {
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		cTabFolder_output = new CTabFolder(sShell, SWT.BORDER);
		cTabFolder_output.setLayoutData(gd);
		cTabFolder_output.setSimple(true);
		CTabItem cTabItem1 = new CTabItem(cTabFolder_output, SWT.CLOSE);
		cTabItem1.setText("output");
		CTabItem cTabItem2 = new CTabItem(cTabFolder_output, SWT.CLOSE);
		cTabItem2.setText("Lexical Output");
		CTabItem cTabItem3 = new CTabItem(cTabFolder_output, SWT.CLOSE);
		cTabItem3.setText("Syntax Output");
		styledText_output = new StyledText(cTabFolder_output, SWT.NONE);
		styledText_lexicalOutput = new StyledText(cTabFolder_output, SWT.NONE);
		styledText_syntaxOuput = new StyledText(cTabFolder_output, SWT.NONE);
		cTabItem3.setControl(styledText_syntaxOuput);
		cTabItem2.setControl(styledText_lexicalOutput);
		cTabItem1.setControl(styledText_output);
	}

	/**
	 * This method initializes cTabFolder	
	 *
	 */
	private void createCTabFolder() {
		cTabFolder = new CTabFolder(sShell, SWT.NONE);
	}

}
