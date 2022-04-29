package ru.otus.libraryapplication.service.string;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
public class StringServiceImpl implements StringService {

    @Override
    public String beautifyStringName(String name) {
        return Optional.ofNullable(name)
                .map(String::strip)
                .map(s -> s.replaceAll("\\s{2,}", " "))
                .map(s -> s.toUpperCase(Locale.ROOT))
                .orElse(null);
    }

    @Override
    public boolean verifyNotBlank(String... strings) {
        return Arrays.stream(strings)
                .allMatch(s -> s != null && !s.isBlank());
    }
}
