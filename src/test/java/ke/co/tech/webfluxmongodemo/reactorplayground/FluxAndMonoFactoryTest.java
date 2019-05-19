package ke.co.tech.webfluxmongodemo.reactorplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void fluxUsinfIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxFromArray() {
        String[] names = new String[] {
                "Adam", "Anna", "Jack", "Jenny"
        };

        Flux<String> stringFlux = Flux.fromArray(names).log();

        StepVerifier.create(stringFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();

    }

    @Test
    public void fluxFromStream() {
        Flux<String> stringFlux = Flux.fromStream(names.stream())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Adam", "Anna", "Jack", "Jenny")
                .verifyComplete();
    }

    @Test
    public void fluxFromJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null);

        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoFromSupplier() {
        Supplier<String> supplier = () -> "Adam";
        Mono<String> stringMono = Mono.fromSupplier(supplier);

        StepVerifier.create(stringMono.log())
                .expectNext("Adam")
                .verifyComplete();
    }

    @Test
    public void fluxFromRange() {
        Flux<Integer> integerFlux = Flux.range(1, 10);
        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .verifyComplete();
    }
}
