import com.google.gson.internal.LinkedTreeMap;
import org.arkecosystem.client.Connection;
import org.arkecosystem.crypto.configuration.Network;
import org.arkecosystem.crypto.networks.Devnet;
import org.arkecosystem.crypto.transactions.builder.MultiPaymentBuilder;
import org.arkecosystem.crypto.transactions.types.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simple example of a MultiPayment transaction using
 * java-crypto - https://github.com/ArkEcosystem/java-crypto and
 * java-client - https://github.com/ArkEcosystem/java-client
 * */

public class MultiPaymentExample {

    static long getNonce(Connection connection, String senderWallet) throws IOException {
        // This is where we get nonce scrape out nonce from api
        return Long.valueOf (((LinkedTreeMap<String, Object>) connection.api().wallets.show(senderWallet).get("data")).get("nonce").toString());
    }

    public static void main(String[] args) throws IOException {
        // Here we initialize data for multiPayment
        String senderWalletAddress = "DBoKkzKAT6YdXhyv1mvcKEg97JxiRqMuK2";
        String senderPassphrase = "blanket swing carpet interest assault mom you fault float cave category cradle";
        // recipient 1
        String recipient1 = "DGLf3ARUbBsfw4y1CLhwxGY1G4oEmixNAC";
        // recipient 2
        String recipient2 = "DJjJNRnDB34KALnNsHh3huRmqcSUvaoa9v";
        // recipient 3
        String recipient3 = "D6fnPjL5Jchn7ct9EfZc34mBSmg9kyPPmy";

        // This is where we configure our network and connect to the node
        Network.set(new Devnet());

        // This is where we make configurations for connection
        HashMap<String, Object> map = new HashMap<>();
        map.put("host", "https://dexplorer.ark.io/api/");
        map.put("content-type","application/json");
        // Here we make a connection to configured node
        Connection connection2 = new Connection(map);

        // This is where we get senders nonce and increment it by one
        long nonce = getNonce(connection2,senderWalletAddress) + 1;

        // Here we initialize payload
        ArrayList<HashMap> payload = new ArrayList<>();

        // This is where we call our Create method and get our transfer transaction
        // This is where we build our MultiPayment transaction
        MultiPaymentBuilder multiPaymentBuilder = new MultiPaymentBuilder()
                .nonce(nonce)
                .vendorField("Learn-ARK Example JAVA - Multipayment");// not necessary to add this

        // This is where we add additional 64 payments
        for (int i = 0; i < 64; i++) {
            multiPaymentBuilder.addPayment(recipient1, i+1);// amount of arktoshis we want to send
        }

        // Here we sign our transaction
        Transaction transaction = multiPaymentBuilder
                .sign(senderPassphrase)
                .transaction;

        // Here we add transaction to payload -it is possible to add multiple transaction to payload
        payload.add(transaction.toHashMap());

        // This is where we send our transaction to node
        LinkedTreeMap<String, Object> postResponse = connection2.api().transactions.create(payload);
        System.out.println(postResponse);
    }
}
