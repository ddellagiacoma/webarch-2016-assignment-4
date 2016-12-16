package bookstoreclient;

import bean.AccountRemote;
import bean.ShoppingCartRemote;

import static java.lang.System.exit;
import java.util.List;

import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Daniele
 */
public class BookStoreClient {

    public static void main(String[] args) {
        try {

            // set up for widfly access
            Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            jndiProperties.put("jboss.naming.client.ejb.context", true);

            final Context ctx = new InitialContext(jndiProperties);

            // look up accountmanager bean
            AccountRemote am = (AccountRemote) ctx.lookup("BookStoreEE/BookStoreEE-ejb/AccountManager!bean.AccountRemote");

            ShoppingCartRemote sc = (ShoppingCartRemote) ctx.lookup("BookStoreEE/BookStoreEE-ejb/ShoppingCartManager!bean.ShoppingCartRemote");

            //the user need to log in or register
            Scanner in = new Scanner(System.in);
            System.out.println("Do you want register (a) or log in (b)?");
            String answer = in.next();
            //user id
            int id = 0;
            switch (answer) {
                case "a":
                    id = registration(am);
                    sc.getCart(id);
                    break;
                case "b":
                    id = login(am);
                    sc.getCart(id);
                    break;
            }

            //the list of all the books available
            System.out.println("These are the books available:");
            List<String> list = sc.list();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }

            while (true) {
                System.out.println("Do you want to add books to the cart (a), buy the cart (b), empty the cart (c), see the content of the cart (d) or exit (e)?");
                answer = in.next();

                switch (answer) {
                    //add book to cart
                    case "a":
                        addBook(sc);
                        break;
                    //buy all books in the cart
                    case "b":
                        sc.buy();
                        System.out.println("Thank you for buying!");
                        exit(0);
                    //empty the cart
                    case "c":
                        sc.leave();
                        break;
                    //show the content of the cart
                    case "d":
                        list = sc.getContents();
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(list.get(i));
                        }
                        break;
                    //exit
                    case "e":
                        exit(0);
                }
            }
        } catch (NamingException ex) {
            System.err.println("Look up failed: " + ex);
        }
    }

    //register a new account user
    public static int registration(AccountRemote am) {

        Scanner in = new Scanner(System.in);
        System.out.println("Insert username: ");
        String username = in.next();
        System.out.println("Insert password: ");
        String pw = in.next();
        int id = am.register(username, pw);
        System.out.println("Succesfully registred as " + am.getAccountInfo(id).getUsername() + "!");
        return id;
    }

    public static int login(AccountRemote am) {

        Scanner in = new Scanner(System.in);
        System.out.println("Insert username: ");
        String username = in.next();
        System.out.println("Insert password: ");
        String pw = in.next();
        int id = am.login(username, pw);
        System.out.println("\nHello " + am.getAccountInfo(id).getUsername() + ". Welcome back!");
        return id;

    }

    //add the book with the specified id to the cart
    public static void addBook(ShoppingCartRemote sc) {

        Scanner in = new Scanner(System.in);
        System.out.println("Type the ID of the book that you want to add to the cart: ");
        String book = in.next();
        sc.addToCart(Integer.parseInt(book));
    }
}