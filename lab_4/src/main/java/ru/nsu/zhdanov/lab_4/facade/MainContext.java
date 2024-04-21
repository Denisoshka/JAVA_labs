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
  private final DealerModel dealerModel;
  private final FactoryModel factoryModel;
  private final BodyModel bodyModel;
  private final EngineModel engineModel;
  private final AccessoriesModel accessoriesModel;

  public MainContext(final Properties sparePartProperties, final Properties contextProperties) {
    SparePartFactoryInterface factory = new SparePartFactory(sparePartProperties);
    this.engineModel = new EngineModel(
            factory, Integer.parseInt(contextProperties.get("engineProviderDelay").toString()),
            Integer.parseInt(contextProperties.get("engineRepoSize").toString())
    );
    this.bodyModel = new BodyModel(
            factory, Integer.parseInt(contextProperties.get("bodyProviderDelay").toString()),
            Integer.parseInt(contextProperties.get("bodyRepoSize").toString())
    );
    this.accessoriesModel = new AccessoriesModel(
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
            this.bodyModel.getRepository(),
            this.engineModel.getRepository(),
            this.accessoriesModel.getRepository()
    );
    this.dealerModel = new DealerModel(
            factoryModel.getCarSupplier(),
            Integer.parseInt(contextProperties.get("dealersQuantity").toString()),
            Integer.parseInt(contextProperties.get("dealerDelay").toString())
    );
  }

  public void perform() {
    factoryModel.perform();
    dealerModel.perform();
    engineModel.perform();
    bodyModel.perform();
    accessoriesModel.perform();
  }

  public void shutdown() {
    factoryModel.shutdown();
    dealerModel.shutdown();
    engineModel.shutdownNow();
    bodyModel.shutdownNow();
    accessoriesModel.shutdownNow();
  }

  public int getCarsQuantity() {
    return factoryModel.getRepositoryOccupancy();
  }

  public FactoryModel getFactoryModel() {
    return factoryModel;
  }

  public EngineModel getEngineModel() {
    return engineModel;
  }

  public BodyModel getBodyModel() {
    return bodyModel;
  }

  public AccessoriesModel getAccessoriesModel() {
    return accessoriesModel;
  }
}
