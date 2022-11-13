package kz.ermolaev.z4.ui.home;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kz.ermolaev.z4.R;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private String resourceDir; // Путь к html-страницам в ресурсах приложения
    WebView webView; // Компонент для просмотра html-страниц

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");


    }

    public LiveData<String> getText() {
        return mText;
    }
}