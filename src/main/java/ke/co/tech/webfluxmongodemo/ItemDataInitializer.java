package ke.co.tech.webfluxmongodemo;

import ke.co.tech.webfluxmongodemo.document.Item;
import ke.co.tech.webfluxmongodemo.repository.ItemReactiveRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test") // ensure that it does not running test profile
public class ItemDataInitializer implements CommandLineRunner {

    private ItemReactiveRepository reactiveRepository;

    public ItemDataInitializer(ItemReactiveRepository reactiveRepository) {
        this.reactiveRepository = reactiveRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initialDatSetup();
    }

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.0),
                new Item("ABC", "Beats Headphones TV", 149.0));
    }

    private void initialDatSetup() {
        reactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(reactiveRepository::save)
                .thenMany(reactiveRepository.findAll())
                .subscribe(item -> System.out.println("Item inserted " + item));
    }

}
