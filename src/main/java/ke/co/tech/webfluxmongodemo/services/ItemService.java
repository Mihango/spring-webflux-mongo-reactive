package ke.co.tech.webfluxmongodemo.services;

import ke.co.tech.webfluxmongodemo.document.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<Item> createItem();
    Flux<Item> getAllItems();
    Mono<Item> findItem(String itemId);
}
