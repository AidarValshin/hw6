package com.github.javarar.poke.service;

import com.github.javarar.poke.dto.PokeDTO;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PokeService {
    private final Map<String, PokeDTO> pokemons = new HashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Optional<PokeDTO> getPokeByName(String name) {
        if(pokemons.get(name)==null){
            return Optional.empty();
        }
        return Optional.of(pokemons.get(name));
    }

    public List<PokeDTO> getPokesByNamesParallel(List<String> names) {
        CompletableFuture<Optional<PokeDTO>>[] completableFutures = new CompletableFuture[names.size()];
        int length = 0;
        for (String name : names) {
            completableFutures[length++] = CompletableFuture.supplyAsync(() -> getPokeByName(name), executorService);
        }
        ArrayList<PokeDTO> result = new ArrayList<>();
        CompletableFuture.allOf(completableFutures).thenAccept(ignore -> Arrays.stream(completableFutures).forEach(fut ->
                fut.join().ifPresent(result::add)
        ));
        return result;
    }

    public Optional<PokeDTO> getAny(List<String> names) {
        Optional<PokeDTO> emptyResult = Optional.empty();
        for (String name : names) {
            PokeDTO pokeDTO = pokemons.get(name);
            if (pokeDTO != null) {
                return Optional.of(pokeDTO);
            }
        }
        return emptyResult;
    }

    public Optional<PokeDTO> getAnyParallel(List<String> names) throws ExecutionException,InterruptedException {

        CompletableFuture<Optional<PokeDTO>>[] completableFutures = new CompletableFuture[names.size()];
        int length = 0;
        for (String name : names) {
            completableFutures[length++] = CompletableFuture.supplyAsync(() -> getPokeByName(name), executorService);
        }
        while (!Arrays.stream(completableFutures).allMatch(fut -> {
            try {
                return (fut.isDone() && fut.get().isPresent()) || fut.isCancelled();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        })) {

        }
        for (CompletableFuture<Optional<PokeDTO>> future : completableFutures) {
            if (future.isDone() && future.get().isPresent()) {
                return future.get();
            }
        }
        return Optional.empty();
    }

    @PostConstruct
    public void load() {
        pokemons.put("Bulbasaur", PokeDTO.builder().name("Bulbasaur").age(5).build());
        pokemons.put("Ivysaur", PokeDTO.builder().name("Ivysaur").age(6).build());
        pokemons.put("Venusaur", PokeDTO.builder().name("Venusaur").age(7).build());
        pokemons.put("Charmander", PokeDTO.builder().name("Charmander").age(4).build());
        pokemons.put("Charmeleon", PokeDTO.builder().name("Charmeleon").age(8).build());
        pokemons.put("Charizard", PokeDTO.builder().name("Charizard").age(4).build());
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
