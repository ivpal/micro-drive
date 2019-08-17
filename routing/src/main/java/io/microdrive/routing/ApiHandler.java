package io.microdrive.routing;

import io.microdrive.core.dto.routing.RouteInfo;
import io.microdrive.core.dto.routing.RouteRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class ApiHandler {

    private final RouteProvider routeProvider;

    Mono<ServerResponse> getRoute(ServerRequest request) {
        val routeInfoMono = request.bodyToMono(RouteRequest.class)
                .flatMap(r -> routeProvider.calculateRoute(r.getFrom(), r.getTo()));

        return ok().body(routeInfoMono, RouteInfo.class);
    }

}
