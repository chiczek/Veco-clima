package pl.vecoclima.other;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.vecoclima.data.entity.ShoppingCart;

import java.io.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RestApiService {

    String tokenTest = "a4266cb4-6e36-4545-a006-1d8b74f39a4f";
    final String tokenProduction = "fc6eaaa6-fd79-61f6-b6c4a21975c33f80";

    public String createJson (ShoppingCart cart) {

        JSONArray products = new JSONArray();

        AtomicInteger total = new AtomicInteger();
        cart.getShoppingCartLines().forEach(line -> {
            JSONObject product = new JSONObject()
                    .put("name", line.getProduct().getName())
                    .put("unitPrice", (int) (line.getPriceGross().doubleValue() * 100) )
                    .put("quantity", line.getAmount());
            total.addAndGet((int) (line.getPriceGross().doubleValue() * 100 * line.getAmount()));
            products.put(product);
        });


        String json = new JSONObject()
                .put("customerIp", "127.0.0.1")
                .put("notifyUrl", "https://veco-clima.pl/paymentStatus")
                .put("merchantPosId", "469935")
                .put("description", "Cart: " + cart.getId())
                .put("currencyCode", "PLN")
                .put("totalAmount", total.get())
                .put("continueURL", "https://veco-clima.pl/kontakt")
                //.put("extOrderId", "Mój numer zamowienia " + LocalDate.now().hashCode())
                .put("buyer", new JSONObject()
                        .put("email", cart.getCreatedBy().getEmail())
                        .put("phone", cart.getCreatedBy().getPhone())
                        .put("firstName", cart.getCreatedBy().getFirstName())
                        .put("lastName", cart.getCreatedBy().getLastName())
                        .put("language", "pl")
                )
                .put("products", products)
                    .toString();
        return json;
    }

    public String submit(ShoppingCart cart) throws UnsupportedEncodingException {
        final String url = "https://secure.snd.payu.com/api/v2_1/orders";
        final HttpPost httpPost = new HttpPost(url);


        System.out.println("---------------");

        httpPost.addHeader("Content-Type", "application/json");
        String token = getTokenFromPayU();
        httpPost.addHeader("Authorization", "Bearer " + token);
        httpPost.addHeader("continueURL", "https://veco-clima.pl/kontakt");

        StringEntity stringEntity = new StringEntity(createJson(cart));
        httpPost.getRequestLine();
        httpPost.setEntity(stringEntity);

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String retSrc = EntityUtils.toString(entity);
            JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
            System.out.println(result);
            UI.getCurrent().getPage().setLocation(result.get("redirectUri").toString());
        } catch (IOException e) {
            tokenTest = getTokenFromPayU();
            e.printStackTrace();
        }

        return "Failed";

    }

    public String getTokenFromPayU() throws UnsupportedEncodingException {
        final String url = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";
        final HttpPost httpPost = new HttpPost(url);
        String id = "469935";
        String secret = "316ad2c42b7cd2a639a44c40bf1e2ec3";
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity stringEntity = new StringEntity("grant_type=client_credentials&client_id="+id+"&client_secret="+secret);
        httpPost.getRequestLine();
        httpPost.setEntity(stringEntity);

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String retSrc = EntityUtils.toString(entity);
            JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
            System.out.println("RES1: " + result);
            String token = (result.get("access_token").toString());
            System.out.println("TOKEN: " + token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Failed";

    }

    @RequestMapping(value = "/paymentStatus", method = RequestMethod.POST)
    public ResponseEntity<Object> createProduct(@RequestBody String reply) {
        System.out.println("-----------REPLY-----------");
        System.out.println(reply);
        System.out.println("-----------END REPLY-----------");


        return new ResponseEntity<>("okPayment", HttpStatus.OK);
    }

}
