package edu.wpi.cs3733.D21.teamF.Translation;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**appea
 * This class is used to manage translation across the application
 * @author Alex Friedman (ahf
 */
public class Translator {

    //FIXME: ALWAYS SET THIS TO BE TRUE FOR JARS!!!!!
    private static final boolean isProduction = true;

    private final List<String> languages;

    private final HashMap<String, String> langCodes = new HashMap<>();

    private static final StringConverter<String> languageCodeConverter = new StringConverter<String>() {
        @Override
        public String toString(String object) {
            for(String s : Translator.getTranslator().getLanguages())
                if(Translator.getTranslator().getLangCode(s).equals(object))
                    return s;
            return null;
        }

        @Override
        public String fromString(String string) {
            return Translator.getTranslator().getLangCode(string);
        }
    };

    public static StringConverter<String> getLanguageCodeConverter() {
        return languageCodeConverter;
    }

    /**
     * Used to track what language code we are using. Defaults to english.
     */
    private final StringProperty language = new SimpleStringProperty("en");


    /**
     * Used to cache translations. This works in the format of:
     * <code>translationLookupTable.get(language).get(textInEnglish)</code>
     * Assuming that the string has been cached
     */
    private final HashMap<String, HashMap<String, String>> translationLookupTable = new HashMap<>();

    /**
     * Used to get a binding to translate a string to
     * @param text The text in english
     * @return A binding that binds the text to a string
     * @author Alex Friedman (ahf)
     */
    public ObservableValue<String> getTranslationBinding(String text) {
        return Bindings.createStringBinding(() -> translate(text), language);
    }

    /**
     * Used to get a binding to translate a string to
     * @param text The text in english
     * @return A binding that binds the text to a string
     * @author Alex Friedman (ahf)
     */
    public ObservableValue<String> getTranslationBinding(StringProperty text) {
        return Bindings.createStringBinding(() -> translate(text.get()), text, language);
    }

    /**
     * Used to set the language that we translate to
     * @param language The language code that we translate to (eg en for English)
     * @author Alex Friedman
     */
    public void setLanguage(String language)
    {
        Translator.getTranslator().language.setValue(language);
    }

    /**
     * Given a string, returns a StringProperty that is bound to the translate function.
     * @param text The text to translate.
     * @return A StringProperty for the corresponding text that is automatically translated
     * @author Alex Friedman (ahf)
     */
    public StringProperty getTranslationFor(String text)
    {
        final StringProperty property = new SimpleStringProperty(text);
        property.bind(getTranslationBinding(text));
        return property;
    }

    /**
     * Used to convert a list of strings to an observable list of strings that are each bound to translate.
     * @param list The list of strings to bind
     * @return An ObservableList of StringProperties that are bound to translate
     * @author Alex Friedman (ahf)
     */
    public ObservableList<StringProperty> getTranslationsFor(List<String> list)
    {
        return list.stream().map(this::getTranslationFor).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Used to get a string converter to convert StringProperties to Strings
     * @return A string converter to convert StringProperties to Strings
     * @author Alex Friedman (ahf)
     */
    public StringConverter<StringProperty> getTranslationStringConverter()
    {
        return new StringConverter<StringProperty>() {
            @Override
            public String toString(StringProperty object) {
                return object.get();
            }

            @Override
            public StringProperty fromString(String string) {
                return null;
            }
        };
    }

    /**
     * Used to convert a list of strings to an observable list of strings that are each bound to translate.
     * @param list The list of strings to bind
     * @return An ObservableList of StringProperties that are bound to translate
     * @author Alex Friedman (ahf)
     */
    public ObservableList<StringProperty> getTranslationsFor(String...list)
    {
        return Arrays.stream(list).map(this::getTranslationFor).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }


    /**
     * Given a String, translates it from English to the current set language
     * @param text The text to translate
     * @return The string of the text in the current Language
     * @throws IOException If an error occurred w/ the API
     * @author Alex Friedman (ahf)
     */
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


    public List<String> getLanguages() {
        return Collections.unmodifiableList(languages);
    }

    public String getLangCode(String language)
    {
        return langCodes.get(language); //Prevents direct access to our codes
    }

    public StringProperty languageProperty() {
        return language;
    }

    private Translator() {

        languages = new ArrayList<>();

        languages.add("Arabic");
        languages.add("Dutch");
        languages.add("English");
        languages.add("French");
        languages.add("German");
        languages.add("Greek");
        languages.add("Haitian Creole");
        languages.add("Italian");
        languages.add("Japanese");
        languages.add("Korean");
        languages.add("Portuguese");
        languages.add("Russian");
        languages.add("Spanish");
        languages.add("Vietnamese");

        Collections.sort(languages);

        langCodes.put("Arabic", "ar");
        langCodes.put("Dutch", "nl");
        langCodes.put("English", "en");
        langCodes.put("French", "fr");
        langCodes.put("German", "de");
        langCodes.put("Greek", "el");
        langCodes.put("Haitian Creole", "ht");
        langCodes.put("Italian", "it");
        langCodes.put("Japanese", "ja");
        langCodes.put("Korean", "ko");
        langCodes.put("Portuguese", "pt");
        langCodes.put("Russian", "ru");
        langCodes.put("Spanish", "es");
        langCodes.put("Vietnamese", "vi");


        //If we're not in production mode, we need to collapse all the files, and track new changes.
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

    /**
     * Gets the translator
     * @return The singleton instance of the translator
     * @author Alex Friedman (ahf)
     */
    public static Translator getTranslator(){
        return TranslatorSingletonHelper.translator;
    }
}
