package edu.wpi.cs3733.D21.teamF.Translation;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to manage translation across the application
 * @author Alex Friedman (ahf
 */
public class Translator {


    /**
     * Used to track what language code we are using. Defaults to english.
     */
    private final StringProperty language = new SimpleStringProperty("en");



    public ObservableValue<String> getTranslationBinding(String text) {
        return Bindings.createStringBinding(() -> translate(text), language);
    }

    public void setLanguage(String language)
    {
        Translator.getTranslator().language.setValue(language);
    }


    public String translate(String text) throws IOException {
        if(language.get().equals("en")) //Block english to english spam api calls
            return text;
        return translate("en", language.get(), text);
    }

    /**
     * web scraper that uses javascript to access a translation generator and return translations
     * @param src the original language that needs to be translated
     * @param target the language that the translation will be provided in
     * @param text the string that needs to translated
     * @return a translated string
     * @throws IOException if an error occurred
     * @author Johvanni Perez
     */
    public String translate(String src, String target, String text) throws IOException {

        String urlStr = "https://script.google.com/macros/s/AKfycbzk_1ZP98MqQNuWvs_Yo3UamuN7WCABIG3UiUUighYgCeqIf4ha4qUzubb2jxopuTP7/exec"
                + "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + target +
                "&source=" + src;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        while((inputLine = in.readLine()) != null){
            response.append(StringEscapeUtils.unescapeHtml4(inputLine));
        }
        in.close();
        return response.toString();
    }


    private Translator() {}

    private static class TranslatorSingletonHelper{
        private static final Translator translator = new Translator();
    }

    public static Translator getTranslator(){
        return TranslatorSingletonHelper.translator;
    }
}
