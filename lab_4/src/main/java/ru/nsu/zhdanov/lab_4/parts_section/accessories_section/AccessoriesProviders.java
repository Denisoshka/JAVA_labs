package ru.nsu.zhdanov.lab_4.parts_section.accessories_section;

import ru.nsu.zhdanov.lab_4.parts_section.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesProviders extends SparePartProvider<Accessories> {
   public AccessoriesProviders(final int providersQuantity, final AtomicInteger delay) {
    super("ACCESSORIES", providersQuantity, delay);
//    todo хули ты ебешь мне мозг дура
  }
}
