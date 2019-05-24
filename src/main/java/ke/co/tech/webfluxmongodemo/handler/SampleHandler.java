package ke.co.tech.webfluxmongodemo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SampleHandler {

    public Mono<ServerResponse> handleFlux(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Flux.just(1, 2, 3, 4).log(), Integer.class);
    }

    public Mono<ServerResponse> handleFluxStream(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Flux.just(1, 2, 3, 4).delayElements(Duration.ofSeconds(1)).log(), Integer.class);
    }

    public Mono<ServerResponse> handleMono(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just(1), Integer.class);
    }
}
