package Auction.service.cache;

import org.springframework.data.repository.CrudRepository;

public interface ProductSearchCacheRepository extends CrudRepository<RedisCategoryContainer, Long> {
}
