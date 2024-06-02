package server.model.server_sections.file;

import jakarta.persistence.*;
import org.slf4j.Logger;

import java.util.List;

public class SmallFileDAO {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SmallFileDAO.class);
  private final EntityManagerFactory entityManagerFactory;

  public SmallFileDAO() {
    entityManagerFactory = Persistence.createEntityManagerFactory("serverdb");
  }

  public Long saveSmallFile(String fileName, String userName, String mimeType, int size, byte[] fileData) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = null;
    Long fileId = null;

    try {
      transaction = entityManager.getTransaction();
      transaction.begin();

      SmallFileEntity fileEntity = new SmallFileEntity(fileName, userName, mimeType, size, fileData);

      entityManager.persist(fileEntity);
      transaction.commit();

      fileId = fileEntity.getId();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return fileId;
  }

  public SmallFileEntity getFile(Long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    SmallFileEntity fileEntity = null;

    try {
      fileEntity = entityManager.find(SmallFileEntity.class, id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }
    return fileEntity;
  }

  public SmallFileEntity getFilePreview(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    SmallFileEntity file = null;

    try {
      Query query = entityManager.createQuery(
              "SELECT new SmallFileEntity (f.id, f.userName, f.fileName, f.mimeType, f.size, null) FROM SmallFileEntity f WHERE f.id = :id"
      );
      query.setParameter("id", id);
      file = (SmallFileEntity) query.getSingleResult();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }
    return file;
  }

  public List<SmallFileEntity> getFilesPreview() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    List<SmallFileEntity> files = null;

    try {
      Query query = entityManager.createQuery(
              "SELECT new SmallFileEntity(f.id, f.userName, f.fileName, f.mimeType, f.size, null) FROM SmallFileEntity f"
      );
      files = (List<SmallFileEntity>) query.getResultList();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return files;
  }


}
