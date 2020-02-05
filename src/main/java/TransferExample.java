import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.client.Connection;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.transactions.Transaction;
import org.arkecosystem.crypto.transactions.builder.Transfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simple example of a transfer transaction using
 * java-crypto - https://github.com/ArkEcosystem/java-crypto and
 * java-client - https://github.com/KovacZan/java-client
* */
public class TransferExample {


    public static Transaction CreateTransferTransaction(int amount, String recipientAddress, String passphrase, long nonce) {
        // This is where we build out transfer transaction
        Transaction actual = new Transfer()
                .recipient(recipientAddress)
                .amount(amount)
                .nonce(nonce)
                .vendorField("Java \uD83D \uDD31 \uD83C \uDF7A")
                .sign(passphrase)
                .transaction;

        return actual;
    }
    public static long getNonce(Connection connection, String senderWallet) throws IOException {
        return Long.valueOf (((LinkedTreeMap<String, Object>) connection.api().wallets.show(senderWallet).get("data")).get("nonce").toString());
    }

    public static void main(String[] args) throws IOException {
        String recipientAddress = "D8rr7B1d6TL6pf14LgMz4sKp1VBMs6YUYD";
        String senderAddress = "DBoKkzKAT6YdXhyv1mvcKEg97JxiRqMuK2";
        String senderPassphrase = "blanket swing carpet interest assault mom you fault float cave category cradle";

        // This is where we configure our network and connect to the node
        Network.set(new Devnet());

        HashMap<String, Object> map = new HashMap<>();
        map.put("host", "http://137.74.27.246:4003/api/");
        map.put("content-type","application/json");
        Connection connection2 = new Connection(map);

        // This is where we get senders nonce and increment it by one
        long nonce = getNonce(connection2,senderAddress) + 1;

        ArrayList<HashMap> payload = new ArrayList<>();
        Transaction transfer1 = CreateTransferTransaction(10,
                        recipientAddress,
                        senderPassphrase,
                        nonce
                );
        payload.add(transfer1.toHashMap());

        // This is where we send our transaction to node
        LinkedTreeMap<String, Object> postResponse = connection2.api().transactions.create(payload);
        System.out.println(postResponse);

    }
}
