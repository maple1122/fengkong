import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author wufeng
 * @date 2025/5/19 14:54
 */
public class RestClient {

    public CloseableHttpResponse get(String url) throws IOException {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpGet httpGet=new HttpGet(url);
        CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
        return httpResponse;
    }
}
