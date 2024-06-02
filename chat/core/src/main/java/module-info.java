module core {
  exports dto;
  exports dto.exceptions;
  exports dto.interfaces;
  exports io_processing;
  exports file_section;
  exports dto.subtypes.file;
  exports dto.subtypes.list;
  exports dto.subtypes.other;
  exports dto.subtypes.login;
  exports dto.subtypes.logout;
  exports dto.subtypes.message;
  exports dto.subtypes.user_profile;

  requires java.xml;
  requires jakarta.xml.bind;
  requires static lombok;

  opens dto.subtypes.file to jakarta.xml.bind;
  opens dto.subtypes.list to jakarta.xml.bind;
//  opens dto.subtypes.other to jakarta.xml.bind;
  opens dto.subtypes.login to jakarta.xml.bind;
  opens dto.subtypes.logout to jakarta.xml.bind;
  opens dto.subtypes.message to jakarta.xml.bind;
  opens dto.subtypes.user_profile to jakarta.xml.bind;
  opens dto.subtypes.other to org.glassfish.jaxb.runtime;
}