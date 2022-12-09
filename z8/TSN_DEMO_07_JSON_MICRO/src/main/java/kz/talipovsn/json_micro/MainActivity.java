package kz.talipovsn.json_micro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {

    private TextView textView; // Компонент для отображения данных
    private View card;
    private LayoutInflater inflater;

    private static final String BASE_URL = "https://github.com/proffix4?tab=stars"; // Адрес

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

        generateCards(LL, inflater);
//        onClick(null); // Нажмем на кнопку "Обновить"
    }



    // Получить данные "Обновить"
    public void generateCards(LinearLayout LL, LayoutInflater inflater) {
        StringBuilder data = new StringBuilder();
            try {

                Document doc = Jsoup.connect(BASE_URL).timeout(5000).get(); // Создание документа JSOUP из html
                Element e = doc.select("#user-starred-repos>div>div").get(0);
//                Log.i("INFO_1", String.valueOf(e));
                int i = 1;
                for (Element el : e.select(".col-12.width-full")) {
                    ++i;
                    Log.i("INFO_1", el.select("h3 a").get(0).text());
//                    Log.i("INFO_1", el.select("div:nth-child(2) > div.f6.color-fg-muted.mt-2 > a:nth-child(3)").get(0).text());

//                for(int i = 0; i < allData.length(); i++) {
                    //store your variable

                    String j_url = el.select("h3 a").get(0).attr("href");
                    String j_name = el.select("h3 a").get(0).text();

                    Log.i("INFO_1", "====111 ===");
                    String j_full_name = el.select("[itemprop=\"description\"]").get(0).text();
                    String j_language = el.select("[itemprop=\"programmingLanguage\"]").get(0).text();

                    String j_stargazers_count = el.select("div:nth-child(" + i + ")>div.f6.color-fg-muted.mt-2>a:nth-child(2)").text();
                    String j_forks = el.select("div:nth-child(" + i + ") > div.f6.color-fg-muted.mt-2 > a:nth-child(3)").text();
                    String j_watchers = el.select("div:nth-child(" + i + ") > div.f6.color-fg-muted.mt-2 > relative-time").text();
                    Log.i("INFO_1", "====22222 ===");

                    card = inflater.inflate(R.layout.card_widget, null);



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

                    name.setText(j_name);
                    full_name.setText(j_full_name);
                    language.setText(j_language);
                    forks.setText(j_forks);
                    watchers.setText(j_watchers);
                    stargazers_count.setText(j_stargazers_count);

                    Log.i("INFO_1", j_forks);
                    Log.i("INFO_1", j_watchers);
                    Log.i("INFO_1", j_stargazers_count);
                    Log.i("INFO_1", "=======");


                    // Цикл по всем карточкам
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(30, 20,30, 5);
                    card.setLayoutParams(layoutParams);

                    LL.addView(card);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
