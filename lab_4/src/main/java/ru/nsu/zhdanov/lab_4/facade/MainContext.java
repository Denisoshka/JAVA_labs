package ru.nsu.zhdanov.lab_4.facade;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.parts_section.SparePartFactory;

import java.util.Properties;
@Slf4j
public class MainContext {
  private final int standardProvideDelay = 1000;
  private final Properties contextProperties;
  //  final Map<String, SparePartSectionController> sparePartSectionControllers;
  final FactoryController factoryController;
  final DealerController dealerController;
  final EngineSectionController engineSectionController;
  final BodySectionController bodySectionController;
  final AccessoriesSectionController accessoriesSectionController;
  //  final Eng

  public MainContext(final Properties sparePartProperties, final Properties contextProperties) {
    log.info("init main context");
    this.contextProperties = contextProperties;

    SparePartFactory.Instance(sparePartProperties);
    this.engineSectionController = new EngineSectionController(standardProvideDelay, Integer.parseInt(this.contextProperties.get("engineRepoSize").toString()));
    this.bodySectionController = new BodySectionController(standardProvideDelay, Integer.parseInt(this.contextProperties.get("bodyRepoSize").toString()));
    this.accessoriesSectionController = new AccessoriesSectionController(
            standardProvideDelay,
            Integer.parseInt(this.contextProperties.get("accessoriesProviders").toString()),
            Integer.parseInt(this.contextProperties.get("accessoriesRepoSize").toString())
    );

    this.factoryController = new FactoryController(
            Integer.parseInt(this.contextProperties.get("factoryWorkersQuantity").toString()),
            Integer.parseInt(this.contextProperties.get("factoryRepoSize").toString()),
            Integer.parseInt(this.contextProperties.get("dealersQuantity").toString()),
            standardProvideDelay,
            this.bodySectionController.getRepository(),
            this.engineSectionController.getRepository(),
            this.accessoriesSectionController.getRepository()
    );

    this.dealerController = new DealerController(factoryController.getCarSupplier(), Integer.parseInt(this.contextProperties.get("dealersQuantity").toString()), standardProvideDelay);
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
