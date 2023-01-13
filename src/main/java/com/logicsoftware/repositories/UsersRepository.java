package com.logicsoftware.repositories;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.models.User;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<User> {

    public PanacheQuery<User> applyFilters(UserFilterDto filter) {
        if(Objects.isNull(filter)) return findAll();

        PanacheQuery<User> query = findAll();

        if(Objects.nonNull(filter.getName())) {
            query = query.filter("name", Parameters.with("name", filter.getName()));
        }

        if(Objects.nonNull(filter.getEmail())) {
            query = query.filter("email", Parameters.with("email", filter.getEmail()));
        }

        return query;
    }

    public List<User> findPage(UserFilterDto filter, Integer page, Integer size) {
        PanacheQuery<User> query = applyFilters(filter);
        query.page(Page.of(page - 1, size));
        return query.list();
    }

    public Long count(UserFilterDto filter) {
        PanacheQuery<User> query = applyFilters(filter);
        return query.count();
    }
    
    public Optional<User> find(Long id) {
        return findByIdOptional(id);
    }

    public User create(User user) {
        persist(user);
        return user;
    }

    public User update(User user) {
        persist(user);
        return user;
    }

    public boolean delete(Long id) {
        return deleteById(id);
    }
}
