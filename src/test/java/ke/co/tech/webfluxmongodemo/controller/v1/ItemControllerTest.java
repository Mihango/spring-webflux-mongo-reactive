package ke.co.tech.webfluxmongodemo.controller.v1;

import ke.co.tech.webfluxmongodemo.document.Item;
import ke.co.tech.webfluxmongodemo.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient // to inject WebTestClient
@ActiveProfiles("test") // use profile test
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Before
    public void setup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted item is: " + item);
                })
                .blockLast();
    }


    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.0),
                new Item("ABC", "Beats Headphones TV", 149.0));
    }

    @Test
    public void getAllItems() {
        webTestClient.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    public void getAllItems_approach2() {
        webTestClient.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    assert items != null;
                    items.forEach(item -> assertNotNull(item.getId()));
                });
    }

    @Test
    public void getItemById() {
        webTestClient.get().uri("/v1/items/ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 400.0);
    }

    @Test
    public void getItemById_notFound() {
        webTestClient.get().uri("/v1/items/1c")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem() {
        webTestClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new Item(null, "Iphone x", 999.0)), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone x")
        .jsonPath("$.price").isEqualTo(999.0);
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri("/v1/items/ABC")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        webTestClient.put().uri("/v1/items/ABC")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new Item(null, "Pixel 3", 1299.0)), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.description").isEqualTo("Pixel 3")
                .jsonPath("$.price").isEqualTo(1299.0);
    }
}
