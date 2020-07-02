package wf.bitcoin.javabitcoindrpcclient;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.ScanObject;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.UnspentTxOutput;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.UtxoSet;
import wf.bitcoin.krotjson.JSON;

/**
 * Created by fpeters on 11-01-17.
 */

public class BitcoinJSONRPCClientTest {

    class MyClientTest extends BitcoinJSONRPCClient {

        String expectedMethod;
        Object[] expectedObject;
        String result;

        MyClientTest(boolean testNet, String expectedMethod, Object[] expectedObject, String result) {
            super(testNet);
            this.expectedMethod = expectedMethod;
            this.expectedObject = expectedObject;
            this.result = result;
        }

        @Override
        public Object query(String method, Object... o) throws GenericRpcException {
            if(method!=expectedMethod) {
                throw new GenericRpcException("wrong method");
            }
            if(o.equals(expectedObject)){
                throw new GenericRpcException("wrong object");
            }
            return JSON.parse(result);
        }
    }

    MyClientTest client;

    @Test
    public void signRawTransactionTest() throws Exception {
        client = new MyClientTest(false, "signrawtransaction", null,
                                    "{\n" +
                                            "  \"hex\": \"0100000001b8b2244faca910c1ffff24ecd2b559b4699338398bf77e4cb1fdeb19ad419ea0010000006b483045022100b68b7fe9cfabb32949af6747b6769dffcf2aa4170e4df2f0e9d0a4571989e94e02204cf506c210cdb6b6b4413bf251a0b57ebcf1b1b2d303ba6183239b557ef0a310012102ab46e1d7b997d8094e97bc06a21a054c2ef485fac512e2dc91eb9831af55af4effffffff012e2600000000000017a9140b2d7ed4e5076383ba8e98b9b3bce426b7a2ea1e8700000000\",\n" +
                                            "  \"complete\": true\n" +
                                            "}\n");
        LinkedList<BitcoindRpcClient.ExtendedTxInput> inputList = new LinkedList<BitcoindRpcClient.ExtendedTxInput>();
        LinkedList<String> privateKeys = new LinkedList<String>();
        privateKeys.add("cSjzx3VAM1r9iLXLvL6N61oS3zKns9Z9DcocrbkEzesPTDHWm5r4");
        String hex = client.signRawTransaction("0100000001B8B2244FACA910C1FFFF24ECD2B559B4699338398BF77E4CB1FDEB19AD419EA0010000001976A9144CB4C3B90994FEF58FABB6D8368302E917C6EFB188ACFFFFFFFF012E2600000000000017A9140B2D7ED4E5076383BA8E98B9B3BCE426B7A2EA1E8700000000",
                                                inputList, privateKeys, "ALL");
        assertEquals("0100000001b8b2244faca910c1ffff24ecd2b559b4699338398bf77e4cb1fdeb19ad419ea0010000006b483045022100b68b7fe9cfabb32949af6747b6769dffcf2aa4170e4df2f0e9d0a4571989e94e02204cf506c210cdb6b6b4413bf251a0b57ebcf1b1b2d303ba6183239b557ef0a310012102ab46e1d7b997d8094e97bc06a21a054c2ef485fac512e2dc91eb9831af55af4effffffff012e2600000000000017a9140b2d7ed4e5076383ba8e98b9b3bce426b7a2ea1e8700000000",
                    hex);
    }

    @Test
    public void signRawTransactionTestException() throws Exception {
        client = new MyClientTest(false, "signrawtransaction", null,
                "{\n" +
                        "  \"hex\": \"0100000001b8b2244faca910c1ffff24ecd2b559b4699338398bf77e4cb1fdeb19ad419ea00100000000ffffffff012e2600000000000017a9140b2d7ed4e5076383ba8e98b9b3bce426b7a2ea1e8700000000\",\n" +
                        "  \"complete\": false,\n" +
                        "  \"errors\": [\n" +
                        "    {\n" +
                        "      \"txid\": \"a09e41ad19ebfdb14c7ef78b39389369b459b5d2ec24ffffc110a9ac4f24b2b8\",\n" +
                        "      \"vout\": 1,\n" +
                        "      \"scriptSig\": \"\",\n" +
                        "      \"sequence\": 4294967295,\n" +
                        "      \"error\": \"Operation not valid with the current stack size\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}");
        LinkedList<BitcoindRpcClient.ExtendedTxInput> inputList = new LinkedList<BitcoindRpcClient.ExtendedTxInput>();
        LinkedList<String> privateKeys = new LinkedList<String>();
        try {
            client.signRawTransaction("0100000001B8B2244FACA910C1FFFF24ECD2B559B4699338398BF77E4CB1FDEB19AD419EA0010000001976A9144CB4C3B90994FEF58FABB6D8368302E917C6EFB188ACFFFFFFFF012E2600000000000017A9140B2D7ED4E5076383BA8E98B9B3BCE426B7A2EA1E8700000000",
                    inputList, privateKeys, "ALL");
        }
        catch(Exception e) {
            assertThat(e.getMessage(), is("Incomplete"));
        }
    }

    @Test
    public void signRawTransactionTest2() throws Exception {
        client = new MyClientTest(false, "signrawtransaction", null,
                "{\n" +
                        "  \"hex\": \"0100000001b8b2244faca910c1ffff24ecd2b559b4699338398bf77e4cb1fdeb19ad419ea0010000006b483045022100b68b7fe9cfabb32949af6747b6769dffcf2aa4170e4df2f0e9d0a4571989e94e02204cf506c210cdb6b6b4413bf251a0b57ebcf1b1b2d303ba6183239b557ef0a310012102ab46e1d7b997d8094e97bc06a21a054c2ef485fac512e2dc91eb9831af55af4effffffff012e2600000000000017a9140b2d7ed4e5076383ba8e98b9b3bce426b7a2ea1e8700000000\",\n" +
                        "  \"complete\": true\n" +
                        "}\n");
        String hex = client.signRawTransaction("0100000001B8B2244FACA910C1FFFF24ECD2B559B4699338398BF77E4CB1FDEB19AD419EA0010000001976A9144CB4C3B90994FEF58FABB6D8368302E917C6EFB188ACFFFFFFFF012E2600000000000017A9140B2D7ED4E5076383BA8E98B9B3BCE426B7A2EA1E8700000000");
        assertEquals("0100000001b8b2244faca910c1ffff24ecd2b559b4699338398bf77e4cb1fdeb19ad419ea0010000006b483045022100b68b7fe9cfabb32949af6747b6769dffcf2aa4170e4df2f0e9d0a4571989e94e02204cf506c210cdb6b6b4413bf251a0b57ebcf1b1b2d303ba6183239b557ef0a310012102ab46e1d7b997d8094e97bc06a21a054c2ef485fac512e2dc91eb9831af55af4effffffff012e2600000000000017a9140b2d7ed4e5076383ba8e98b9b3bce426b7a2ea1e8700000000",
                    hex);
    }

    @Test
    public void scanTxOutSetTest() {
      ScanObject scanObject1 = new ScanObject("addr(mtoffFXQWh6YNP86TRsRETNn9nDaMmsKsL)", null);
      ScanObject scanObject2 = new ScanObject("addr(mi11rWuB14Eb2L5tpdqfD77DGMhschQdgx)", null);
      List<ScanObject> list = Arrays.asList(scanObject1, scanObject2);

      String json = "{\n" + 
          "  \"success\": true,\n" + 
          "  \"searched_items\": 22462153,\n" + 
          "  \"unspents\": [\n" + 
          "    {\n" + 
          "      \"txid\": \"6415d590f46344a6f72c0e1544eb183a5ac3d8ff9a2ab48435f3255794af3915\",\n" + 
          "      \"vout\": 0,\n" + 
          "      \"scriptPubKey\": \"76a9141b3edeb7188b1cef9996e81ae22b68dfb3f7806688ac\",\n" + 
          "      \"amount\": 0.00900000,\n" + 
          "      \"height\": 1442023\n" + 
          "    },\n" + 
          "    {\n" + 
          "      \"txid\": \"2d3bb59ba7bf690b43f604d7289e76534a9a32e92dd4f1945413a59832fe0723\",\n" + 
          "      \"vout\": 0,\n" + 
          "      \"scriptPubKey\": \"76a91491c2d21b865e338794bc92326de5dd0c15663d8788ac\",\n" + 
          "      \"amount\": 0.00300000,\n" + 
          "      \"height\": 1441179\n" + 
          "    },\n" + 
          "    {\n" + 
          "      \"txid\": \"b6573ad024dd97172238712a8d417e39ff9fbeb15e35bbae447b86966503289b\",\n" + 
          "      \"vout\": 1,\n" + 
          "      \"scriptPubKey\": \"76a91491c2d21b865e338794bc92326de5dd0c15663d8788ac\",\n" + 
          "      \"amount\": 0.00200000,\n" + 
          "      \"height\": 1440923\n" + 
          "    }\n" + 
          "  ],\n" + 
          "  \"total_amount\": 0.01400000\n" + 
          "}\n" + 
          "";

      client = new MyClientTest(false, "scantxoutset", new Object[] { "start", list }, json);
      UtxoSet utxoSet = client.scanTxOutSet(list);
      assertEquals(22462153, utxoSet.searchedItems().intValue());
      assertEquals(new BigDecimal("0.01400000"), utxoSet.totalAmount());
      assertEquals(3, utxoSet.unspents().size());
      UnspentTxOutput utxo = utxoSet.unspents().get(0);
      assertEquals("6415d590f46344a6f72c0e1544eb183a5ac3d8ff9a2ab48435f3255794af3915", utxo.txid());
      assertEquals(0, utxo.vout().intValue());
    }

    @Test
    public void transactionCategoryTest()
    {
        String result = "{\n" +
                "  \"amount\": -1.00000000,\n" +
                "  \"fee\": -0.00003320,\n" +
                "  \"confirmations\": 0,\n" +
                "  \"trusted\": true,\n" +
                "  \"txid\": \"e59e42353c0f2f5565f7935547b35c4fa98ab574920cc76e5abbc0c60ae704a6\",\n" +
                "  \"walletconflicts\": [\n" +
                "  ],\n" +
                "  \"time\": 1593556785,\n" +
                "  \"timereceived\": 1593556785,\n" +
                "  \"bip125-replaceable\": \"no\",\n" +
                "  \"details\": [\n" +
                "    {\n" +
                "      \"address\": \"2NBFSxR84pvjGbw4LAE2mxzPEniCbHHJVEj\",\n" +
                "      \"category\": \"send\",\n" +
                "      \"amount\": -1.00000000,\n" +
                "      \"vout\": 1,\n" +
                "      \"fee\": -0.00003320,\n" +
                "      \"abandoned\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"hex\": \"0200000000010189cf7cbed220051d2629a79f4b9eae178987ae680f7c38340cc8c8fb1d7a41640100000017160014e53ae3a87e6d00806ed75e27f6dfd2ac578f7c69feffffff0210161a1e0100000017a914676b8deb459d16b58d6f5b63c113a59067381f0e8700e1f5050000000017a914c57d16ce6f56128f5d530bd4e736ac45f6addfea870247304402206ae351a4a28cbf8f6d9d118d18aac6fa68074a990936aa6f2e7b6a3f25ac3f8402204059516e166ba6fdded053deb68130fb614a9cfea7f40fcde11d85f7b4f6f9c901210246d78f0fd3aba9bbbe24431cdcefd9cc9be1dc72d378985de3759e7d521cd6d100000000\"\n" +
                "}\n";

        client = new MyClientTest(false, "gettransaction", null, result);
        BitcoindRpcClient.Transaction transaction = client.getTransaction("e59e42353c0f2f5565f7935547b35c4fa98ab574920cc76e5abbc0c60ae704a6");
        String category = transaction.category();

        assertEquals(category, "send");
    }
}