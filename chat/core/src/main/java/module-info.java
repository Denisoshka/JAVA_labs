module core {
  exports dto.subtypes;
  exports dto;
  exports dto.exceptions;
  exports dto.interfaces;
//  opens dto to jakarta.xml.bind;  // Открытие пакета для JAXB
//  opens dto to com.sun.xml.bind;
//  opens dto to jakarta.xml.bind;
  opens dto to org.glassfish.jaxb.runtime;
  requires java.xml;
  requires static lombok;
  requires jakarta.xml.bind;
  opens dto.subtypes to jakarta.xml.bind;
  exports io_processing;
  exports file_section;
  exports dto.subtypes.file;
  opens dto.subtypes.file to jakarta.xml.bind;
  exports dto.subtypes.login;
  opens dto.subtypes.login to jakarta.xml.bind;
  exports dto.subtypes.user_profile;
  exports dto.subtypes.message;
  exports dto.subtypes.list;
  exports dto.subtypes.logout;
}