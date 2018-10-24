package com.klarna.rest.api.order_management;

import com.klarna.rest.FakeHttpUrlConnectionTransport;
import com.klarna.rest.TestCase;
import com.klarna.rest.model.order_management.Capture;
import com.klarna.rest.model.order_management.CaptureObject;
import com.klarna.rest.model.order_management.Refund;
import com.klarna.rest.model.order_management.RefundObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RefundsApiTest extends TestCase {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private FakeHttpUrlConnectionTransport transport;

    @Before
    public void setUp() {
        transport = new FakeHttpUrlConnectionTransport();
    }

    @Test
    public void testCreate() throws IOException {
        when(transport.conn.getResponseCode()).thenReturn(201);
        when(transport.conn.getHeaderFields()).thenReturn(new HashMap<String, List<String>>(){{
            put("Content-Type", new ArrayList<String>(){
                {
                    add(MediaType.APPLICATION_JSON);
                }
            });
            put("Location", new ArrayList<String>(){
                {
                    add("https://example.com/new-location");
                }
            });
            put("Refund-Id", new ArrayList<String>(){
                {
                    add("new-refund-id");
                }
            });
        }});

        RefundObject data = new RefundObject(){
            {
                setRefundedAmount(100L);
                setDescription("test");
            }
        };

        RefundsApi api = new RefundsApi(transport, "my-order-id");
        String refundId = api.create(data);

        verify(transport.conn, times(1)).setRequestMethod("POST");
        assertEquals("/ordermanagement/v1/orders/my-order-id/refunds", transport.requestPath);
        assertEquals("new-refund-id", refundId);

        final String requestPayout = transport.requestPayout.toString();
        assertTrue(requestPayout.contains("\"refunded_amount\":100"));

        // Location header should be set up
        when(transport.conn.getResponseCode()).thenReturn(200);
        api.fetch(); // Fetch by location header
        assertEquals("https://example.com/new-location", transport.requestPath);
    }

    @Test
    public void testFetchById() throws IOException {
        when(transport.conn.getResponseCode()).thenReturn(200);
        when(transport.conn.getHeaderFields()).thenReturn(new HashMap<String, List<String>>(){{
            put("Content-Type", new ArrayList<String>(){
                {
                    add(MediaType.APPLICATION_JSON);
                }
            });
        }});

        final String payload = "{ \"refund_id\": \"4ba29b50-be7b-44f5-a492-113e6a865e22\", \"refunded_amount\": 100 }";
        when(transport.conn.getInputStream()).thenReturn(this.makeInputStream(payload));

        RefundsApi api = new RefundsApi(transport, "my-order-id");
        Refund refund = api.fetch("my-refund-id");

        assertEquals("4ba29b50-be7b-44f5-a492-113e6a865e22", refund.getRefundId());
        Long amount = 100L;
        assertEquals(amount, refund.getRefundedAmount());
        verify(transport.conn, times(1)).setRequestMethod("GET");
        assertEquals("/ordermanagement/v1/orders/my-order-id/refunds/my-refund-id", transport.requestPath);
    }
}
