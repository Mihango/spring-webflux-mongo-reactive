package ke.co.tech.webfluxmongodemo.controllers.v1;

import ke.co.tech.webfluxmongodemo.document.Item;
import ke.co.tech.webfluxmongodemo.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/items")
@Slf4j
public class ItemController {

    private final ItemReactiveRepository itemReactiveRepository;

    public ItemController(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    @GetMapping
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable("id") String id) {
        return itemReactiveRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteItem(@PathVariable("id") String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable("id") String id, @RequestBody Item item) {
        return itemReactiveRepository.findById(id)
                .flatMap(updateItem -> {
                    updateItem.setPrice(item.getPrice());
                    updateItem.setDescription(item.getDescription());
                    return itemReactiveRepository.save(updateItem);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
