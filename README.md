# Dispatch Labs Java SDK 

## Prerequisites
- Java 1.8 or higher
- Maven 3.5.0 or higher

# ![](https://storage.googleapis.com/material-icons/external-assets/v4/icons/svg/ic_directions_run_black_24px.svg) Setup and Run
- `git clone https://github.com/dispatchlabs/java-sdk.git`<br>
- `cd java-sdk`<br>
if macOS:
- `brew install automake`<br>
- `brew install libtool`<br>
if Unix:
- `apt-get install autoreconf`<br>
- `apt-get install libtool`<br>
then:
- `sh build.sh`<br>
- Remember to set `-Djava.library.path="...secp256k1/.libs"` in your VM Arguments

## Sample

~~~java
        System.out.println("Dispatch Labs SDK Example");
        try {
            Sdk sdk = new Sdk("10.0.1.2");
            List<Contact> nodes = sdk.getDelegates();
            Account fromAccount = sdk.createAccount();
            Account toAccount = sdk.createAccount();
            Receipt receipt = sdk.transferTokens(nodes.get(0), fromAccount, toAccount, 45);
            System.out.println(receipt.getStatus());

            // Pending?
            while ((receipt = sdk.getLastStatus()).getStatus().equals(Receipt.Status.PENDING)) {
                Thread.sleep(100);
            }
            System.out.println(receipt.getStatus());

            // Get transactions.
            List<Transaction> transactions = sdk.getTransactions(nodes.get(0));
            System.out.println(transactions);
        } catch (Throwable t) {
            System.out.println(t);
        }
~~~

