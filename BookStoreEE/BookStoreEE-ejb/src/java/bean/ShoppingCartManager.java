package bean;

import entity.Book;
import entity.Operation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Daniele
 */
@Stateful
public class ShoppingCartManager implements ShoppingCartRemote {

    @PersistenceContext
    private EntityManager em;

    int costumerId;
    List<Book> books;

    @Override
    public void getCart(int id) {
        costumerId = id;
        books = new ArrayList<>();
    }

    @Override
    public List<String> list() {
        Query query = em.createQuery("SELECT b FROM Book b WHERE b.available=true");
        List<Book> list = query.getResultList();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add("ID: " + list.get(i).getID() + " TITLE: " + list.get(i).getTitle() + " PRICE: " + list.get(i).getPrice());
        }
        return result;
    }

    @Override
    public void addToCart(int bookID) {
        Book book = em.find(Book.class, bookID);
        books.add(book);
    }

    @Override
    public void buy() {
        Book editBook;
        for (Book book : books) {
            Operation operation = new Operation();
            operation.setbookID(book.getID());
            operation.setcostumerID(Integer.toString(costumerId));
            em.persist(operation);
            editBook = em.find(Book.class, Integer.parseInt(book.getID()));
            editBook.setAvailable(Boolean.FALSE);
            em.merge(editBook);
        }
        books.clear();
    }

    @Override
    public List<String> getContents() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            result.add("ID: " + books.get(i).getID() + " TITLE: " + books.get(i).getTitle() + " PRICE: " + books.get(i).getPrice());
        }
        return result;

    }

    @Override
    public void leave() {
        books.clear();
    }
}