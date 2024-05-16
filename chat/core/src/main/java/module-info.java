module core {
  exports dto.subtypes;
  exports dto;
  exports dto.exceptions;
  exports dto.interfaces;
  opens dto to java.xml.bind;  // Открытие пакета для JAXB

  requires java.xml;
  requires java.xml.bind;
  requires static lombok;

  opens dto.subtypes to java.xml.bind;
  exports io_processing;

}