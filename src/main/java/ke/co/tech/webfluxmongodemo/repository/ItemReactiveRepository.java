package ke.co.tech.webfluxmongodemo.repository;

import ke.co.tech.webfluxmongodemo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

}
