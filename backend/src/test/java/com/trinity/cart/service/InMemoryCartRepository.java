package com.trinity.cart.service;

import com.trinity.cart.model.CartEntity;
import com.trinity.cart.repository.CartRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Minimal in-memory fake of CartRepository for fast, framework-free service tests.
 * Only the methods CartService actually uses are implemented; the rest throw.
 */
class InMemoryCartRepository implements CartRepository {

    private final Map<UUID, CartEntity> byId = new HashMap<>();

    private UUID ensureId(CartEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        return entity.getId();
    }

    @Override
    public <S extends CartEntity> S save(S entity) {
        ensureId(entity);
        byId.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends CartEntity> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public Optional<CartEntity> findById(UUID id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<CartEntity> findByCustomerId(UUID customerId) {
        return byId.values().stream()
                .filter(c -> customerId.equals(c.getCustomerId()))
                .findFirst();
    }

    @Override
    public boolean existsByCustomerId(UUID customerId) {
        return findByCustomerId(customerId).isPresent();
    }

    @Override
    public void deleteByCustomerId(UUID customerId) {
        findByCustomerId(customerId).ifPresent(c -> byId.remove(c.getId()));
    }

    @Override
    public void delete(CartEntity entity) {
        byId.remove(entity.getId());
    }

    // --- everything below is unused by CartService ---

    private UnsupportedOperationException nope() {
        return new UnsupportedOperationException("not needed in CartService tests");
    }

    @Override public void flush() { /* no-op */ }
    @Override public <S extends CartEntity> List<S> saveAllAndFlush(Iterable<S> entities) { throw nope(); }
    @Override public void deleteAllInBatch(Iterable<CartEntity> entities) { throw nope(); }
    @Override public void deleteAllByIdInBatch(Iterable<UUID> ids) { throw nope(); }
    @Override public void deleteAllInBatch() { throw nope(); }
    @Override public CartEntity getOne(UUID id) { throw nope(); }
    @Override public CartEntity getById(UUID id) { throw nope(); }
    @Override public CartEntity getReferenceById(UUID id) { throw nope(); }
    @Override public <S extends CartEntity> List<S> findAll(Example<S> example) { throw nope(); }
    @Override public <S extends CartEntity> List<S> findAll(Example<S> example, Sort sort) { throw nope(); }
    @Override public <S extends CartEntity> List<S> saveAll(Iterable<S> entities) { throw nope(); }
    @Override public List<CartEntity> findAll() { throw nope(); }
    @Override public List<CartEntity> findAllById(Iterable<UUID> ids) { throw nope(); }
    @Override public List<CartEntity> findAll(Sort sort) { throw nope(); }
    @Override public Page<CartEntity> findAll(Pageable pageable) { throw nope(); }
    @Override public boolean existsById(UUID id) { return byId.containsKey(id); }
    @Override public long count() { return byId.size(); }
    @Override public void deleteById(UUID id) { byId.remove(id); }
    @Override public void deleteAllById(Iterable<? extends UUID> ids) { throw nope(); }
    @Override public void deleteAll(Iterable<? extends CartEntity> entities) { throw nope(); }
    @Override public void deleteAll() { byId.clear(); }
    @Override public <S extends CartEntity> Optional<S> findOne(Example<S> example) { throw nope(); }
    @Override public <S extends CartEntity> Page<S> findAll(Example<S> example, Pageable pageable) { throw nope(); }
    @Override public <S extends CartEntity> long count(Example<S> example) { throw nope(); }
    @Override public <S extends CartEntity> boolean exists(Example<S> example) { throw nope(); }
    @Override public <S extends CartEntity, R> R findBy(Example<S> example, Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { throw nope(); }
}
