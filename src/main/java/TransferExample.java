import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.client.Connection;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.transactions.builder.TransferBuilder;
import org.arkecosystem.crypto.transactions.types.Transaction;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simple example of a transfer transaction using
 * java-crypto - https://github.com/ArkEcosystem/java-crypto and
 * java-client - https://github.com/ArkEcosystem/java-client
* */
public class TransferExample {


    public static Transaction CreateTransferTransaction(int amount, String recipientAddress, String passphrase, long nonce) {
        // This is where we build our transfer transaction
        Transaction actual = new TransferBuilder()
                .recipient(recipientAddress)
                .amount(amount)
                .nonce(nonce)
                .vendorField("Java \uD83D \uDD31 \uD83C \uDF7A") // not necessary to add this
                .sign(passphrase)
                .transaction;

        return actual;
    }
    public static long getNonce(Connection connection, String senderWallet) throws IOException {
        // This is where we get nonce scrape out nonce from api
        return Long.valueOf (((LinkedTreeMap<String, Object>) connection.api().wallets.show(senderWallet).get("data")).get("nonce").toString());
    }

    public static void main(String[] args) throws IOException {
        // Here we initialize recipientAddress, senderAddress and senderPassphrase
        String recipientAddress = "D8rr7B1d6TL6pf14LgMz4sKp1VBMs6YUYD";
        String senderAddress = "DBoKkzKAT6YdXhyv1mvcKEg97JxiRqMuK2";
        String senderPassphrase = "blanket swing carpet interest assault mom you fault float cave category cradle";

        // This is where we configure our network and connect to the node
        Network.set(new Devnet());

        // This is where we make configurations for connection
        HashMap<String, Object> map = new HashMap<>();
        map.put("host", "https://dexplorer.ark.io/api/");
        map.put("content-type","application/json");
        // Here we make a connection to configured node
        Connection connection2 = new Connection(map);

        // This is where we get senders nonce and increment it by one
        long nonce = getNonce(connection2,senderAddress) + 1;

        // Here we initialize payload
        ArrayList<HashMap> payload = new ArrayList<>();

        // This is where we call our Create method and get our transfer transaction
        Transaction transfer1 = CreateTransferTransaction(10, // amount of arktoshis we want to send
                        recipientAddress,
                        senderPassphrase,
                        nonce
                );
        // Here we add transaction to payload -it is possible to add multiple transaction to payload
        payload.add(transfer1.toHashMap());

        // This is where we send our transaction to node
        LinkedTreeMap<String, Object> postResponse = connection2.api().transactions.create(payload);
        System.out.println(postResponse);

    }
}
