package dev.naimsulejmani.grupi3javafxregistration.infrastructure;

import java.util.List;

public interface Repository<T, TId> {
    boolean add(T model); // INSERT
    boolean modify(TId id, T model); // UPDATE
    boolean removeById(TId id); // DELETE
    T findById(TId id); // SELECT FROM WHERE
    List<T> findAll(); // SELECT FROM
}
