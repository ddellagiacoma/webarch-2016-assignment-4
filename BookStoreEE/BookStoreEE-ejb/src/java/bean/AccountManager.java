package bean;

import entity.Account;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.AccountInfo;

@Stateless
public class AccountManager implements AccountRemote {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public AccountInfo getAccountInfo(int accountId) {
        Account account = manager.find(Account.class, accountId);
        return account.getAccountInfo();
    }

    @Override
    public int register(String username, String pw) {

        Account account = new Account();
        account.setUsername(username);
        account.setPw(pw);
        manager.persist(account);
        
        Query query = manager.createQuery("select a.accountId from Account a where a.username=:username and a.pw=:pw");
        query.setParameter("username", username);
        query.setParameter("pw", pw);
         List<Integer> id = query.getResultList();
        
        return id.get(0);
    }

    @Override
    public int login(String username, String pw) {
        Query query = manager.createQuery("select a.accountId from Account a where a.username=:username and a.pw=:pw");
        query.setParameter("username", username);
        query.setParameter("pw", pw);

        List<Integer> id = query.getResultList();
      
        return id.get(0);
    }

}
