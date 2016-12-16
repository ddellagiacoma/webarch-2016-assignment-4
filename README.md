# Accessing a DB through an EJB Layer

## 1. INTRODUCTION

The goal of this assignment is to develop an Enterprise JavaBean (EJB) which allows users to perform some
typical operation of online stores and saves information into a DB.

Moreover, two different client have been developed, one for administrator and one for regular users.
The administrator client **BookStoreAdministrator** allows to add a new book to the DB, specifying title and
price, and to list all of buying operation performed by users.

On the other hand, the user client **BookStoreClient** requires users to register or login to the db at first.
After that the client allows them to add books to the cart, buy all the books present in the cart, empty the
cart and see the content of the cart.

However, the purpose of the assignment is to correctly identify where to use stateful beans, stateless
beans and entities, using at least one of each sort.

## 2. IMPLEMENTATION

The Enterprise Application **BookStoreEE** is composed by the EJB module **BookStoreEE-ejb**. The EJB module
includes all the beans, interfaces, entities and classes needed to work correctly.

For example, the **AccountManager** bean provides the **register(username, pw)** method to create a new user
into the DB and return his/her ID, the **login(username, pw)** method to return the ID of a user already
present in the DB and the **getAccountInfo(accountID)** method to return username and password of the
user. This is the implementation of the **login(username, pw)** method:
```java
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
```
The **OperationManager** bean implements the operation the administrator can perform such as
**addBook(title, price)** and **listOperation()**. The **listOperation()** method returns a list of book bought and by
whom:
```java
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
```
Finally, the **ShoppingCartManager** stateful bean includes all the remote methods that concerns the user
cart mentioned above. For example, the **buy()** method is used to buy all the books present in the cart. The
method register a new Operation with the ID of the buyer and the ID of the book. After that it set the
availability of each book bought as false in the DB.

```java
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
```
Moreover, the application include three entity **Book**, **Account** and **Operation** in order to handle the DB.

Finally, the **AccountInfo** class is used only to simplify the access to the information (username and
password) of the users.

On the other hand, the client **BookStoreAdministrator** has only a remote interface named
**OperationRemote** and the main class. First of all, the client establishes a connection to WildFly and
connects to the **OperationManager** bean. After that, it asks user whether he/she wants to add a new book
into the DB or view all the buying operations performed:

```java
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
```
The BookStoreClient connects to the **AccountManager** and **OperationManager** beans. First of all, the user
can log in or register to the DB. After that he/she can choose from some different option:

```java
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
```
Furthermore, the **persistence.xml** file is essential to connect to the DB:
```xml
<persistence-unit name="manager">
		<jta-data-source>java:/BookStoreDS</jta-data-source>
		<properties>
			<property name="hibernate.hbm2ddl.auto" value="update"/>
		</properties>
   </persistence-unit>
```

## 3. EXPLANATION
Three different entities have been used in this assignment. **Book** entity is used to store all the book added
by the administrator. It has the following fields: a primary key **id**, a string **title**, a string **price** and a boolean
**available** (which is set to false when the book is sold).

The **Account** entity is used to store all the users. It has the primary key **accountId**, the **username** and the
**password**.

Finally, the **Operation** entity is used to store which book has been bought and by whom. It has a primary
key **id**, a **costumerID** and a **bookID**.

Moreover, each entity has its get and set methods.

Two stateless bean have been used to complete the assignment. The first one, **AccountManager**, has the
task of registering new users and logging in the existent users. The other stateless bean,
**OperationManager**, has instead the task of adding new books into the DB and listing all the buying
operations.

In this case, the stateless beans were used because they don’t need to maintain state across method
invocations.

The only stateful bean used is the **ShoppingCartManager**. It was used to manage the operations of the
shopping cart, maintaining the state of its instance variables (i.e. List\<Book> **books**).

## 4. DEPLOYMENT

To run the application is important configure correctly the environment.

First of all, it is necessary to run Apache Derby typing the following command:

**java –jar DERBY_HOME/lib/derbyrun.jar server start &**
![image](https://cloud.githubusercontent.com/assets/24565161/21267731/d7ba5020-c3ab-11e6-83bb-35fabd7f8229.png)

After that, it is necessary to create a new JDBC Datasource in WildFly using the **derbyclient.jar** driver.
![image](https://cloud.githubusercontent.com/assets/24565161/21267739/db330d28-c3ab-11e6-8ddd-5028e4b06893.png)

Moreover, the connection URL ensure the connection to Derby and the creation of a DB named
**BookStoreDB** whether it is not already present.

![image](https://cloud.githubusercontent.com/assets/24565161/21267744/dea6bf0e-c3ab-11e6-9018-d588f1da1514.png)

Now the ear file **BookStoreEE.ear** have to be copied in the deployments folder of WildFly. Launching the
file **standalone.bat** WildFly starts and deploys the file.

The admin client can be start simply typing this in the terminal:

**java -jar BookStoreAdministrator.jar**

And then the interface allows to perform the administration operations:
![image](https://cloud.githubusercontent.com/assets/24565161/21267747/e22b28d6-c3ab-11e6-8a9b-dc906c042397.png)

The user client works at the same way. It can be start typing this in the terminal and the

**java -jar BookStoreClient.jar**
![image](https://cloud.githubusercontent.com/assets/24565161/21267749/e6871cd2-c3ab-11e6-9393-29845e972048.png)

## 5. COMMENTS AND NOTES

I had several issues during the initial configuration of the environment. In fact, I spent a lot of time trying to
figure it out. Although the program works correctly, I didn’t have enough time to optimize it and add more
controls.
