package br.ufpe.cin.if688.minijava.main;

import java.util.Iterator;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDecl;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclList;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.Exp;
import br.ufpe.cin.if688.minijava.ast.ExpList;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.FormalList;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierExp;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.MethodDeclList;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.Statement;
import br.ufpe.cin.if688.minijava.ast.StatementList;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.VarDeclList;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.main.AntlrParser.ClassDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.ExpressionContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.GoalContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.IdentifierContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MainClassContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MethodDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.StatementContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.TypeContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.VarDeclarationContext;
public class MyVisitor implements AntlrVisitor<Object>{

	@Override
	public Object visit(ParseTree arg0) {
		return null;
	}

	@Override
	public Object visitChildren(RuleNode arg0) {
		return null;
	}

	@Override
	public Object visitErrorNode(ErrorNode arg0) {
		return null;
	}

	@Override
	public Object visitTerminal(TerminalNode arg0) {
		return null;
	}

	@Override
	public Object visitIdentifier(IdentifierContext ctx) {
		String id = ctx.getText();
		return new Identifier(id);
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclarationContext ctx) {
		Type t = (Type) ctx.type(0).accept(this);
		Identifier i = (Identifier) ctx.identifier(0).accept(this);
		boolean first = true;
		
		FormalList fl = new FormalList();
		for(int n = 0; n < ctx.identifier().size(); n++) {
			Type ft = (Type) ctx.type(n).accept(this);
			Identifier fi = (Identifier) ctx.identifier(n).accept(this);
			fl.addElement(new Formal(ft, fi));
		}
		
		Iterator<VarDeclarationContext> iv = (Iterator) ctx.varDeclaration().iterator();
		VarDeclList vl = new VarDeclList();
		while(iv.hasNext()) {
			vl.addElement((VarDecl) iv.next().accept(this));
		}
		
		Iterator<StatementContext> is = (Iterator) ctx.varDeclaration().iterator();
		StatementList sl = new StatementList();
		while(is.hasNext()) {
			sl.addElement((Statement) is.next().accept(this));
		}
		
		
		Exp e = (Exp) ctx.expression().accept(this);
		return new MethodDecl(t, i, fl, vl, sl, e);
	}

	@Override
	public Object visitGoal(GoalContext ctx) {
		MainClass m = (MainClass) ctx.mainClass().accept(this);
		ClassDeclList cl = new ClassDeclList();
		Iterator<ClassDeclarationContext> it = (Iterator) ctx.classDeclaration();
		while(it.hasNext()) {
			cl.addElement((ClassDecl) it.next().accept(this));
		}
		return new Program(m, cl);
	}

	@Override
	public Object visitExpression(ExpressionContext ctx) {
		ExpList el = (ExpList) ctx.expression();
		switch(el.size()) {
		case 0:
			if(!ctx.identifier().equals(null)) {
				return (Identifier) ctx.identifier().accept(this);
			}
			String s = ctx.getText();
			switch(s) {
			case "true":
				return new True();
			case "false":
				return new False();
			case "this":
				return new This();
			default:
				int x = Integer.parseInt(s);
				return new IntegerLiteral(x);
				
			}
			break;
		case 1:
			Exp e = (Exp) el.elementAt(0).accept(this);
			
			break;
		case 2:
			break;
		default:
			break;
		}
		Iterator<ExpressionContext> it = (Iterator) ctx.expression();
		while(it.hasNext()) {
			el.addElement((Exp) it.next().accept(this));
		}
		
		return null;
	}

	@Override
	public Object visitMainClass(MainClassContext ctx) {
		Identifier im = (Identifier) ctx.identifier(0).accept(this);
		Identifier ia = (Identifier) ctx.identifier(1).accept(this);
		Statement s = (Statement) ctx.statement().accept(this);
		return new MainClass(im, ia, s);
	}

	@Override
	public Object visitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitType(TypeContext ctx) {
		String type = ctx.getText();
		if(type.equals("int []")){
			return new IntArrayType();
		} else if (type.equals("boolean")){
			return new BooleanType();
		} else if (type.equals("int")){
			return new IntegerType();
		} else {
			return new IdentifierType(type);
		}
	}

	@Override
	public Object visitVarDeclaration(VarDeclarationContext ctx) {
		Type t = (Type) ctx.type().accept(this);
		Identifier i = (Identifier) ctx.identifier().accept(this);
		
		return new VarDecl(t, i);
	}

	@Override
	public Object visitClassDeclaration(ClassDeclarationContext ctx) {
		Identifier i1 = (Identifier) ctx.identifier(0).accept(this);
		
		Iterator<VarDeclarationContext> it = (Iterator) ctx.varDeclaration().iterator();
		VarDeclList vl = new VarDeclList();
		while(it.hasNext()) {
			vl.addElement((VarDecl) it.next().accept(this));
		}
		
		Iterator<MethodDeclarationContext> it2 = (Iterator) ctx.methodDeclaration();
		MethodDeclList ml = new MethodDeclList();
		while(it2.hasNext()) {
			ml.addElement((MethodDecl) it2.next().accept(this));
		}
		if( ctx.identifier(1) != null) {
			Identifier i2 = (Identifier) ctx.identifier(1).accept(this);
			return new ClassDeclExtends(i1, i2, vl, ml);
		}
		return new ClassDeclSimple(i1, vl, ml);
	}

}






