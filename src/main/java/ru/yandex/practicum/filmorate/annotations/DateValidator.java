package ru.yandex.practicum.filmorate.annotations;

import java.time.format.DateTimeFormatter;

public class DateValidator {
    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
