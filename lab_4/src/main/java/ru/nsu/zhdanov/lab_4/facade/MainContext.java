package ru.nsu.zhdanov.lab_4.facade;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.*;
import ru.nsu.zhdanov.lab_4.model.AccessoriesModel;
import ru.nsu.zhdanov.lab_4.model.BodyModel;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartFactory;

import java.util.Properties;

@Slf4j
public class MainContext {
  private final FactoryModel factoryModel;
  private final DealerModel dealerModel;
    private final EngineModel engineSectionModel;
  private final BodyModel bodySectionModel;
  private final AccessoriesModel accessoriesSectionController;

  public MainContext(final Properties sparePartProperties, final Properties contextProperties) {
    log.info("init main context");
    SparePartFactoryInterface factory = new SparePartFactory(sparePartProperties);
    this.engineSectionModel = new EngineModel(
            factory, Integer.parseInt(contextProperties.get("engineProviderDelay").toString()),
            Integer.parseInt(contextProperties.get("engineRepoSize").toString())
    );
    this.bodySectionModel = new BodyModel(
            factory, Integer.parseInt(contextProperties.get("bodyProviderDelay").toString()),
            Integer.parseInt(contextProperties.get("bodyRepoSize").toString())
    );
    this.accessoriesSectionController = new AccessoriesModel(
            factory,
            Integer.parseInt(contextProperties.get("accessoriesProviderDelay").toString()),
            Integer.parseInt(contextProperties.get("accessoriesProvidersQuantity").toString()),
            Integer.parseInt(contextProperties.get("accessoriesRepoSize").toString())
    );
    this.factoryModel = new FactoryModel(
            Integer.parseInt(contextProperties.get("factoryWorkersQuantity").toString()),
            Integer.parseInt(contextProperties.get("factoryRepoSize").toString()),
            Integer.parseInt(contextProperties.get("dealersQuantity").toString()),
            Integer.parseInt(contextProperties.get("factoryDelay").toString()),
            this.bodySectionModel.getRepository(),
            this.engineSectionModel.getRepository(),
            this.accessoriesSectionController.getRepository()
    );
    this.dealerModel = new DealerModel(
            factoryModel.getCarSupplier(),
            Integer.parseInt(contextProperties.get("dealersQuantity").toString()),
            Integer.parseInt(contextProperties.get("dealerDelay").toString())
    );
  }

  public int getCarsQuantity() {
    int x = factoryModel.getRepositoryOccupancy();
    log.info("call factoryController.getRepositoryOccupancy():" + x);
    return x;
  }

  public void perform() {
    log.info("perform work");
    factoryModel.perform();
    dealerModel.perform();
    engineSectionModel.perform();
    bodySectionModel.perform();
    accessoriesSectionController.perform();
  }

  public void shutdown() {
    log.info("shut down controller");
    factoryModel.shutdown();
    dealerModel.shutdown();
    engineSectionModel.shutdown();
    bodySectionModel.shutdown();
    accessoriesSectionController.shutdown();
  }

  public EngineModel getEngineSectionModel() {
    return engineSectionModel;
  }

  public BodyModel getBodySectionModel() {
    return bodySectionModel;
  }

  public AccessoriesModel getAccessoriesSectionController() {
    return accessoriesSectionController;
  }
}
