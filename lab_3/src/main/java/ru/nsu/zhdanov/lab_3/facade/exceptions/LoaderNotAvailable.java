package ru.nsu.zhdanov.lab_3.facade.exceptions;

public class LoaderNotAvailable extends FacadeException {
  public LoaderNotAvailable() {
    super("Unable to get loader");
  }
}
