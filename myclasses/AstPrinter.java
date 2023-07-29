package myclasses;


class AstPrinter implements Expr.Visitor<String>{
  public static void main(String[] args) {
    Expr expression = new Expr.Binary(
      new Expr.Binary(
        new Expr.Literal(1),
        new Token(TokenType.PLUS, "+", null, 1),
        new Expr.Literal(2)
      ),
      new Token(TokenType.STAR, "*", null, 1),
      new Expr.Binary(
        new Expr.Literal(4),
        new Token(TokenType.MINUS, "-", null, 1),
        new Expr.Literal(3)
      )
    );

    System.out.println(new AstPrinter().print(expression));
  }
  
  String print(Expr expr) {
    return expr.accept(this);
  }  
  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override 
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  // RPN parenthesize
  private String parenthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(");
    for (Expr expr: exprs) {
      builder.append(expr.accept(this));
      builder.append(" ");
    }

    builder.append(name).append(")");
    return builder.toString();
  }


}
