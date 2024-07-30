package com.github.javarar.poke.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PokeDTO {
    private String name;
    private int age;
}
