module core {
  exports dto.subtypes;
  exports dto;
  exports dto.exceptions;
  exports dto.interfaces;

  requires java.xml;
  requires java.xml.bind;

  opens dto.subtypes to java.xml.bind;
  exports io_processing;

}