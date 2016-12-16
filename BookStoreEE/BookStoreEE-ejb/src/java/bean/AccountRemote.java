package bean;

import javax.ejb.Remote;
import util.AccountInfo;

@Remote
public interface AccountRemote {

    AccountInfo getAccountInfo(int accountId);

    int login(String username, String pw);

    int register(String username, String pw);
}
