package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class ClassLoaderException extends FactoryException {
    public ClassLoaderException() {
        super("Class is not loaded");
    }
}
