package dispatchlabs;

import dispatchlabs.states.Account;
import dispatchlabs.states.Receipt;
import dispatchlabs.states.Node;
import dispatchlabs.states.Transaction;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Http;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class Sdk {

    /**
     * Class level-declarations.
     */
    private String seedNodeIp;
    private Receipt receipt;

    /**
     * @throws Exception
     */
    public Sdk(String seedNodeIp) throws Exception {
        this.seedNodeIp = seedNodeIp;
    }

    /**
     * @return
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<Node> getDelegates() throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + seedNodeIp + ":1975/v1/delegates", getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return AJson.deserializeList(Node.class, jsonObject.get("data").toString());
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public Account createAccount() throws Exception {
        return Account.create();
    }

    /**
     * @param node
     * @param privateKey
     * @param from
     * @param to
     * @param tokens
     * @return
     * @throws Exception
     */
    public Receipt transferTokens(Node node, String privateKey, String from, String to, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(privateKey, from, to, Transaction.Type.TRANSFER_TOKENS, String.valueOf(tokens), "", "", "", System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }

    /**
     * @param node
     * @param fromAccount
     * @param toAccount
     * @param tokens
     * @return
     * @throws Exception
     */
    public Transaction transferTokens(Node node, Account fromAccount, Account toAccount, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), toAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, String.valueOf(tokens), "", "", "", System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + node.getHttpEndpoint().getHost()  + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions", getHeaders(), transaction.toString()));
            //receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return transaction;
        }
    }

    /*
    curl -X POST http://localhost:1175/v1/transactions -d '
    {
    		"hash":"0bb8c64779b0ab9d04b84b1d33d8cff40d4802c91cea815afcbee21b06895254",
    		"type":0,
    		"from":"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",
    		"to":"",
    		"value":"0",
    		"code":"6060604052600160005534610000575b6101168061001e6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806329e99f07146046578063cb0d1c76146074575b6000565b34600057605e6004808035906020019091905050608e565b6040518082815260200191505060405180910390f35b34600057608c6004808035906020019091905050609d565b005b6000816000540290505b919050565b806000600082825401925050819055507ffa753cb3413ce224c9858a63f9d3cf8d9d02295bdb4916a594b41499014bb57f6000546040518082815260200191505060405180910390a15b505600a165627a7a723058203f0887849cabeb36c6f72cc345c5ff3521d889356357e6815dd8dbe9f7c41bbe0029",
    		"method":"",
    		"time":1526859114441,
    		"signature":"62411c9fe1b084ed6ccbe1f5a2ffc89484abce8fc7b482948d8012ab0e462b866a96d19bfd48f304c2d906c3ab950758c1d28ae7aba14e1bfb29ec8949967b4c01",
    		"hertz":"0",
    		"fromName":"",
    		"toName":""
    	}'
     */
    public Receipt deployContract(Node node, Account fromAccount, String code) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(
                    fromAccount.getPrivateKey(),
                    fromAccount.getAddress(),
                    "",
                    Transaction.Type.SMART_CONTRACT,
                    "0",
                    code,
                    "",
                    "",
                    System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }
    
    /*
	curl -X POST http://localhost:1175/v1/transactions -d '
	{
		"hash":"91bbda817e38dccf939a5131d500ce110498d13c47d4dfde4ce3af558c82114d",
		"type":0,
		"from":"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",
		"to":"c3be1a3a5c6134cca51896fadf032c4c61bc355e",
		"value":10,
		"code":"5b7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a2274657374222c0a090922636f6e7374616e74223a747275652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c2274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b7b0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a2276696577220a097d2c7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a22746573744173796e63222c0a090922636f6e7374616e74223a66616c73652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a226e6f6e70617961626c65220a097d2c7b0a09092274797065223a226576656e74222c0a0909226e616d65223a224c6f63616c4368616e6765222c0a090922616e6f6e796d6f7573223a66616c73652c0a090922696e70757473223a5b7b0a09090922696e6465786564223a66616c73652c0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d0a097d5d22",
		"method":"test",
		"time":1526859280944,
		"signature":"fc5ee5c86a7eb3fea2b7f6dc3d417ef2c87e18a5c107dfd7faf051c11dd22bf53918d8f926cdbe251624e3459e458711d1e3e76542f1d225c170279b52aee99f00",
		"hertz":"0",
		"fromName":"",
		"toName":""
	}'
     */
    public Receipt executeContract(Node node, Account fromAccount, String contractAddress, String method, String params) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(
                    fromAccount.getPrivateKey(),
                    fromAccount.getAddress(),
                    contractAddress,
                    Transaction.Type.SMART_CONTRACT,
                    "0",
                    "",
                    method,
                    params,
                    System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }

    /**
     * @param node
     * @param address
     * @return
     * @throws Exception
     */
    public Account getAccount(Node node, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/accounts/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return (Account) AJson.deserialize(Account.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param node
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactions(Node node) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions", getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param node
     * @param address
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactionsByFromAddress(Node node, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions/from/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param node
     * @param address
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactionsByToAddress(Node node, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + node.getHttpEndpoint().getHost() + ":" + String.valueOf(node.getHttpEndpoint().getPort()) + "/v1/transactions/to/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public Receipt getLastStatus(Node node, String hash) throws Exception {
        if (receipt == null) {
            return null;
        }
        return getStatus(node, hash);
    }

    /**
     * @return
     * @throws Exception
     */
    public Receipt getStatus(Node node, String hash) throws Exception {
        try (Http http = new Http()) {
            // TODO: receipt.getNodeIp() is returning null. Plus, port 1975 shouldn't be hardcoded here.
            JSONObject jsonObject = new JSONObject(http.get("http://" + node.getHttpEndpoint().getHost() + ":" + node.getHttpEndpoint().getPort() + "/v1/transactions/" + hash, getHeaders()));
            this.receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return this.receipt;
        }
    }

    /**
     * @param genesisAccount
     * @return
     * @throws Exception
     */
    public String createGenesisTransactionString(Account genesisAccount, long tokens) throws Exception {
        Account fromAccount = Account.create();
        Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), genesisAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, String.valueOf(tokens), "", "", "", 0);
        return transaction.toString();
    }

    /**
     * @return @throws Exception
     */
    private Map<String, String> getHeaders() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("Dispatch Labs SDK Example");
        try {
            Sdk sdk = new Sdk("localhost");
            List<Node> nodes = sdk.getDelegates();

            
            Account fromAccount = sdk.createAccount();
            Account toAccount = sdk.createAccount();
            Transaction transaction = sdk.transferTokens(nodes.get(0), fromAccount, toAccount, 45);
            

            //Receipt receipt = sdk.transferTokens(nodes.get(0), "0f86ea981203b26b5b8244c8f661e30e5104555068a4bd168d3e3015db9bb25a", "3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c", "1dcfccb29a15aa5bfb70ce944c745eb421d04bb5", 69);
            Receipt receipt;
            
            // Pending?
            while ((receipt = sdk.getLastStatus(nodes.get(0), transaction.getHash())).getStatus().equals(Receipt.Status.PENDING)) {
                Thread.sleep(200);
                System.out.println(receipt.getStatus());
            }

            System.out.println(receipt.getStatus());

        } catch (Throwable t) {
            System.out.println(t);
        }
    }
}
