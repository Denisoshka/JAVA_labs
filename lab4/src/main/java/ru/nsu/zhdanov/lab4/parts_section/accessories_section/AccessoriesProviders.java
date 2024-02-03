package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import ru.nsu.zhdanov.lab4.parts_section.SparePartFactory;
import ru.nsu.zhdanov.lab4.parts_section.SparePartProvider;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesProviders extends SparePartProvider<Accessories> {
   public AccessoriesProviders(final int providersQuantity, final AtomicInteger delay) {
    super(providersQuantity, delay);
//    todo хули ты ебешь мне мозг дура
  }
}
