package ru.vivt.applicationmvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class StartActivity extends AppCompatActivity {
    private static final String serverIP = "servermvp.ru:49207";//"servermvp.ru:49207"; // for test 10.0.2.2:8082
                                                        // for prod servermvp.ru:49207

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread thread = new Thread(() -> {{
            Server server = Server.getInstance(serverIP);
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            MemoryValues memoryValues = MemoryValues.init(getApplicationContext());
            String tokenInMemory = memoryValues.getToken();
            if (tokenInMemory != null) {
                server.setTokenConnection(tokenInMemory);
            } else {
                requestQueue.add(server.registration2(response -> {
                    System.out.println(response);
                    try {
                        server.saveDataInMemory(memoryValues, response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));
                requestQueue.add(server.getQrCode(response -> {
                    try {
                        memoryValues.setQrCode(response.getString("qrCode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));
            }

//            if (!server.tokenActive()) {
//                server.registration();
//            }

            server.saveDataInMemory(memoryValues);

            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }});

        thread.start();
    }
}