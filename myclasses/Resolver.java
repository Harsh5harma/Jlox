package myclasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void>{
  private final Interpreter interpreter;
  private final Stack<Map<String, Boolean>> scopes = new Stack<>();
  private FunctionType currentFunction = FunctionType.NONE;


  Resolver(Interpreter interpreter) {
    this.interpreter = interpreter;
  }

  private enum FunctionType {
  NONE, 
  FUNCTION
  }
  
  @Override
  public Void visitBlockStmt(Stmt.Block stmt) {
    beginScope();
    resolve(stmt.statements);
    endScope();
    return null;
  }

  @Override 
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    declare(stmt.name);
    if (stmt.initializer != null) {
      resolve(stmt.initializer);
    }
    define(stmt.name);
    return null;
  }
  @Override
  public Void visitWhileStmt(Stmt.While stmt) {
    resolve(stmt.condition);
    resolve(stmt.body);
    return null;
  }

  @Override 
  public Void visitVariableExpr(Expr.Variable expr) {
    // This checks if we're initializing a variable with itself at declaration
    // We key our variable name and check if its value is false
    // if it is, then it means that we've not defined the variable yet
    if (!scopes.isEmpty() && 
    scopes.peek().get(expr.name.lexeme)== Boolean.FALSE) {
      Lox.error(expr.name, "Can't read local variable in its own initializer.");
    }

    resolveLocal(expr, expr.name);
    return null;
  }

  @Override
  public Void visitAssignExpr(Expr.Assign expr) {
    resolve(expr.value);
    resolveLocal(expr, expr.name);
    return null;
  }

  @Override 
  public Void visitBinaryExpr(Expr.Binary expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitCallExpr(Expr.Call expr) {
    resolve(expr.callee);

    for (Expr argument : expr.arguments) {
      resolve(argument);
    }

    return null;
  }

  @Override 
  public Void visitGroupingExpr(Expr.Grouping expr) {
    resolve(expr.expression);
    return null;
  }

  @Override 
  public Void visitLiteralExpr(Expr.Literal expr) {
    return null;
  }

  @Override 
  public Void visitLogicalExpr(Expr.Logical expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitUnaryExpr(Expr.Unary expr) {
    resolve(expr.right);
    return null;
  }
  
  @Override 
  public Void visitFunctionStmt(Stmt.Function stmt) {
    declare(stmt.name);
    define(stmt.name);

    resolveFunction(stmt, FunctionType.FUNCTION);
    return null;
  }

  public Void visitIfStmt(Stmt.If stmt) {
    // We resolve the condition and both branches.
    // Where a dynamic execution steps only into the branch that is run, a static analysis is conservative
    // It analyzes any branch that could be run
    resolve(stmt.condition);
    resolve(stmt.thenBranch);
    if (stmt.elseBranch != null) resolve(stmt.elseBranch);
    return null;
  }

  @Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override 
  public Void visitReturnStmt(Stmt.Return stmt) {
    if (currentFunction == FunctionType.NONE) {
      Lox.error(stmt.keyword, "Can't return from top-level code.");
    }
    if (stmt.value != null) {
      resolve(stmt.value);
    }

    return null;
  }
  
  // These methods are similar to the evaluate() and execute() methods in Interpreter 
  // they turn around and apply the Visitor pattern to the given syntax tree node.
  public void resolve(List<Stmt> statements) {
    for (Stmt statement : statements) {
      resolve(statement);
    }
  }

  private void resolve(Stmt stmt) {
    stmt.accept(this);
  }


  private void resolve(Expr expr) {
    expr.accept(this);
  }

  private void resolveFunction(Stmt.Function function, FunctionType type) {
    FunctionType enclosingFunction = currentFunction;
    currentFunction = type;

    beginScope();
    for (Token param: function.params) {
      declare(param);
      define(param);
    }

    resolve(function.body);
    endScope();
    currentFunction = enclosingFunction;
  }
  private void beginScope() {
    scopes.push(new HashMap<String, Boolean>());
  }

  private void endScope() {
    scopes.pop();
  }

  private void declare(Token name) {
    if (scopes.isEmpty()) return;

    Map<String, Boolean> scope = scopes.peek();

    if (scope.containsKey(name.lexeme)) {
      Lox.error(name, 
      "Already a variable with this name in this scope.");
    }

    scope.put(name.lexeme, false);
  }

  private void define(Token name) {
    if (scopes.isEmpty()) return;
    scopes.peek().put(name.lexeme, true);
  }




  private void resolveLocal(Expr expr, Token name) {
    // Start at the innermost scope and keep walking through the 
    // scope chain, checking if we've got our variable declared at any of them
    // if we find one, we call resolve on it and pass the number of scopes that've
    // passed b/w the innermost scope and the current one
    // kinda like telling how far we've popped out the scope stack.
    for (int i = scopes.size() -1; i >=0; i--) {
      if (scopes.get(i).containsKey(name.lexeme)) {
        interpreter.resolve(expr, scopes.size() -i );
        return;
      }
    }
  }


    /**
     * @return FunctionType return the currentFunction
     */
    public FunctionType getCurrentFunction() {
        return currentFunction;
    }

    /**
     * @param currentFunction the currentFunction to set
     */
    public void setCurrentFunction(FunctionType currentFunction) {
        this.currentFunction = currentFunction;
    }

}

