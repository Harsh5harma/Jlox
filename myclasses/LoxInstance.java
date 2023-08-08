package myclasses;

import java.util.HashMap;
import java.util.Map;

class LoxInstance {
  private LoxClass klass;
  private final Map<String, Object> fields = new HashMap<>();
  LoxInstance(LoxClass klass) {
    this.klass = klass;
  }

  Object get(Token name) {
    // Looking for a field first implies that fields shadow methods, a subtle but important semantic point.
    if (fields.containsKey(name.lexeme)) {
      return fields.get(name.lexeme);
    }

    /*This is where the distinction between “field” and “property” becomes meaningful. When accessing a property, 
    you might get a field - a bit of state stored on the instance—or you could hit a method defined on the instance’s class. */
    LoxFunction method = klass.findMethod(name.lexeme);
    if (method!=null) return method.bind(this);

    throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
  }



  void set(Token name, Object value) {
    fields.put(name.lexeme, value);
  }
  @Override
  public String toString() {
    return klass.name + " instance";
  }
}
