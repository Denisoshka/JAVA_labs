package ru.nsu.zhdanov.lab_4.model.exceptions;

public class ClassLoaderException extends FactoryException {
    public ClassLoaderException() {
        super("Class is not loaded");
    }
}
