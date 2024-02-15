package ru.nsu.ccfit.zhdanov.firstAttemp.context;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.ContextInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.VariableHasAlreadyDefined;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.EmptyContextStack;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.IncorrectVariableName;

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
    return values.capacity();
  }

  @Override
  public void push(final double x) {
    log.info("push " + x);
    values.push(x);
  }

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
   * desc: delete top of context stack and return this value
   * arguments: none
   * throws: EmptyContextStack()
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
    log.info("define " + name + "=" + value);
  }

  @Override
  public double decode(final String name) {
    try {
      Double val = variables.get(name);
      if (val == null) {
        val = Double.parseDouble(name);
      }
      log.info("decode " + name + " into: " + val);
      return val;
    } catch (NullPointerException | NumberFormatException e) {
      throw new IncorrectVariableName();
    }
  }

  @Override
  public void print(double var) throws IOException {
    log.info("print " + var);
    out.write(String.valueOf(var));
    out.flush();
  }
}
