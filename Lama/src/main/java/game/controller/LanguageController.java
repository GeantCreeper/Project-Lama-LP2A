package game.controller;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;

public class LanguageController {
    private static final String BUNDLE_NAME = "string"; // Default property file 
    private static Locale currentLocale = Locale.ENGLISH; // By default, we use English
    
    public static void setLocale(Locale locale) { // Change the current locale
        currentLocale = locale;
    }

    public static Locale getCurrentLocale() { // Get the current locale
        return currentLocale;
    }

    public static String getString(String key) { // Get the string associated with the given key for the current locale
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale); // Load the appropriate property file based on the current locale
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}