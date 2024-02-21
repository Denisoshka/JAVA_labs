package ru.nsu.ccfit.zhdanov.firstAttemp.context;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.VariableRedefinition;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.EmptyContextStack;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.IncorrectVariable;

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
    log.info("occupancy " + values.capacity());
    return values.size();
  }

  @Override
  public void push(final double x) {
    log.info("push " + x);
    values.push(x);
  }

  /**
   * @throws: EmptyContextStack
   */
  @Override
  public double peek() {
    try {
      double x = values.peek();
      log.info("peek " + x);
      return x;
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  /**
   * @throws: EmptyContextStack
   */
  @Override
  public double pop() {
    try {
      double x = values.pop();
      log.info("pop " + x);
      return x;
    } catch (EmptyStackException e) {
      throw new EmptyContextStack();
    }
  }

  /**
   * @throws IncorrectVariable
   * @throws VariableRedefinition
   */
  @Override
  public void define(final String name, final String value) {
    if (name == null || name.isEmpty()) {
      throw new IncorrectVariable("name");
    }
    variables.compute(name, (key, val) -> {
      if (val == null) {
        try {
          return Double.parseDouble(value);
        } catch (Exception ignored) {
          throw new IncorrectVariable("value");
        }
      }
      throw new VariableRedefinition(key);
    });
    log.info("define " + name + "=" + value);
  }

  /**
   * @throws IncorrectVariable
   */
  @Override
  public double decode(final String name) {
    try {
      Double val = variables.get(name);
      if (val == null) {
        val = Double.parseDouble(name);
      }
      log.info("decode " + name + " into: " + val);
      return val;
    } catch (Exception ignored) {
      throw new IncorrectVariable(name);
    }
  }

  @Override
  public void print(double var) throws IOException {
    log.info("print " + var);
    out.write(String.valueOf(var));
    out.flush();
  }
}
