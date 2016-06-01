package sample;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface AccountRepository extends EntityRepository<Account, Long> {
}
