package server.model.connection_section;

import jakarta.persistence.*;
import org.slf4j.Logger;

import java.util.List;

public class ChatUsersDAO {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChatUsersDAO.class);
  private final EntityManagerFactory entityManagerFactory;

  public ChatUsersDAO() {
    entityManagerFactory = Persistence.createEntityManagerFactory("serverdb");
  }

  public Long saveAccount(ChatUserEntity accountEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    Long accountId = null;

    try {
      transaction.begin();
      entityManager.persist(accountEntity);
      transaction.commit();
      accountId = accountEntity.getUserId();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return accountId;
  }

  public ChatUserEntity findAccountById(Long userId) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    ChatUserEntity accountEntity = null;

    try {
      accountEntity = entityManager.find(ChatUserEntity.class, userId);
    } finally {
      entityManager.close();
    }

    return accountEntity;
  }

  public ChatUserEntity findAccountByUserName(String userName) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    ChatUserEntity accountEntity = null;

    try {
      TypedQuery<ChatUserEntity> query = entityManager.createQuery(
              "SELECT a FROM ChatUserEntity a WHERE a.userName = :userName", ChatUserEntity.class);
      query.setParameter("userName", userName);
      accountEntity = query.getSingleResult();
    } catch (NoResultException e) {
      log.error(e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return accountEntity;
  }

  public List<ChatUserEntity> findAllAccounts() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    List<ChatUserEntity> accounts = null;

    try {
      TypedQuery<ChatUserEntity> query = entityManager.createQuery("SELECT a FROM ChatUserEntity a", ChatUserEntity.class);
      accounts = query.getResultList();
    } finally {
      entityManager.close();
    }

    return accounts;
  }

  public void updateAccount(ChatUserEntity accountEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();

    try (entityManager) {
      transaction.begin();
      entityManager.merge(accountEntity);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      log.error(e.getMessage(), e);
    }
  }

  public void deleteAccount(Long userId) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();

    try (entityManager) {
      transaction.begin();
      ChatUserEntity accountEntity = entityManager.find(ChatUserEntity.class, userId);
      if (accountEntity != null) {
        entityManager.remove(accountEntity);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      log.error(e.getMessage(), e);
    }
  }

  public String updateProfilePictureByUserName(String userName, String mimeType, byte[] fileContent) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    String message = null;
    try (entityManager) {
      transaction.begin();
      TypedQuery<ChatUserEntity> query = entityManager.createQuery(
              "SELECT a FROM ChatUserEntity a WHERE a.userName = :userName", ChatUserEntity.class);
      query.setParameter("userName", userName);
      ChatUserEntity accountEntity = query.getSingleResult();
      if (accountEntity != null) {
        accountEntity.setAvatarMimeType(mimeType);
        accountEntity.setAvatar(fileContent);
        entityManager.merge(accountEntity);
      }
      transaction.commit();
    } catch (NoResultException e) {
      log.info("No account found with userName: " + userName);
      message = e.getMessage();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      message = e.getMessage();
      log.error(e.getMessage(), e);
    }
    return message;
  }

  public String deleteProfilePictureByUserName(String userName) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    String message = null;
    try (entityManager) {
      transaction.begin();
      TypedQuery<ChatUserEntity> query = entityManager.createQuery(
              "SELECT a FROM ChatUserEntity a WHERE a.userName = :userName", ChatUserEntity.class);
      query.setParameter("userName", userName);
      ChatUserEntity accountEntity = query.getSingleResult();
      if (accountEntity != null) {
        accountEntity.setAvatarMimeType(null);
        accountEntity.setAvatar(null);
        entityManager.merge(accountEntity);
      }
      transaction.commit();
    } catch (NoResultException e) {
      message = e.getMessage();
      log.info("No account found with userName: " + userName);
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      message = e.getMessage();
      log.error(e.getMessage(), e);
    }
    return message;
  }

  public String updateProfilePicture(Long userId, String mimeType, byte[] fileContent) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    String message = null;
    try (entityManager) {
      transaction.begin();
      ChatUserEntity accountEntity = entityManager.find(ChatUserEntity.class, userId);
      if (accountEntity != null) {
        accountEntity.setAvatarMimeType(mimeType);
        accountEntity.setAvatar(fileContent);
        entityManager.merge(accountEntity);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      log.error(e.getMessage(), e);
      message = e.getMessage();
    }
    return message;
  }

  public String deleteProfilePicture(Long userId) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    String message = null;

    try (entityManager) {
      transaction.begin();
      ChatUserEntity accountEntity = entityManager.find(ChatUserEntity.class, userId);
      if (accountEntity != null) {
        accountEntity.setAvatarMimeType(null);
        accountEntity.setAvatar(null);
        entityManager.merge(accountEntity);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      message = e.getMessage();
      log.error(e.getMessage(), e);
    }
    return message;
  }
}
