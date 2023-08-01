package myclasses;
import java.util.List;
class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

  private Environment environment = new Environment();

  void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement: statements) {
        execute(statement);
      }
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
  }

  @Override 
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }


  @Override 
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case BANG:
      return !isTruthy(right);
      case MINUS:
        return -(double)right;
    }

    // Unreachable
    return null;
  }

  @Override
  public Object visitVariableExpr(Expr.Variable expr) {
    return environment.get(expr.name);
  }

  private void checkNumberOperand(Token operator, Object operand) {
    if (operand instanceof Double) return;
    throw new RuntimeError(operator, "Operand must be a number.");
  }

  private void checkNumberAndStringOperands(Token operator, Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    if (left instanceof String && right instanceof String) return;
    throw new RuntimeError(operator, "Operands must be numbers or Strings.");
  }

  private void checkNumberOperands(Token operator, Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    throw new RuntimeError(operator, "Operands must be numbers.");
  }

  private boolean isTruthy(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (boolean)object;
    return true;
  }

  private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;

    return a.equals(b);
  }

  private static Boolean isStrGreater(String a, String b) {
    if (a.length()>b.length()) {
        return true;
    } 
    if (a.length() == b.length()) {
        if (a==b) return false;
        for (int i = 0; i<a.length(); i++ ) {
            if ((int)a.charAt(i) < (int)b.charAt(i)){
                return false;
            }
        }
        return true;
    } 
    return false;
}

  private static Boolean isStrLesser(String a, String b) {
    if (a.length()<b.length()) {
        return true;
    } 
    if (a.length() == b.length()) {
        if (a==b) return false;
        for (int i = 0; i<a.length(); i++ ) {
            if ((int)a.charAt(i) < (int)b.charAt(i)){
                return false;
            }
        }
        return true;
    } 
    return false;
}

  private String stringify(Object object) {
    if (object == null) return "nil";

    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length()-2);
      }
      return text;
    }

    return object.toString();
  }


  @Override 
  public Object visitGroupingExpr (Expr.Grouping expr) {
    return evaluate(expr.expression);
  }

  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  private void execute(Stmt stmt) {
    stmt.accept(this);
  }

  void executeBlock(List<Stmt> statements, Environment environment) {
    Environment previous = this.environment;
    try { 
      this.environment = environment;

      for (Stmt statement : statements) {
        execute(statement);
      }
    } finally {
      this.environment = previous;
    }
  }

  @Override
  public Void visitBlockStmt(Stmt.Block stmt) {
    executeBlock(stmt.statements, new Environment(environment));
    return null;
  }
  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }

  @Override 
  public Void visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }

  @Override 
  public Void visitVarStmt(Stmt.Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }
    environment.define(stmt.name.lexeme, value);
    return null;
  }

  @Override 
  public Object visitAssignExpr(Expr.Assign expr) {
    Object value = evaluate(expr.value);
    environment.assign(expr.name, value);
    return value;
  }
  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch(expr.operator.type) {
      case GREATER:
        checkNumberAndStringOperands(expr.operator, left, right);
        if (left instanceof String && right instanceof String) {
          return isStrGreater((String)left, (String)right);
        }
        return (double) left > (double) right;
      case GREATER_EQUAL:
        checkNumberAndStringOperands(expr.operator, left, right);
        if (left instanceof String && right instanceof String) {
          if (isStrGreater((String)left, (String)right) ||
          (String) left == (String) right) {
            return true;
          }
          return false;
        }
        return (double) left >= (double) right;
      case LESS:
        checkNumberAndStringOperands(expr.operator, left, right);
        if (left instanceof String && right instanceof String) {
          return isStrLesser((String)left, (String)right);
        }
        return (double) left < (double) right;
      case LESS_EQUAL:
        checkNumberAndStringOperands(expr.operator, left, right);
        if (left instanceof String && right instanceof String) {
          if (isStrLesser((String)left, (String)right)) {
              return true;
          }else if ((String) left == (String) right){
            return true;
          }else {
            return false;
          }
        }
        return (double) left <= (double) right;
      case BANG_EQUAL: return !isEqual(left, right);
      case EQUAL_EQUAL: return isEqual(left, right);
      case MINUS:
        checkNumberOperands(expr.operator, left, right);
        return (double)left - (double)right;
      case PLUS:
        if (left instanceof Double && right instanceof Double) {
          return (double)left + (double)right;
        }

        if (left instanceof String && right instanceof String) {
          return (String)left + (String)right;
        }
        
        if (left instanceof String && right instanceof Double) {
          String text = ((Double) right).toString();
          if (text.endsWith(".0")) {
            text = text.substring(0, text.length()-2);
          }
          return (String) left + text;
        }

        if (left instanceof Double && right instanceof String) {
          String text = ((Double) left).toString();
          if (text.endsWith(".0")) {
            text = text.substring(0, text.length()-2);
          }
          return text + (String) right;
        }
        throw new RuntimeError(expr.operator, 
        "Operands must be a valid number or String");

      case SLASH:
        checkNumberOperands(expr.operator, left, right);
        if ((double) right == 0) {
          throw new RuntimeError(expr.operator, "Division by zero error.");
        }
        return (double)left / (double)right;
      case STAR:
        checkNumberOperands(expr.operator, left, right);
        return (double)left * (double)right;
    }

    // Unreachable
    return null;
  }
}
