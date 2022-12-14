package kz.talipovsn.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class SecondActivity extends AppCompatActivity {

    // Локальные переменные для доступа к компонентам окна
    private CheckBox checkBox;
    private RadioGroup radioGroup;
    private RadioButton selectRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        // Инициализация переменных доступа к компонентам окна
//        editText =   findViewById(R.id.editText);
        checkBox =   findViewById(R.id.checkBox);
        radioGroup = findViewById(R.id.radioGroup);
//        spinner =    findViewById(R.id.spinner);
    }

    public void onBackPressed(View v) {
        onBackPressed();
    }
    // МЕТОД ДЛЯ КНОПКИ "ИТОГО"
    public void onResult(View v) {
        // Создание второго окна
        Intent intent = new Intent(this, ResultActivity.class);
        //Intent intent = new Intent("kz.talipovsn.questionnaire.ResultActivity");

        selectRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        // Подготовка параметров для второго окна
        intent.putExtra("edu", selectRadioButton.getText());
        intent.putExtra("gender", checkBox.isChecked() ? getString(R.string.Мужчина) : getString(R.string.Женщина));

        intent.putExtra("fio", getIntent().getStringExtra("fio"));
        intent.putExtra("country",getIntent().getStringExtra("country"));

        // Запуск второго окна
        startActivity(intent);

    }

}
