package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;

@RequiredArgsConstructor
@Service("mpaServiceDb")
public class MpaService {

    private final MpaDbStorage mpaStorage;

    public Mpa findById(Long mpaId) {
        return mpaStorage.findById(mpaId);
    }

    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
