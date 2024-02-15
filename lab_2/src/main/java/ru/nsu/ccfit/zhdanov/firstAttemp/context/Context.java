package ru.nsu.ccfit.zhdanov.firstAttemp.context;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.ContextInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.VariableHasAlreadyDefined;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.EmptyContextStack;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.IncorrectVariableName;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


@Slf4j
public class Context implements ContextInterface {
  private final Stack<Double> values;
  private final Map<String, Double> variables;
  private final BufferedWriter out;

  public Context(Writer out) {
    this.values = new Stack<>();
    this.variables = new TreeMap<>();
    this.out = new BufferedWriter(out);
  }

  @Override
  public int occupancy() {
    return values.capacity();
  }

  @Override
  public void push(final double x) {
    values.push(x);
  }

  @Override
  public double peek() {
    try {
      return values.peek();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  /**
   * desc: delete top of context stack and return this value
   * arguments: none
   * throws: EmptyContextStack()
   */
  @Override
  public double pop() {
    try {
      return values.pop();
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  @Override
  public void define(String name, double value) {
    if (name == null || name.isEmpty()) {
      throw new IncorrectVariableName();
    }
    variables.compute(name, (key, val) -> {
      if (val == null) {
        return value;
      }
      throw new VariableHasAlreadyDefined(key);
    });
  }

  @Override
  public double decode(final String name) {
    try {
      Double val = variables.get(name);
      if (val == null) {
        return Double.parseDouble(name);
      }
      return val;
    } catch (NullPointerException e) {
      throw new IncorrectVariableName();
    }
  }

  @Override
  public void print(double var) throws IOException {
    out.write(String.valueOf(var));
    out.flush();
  }
}