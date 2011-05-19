package com.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

import com.compiler.Compiler;
import com.compiler.ParseException;
import com.visitor.MyVisitor;

public class CompilerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JScrollPane jScrollPane3 = null;  //  @jve:decl-index=0:
	private JPanel jContentPane = null;
	private JTextArea jOutPutArea = null;
	private JButton Bt_begin = null;
	private JTextField tf_fileLocation = null;
	private JTabbedPane jTabbedPane = null;
	private JTextArea ta_lexicalResult = null;
	private JTextArea ta_typeResult = null;
	private JTextArea ta_syntaxResult = null;
	private JButton bt_Start = null;
	private File sourceFile;

	private Compiler compiler; // @jve:decl-index=0:
	private JTabbedPane jSideTabbedPane1 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane2= null;
	private JTable jVarTable = null;
	private JTable jTypeTable = null;
	private JTree jSyntaxTree = null;
	private DefaultMutableTreeNode root;
	String[] name = { "变量", "频率" };
	SimpleModel simplemodel = new SimpleModel();
	// private DefaultTableModel model = new DefaultTableModel(new
	// Object[][]{{"fsa","fsdf"}}, name);
	private JScrollPane jCodeWindowScrollPane = null;
	private JTextPane ep_CodeWindow = null;

	/**
	 * This is the default constructor
	 */
	public CompilerFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1200, 765);
		this.setContentPane(getJContentPane());
		this.setTitle("Compiler");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBt_begin(), null);
			jContentPane.add(getTf_fileLocation(), null);
			jContentPane.add(getJTabbedPane(), null);
			jContentPane.add(getBt_Start(), null);
			jContentPane.add(getJSideTabbedPane1(), null);
			jContentPane.add(getJCodeWindowScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes OutPutAre
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getOutPutAre() {
		if (jOutPutArea == null) {
			jOutPutArea = new JTextArea();
			jOutPutArea.setLineWrap(true);
		}
		return jOutPutArea;
	}

	/**
	 * This method initializes Bt_begin
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBt_begin() {
		if (Bt_begin == null) {
			Bt_begin = new JButton();
			Bt_begin.setText("选择文件");
			Bt_begin.setBounds(new Rectangle(403, 7, 91, 34));
			Bt_begin.addActionListener(new ButtonHandler());
		}
		return Bt_begin;
	}

	private class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFileChooser jc = new JFileChooser();
			jc.setDialogTitle("choose source file");
			int returnVar = jc.showOpenDialog(jContentPane);
			if (returnVar == JFileChooser.APPROVE_OPTION) {
				sourceFile = jc.getSelectedFile();
				// System.out.println(soureFile.getPath());
				tf_fileLocation.setText(sourceFile.getPath());
				try {
					FileInputStream fi = new FileInputStream(sourceFile);
					byte[] bytesource = new byte[(int) sourceFile.length()];
					fi.read(bytesource);
					String codeText = new String(bytesource);
					ep_CodeWindow.setText(codeText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				bt_Start.setEnabled(true);
			}
		}

	}

	private class StartButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String temp = ep_CodeWindow.getText();
			if(compiler == null)
				compiler = new Compiler(temp);
			else
				compiler.ReInit(temp);
			
			jOutPutArea.append("开始词法分析....\n");
			String output = compiler.LexicalAnalyse();
			Map<String, Integer> tokenRecord = compiler.getTokenRecord();
			SimpleModel model = (SimpleModel) jVarTable.getModel();
			model.RemoveAll();
			for (String o : tokenRecord.keySet()) {
				model.addRow(new Object[] { o,
						tokenRecord.get(o).toString() });
				//System.out.println(o + " " + tokenRecord.get(o).toString());
			}
			if (compiler.getLexicalState() == false) {
				jOutPutArea.append("词法错误!\n");
				String lexicalError = compiler.getLexicalErrorMessage();
				ta_lexicalResult.append(lexicalError);
				return;
			}
			compiler.ReInit(temp);
			ta_lexicalResult.append("Token Stream:\n");
			ta_lexicalResult.append(output);
			jOutPutArea.append("词法分析完成!\n");
			jOutPutArea.append("语法分析开始....\n");


			// begin syntax analysis
			try {
				String result = compiler.getSyntaxTreeString();
				Integer weigth = compiler.getWeigth();
				Integer num = compiler.getsimpleStatCnt();
				root.add(compiler.getSyntaxTree());
				ta_syntaxResult.append(result + '\n');
				ta_syntaxResult.append("权重：\n");
				ta_syntaxResult.append(weigth.toString() + "\n");
				ta_syntaxResult.append("number of simple statement：\n");
				ta_syntaxResult.append(num.toString() + "\n");
				jOutPutArea.append("语法分析完成\n");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ta_syntaxResult.append(e1.getMessage());
				jOutPutArea.append("语法分析失败\n");
				return;
			}
			jOutPutArea.append("语义分析开始\n");
			ta_typeResult.append("Type Checking Start!\n");
			try{
				MyVisitor visitor = new MyVisitor();
				compiler.getRoot().jjtAccept(visitor, 1);
				compiler.getRoot().jjtAccept(visitor, 2);
				compiler.getRoot().jjtAccept(visitor, 3);
				for (int i = 0 ; i < visitor.error.getNumErrors();i++ )
				{
					ta_typeResult.append(visitor.error.getErrorsList().get(i)+"\n");
				}
				ta_typeResult.append("Type Checking Finish!\n");
				jOutPutArea.append("语义分析完成\n");
			}
			catch (Exception e2) {
				ta_typeResult.append(e2.getMessage());
				jOutPutArea.append("语义分析失败\n");
			}
		}

	}

	/**
	 * This method initializes tf_fileLocation
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTf_fileLocation() {
		if (tf_fileLocation == null) {
			tf_fileLocation = new JTextField();
			tf_fileLocation.setBounds(new Rectangle(103, 13, 293, 26));
			tf_fileLocation.setEditable(false);
		}
		return tf_fileLocation;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(new Rectangle(3, 375, 499, 346));
			jTabbedPane.addTab("output", null, getOutPutAre(), null);
			jTabbedPane.addTab("lexical report", null, getTa_lexicalResult(),
					null);
			jTabbedPane.addTab("syntax report", null, getTa_syntaxResult(),
					null);
			jTabbedPane.addTab("Type report", null, getTa_typeResult(),
					null);
		}
		return jTabbedPane;
	}

	private JScrollPane getTa_typeResult() {
		JScrollPane jp = null;
		if (ta_typeResult == null) {
			ta_typeResult = new JTextArea();
			ta_typeResult.setWrapStyleWord(true);
			ta_typeResult.setLineWrap(true);
			jp = new JScrollPane(ta_typeResult);
			// getContentPane().add(jp);
		}
		// return ta_lexicalResult;
		return jp;
	}
	
	/**
	 * This method initializes ta_lexicalResult
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JScrollPane getTa_lexicalResult() {
		JScrollPane jp = null;
		if (ta_lexicalResult == null) {
			ta_lexicalResult = new JTextArea();
			ta_lexicalResult.setWrapStyleWord(true);
			ta_lexicalResult.setLineWrap(true);
			jp = new JScrollPane(ta_lexicalResult);
			// getContentPane().add(jp);
		}
		// return ta_lexicalResult;
		return jp;
	}

	/**
	 * This method initializes ta_syntaxResult
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JScrollPane getTa_syntaxResult() {
		JScrollPane jp = null;
		if (ta_syntaxResult == null) {
			ta_syntaxResult = new JTextArea();
			 jp = new JScrollPane(ta_syntaxResult);
		}
		return jp;
	}

	/**
	 * This method initializes bt_Start
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBt_Start() {
		if (bt_Start == null) {
			bt_Start = new JButton();
			bt_Start.setBounds(new Rectangle(5, 6, 78, 34));
			bt_Start.setText("开始");
			bt_Start.setEnabled(false);
			bt_Start.addActionListener(new StartButtonHandler());
		}
		return bt_Start;
	}

	/**
	 * This method initializes jSideTabbedPane1
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJSideTabbedPane1() {
		if (jSideTabbedPane1 == null) {
			jSideTabbedPane1 = new JTabbedPane();
			jSideTabbedPane1.setBounds(new Rectangle(503, 8, 673, 723));
			jSideTabbedPane1.addTab("Var", null, getJScrollPane(), null);
			jSideTabbedPane1
					.addTab("Syntax Tree", null, getJScrollPane2(), null);
		}
		return jSideTabbedPane1;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJVarTable());
		}
		return jScrollPane;
	}
	
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getJSyntaxTree());
		}
		return jScrollPane2;
	}
	
	private JScrollPane getJScrollPane3(){
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(getJTypeTable());
		}
		return jScrollPane3;
	}
	
	private JTable getJTypeTable(){
		if (jTypeTable == null) {
			jTypeTable = new JTable(simplemodel);
		}
		return jVarTable;
	}
	/**
	 * This method initializes jVarTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJVarTable() {
		if (jVarTable == null) {
			jVarTable = new JTable(simplemodel);
		}
		return jVarTable;
	}

	/**
	 * This method initializes jSyntaxTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getJSyntaxTree() {
		if (jSyntaxTree == null) {
			root = new DefaultMutableTreeNode("Tree");
			jSyntaxTree = new JTree(root);
		}
		return jSyntaxTree;
	}

	/**
	 * This method initializes jCodeWindowScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJCodeWindowScrollPane() {
		if (jCodeWindowScrollPane == null) {
			jCodeWindowScrollPane = new JScrollPane();
			jCodeWindowScrollPane.setBounds(new Rectangle(6, 51, 495, 322));
			jCodeWindowScrollPane.setViewportView(getEp_CodeWindow());
		}
		return jCodeWindowScrollPane;
	}

	/**
	 * This method initializes ep_CodeWindow
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private JTextPane getEp_CodeWindow() {
		if (ep_CodeWindow == null) {
			ep_CodeWindow = new JTextPane();
			// ep_CodeWindow.addMouseListener(new MouseAdapter() {
			// public void mouseClicked(MouseEvent e){
			// Rectangle r;
			// try {
			// r = ep_CodeWindow.modelToView(ep_CodeWindow.getCaretPosition());
			// System.out.println(r.y / r.height + 1);
			// setTextColor(ep_CodeWindow, r.y / r.height + 1);
			// } catch (BadLocationException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			//
			// }
			// });
		}
		return ep_CodeWindow;
	}

	public void setTextColor(JTextPane textPane, int rowId) {

		StyledDocument doc = textPane.getStyledDocument();
		String selected = textPane.getText();
		// 注意，在JTextPane中，按回车输入的是\r\n
		selected = selected.replace("\r\n", "\n");
		// 根据换行符把每一行文字分割出来
		String[] split = selected.split("\n");
		// 先清空textPane
		textPane.setText("");
		// 循环找到选中的行，设置选中行背景颜色为pink，其他行颜色为white
		for (int i = 0; i < split.length; i++) {
			if ((i + 1) == rowId) {
				SimpleAttributeSet attrSet = new SimpleAttributeSet();
				StyleConstants.setBackground(attrSet, Color.pink);
				try {
					doc.insertString(doc.getLength(), split[i] + "\n", attrSet);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			} else {
				SimpleAttributeSet attrSet = new SimpleAttributeSet();
				StyleConstants.setBackground(attrSet, Color.white);
				try {
					doc.insertString(doc.getLength(), split[i] + "\n", attrSet);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class SimpleModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String[] columnNames = { "类别", "数量" };
		Vector<String> data1 = new Vector<String>();
		Vector<String> data2 = new Vector<String>();
		Object[][] data = new Object[10][2];

		int length = 0;

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			// return data1.size();
			return data1.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			if (columnIndex == 0)
				return data1.get(rowIndex);
			else
				return data2.get(rowIndex);
		}

		public void addRow(Object[] d) {
			data1.add((String)d[0]);
			data2.add((String)d[1]);
			fireTableDataChanged();
		}

		public void RemoveAll() {
			data1.clear();
			data2.clear();
			fireTableDataChanged();
		}
	}

}  //  @jve:decl-index=0:visual-constraint="1,5"
