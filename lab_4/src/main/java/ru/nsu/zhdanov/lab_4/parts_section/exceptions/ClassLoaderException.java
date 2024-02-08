package ru.nsu.zhdanov.lab_4.parts_section.exceptions;

public class ClassLoaderException extends FactoryException {
    public ClassLoaderException() {
        super("Class is not loaded");
    }
}
