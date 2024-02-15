package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import java.io.IOException;

public interface ContextInterface {
  abstract public double pop();

  abstract public void define(final String name, double value);

  abstract public double peek();

  abstract public void push(double x);

  abstract public int occupancy();

  abstract public void print(double var) throws IOException;

  abstract public double decode(final String name);
}
