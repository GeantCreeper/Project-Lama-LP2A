package game.controller;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;

public class LanguageController {
    private static final String BUNDLE_NAME = "resources.string";
    private static Locale currentLocale = Locale.ENGLISH; // Par défaut, on utilise l'anglais
    
    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    public static String getString(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}