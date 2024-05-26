package server.model.connection_section;

//import jakarta.persistence.Entity;
import jakarta.persistence.*;
@Entity
@Table(name = "accounts")
public class ChatUserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "password_hash", nullable = false)
  int passwordHash;

  @Column(name = "avatar_mime_type")
  private String avatarMimeType;

  @Column(name = "avatar")
  private byte[] avatar;

  public ChatUserEntity() {
  }

  public ChatUserEntity(String userName, int passwordHash) {
    this.userName = userName;
    this.passwordHash = passwordHash;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(int passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getAvatarMimeType() {
    return avatarMimeType;
  }

  public void setAvatarMimeType(String avatarMimeType) {
    this.avatarMimeType = avatarMimeType;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public void setAvatar(byte[] avatar) {
    this.avatar = avatar;
  }
}
