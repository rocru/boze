package dev.boze.client.core;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

import java.util.ArrayList;
import java.util.List;

public class ErrorLogger {
    private static final List<Exception> errors = new ArrayList();

    public static void log(Exception e) {
        Log.error(LogCategory.LOG, "Non-Fatal Error in mod Boze: ", e);
        errors.add(e);
    }
}
