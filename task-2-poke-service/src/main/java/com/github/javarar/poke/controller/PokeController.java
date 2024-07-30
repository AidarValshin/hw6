package com.github.javarar.poke.controller;

import com.github.javarar.poke.dto.PokeDTO;
import com.github.javarar.poke.service.PokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class PokeController {
    private final PokeService pokeService;


    /**
     * Последовательный, синхронный
     *
     * @param names
     * @return возвращает всех запрошенных покемонов, запрашивая их у сервиса последовательно
     */
    @GetMapping("/getAll") //http://localhost:8080/getAll?names=Bulbasaur,Ivysaur,Charizard,undefined
    public List<PokeDTO> getAllByNamesSync(@RequestParam(value = "names") List<String> names) {
        if (names == null || names.size() == 0) {
            throw new IllegalArgumentException("Names must exist");
        }

        List<PokeDTO> response = new ArrayList<>();
        for (String name : names) {
            pokeService.getPokeByName(name).ifPresent(response::add);
        }

        return response;
    }

    /**
     * Одиз ИЗ. Синхронный
     *
     * @param names
     * @return возвращает одного из запрошенных покемонов
     */
    @GetMapping("/getAny")//http://localhost:8080/getAny?names=Bulbasaur,Ivysaur,Charizard,undefined
    public PokeDTO getAnySync(@RequestParam(value = "names") List<String> names) {
        Optional<PokeDTO> optionalPokeDTO = pokeService.getAny(names);
        if (optionalPokeDTO.isEmpty()) {
            throw new IllegalArgumentException("No pokemons were found");
        }
        return optionalPokeDTO.get();
    }

    /**
     * Параллельный, синхронный
     *
     * @param names
     * @return возвращает всех запрошенных покемонов, запрашивая их у сервиса параллельно
     */
    @GetMapping("/getAllParallel")//http://localhost:8080/getAllParallel?names=Bulbasaur,Ivysaur,Charizard,undefined
    public List<PokeDTO> getAllByNamesSyncParalel(@RequestParam(value = "names") List<String> names) {
        if (names == null || names.size() == 0) {
            throw new IllegalArgumentException("Names must exist");
        }
        return pokeService.getPokesByNamesParallel(names);
    }

    /**
     * Одиз ИЗ. Параллельный, cинхронный
     *
     * @param names
     * @return возвращает одного из запрошенных покемонов
     */
    @GetMapping("/getAnyParallel")//http://localhost:8080/getAnyParallel?names=Bulbasaur,Ivysaur,Charizard,undefined
    public PokeDTO getAnySyncParallel(@RequestParam(value = "names") List<String> names) throws ExecutionException, InterruptedException {
        Optional<PokeDTO> optionalPokeDTO = pokeService.getAnyParallel(names);
        if (optionalPokeDTO.isEmpty()) {
            throw new IllegalArgumentException("No pokemons were found");
        }
        return optionalPokeDTO.get();
    }
}
