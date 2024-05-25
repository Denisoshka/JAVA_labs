module server_application {
  requires static lombok;

  requires core;
  requires org.apache.commons.cli;
  requires org.slf4j;
  requires java.xml;
  requires org.apache.commons.collections4;
  requires jakarta.persistence;

//  requires java.persistence;
  requires org.hibernate.orm.core;
  requires java.sql;

  opens server.model.server_sections.file to org.hibernate.orm.core;
  opens server.model.connection_section to org.hibernate.orm.core;
//  opens server.model.server_sections.file to org.hibernate.orm.core;
}