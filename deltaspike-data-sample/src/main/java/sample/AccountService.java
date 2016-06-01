package sample;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AccountService {
    @Inject
    private AccountRepository repository;

    public Account save(Account entity) {
        return repository.save(entity);
    }

    public Account findBy(Long primaryKey) {
        return repository.findBy(primaryKey);
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account update(Long id, String name) {
        Account entity = repository.findBy(id);
        entity.setName(name);
        return entity;
    }

    public Account create(String name) {
        Account entity = new Account();
        entity.setName(name);
        repository.save(entity);
        return entity;
    }

    public void delete(Long id) {
        Account entity = repository.findBy(id);
        repository.remove(entity);
    }
}
