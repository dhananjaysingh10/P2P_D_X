package com.example.guice;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InjectionFactory {
    @Getter
    private static Injector injector;

    public static void init(Module... modules) {
        injector = Guice.createInjector(modules);
    }

    public static <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}