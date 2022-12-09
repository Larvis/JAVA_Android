package kz.talipovsn.json_micro;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;


import android.widget.ImageView;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private TextView textView; // Компонент для отображения данных
    private View card;
    private LayoutInflater inflater;
    String url = "https://api.github.com/users/proffix4/starred"; // Адрес получения JSON - данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ЭТОТ КУСОК КОДА НЕОБХОДИМ ДЛЯ ТОГО, ЧТОБЫ ОТКРЫВАТЬ САЙТЫ С HTTPS!
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        // ----------------------------------------------------------------------

        // Разрешаем запуск в общем потоке выполнеия длительных задач (например, чтение с сети)
        // ЭТО ТОЛЬКО ДЛЯ ПРИМЕРА, ПО-НОРМАЛЬНОМУ НАДО ВСЕ В ОТДЕЛЬНЫХ ПОТОКАХ
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        textView = findViewById(R.id.textView);

        LinearLayout LL = (LinearLayout) findViewById(R.id.master);
        inflater = getLayoutInflater();



        getAllData(LL, inflater);
//        onClick(null); // Нажмем на кнопку "Обновить"
    }
//
    // Получить данные "Обновить"
    public void getAllData(LinearLayout LL, LayoutInflater inflater) {
//        textView.setText(R.string.not_data);

        String json = getHTMLData(url);
        if (json != null) {
            JSONArray allData = null;

            try {
                allData = new JSONArray(json);

                for(int i = 0; i < allData.length(); i++) {
                    JSONObject obj = allData.getJSONObject(i);
                    //store your variable
                        String j_name = obj.getString("name");
                        String j_full_name = obj.getString("full_name");
                        String j_language = obj.getString("language");
                        String j_forks = obj.getString("forks");
                        String j_watchers = obj.getString("watchers");

                    String j_stargazers_count = obj.getString("stargazers_count");
                    String j_avatar_url = obj.getJSONObject("owner").getString("avatar_url");
                    String j_url = obj.getJSONObject("owner").getString("html_url");

//                return;
                ///////-----------------------
//
//                "name":"FarManager",
//                        "full_name":"FarGroup/FarManager",
//                        "language":"C++",
//                        "forks":172,
//                        "watchers":1388,
//                        "stargazers_count":1388,
//
//                        "owner":{
//                    "avatar_url":"https://avatars.githubusercontent.com/u/3636093?v=4",
//                            "url":"https://api.github.com/users/FarGroup",


                        card = inflater.inflate(R.layout.card_widget, null);


                        ImageView avatar = (ImageView) card.findViewById(R.id.avatar);       // Аватар
                        TextView name = (TextView) card.findViewById(R.id.name);// Имя
                        TextView full_name = (TextView) card.findViewById(R.id.full_name); // Полное имя
                        TextView language = (TextView) card.findViewById(R.id.language); // Язык
                        TextView forks = (TextView) card.findViewById(R.id.forks);   // Форки
                        TextView watchers = (TextView) card.findViewById(R.id.watchers);  // Звезды
                        TextView stargazers_count = (TextView) card.findViewById(R.id.stargazers_count); // Следящие
                        Button button_linkto = (Button) card.findViewById(R.id.linkto);        // Ссылка
                        button_linkto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String url = j_url;

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });

                        avatar.setImageDrawable(LoadImageFromWebOperations(j_avatar_url));
                        name.setText(j_name);
                        full_name.setText(j_full_name);
                        language.setText(j_language);
                        forks.setText(j_forks);
                        watchers.setText(j_watchers);
                        stargazers_count.setText(j_stargazers_count);

                        // Цикл по всем карточкам
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(30, 20,30, 5);
                        card.setLayoutParams(layoutParams);

                        LL.addView(card);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
            }

        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    // Метод чтения данных с сети по протоколу HTTP
    public static String getHTMLData(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                StringBuilder data = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return data.toString();
            } else {
                return null;
            }
        } catch (Exception ignored) {
        } finally {
            conn.disconnect();
        }
        return null;
    }
}
