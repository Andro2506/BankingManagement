# Banking Management System

A complete Banking Management System built as a **Java Dynamic Web Project** using
Servlets, JSP, JDBC, and SQLite. Styled with Bootstrap 5.

It covers every requirement from the spec:

- **UI features (US001 - US007):** Employee registration & login, customer CRUD,
  transaction processing, loan management.
- **Additional Servlet/JSP modules:** NetBanking login (Servlet + JSP),
  Customer DB login, Simple customer CRUD (Servlet + JSP).
- **Java/DB modules:** Collection-based customer data manager, exception handling
  demo, account inheritance (Savings/Checking), fund transfer, balance inquiry.
- **SQLite DB modules:** Employee table seeding + Clerk/Manager salary updates,
  Customer DB ops (US001/US002/US003), Transaction DB ops (US004/US005/US006).
- **Unix/file operations:** Loads `customers.txt` into SQLite and runs the four
  required queries.

---

## Tech stack

| Component | Version |
|-----------|---------|
| Java EE   | Servlet 3.1, JSP 2.3 |
| Database  | SQLite (`banking.db` is auto-created) |
| JDBC      | sqlite-jdbc 3.7.2 |
| Server    | Apache Tomcat 9.0.71 |
| UI        | Bootstrap 5 (CDN) |

---

## Project layout

```
BankingManagement/
├── .project, .classpath, .settings/   <- Eclipse Dynamic Web Project metadata
├── src/                               <- Java source code
│   └── com/bank/
│       ├── util/                      <- DBConnection, DatabaseInitializer
│       ├── model/                     <- Employee, Customer, Transaction, Loan POJOs
│       ├── dao/                       <- EmployeeDAO, CustomerDAO, TransactionDAO, LoanDAO
│       ├── servlet/                   <- All UI and additional-module servlets
│       └── javadb/                    <- Standalone Java/DB demo classes
├── WebContent/
│   ├── WEB-INF/
│   │   ├── lib/                       <- sqlite-jdbc-3.7.2.jar lives here
│   │   └── web.xml                    <- Servlet mappings, listener, error page
│   ├── *.jsp                          <- Every JSP page
│   ├── netbanking_login.html          <- HTML form for NetBanking servlet
│   └── css/style.css
├── lib/                               <- Optional copy of the SQLite jar for IDE
└── README.md
```

---

## Setup

### 1. Add the SQLite JDBC driver

Download the jar:
<https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.7.2/sqlite-jdbc-3.7.2.jar>

Place a copy at:
```
WebContent/WEB-INF/lib/sqlite-jdbc-3.7.2.jar
```
(also at `lib/sqlite-jdbc-3.7.2.jar` if you want IDE auto-completion)

### 2. Import into Eclipse

1. **File -> Import -> Existing Projects into Workspace**
2. Browse to the `BankingManagement/` folder and click *Finish*.
3. Right-click the project -> **Properties -> Targeted Runtimes -> Apache Tomcat v9.0**
4. Right-click the project -> **Properties -> Java Build Path -> Libraries** -> verify
   `WebContent/WEB-INF/lib/sqlite-jdbc-3.7.2.jar` is there.

### 3. Configure Tomcat 9

- Window -> Preferences -> Server -> Runtime Environments -> *Add*
- Pick **Apache Tomcat v9.0** and point it at your Tomcat 9.0.71 install.

### 4. Run

- Right-click `BankingManagement` -> **Run As -> Run on Server** -> pick Tomcat 9.0.
- Eclipse opens the browser at:

  http://localhost:8080/BankingManagement/

The first request triggers `DatabaseInitializer` which:

- creates the four tables (`employee`, `customer`, `customer_transactions`, `loan`)
- seeds **10 demo records** in each
- applies the **Clerk +1000** and **Manager +10%** salary updates

---

## Default credentials (seeded)

After the first run, you can log in with any of the demo employees.
The first seeded employee gets ID **1000001**:

| Employee ID | Password | Designation |
|-------------|----------|-------------|
| 1000001     | pass123  | Clerk       |
| 1000002     | pass123  | Manager     |
| ...         | pass123  | ...         |

For customer login (Servlet US002 module): use any 7-digit SSN like `1800001`
with password `cust123`.

For NetBanking login (Servlet US001 / JSP US003): use the predefined password
`netbank123`.

---

## URL map

| URL pattern | Description |
|-------------|-------------|
| `/`                       | Landing page (`index.jsp`) |
| `/register`               | Employee registration (US001) |
| `/login` / `/logout`      | Employee login (US002) / logout |
| `/home.jsp`               | Employee dashboard |
| `/customers`              | Customer list view |
| `/customer/register`      | Register new customer (US003) |
| `/customer/edit?ssn=...`  | Edit customer (US004) |
| `/customer/delete?ssn=...`| Delete customer (US005) |
| `/transactions`           | Transaction list |
| `/transaction`            | New transaction (US006) |
| `/loans`                  | Loan list (US007) |
| `/loan`                   | Initiate new loan (US006 loan) |
| `/loan/edit?id=...`       | Edit/approve loan (US007) |
| `/loan/delete?id=...`     | Delete loan |
| `/netbanking_login.html`  | NetBanking login form (Servlet US001) |
| `/netbankingLogin`        | Servlet endpoint that validates predefined password |
| `/netbanking_login_jsp.jsp` | Same module via JSP (US003) |
| `/customer_login.jsp`     | Customer login form |
| `/customerLogin`          | Servlet that validates against `customer` table (US002), redirects to dashboard |
| `/customer_home.jsp`      | **Customer dashboard** (after login) - balance, account info, quick actions |
| `/customer/transaction`   | Customer makes a deposit / withdraw on their own account |
| `/customer/loan`          | Customer applies for a loan |
| `/customer/my-transactions` | Customer views their own transaction history |
| `/customer/my-loans`      | Customer tracks their own loan applications |
| `/customer/logout`        | Customer logout (invalidates session) |
| `/simpleCustomer`         | Simple customer CRUD via Servlet (US004) |
| `/simple_customer_jsp.jsp`| Simple customer CRUD via JSP (US005) |

---

## Standalone console programs (Java/DB modules)

You can run these directly from `src/`. They share the same `banking.db` file
that the web app uses. Compile first (or run inside Eclipse with **Run As ->
Java Application**).

```bash
java -cp build/classes:WebContent/WEB-INF/lib/sqlite-jdbc-3.7.2.jar \
     com.bank.javadb.CustomerCollectionManager   # US001 - in-memory list
java -cp ... com.bank.javadb.ExceptionDemo         # US002 - exception handling
java -cp ... com.bank.javadb.AccountInheritanceDemo # US003 - inheritance
java -cp ... com.bank.javadb.FundTransfer          # US004 - fund transfer
java -cp ... com.bank.javadb.BalanceInquiry        # US005 - balance inquiry

java -cp ... com.bank.javadb.EmployeeDBDemo        # SQLite DB - Employee
java -cp ... com.bank.javadb.CustomerDBDemo        # SQLite DB - Customer
java -cp ... com.bank.javadb.TransactionDBDemo     # SQLite DB - Transactions

java -cp ... com.bank.javadb.UnixFileDemo \
     src/com/bank/javadb/customers.txt             # Unix file ops
```

The four required Unix shell equivalents are listed in
`UnixFileDemo.java` Javadoc.

---

## Notes

- Passwords are stored in plain text (project scope).
- All servlets use `HttpSession` for login state and redirect to `login.jsp`
  if the session is invalid.
- DAO classes manage their own JDBC `Connection`, `PreparedStatement`, and
  `ResultSet`, closing each in `finally` blocks.
- The code is intentionally written in a beginner-friendly style: plain `if/else`,
  classic `for` loops, no streams or lambdas.
