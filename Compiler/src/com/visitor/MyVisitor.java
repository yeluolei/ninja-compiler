package com.visitor;

import java.awt.Checkbox;
import java.util.Vector;

import com.compiler.ASTAllocationExpression;
import com.compiler.ASTAndExpression;
import com.compiler.ASTArrayAllocationExpression;
import com.compiler.ASTArrayAssignmentStatement;
import com.compiler.ASTArrayLength;
import com.compiler.ASTArrayLookup;
import com.compiler.ASTArrayType;
import com.compiler.ASTAssignmentStatement;
import com.compiler.ASTBlock;
import com.compiler.ASTBooleanType;
import com.compiler.ASTBracketExpression;
import com.compiler.ASTClassDeclaration;
import com.compiler.ASTClassExtendsDeclaration;
import com.compiler.ASTCompareExpression;
import com.compiler.ASTDataExpression;
import com.compiler.ASTExpression;
import com.compiler.ASTExpressionList;
import com.compiler.ASTExpressionRest;
import com.compiler.ASTFalseLiteral;
import com.compiler.ASTFormalParameter;
import com.compiler.ASTFormalParameterList;
import com.compiler.ASTFormalParameterRest;
import com.compiler.ASTIdentifier;
import com.compiler.ASTIfStatement;
import com.compiler.ASTIntArrayAllocationExpression;
import com.compiler.ASTIntegerLiteral;
import com.compiler.ASTIntegerType;
import com.compiler.ASTLongArrayAllocationExpression;
import com.compiler.ASTLongArrayType;
import com.compiler.ASTLongLiteral;
import com.compiler.ASTLongType;
import com.compiler.ASTMainClass;
import com.compiler.ASTMessageSend;
import com.compiler.ASTMethodDeclaration;
import com.compiler.ASTNotExpression;
import com.compiler.ASTPlusExpression;
import com.compiler.ASTPostDecl;
import com.compiler.ASTPreDecl;
import com.compiler.ASTPrimaryExpression;
import com.compiler.ASTPrintStatement;
import com.compiler.ASTStart;
import com.compiler.ASTStatement;
import com.compiler.ASTThisExpression;
import com.compiler.ASTTimesExpression;
import com.compiler.ASTTrueLiteral;
import com.compiler.ASTType;
import com.compiler.ASTTypeDeclaration;
import com.compiler.ASTVarDeclaration;
import com.compiler.ASTWhileStatement;
import com.compiler.SimpleNode;
import com.compiler.XYZ2Visitor;
import com.error.ErrorMessage;
import com.table.ClassEntity;
import com.table.MethodEntity;
import com.table.ProgramTable;
import com.table.Variability;

public class MyVisitor implements XYZ2Visitor {
	private ErrorMessage error = new ErrorMessage();
	private ClassEntity currClass = null;
	private MethodEntity currMethod = null;
	ProgramTable programTable = null;
	
	public MyVisitor(){
		super();
		programTable = new ProgramTable();
	}
	
	@Override
	public Object visit(SimpleNode node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	
	/**
	 * build the classTable and MethodTable first
	 * for caller in other place
	 */
	@Override
	public Object visit(ASTStart node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTMainClass node, Object data) {
		currClass = new ClassEntity();
		String className=((ASTIdentifier)node.jjtGetChild(0)).toString();
		programTable.setMainClassName(className);
		currClass.setClassName(className);
		
		node.childrenAccept(this, data);
		
		if(programTable.getClassTable().get(className)!=null)
		{
			error.addError(((SimpleNode)node.jjtGetChild(0)).jjtGetFirstToken().beginLine,
					className+" class redefined");
		}else {
			programTable.getClassTable().put(className, currClass);
		}
		currClass = null;
		return null;
	}

	@Override
	public Object visit(ASTTypeDeclaration node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTClassDeclaration node, Object data) {
		currClass = new ClassEntity();
		String className=((ASTIdentifier)node.jjtGetChild(0)).toString();
		currClass.setClassName(className);
		
		node.childrenAccept(this, data);
		
		if(programTable.getClassTable().get(className)!=null)
		{
			error.addError(((SimpleNode)node.jjtGetChild(0)).jjtGetFirstToken().beginLine,
					className+"class redefined");
		}else {
			programTable.getClassTable().put(className, currClass);
		}
		currClass = null;
		return null;
	}

	@Override
	public Object visit(ASTClassExtendsDeclaration node, Object data) {
		currClass = new ClassEntity();
		String className=((ASTIdentifier)node.jjtGetChild(0)).toString();
		currClass.setClassName(className);
		
		String parentClassName = ((ASTIdentifier)node.jjtGetChild(1)).toString();
		currClass.setParentClassName(parentClassName);
		
		node.childrenAccept(this, data);
		
		if(programTable.getClassTable().get(className)!=null)
		{
			error.addError(((SimpleNode)node.jjtGetChild(0)).jjtGetFirstToken().beginLine,
					className+"class redefined");
		}else {
			programTable.getClassTable().put(className, currClass);
		}
		currClass = null;
		return null;
	}

	@Override
	public Object visit(ASTMethodDeclaration node, Object data) {
		currMethod = new MethodEntity();
		String methodName = ((SimpleNode)node.jjtGetChild(1)).jjtGetFirstToken().image;
		currMethod.setReturnType(((SimpleNode)node.jjtGetChild(0)).jjtGetFirstToken().image);
		currMethod.setMethodName(methodName);
		
		node.childrenAccept(this, data);
		if(currClass.getMethodTable().get(methodName) != null){
			error.addError(((SimpleNode)node.jjtGetChild(1)).jjtGetFirstToken().beginLine,
					methodName + "method redefined");
		}
		currMethod = null; //当前方法定义域结束
		return null;
	}

	@Override
	public Object visit(ASTPreDecl node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTPostDecl node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTFormalParameterList node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTFormalParameter node, Object data) {
		Variability variability= new Variability();
		variability.setTypeName((String)node.jjtGetChild(0).jjtAccept(this, data));
		variability.setVarName(((SimpleNode)node.jjtGetChild(1)).toString());
		variability.setDeclearLine(((SimpleNode)node.jjtGetChild(1)).jjtGetFirstToken().beginLine);
		if (!currMethod.getParameters().add(variability)){ // Add the parameter to the para Table
			error.addError(variability.getDeclearLine(), 
					variability.getVarName() +"redefined");
		}
		return null;
	}

	@Override
	public Object visit(ASTFormalParameterRest node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTType node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTArrayType node, Object data) {
		return "IntArray";
	}

	@Override
	public Object visit(ASTLongArrayType node, Object data) {
		return "LongArray";
	}

	@Override
	public Object visit(ASTBooleanType node, Object data) {
		return "Boolean";
	}

	@Override
	public Object visit(ASTIntegerType node, Object data) {
		return "Int";
	}

	@Override
	public Object visit(ASTLongType node, Object data) {
		return "Long";
	}

	@Override
	public Object visit(ASTStatement node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTVarDeclaration node, Object data) {
		Variability v = new Variability();
		v.setTypeName((String)node.jjtGetChild(0).jjtAccept(this, data));
		v.setVarName((String)node.jjtGetChild(1).jjtAccept(this, data));
		v.setDeclearLine(((SimpleNode)node.jjtGetChild(1)).jjtGetFirstToken().beginLine);
		
		/**
		 *  Current method is null , so this var is in class
		 *  Put it into the FieldTable
		 */
		if (currMethod == null){
			currClass.getFieldTable().put(v.getVarName(), v);
		}else {
			currMethod.getLocalTable().put(v.getVarName(), v);
		}
		return null;
	}

	@Override
	public Object visit(ASTBlock node, Object data) {
		return null;
	}

	/**
	 * Identifier = Exp
	 */
	@Override
	public Object visit(ASTAssignmentStatement node, Object data) {
		String identName = (String)node.jjtGetChild(0).jjtAccept(this, data);
		String expType = (String)node.jjtGetChild(1).jjtAccept(this, data);
		// TODO Check exp Type can change to identType
		Variability variability = currClass.getFieldTable().get(identName);
		if (variability == null)
		{
			variability = currMethod.getLocalTable().get(identName);
		}
		return null;
	}

	/**
	 * Identifier[Exp] = Exp
	 */
	@Override
	public Object visit(ASTArrayAssignmentStatement node, Object data) {
		String identName = (String)node.jjtGetChild(0).jjtAccept(this, data);
		String indexExpType = (String)node.jjtGetChild(1).jjtAccept(this, data);
		String expType = (String)node.jjtGetChild(2).jjtAccept(this, data);
		
		if (checkIdentifyType(identName).equals("IntArray"))
		{
			if (!(expType.equals("Int")||expType.equals("Boolean")))
			{
				error.addError(node.jjtGetFirstToken().beginLine, "Assignment to IntArray Error");
			}
		}
		else if (checkIdentifyType(identName).equals("LongArray")){
			if (!(expType.equals("Long")||expType.equals("Int")||expType.equals("Boolean")))
			{
				error.addError(node.jjtGetFirstToken().beginLine, "Assignment to LongArray Error");
			}
		}else {
			error.addError(node.jjtGetFirstToken().beginColumn, "Identifier is not Array type");
		}
		
		
		if (!indexExpType.equals("Int"))
		{
			error.addError(((SimpleNode)node.jjtGetChild(1)).jjtGetFirstToken().beginLine,
					"Array Index Error");
		}
		return null;
	}

	/**
	 * check the exp in "if (exp)" can change to boolean 
	 */
	@Override
	public Object visit(ASTIfStatement node, Object data) {
		String expType = (String)node.jjtGetChild(0).jjtAccept(this, data);
		
		if (!(expType.equals("Int") || expType.equals("Long")
				|| expType.equals("Boolean")))
		{
			error.addError(node.jjtGetFirstToken().beginLine,
					"If Expression Error");
		}
		node.jjtGetChild(1).jjtAccept(this, data);
		node.jjtGetChild(2).jjtAccept(this, data);
		return null;
	}

	/**
	 *  check the exp in "while (exp)" can change to boolean 
	 */
	@Override
	public Object visit(ASTWhileStatement node, Object data) {
		String expType = (String)node.jjtGetChild(0).jjtAccept(this, data);
		if (!(expType.equals("Int") || expType.equals("Long")
				|| expType.equals("Boolean")))
		{
			error.addError(node.jjtGetFirstToken().beginLine,
					"While Expression Error");
		}
		node.jjtGetChild(1).jjtAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTPrintStatement node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		if (node.jjtGetNumChildren() == 1){
			return node.jjtGetChild(0).jjtAccept(this, data);
		}else {
			return "Boolean";
		}
	}

	@Override
	public Object visit(ASTAndExpression node, Object data) {
		if (node.jjtGetNumChildren() == 1){
			return node.jjtGetChild(0).jjtAccept(this, data);
		}else {
			return "Boolean";
		}
	}

	@Override
	public Object visit(ASTCompareExpression node, Object data) {
		if (node.jjtGetNumChildren() == 1) {
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
		else {
			// TODO 这里要比较两边的类型
			return "Boolean";
		}
		
	}

	@Override
	public Object visit(ASTPlusExpression node, Object data) {
		if (node.jjtGetNumChildren() == 1) {
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
		else {
			// TODO 这里要比较两边的类型
			return "";
		}
	}

	@Override
	public Object visit(ASTTimesExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTArrayLookup node, Object data) {
		String priExpType = (String)node.jjtGetChild(0).jjtAccept(this, data);
		String expType = (String)node.jjtGetChild(1).jjtAccept(this, data);
		if (!expType.equals("Int"))
		{
			error.addError(node.jjtGetFirstToken().beginLine, "Array index mast be int");
		}
		if (priExpType.equals("IntArray"))
		{
			return "Int";
		}
		else if (priExpType.equals("LongArray")) {
			return "Long";
		}
		else {
			error.addError(node.jjtGetFirstToken().beginLine, "Not an Array");
			return "default";
		}
	}

	@Override
	public Object visit(ASTArrayLength node, Object data) {
		if (node.jjtGetChild(0) instanceof ASTIdentifier)
		{
			String identName = (String)node.jjtGetChild(0).jjtAccept(this, data);
			String identType = checkIdentifyType(identName);
			if (!(identType.equals("LongArray")||identType.equals("IntArray")))
			{
				error.addError(node.jjtGetFirstToken().beginLine,
						identName + " is not an Array");
			}
		}
		return "Int";
	}

	
	/**
	 * prim.identifier(exps)
	 */
	@Override
	public Object visit(ASTMessageSend node, Object data) {
		String callerType = (String)node.jjtGetChild(0).jjtAccept(this, data);
		String funcname = (String)node.jjtGetChild(1).jjtAccept(this, data);
		Vector<String>exps = (Vector<String>)node.jjtGetChild(2).jjtAccept(this, data);
		
		ClassEntity tempClassEntity = programTable.getClassTable().get(callerType);
		if (tempClassEntity == null)
		{
			// TODO
		}else {
			
		}
		return tempClassEntity.getMethodTable().get(funcname).getReturnType();
	}

	@Override
	public Object visit(ASTExpressionList node, Object data) {
		Vector<String>expTypes = new Vector<String>();
		for (int i = 0 ; i < node.jjtGetNumChildren() ; i++)
		{
			expTypes.add((String)node.jjtGetChild(i).jjtAccept(this, data));
		}
		return expTypes;
	}

	@Override
	public Object visit(ASTExpressionRest node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	/**
	 * return type
	 */
	@Override
	public Object visit(ASTPrimaryExpression node, Object data) {
		if (node.jjtGetChild(0) instanceof ASTIdentifier) {
			return checkIdentifyType((String)node.jjtGetChild(0).jjtAccept(this, data));
		}
		else 
			return node.jjtGetChild(0).jjtAccept(this, data);
	}
	
	@Override
	public Object visit(ASTDataExpression node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTLongLiteral node, Object data) {
		return "Long";
	}

	@Override
	public Object visit(ASTIntegerLiteral node, Object data) {
		return "Int";
	}

	@Override
	public Object visit(ASTTrueLiteral node, Object data) {
		return "Boolean";
	}

	@Override
	public Object visit(ASTFalseLiteral node, Object data) {
		return "Boolean";
	}

	
	/**
	 * Identifier
	 * "Identifier" node need to return the name of itself 
	 */
	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.jjtGetFirstToken().image;
	}

	/**
	 * this
	 * "this" node need to return the type , that is the class name
	 */
	@Override
	public Object visit(ASTThisExpression node, Object data) {
		return currClass.getClassName();
	}

	
	/**
	 * IntArray
	 * LongArray
	 */
	@Override
	public Object visit(ASTArrayAllocationExpression node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	/**
	 *  "new" Identifier() "(" ")"
	 *  get the type of the Identifier
	 */
	@Override
	public Object visit(ASTAllocationExpression node, Object data) {
		String type = (String)node.jjtGetChild(0).jjtAccept(this, data);
		if (programTable.getClassTable().get(type) == null){
			error.addError(node.jjtGetFirstToken().beginLine, "Type not exist");
			return "Default";
		}
		else {
			return type;
		}
	}
	
	/**
	 * "new" "long" "[" Expression() "]"
	 */
	@Override
	public Object visit(ASTLongArrayAllocationExpression node, Object data) {
		if (!((String)node.jjtGetChild(0).jjtAccept(this, data)).equals("Int"))
		{
			error.addError(node.jjtGetFirstToken().beginLine ,
					"Array Type Error");
		}
		return "LongArray";
	}

	/**
	 * "new" "int" "[" Expression() "]"
	 */
	@Override
	public Object visit(ASTIntArrayAllocationExpression node, Object data) {
		if (!((String)node.jjtGetChild(0).jjtAccept(this, data)).equals("Int"))
		{
			error.addError(node.jjtGetFirstToken().beginLine, 
					"Array Type Error");
		}
		return "IntArray";
	}

	@Override
	public Object visit(ASTNotExpression node, Object data) {
		return "Boolean";
	}

	/**
	 * Bracket->Exp
	 * here return the type of exp
	 */
	@Override
	public Object visit(ASTBracketExpression node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	public String checkIdentifyType(String identifier)
	{
		return "";
	}
}
