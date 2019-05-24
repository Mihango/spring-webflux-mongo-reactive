package ke.co.tech.webfluxmongodemo.repository;

import ke.co.tech.webfluxmongodemo.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

// @DirtiesContext
@RunWith(SpringRunner.class)
@DataMongoTest
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item("null", "Samsung TV", 400.0),
            new Item("null", "LG TV", 420.0),
            new Item("null", "Aple Watch", 299.0),
            new Item("null", "Beats Headphones TV", 149.0));

    @Before
    public void setup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Insted item is" + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        Flux<Item> itemFlux = itemReactiveRepository.findAll()
                .log();

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
