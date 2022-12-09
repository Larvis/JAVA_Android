package kz.altenic.task8;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView title; // заголовок новости
    private TextView text; // подзаголовок
    private TextView author; // автор
    private TextView date; // дата
    private TextView viewsCount; // кол-во просмотров
    private ScrollView mainContent; //основной кнотейнер
    private ProgressBar progressBar; //лоадер
    private TextView more; // еще

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);
        viewsCount = findViewById(R.id.viewsCount);
        mainContent = findViewById(R.id.mainContent);
        progressBar = findViewById(R.id.progressBar);
        more = findViewById(R.id.more);

        JSOUPRatesTask task = new JSOUPRatesTask(); // Создание потока загрузки новостей
        task.execute(); // Активация потока
    }

    // Кнопка "Обновить"
    public void onClick(View view) {
        mainContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new JSOUPRatesTask().execute(); // Создание и активация потока загрузки новостей
    }

    // Класс отдельного асинхронного потока
    @SuppressLint("StaticFieldLeak")
    private class JSOUPRatesTask extends AsyncTask<String, Void, Post> {

        // Тут реализуем фоновую асинхронную загрузку данных, требующих много времени
        @Override
        protected Post doInBackground(String... params) {
            return NewsReader.getRatesData(); // Получаем данные о новостях
        }
        // ----------------------------------------------------------------------------

        // Тут реализуем что нужно сделать после окончания загрузки данных
        @Override
        protected void onPostExecute(final Post post) {
            super.onPostExecute(post);

            // Выдаем данные о новостях в компонент
            mainContent.post(new Runnable() { //  с использованием синхронизации UI
                @Override
                public void run() {
                    mainContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (post != null) {
                            title.setText(post.getTitle());
                            text.setText(post.getText());
                            author.setText(post.getAuthor());
                            date.setText(post.getDate());
                            viewsCount.setText(post.getViewsCount());
                            more.setText(String.join("\n\n", post.getRelated()));
                    } else {
                        title.setText("");
                        text.setText("");
                        author.setText("");
                        date.setText("");
                        viewsCount.setText("");
                        more.setText("");
                        title.append("Нет данных!" + "\n");
                        title.append("Проверьте доступность Интернета");
                    }
                }
            });

        }
        // ------------------------------------------------------------------------------------

    }

}
