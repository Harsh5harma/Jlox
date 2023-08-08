package myclasses;
import java.util.List;

class LoxFunction implements LoxCallable{
  private final Environment closure;

  private final Stmt.Function declaration;
  private final boolean isInitializer;

  LoxFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
    // closure is nothing but a reference to the environment
    // that was active during function declaration, not when it got called.
    this.isInitializer = isInitializer;
    this.closure = closure;
    this.declaration = declaration;
  }

  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment, isInitializer);
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    // we chain up the new environment to the environment present at
    // time of function declaration, i.e closure.
    Environment environment = new Environment(closure);

    for (int i = 0; i< declaration.params.size(); i++ ) {
      environment.define(declaration.params.get(i).lexeme, 
      arguments.get(i));
    }

    try {
    interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
      if (isInitializer) return closure.getAt(0, "this");
      return returnValue.value;
    }

    if (isInitializer) return closure.getAt(0, "this");
    return null;
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }
}
