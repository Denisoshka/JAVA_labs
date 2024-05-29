module core {
  exports dto;
  exports dto.exceptions;
  exports dto.interfaces;
  exports io_processing;
  exports file_section;
  exports dto.subtypes.file;
  exports dto.subtypes.login;
  exports dto.subtypes.user_profile;
  exports dto.subtypes.message;
  exports dto.subtypes.list;
  exports dto.subtypes.logout;

  requires static lombok;
  requires java.xml;
  requires jakarta.xml.bind;

  opens dto to org.glassfish.jaxb.runtime;
  opens dto.subtypes.file to jakarta.xml.bind;
  opens dto.subtypes.list to jakarta.xml.bind;
  opens dto.subtypes.logout to jakarta.xml.bind;
  opens dto.subtypes.message to jakarta.xml.bind;
  opens dto.subtypes.user_profile to jakarta.xml.bind;
  opens dto.subtypes.login to jakarta.xml.bind;
  exports dto.subtypes.other;
}