package kz.altenic.task8;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

// СОЗДАТЕЛЬ КОТИРОВОК ВАЛЮТ
public class NewsReader {

    private static final String BASE_URL = "https://pavon.kz"; // Адрес с котировками

    // Парсинг новостей с Павлодар онлайн , при ошибке доступа возвращаем null
    public static Post getRatesData() {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(BASE_URL).timeout(5000).get(); // Создание документа JSOUP из html
            Element e = doc.select("article.page-item-large").get(0); // Ищем в документе "<article class="page-item-large"> - главный пост
            post.setTitle(e.select("h3 a span").get(0).text());
            post.setText(e.select("p").get(0).text());
            post.setAuthor(e.select("ul li:nth-child(1) a").get(0).text());
            post.setDate(e.select("ul li:nth-child(2)").get(0).text());
            post.setViewsCount(e.select("ul li:nth-child(3)").get(0).text());
            ArrayList<String> related = new ArrayList<>();
            for (Element el : doc.select("article.page-item")) {
                related.add(el.select("h3 a span").get(0).text());
            }
            post.setRelated(related);
        } catch (Exception ignored) {
            return null; // При ошибке доступа возвращаем null
        }
        return post; // Возвращаем результат
    }

}