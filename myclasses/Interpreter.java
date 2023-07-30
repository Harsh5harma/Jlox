package myclasses;

class Interpreter implements Expr.Visitor<Object> {

  void interpret(Expr expression) {
    try {
      Object value = evaluate(expression);
      System.out.println(stringify(value));
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

        throw new RuntimeError(expr.operator, 
        "Operands must be two numbers or Strings");

      case SLASH:
        checkNumberOperands(expr.operator, left, right);
        return (double)left / (double)right;
      case STAR:
        checkNumberOperands(expr.operator, left, right);
        return (double)left * (double)right;
    }

    // Unreachable
    return null;
  }
}
