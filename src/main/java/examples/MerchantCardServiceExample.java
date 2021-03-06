package examples;

import com.klarna.rest.Client;
import com.klarna.rest.api.merchant_card_service.VirtualCreditCardApi;
import com.klarna.rest.api.merchant_card_service.model.CardServiceSettlementResponse;
import com.klarna.rest.api.merchant_card_service.model.CardServiceSettlementRequest;
import com.klarna.rest.http_transport.HttpTransport;
import com.klarna.rest.model.ApiException;

import java.io.IOException;

public class MerchantCardServiceExample {
    public static class RetrieveExistingSettlementExample {
        /**
         * Runs the example code.
         *
         * @param args Command line arguments
         */
        public static void main(final String[] args) {
            final String username = "K123456_abcd12345";
            final String password = "sharedSecret";
            final String settlementId = "12345";
            final String keyId = "abcde";

            Client client = new Client(username, password, HttpTransport.EU_TEST_BASE_URL);
            VirtualCreditCardApi vccApi = client.newVirtualCreditCardApi();

            try {
                CardServiceSettlementResponse settlement = vccApi.retrieveExistingSettlement(settlementId, keyId);
                System.out.println(settlement);

            } catch (IOException e) {
                System.out.println("Connection problem: " + e.getMessage());
            } catch (ApiException e) {
                System.out.println("API issue: " + e.getMessage());
            }
        }
    }

    public static class RetriveOrdersSettlementsExample {
        /**
         * Runs the example code.
         *
         * @param args Command line arguments
         */
        public static void main(final String[] args) {
            final String username = "K123456_abcd12345";
            final String password = "sharedSecret";
            final String orderId = "12345";
            final String keyId = "abcde";

            Client client = new Client(username, password, HttpTransport.EU_TEST_BASE_URL);
            VirtualCreditCardApi vccApi = client.newVirtualCreditCardApi();

            try {
                CardServiceSettlementResponse settlement = vccApi.retrieveSettledOrderSettlement(orderId, keyId);
                System.out.println(settlement);

            } catch (IOException e) {
                System.out.println("Connection problem: " + e.getMessage());
            } catch (ApiException e) {
                System.out.println("API issue: " + e.getMessage());
            }
        }
    }

    public static class CreateSettlementExample {
        /**
         * Runs the example code.
         *
         * @param args Command line arguments
         */
        public static void main(final String[] args) {
            final String username = "K123456_abcd12345";
            final String password = "sharedSecret";
            final String orderId = "12345";
            final String keyId = "abcde";

            Client client = new Client(username, password, HttpTransport.EU_TEST_BASE_URL);
            VirtualCreditCardApi vccApi = client.newVirtualCreditCardApi();

            try {
                CardServiceSettlementRequest request = new CardServiceSettlementRequest(){
                    {
                        setKeyId(keyId);
                        setOrderId(orderId);
                    }
                };
                CardServiceSettlementResponse settlement = vccApi.createSettlement(request);
                System.out.println(settlement);

            } catch (IOException e) {
                System.out.println("Connection problem: " + e.getMessage());
            } catch (ApiException e) {
                System.out.println("API issue: " + e.getMessage());
            }
        }
    }
}
