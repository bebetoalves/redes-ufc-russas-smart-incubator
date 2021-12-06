package com.utils;

import com.net.header.HeaderAction;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HeaderUtils {
    public static String createMessage(HeaderAction action, String key, String value) {
        return String.format("|%s|%s|%s|", action.name(), key, value);
    }

    public static boolean isValidRequest(String header) {
        String availableActions = Arrays.stream(HeaderAction.values()).map(Enum::toString).collect(Collectors.joining("|"));

        String regex = String.format("^\\|(%s)\\|[A-Z]{4}\\|[A-Z0-9]{1,10}\\|$", availableActions);

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(header).find();
    }

    public static String[] convertToArgs(String message) {
        return Arrays.stream(message.split("\\|")).filter(s -> !s.isBlank()).toArray(String[]::new);
    }
}
