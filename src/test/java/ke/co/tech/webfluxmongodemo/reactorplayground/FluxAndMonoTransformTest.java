package ke.co.tech.webfluxmongodemo.reactorplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jenny");

    @Test
    public void fluxMapTransform() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void fluxMapTransform_Length() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void fluxMapTransform_Length_repeat() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .repeat(2)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4,4,4,5,4,4,4,5,4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void fluxMapTransform_Filter() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .repeat(2)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("JENNY", "JENNY", "JENNY")
                .verifyComplete();
    }

    @Test
    public void fluxFlatmapTransform() {
        Flux<String> names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)))
                .log();

        StepVerifier.create(names)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void fluxFlatmapTransform_Parallel() {
        Flux<String> names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F", "G"))
                .window(2)
                .flatMap(s ->
                    s.map(this::convertToList).subscribeOn(parallel())
                        .flatMap(Flux::fromIterable)
                )
                .log();

        StepVerifier.create(names)
                .expectNextCount(14)
                .verifyComplete();
    }

    @Test
    public void fluxFlatmapTransform_Parallel_maintainOrder() {
        Flux<String> names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F", "G"))
                .window(2)
//                .concatMap(s ->
//                        s.map(this::convertToList).subscribeOn(parallel()).flatMap(Flux::fromIterable))

                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(parallel()).flatMap(Flux::fromIterable))
                .log();

        StepVerifier.create(names)
                .expectNextCount(14)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "NewValue");
    }


}
