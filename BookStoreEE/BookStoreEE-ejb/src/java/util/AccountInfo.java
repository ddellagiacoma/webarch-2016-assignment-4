
package util;

import java.io.Serializable;

public class AccountInfo implements Serializable {
    
    private final String username;
    private final String pw;


    public AccountInfo(String username, String pw) {
        this.username = username;
        this.pw = pw;
    }

    public String getUsername() {
        return username;
    }

    public String getPw() {
        return pw;
    }
}