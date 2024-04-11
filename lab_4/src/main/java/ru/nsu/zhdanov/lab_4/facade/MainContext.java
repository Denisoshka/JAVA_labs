package ru.nsu.zhdanov.lab_4.facade;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.*;
import ru.nsu.zhdanov.lab_4.model.AccessoriesModel;
import ru.nsu.zhdanov.lab_4.model.BodyModel;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartFactory;

import java.util.Properties;
@Slf4j
public class MainContext {
  private final int standardProvideDelay = 10000;
  private final Properties contextProperties;
  //  final Map<String, SparePartSectionController> sparePartSectionControllers;
  final FactoryModel factoryController;
  final DealerModel dealerController;
  final EngineModel engineSectionController;
  final BodyModel bodySectionController;
  final AccessoriesModel accessoriesSectionController;
  //  final Eng

  public MainContext(final Properties sparePartProperties, final Properties contextProperties) {
    log.info("init main context");
    this.contextProperties = contextProperties;

    SparePartFactory.Instance(sparePartProperties);
    this.engineSectionController = new EngineModel(standardProvideDelay, Integer.parseInt(this.contextProperties.get("engineRepoSize").toString()));
    this.bodySectionController = new BodyModel(standardProvideDelay, Integer.parseInt(this.contextProperties.get("bodyRepoSize").toString()));
    this.accessoriesSectionController = new AccessoriesModel(
            standardProvideDelay,
            Integer.parseInt(this.contextProperties.get("accessoriesProviders").toString()),
            Integer.parseInt(this.contextProperties.get("accessoriesRepoSize").toString())
    );

    this.factoryController = new FactoryModel(
            Integer.parseInt(this.contextProperties.get("factoryWorkersQuantity").toString()),
            Integer.parseInt(this.contextProperties.get("factoryRepoSize").toString()),
            Integer.parseInt(this.contextProperties.get("dealersQuantity").toString()),
            standardProvideDelay/10,
            this.bodySectionController.getRepository(),
            this.engineSectionController.getRepository(),
            this.accessoriesSectionController.getRepository()
    );

    this.dealerController = new DealerModel(factoryController.getCarSupplier(), Integer.parseInt(this.contextProperties.get("dealersQuantity").toString()), standardProvideDelay);
//    this.dealerController.setCarSupplier(factoryController.getCarSupplier());
  }

  public int getCarsQuantity() {
    int x = factoryController.getRepositoryOccupancy();
    log.info("call factoryController.getRepositoryOccupancy():" + x);
    return x;
  }

  public void perform() {
    log.info("perform work");
    factoryController.perform();
    dealerController.perform();
    engineSectionController.perform();
    bodySectionController.perform();
    accessoriesSectionController.perform();
  }

  public void shutdown() {
    log.info("shut down controller");
    factoryController.shutdown();
    dealerController.shutdown();
    engineSectionController.shutdown();
    bodySectionController.shutdown();
    accessoriesSectionController.shutdown();
  }
}
