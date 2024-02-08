package ru.nsu.zhdanov.lab_4.facade;

import ru.nsu.zhdanov.lab_4.lab_4.Application;
import ru.nsu.zhdanov.lab_4.parts_section.SparePart;
import ru.nsu.zhdanov.lab_4.parts_section.exceptions.ClassLoaderException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class MainContext {
  private final Properties sparePartProperties;
  final ArrayList<String> sparePartList;
  Map<String, SparePartSectionController> sparePartSectionControllers;

//  final Eng


  public MainContext(final ArrayList<String> sparePartList,
                     final String sparePartProperties,
                     final String sparePartControllerProperties, ArrayList<String> sparePartList1) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    this.sparePartList = sparePartList;
    ClassLoader classLoader;
    try {
      classLoader = MainContext.class.getClassLoader();
      if (classLoader == null) {
        throw new ClassLoaderException();
      }
    } catch (SecurityException e) {
      throw new ClassLoaderException();
    }

    try (InputStream in = classLoader.getResourceAsStream(sparePartProperties)) {
      (this.sparePartProperties = new Properties()).load(in);
    } catch (Exception e) {
//      todo
      throw e;
    }

    try (InputStream in = classLoader.getResourceAsStream(sparePartControllerProperties)) {
      Properties properties = new Properties();
      properties.load(in);
      for (String s : sparePartList) {
        SparePartSectionController<SparePart> controller = (SparePartSectionController<SparePart>) Class.forName(properties.getProperty(s))
                .getDeclaredConstructor()
                .newInstance();
        sparePartSectionControllers.put(s, controller);
      }
    } catch (Exception e) {
//      todo
      throw e;
    }



  }


}
