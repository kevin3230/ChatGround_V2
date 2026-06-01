package com.chatground.utility;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

public class LanguageUtils {
	
	public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
