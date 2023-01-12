package net.plazarov.chirper.data.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public abstract class AbstractService<T> {

    protected final JpaRepository<T, Long> repository;

    public AbstractService(JpaRepository<T, Long> repository)
    {
        this.repository = repository;
    }

    public Optional<T> get(Long id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
