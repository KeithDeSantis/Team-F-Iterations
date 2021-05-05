package edu.wpi.cs3733.D21.teamF.Translation;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to manage translation across the application
 * @author Alex Friedman (ahf
 */
public class Translator {

    //FIXME: ALWAYS SET THIS TO BE TRUE FOR JARS!!!!!
    private static final boolean isProduction = false;

    /**
     * Used to track what language code we are using. Defaults to english.
     */
    private final StringProperty language = new SimpleStringProperty("en");


    private final HashMap<String, HashMap<String, String>> translationLookupTable = new HashMap<>();

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

        /*
         * Accesses our local cache
         */
        final HashMap<String, String> languageLookup = translationLookupTable.get(language.get());
        if(languageLookup == null)
            translationLookupTable.put(language.get(), new HashMap<>());
        else
        {
            final String translation = languageLookup.get(text);
            if(translation != null)
                return  translation;
        }

        System.out.println("Could not translate: " + language.get() + " -> " + text);
        final String translation = translate("en", language.get(), text);

        translationLookupTable.get(language.get()).put(text, translation);

        return translation;
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


    private Translator() {

        if(!isProduction)
        {
            final HashMap<String, HashMap<String, String>> initialLookupTables = new HashMap<>();
            {
                final String tableDirectory = "src/main/resources/TranslationTables/";
                final File translationTables = new File(tableDirectory);
                for(String file : translationTables.list())
                {
                    try {
                        final FileInputStream fileInputStream = new FileInputStream(tableDirectory + file);
                        final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                        final HashMap<String, HashMap<String, String>> currMap = (HashMap<String, HashMap<String, String>>) objectInputStream.readObject();

                        for(String s : currMap.keySet())
                        {
                            if(initialLookupTables.get(s) == null)
                                initialLookupTables.put(s, new HashMap<>());

                            for(String l : currMap.get(s).keySet())
                                initialLookupTables.get(s).put(l, currMap.get(s).get(l));
                        }

                        objectInputStream.close();
                        fileInputStream.close();


                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                for(String s : initialLookupTables.keySet())
                    for(String l : initialLookupTables.get(s).keySet())
                    {
                       if(translationLookupTable.get(s) == null)
                           translationLookupTable.put(s, new HashMap<>());

                       System.out.println("ADDING: " + s + " -> " + l);
                        translationLookupTable.get(s).put(l, initialLookupTables.get(s).get(l));
                    }
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    final Iterator<String> langIterator = translationLookupTable.keySet().iterator();
                    while (langIterator.hasNext())
                    {
                        final String lang = langIterator.next();

                        if(initialLookupTables.get(lang) != null)
                        {
                            translationLookupTable.get(lang).keySet().removeIf(s -> initialLookupTables.get(lang).get(s) != null);
                        }

                        if(translationLookupTable.get(lang).keySet().isEmpty())
                           langIterator.remove();
                    }

                    if(translationLookupTable.keySet().isEmpty())
                        return;

                    System.out.println((new File("")).getAbsolutePath());
                    final FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/TranslationTables/" + System.currentTimeMillis() + ".bin");

                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                    objectOutputStream.writeObject(translationLookupTable);
                    objectOutputStream.close();
                    fileOutputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    private static class TranslatorSingletonHelper{
        private static final Translator translator = new Translator();
    }

    public static Translator getTranslator(){
        return TranslatorSingletonHelper.translator;
    }
}
