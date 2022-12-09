package kz.talipovsn.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MySQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8 ; // НОМЕР ВЕРСИИ БАЗЫ ДАННЫХ И ТАБЛИЦ !

    static final String DATABASE_NAME = "tv"; // Имя базы данных

//    Модель | Цвет | WIFI | Разрешение | SmartTV | Диагональ
    static final String TABLE_NAME = "sony_tv"; // Имя таблицы
    static final String ID = "id"; // Поле с ID
    static final String MODEL = "model"; // Поле с наименованием организации
    static final String INCH = "inch"; // // Поле с наименованием организации в нижнем регистре
    static final String COLOR = "color"; // Поле с наименованием организации
    static final String RAZR = "razr"; // Поле с наименованием организации
    static final String WIFI = "wifi"; // Поле с наименованием организации
    static final String SMART = "smart"; // Поле с наименованием организации

    static final String ASSETS_FILE_NAME = "tv.txt"; // Имя файла из ресурсов с данными для БД
    static final String DATA_SEPARATOR = "|"; // Разделитель данных в файле ресурсов с телефонами

    private Context context; // Контекст приложения

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Метод создания базы данных и таблиц в ней
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + MODEL + " TEXT,"
                + INCH + " TEXT,"
                + COLOR + " TEXT,"
                + RAZR + " TEXT,"
                + WIFI + " TEXT,"
                + SMART + " TEXT"
                + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        System.out.println(CREATE_CONTACTS_TABLE);
        loadDataFromAsset(context, ASSETS_FILE_NAME,  db);
    }

    // Метод при обновлении структуры базы данных и/или таблиц в ней
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление нового контакта в БД
    public void addData(SQLiteDatabase db, String model, String inch, String color, String razr, String wifi, String smart) {
        ContentValues values = new ContentValues();
        values.put(MODEL, model);
        values.put(INCH, inch.toLowerCase());
        values.put(COLOR, color.toLowerCase());
        values.put(RAZR, razr.toLowerCase());
        values.put(WIFI, wifi.toLowerCase());
        values.put(SMART, smart.toLowerCase());

        db.insert(TABLE_NAME, null, values);
    }

    // Добавление записей в базу данных из файла ресурсов
    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db) {
        BufferedReader in = null;

        try {
            // Открываем поток для работы с файлом с исходными данными
            InputStream is = context.getAssets().open(fileName);
            // Открываем буфер обмена для потока работы с файлом с исходными данными
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = in.readLine()) != null) { // Читаем строку из файла
                String strTrim = str.trim(); // Убираем у строки пробелы с концов
                if (!strTrim.equals("")) { // Если строка не пустая, то
                    StringTokenizer st = new StringTokenizer(strTrim, DATA_SEPARATOR); // Нарезаем ее на части
                    String model = st.nextToken().trim(); // Извлекаем из строки название организации без пробелов на концах
                    String inch = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    String color = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    String razr = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    String wifi = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    String smart = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    addData(db, model, inch, color, razr, wifi, smart); // Добавляем название и телефон в базу данных
                }
            }

        // Обработчики ошибок
        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    // Получение значений данных из БД в виде строки с фильтром
    public String getData(String filter) {

        String selectQuery; // Переменная для SQL-запроса

        if (filter.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + MODEL + " LIKE '%" +
                    filter.toLowerCase() + "%'" +
                    " OR " + MODEL + " LIKE '%" + filter + "%'" +
                    " OR " + COLOR + " LIKE '%" + filter + "%'" +
                    " OR " + INCH + " LIKE '%" + filter + "%'" +
                    " OR " + RAZR + " LIKE '%" + filter + "%'" +")" +
                    " ORDER BY " + ID;
        }
        SQLiteDatabase db = this.getReadableDatabase(); // Доступ к БД
        Cursor cursor = db.rawQuery(selectQuery, null); // Выполнение SQL-запроса

        StringBuilder data = new StringBuilder(); // Переменная для формирования данных из запроса

        int num = 0;
        data.append("Модель | Диагональ | Цвет | Разрешение | WIFI | SmartTV | " + "\n");
        if (cursor.moveToFirst()) { // Если есть хоть одна запись, то
            do { // Цикл по всем записям результата запроса
                int a = cursor.getColumnIndex(MODEL);
                int b = cursor.getColumnIndex(INCH);
                int c = cursor.getColumnIndex(COLOR);
                int d = cursor.getColumnIndex(RAZR);
                int e = cursor.getColumnIndex(WIFI);
                int f = cursor.getColumnIndex(SMART);
                String model = cursor.getString(a); // Чтение названия организации
                String inch = cursor.getString(b); // Чтение телефонного номера
                String color = cursor.getString(c); // Чтение телефонного номера
                String razr = cursor.getString(d); // Чтение телефонного номера
                String wifi = cursor.getString(e); // Чтение телефонного номера
                String smart = cursor.getString(f); // Чтение телефонного номера
                data.append(
                        String.valueOf(++num) + ") "
                                + model
                                + "| " + inch
                                + "| " + color
                                + "| " + razr
                                + "| " + wifi
                                + "| " + smart
                                + "\n");
            } while (cursor.moveToNext()); // Цикл пока есть следующая запись
        }
        return data.toString(); // Возвращение результата
    }

}