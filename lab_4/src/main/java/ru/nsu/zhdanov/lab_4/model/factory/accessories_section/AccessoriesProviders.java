package ru.nsu.zhdanov.lab_4.model.factory.accessories_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesProviders extends SparePartProvider<Accessories> {
   public AccessoriesProviders(final int providersQuantity, final AtomicInteger delay) {
    super("ACCESSORIES", providersQuantity, delay);
//    todo хули ты ебешь мне мозг дура
  }
}
