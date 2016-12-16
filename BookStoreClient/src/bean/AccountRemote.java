/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.ejb.Remote;
import util.AccountInfo;

/**
 *
 * @author Daniele
 */
@Remote
public interface AccountRemote {
    AccountInfo getAccountInfo(int accountId);

    int register(String username,String pw);
    int login(String username,String pw); 
}
