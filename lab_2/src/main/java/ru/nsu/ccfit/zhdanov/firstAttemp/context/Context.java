package ru.nsu.ccfit.zhdanov.firstAttemp.context;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.EmptyContextStack;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.IncorrectVariableName;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;
@Slf4j
public class Context {
  private final Stack<Double> _values;
  private final HashMap<String, Double> _variables;
  private final BufferedWriter _out;

  public Context(Writer out) {
    this._values = new Stack<>();
    this._variables = new HashMap<>();
    this._out = new BufferedWriter(out);
  }

  public int capacity(){
    return _values.capacity();
  }
  public void push(final Double x) {
    _values.push(x);
    log.debug("values: " + _values.toString());
  }

  public double peek() {
    try {
      return _values.peek();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  public double pop() {
    try {
      return _values.pop();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  public void pushVariable(String name, Double value) {
    if (name == null || name.isEmpty()) {
      throw new IncorrectVariableName();
    }
    _variables.put(name, value);
  }

  public double peekVariable(String name) {
    Double val;
    try {
      val = _variables.get(name);
    } catch (NullPointerException e) {
      throw new IncorrectVariableName();
    }
    if (val == null){
      throw new NotContainVariable(name);
    }
    return val;
  }

  public void print(double var) throws IOException {
    _out.write(String.valueOf(var));
    _out.flush();
  }
}