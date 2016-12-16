/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.Account;
import entity.Book;
import entity.Operation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Daniele
 */
@Stateless
public class OperationManager implements OperationRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addBook(String title, String price) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setPrice(price);
        em.persist(newBook);
    }

    @Override
    public List<String> listOperation() {
        Query query = em.createQuery("SELECT o FROM Operation o");
        List<Operation> list = query.getResultList();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Book book = em.find(Book.class, Integer.parseInt(list.get(i).getbookID()));
            book.getTitle();
            Account account = em.find(Account.class, Integer.parseInt(list.get(i).getcostumerID()));
            result.add(account.getAccountInfo().getUsername() + " bought " + book.getTitle());
        }
        return result;
    }
}
