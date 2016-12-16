package bookstoreadministrator;

import bean.OperationRemote;
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
public class BookStoreAdministrator {

    public static void main(String[] args) {
        try {

            // set up for widfly access
            Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            jndiProperties.put("jboss.naming.client.ejb.context", true);

            final Context ctx = new InitialContext(jndiProperties);
      
            //look up the administrator remote interface
            OperationRemote or = (OperationRemote) ctx.lookup("BookStoreEE/BookStoreEE-ejb/OperationManager!bean.OperationRemote");

            //administrator options
            while (true) {
                Scanner in = new Scanner(System.in);
                System.out.println("Do you want add a new book (a), list the operations (b) or exit (c)?");
                String answer = in.next();
                switch (answer) {
                    //add a new book
                    case "a":
                        insertBook(or);
                        break;
                     //list buying operation performed
                    case "b":
                        List<String> list = or.listOperation();
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(list.get(i));
                        }
                        break;
                    //exit
                    case "c":
                        exit(0);
                }
            }
        } catch (NamingException ex) {
            System.err.println("Look up failed: " + ex);
        }
    }

    //insert title and price of the new book
    public static void insertBook(OperationRemote or) {
        Scanner in = new Scanner(System.in);

        System.out.println("Insert title: ");
        String title = in.nextLine();
        System.out.println("Insert price: ");
        String price = in.next();
        or.addBook(title, price);
        System.out.println("BOOK ADDED");
    }
}
