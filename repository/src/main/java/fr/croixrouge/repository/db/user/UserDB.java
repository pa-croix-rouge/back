package fr.croixrouge.repository.db.user;

import fr.croixrouge.domain.model.ID;
import jakarta.persistence.*;

@Table(name = "user")
@Entity
public class UserDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public UserDB(ID id, String username, String password) {
        this.userID = id.value();
        this.username = username;
        this.password = password;
    }

    public UserDB() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserID() {
        return userID;
    }
}
