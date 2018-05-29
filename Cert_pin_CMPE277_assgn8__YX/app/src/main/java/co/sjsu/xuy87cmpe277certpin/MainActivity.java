package co.sjsu.xuy87cmpe277certpin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import co.sjsu.xuy87cmpe277certpin.retrofit.httpServ;
import co.sjsu.xuy87cmpe277certpin.retrofit.User;
import de.greenrobot.event.EventBus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PINNED_URL = "https://api.github.com";

    private static final String UNAUTHORIZED_URL = "https://www.oracle.com";

    private static final String USER = "xuy87";

    private static final char[] STORE_PASS = new char[]{'t', 'e', 's', 't', 'i', 'n', 'g'};

    private static final String REQ_HEADER_NAME = "User-Agent";

    private static final String REQ_HEADER_VALUE = "hello-pinnedcerts";

    private TextView mTv_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv_Status = findViewById(R.id.statusTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_pinned_cert_request:
                makePinCertRequest();
                return true;
            case R.id.action_not_allowed:
                makeForbiddenRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makePinCertRequest() {
        HttpGet req = new HttpGet(PINNED_URL);
        req.addHeader(REQ_HEADER_NAME, REQ_HEADER_VALUE);
        EventBus.getDefault().post(req);
    }

    public void onEventAsync(HttpGet request) {
        try {
            DefaultHttpClient client = new HttpClientBuilder()
                    .setHttpPort(80)
                    .setHttpsPort(443)
                    .setCookieStore(new BasicCookieStore())
                    .pinCertificates(getResources(), R.raw.keystore, STORE_PASS)
                    .build();

            HttpResponse response = client.execute(request);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("request", request);
            data.put("response", response);
            EventBus.getDefault().post(data);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void onEventMainThread(Map<String, Object> data) {
          HttpGet request = (HttpGet) data.get("request");
          HttpResponse response = (HttpResponse) data.get("response");

        mTv_Status.setText("Pinned cert http request:\n"+request.getURI().toString()+" "+response.getStatusLine().toString());
    }

    public void onEventMainThread(String e) {
        mTv_Status.setText("Pinned cert http request exception: \n"+e);
    }

    private void makeForbiddenRequest() {
        try {
          OkClient client = new RetrofitClientBuilder()
              .pinCertificates(getResources(), R.raw.keystore, STORE_PASS)
              .build();

          RestAdapter adapter = new RestAdapter.Builder()
              .setEndpoint(UNAUTHORIZED_URL)
              .setLogLevel(RestAdapter.LogLevel.FULL)
              .setClient(client)
              .build();

          httpServ httpServ = adapter.create(httpServ.class);

          httpServ.getCallBack(USER, new Callback<User>() {

            @Override
            public void success(User user, Response response) {
              mTv_Status.setText("Unauthorized visit:\n"+ UNAUTHORIZED_URL +"\n"+response.getStatus() + "\n"+response.getHeaders() + "\n"+response.getBody() + "\n" + response.getReason());
            }

            @Override
            public void failure(RetrofitError error) {
              mTv_Status.setText("Unauthorized visit:\n"+ UNAUTHORIZED_URL + " "+error.getMessage());
            }
          });
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException | UnrecoverableKeyException | KeyManagementException e) {
          e.printStackTrace();
        }
    }
}
