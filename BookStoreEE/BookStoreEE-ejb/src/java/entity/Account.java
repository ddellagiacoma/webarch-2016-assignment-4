package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.AccountInfo;

/**
 *
 * @author Daniele
 */
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    private String username;
    private String pw;

    public Account() {
        username = "";
        pw = "";
    }

    public AccountInfo getAccountInfo() {
        return new AccountInfo(username, pw);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

}
