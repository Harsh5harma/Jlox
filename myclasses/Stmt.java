package myclasses;

import java.util.List;

abstract class Stmt{
  interface Visitor<R> {
  R visitBlockStmt(Block stmt);
  R visitExpressionStmt(Expression stmt);
  R visitIfStmt(If stmt);
  R visitPrintStmt(Print stmt);
  R visitVarStmt(Var stmt);
  R visitWhileStmt(While stmt);
  }
static class Block extends Stmt {
  Block(List<Stmt> statements) {
  this.statements = statements;
  }

  final List<Stmt> statements;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitBlockStmt(this);
    }
  }
static class Expression extends Stmt {
  Expression(Expr expression) {
  this.expression = expression;
  }

  final Expr expression;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitExpressionStmt(this);
    }
  }
static class If extends Stmt {
  If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
  this.condition = condition;
  this.thenBranch = thenBranch;
  this.elseBranch = elseBranch;
  }

  final Expr condition;
  final Stmt thenBranch;
  final Stmt elseBranch;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitIfStmt(this);
    }
  }
static class Print extends Stmt {
  Print(Expr expression) {
  this.expression = expression;
  }

  final Expr expression;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitPrintStmt(this);
    }
  }
static class Var extends Stmt {
  Var(Token name, Expr initializer) {
  this.name = name;
  this.initializer = initializer;
  }

  final Token name;
  final Expr initializer;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitVarStmt(this);
    }
  }
static class While extends Stmt {
  While(Expr condition, Stmt body) {
  this.condition = condition;
  this.body = body;
  }

  final Expr condition;
  final Stmt body;


  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visitWhileStmt(this);
    }
  }

  abstract <R> R accept(Visitor<R> visitor); 
}
