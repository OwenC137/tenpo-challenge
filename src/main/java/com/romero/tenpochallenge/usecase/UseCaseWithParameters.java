package com.romero.tenpochallenge.usecase;

public interface UseCaseWithParameters<T,R>{
    R execute(T parameter);
}
