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
import java.util.Map;
import java.util.Stack;


@Slf4j
public class Context {
  private final Stack<Double> values;
  private final Map<String, Double> variables;
  private final BufferedWriter out;

  public Context(Writer out) {
    this.values = new Stack<>();
    this.variables = new HashMap<>();
    this.out = new BufferedWriter(out);
  }

  public int capacity() {
    return values.capacity();
  }

  public void push(final Double x) {
    values.push(x);
  }

  public double peek() {
    try {
      return values.peek();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  public double pop() {
    try {
      return values.pop();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  public void pushVariable(String name, Double value) {
    if (name == null || name.isEmpty()) {
      throw new IncorrectVariableName();
    }
    variables.put(name, value);
  }

  public double peekVariable(String name) {
    Double val;
    try {
      val = variables.get(name);
    } catch (NullPointerException e) {
      throw new IncorrectVariableName();
    }
    if (val == null) {
      throw new NotContainVariable(name);
    }
    return val;
  }

  public void print(double var) throws IOException {
    out.write(String.valueOf(var));
    out.flush();
  }
}