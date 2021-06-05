package io.microdrive.edge.routing;

import io.microdrive.core.types.routing.BuildRouteRequest;
import io.microdrive.core.types.routing.RouteResponse;
import io.microdrive.edge.routing.config.RoutingConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoutingProxy {
    private final WebClient webClient;
    private final RoutingConfigurationProperties properties;

    public Mono<RouteResponse> buildRoute(BuildRouteRequest request) {
        return webClient.post()
            .uri(properties.getServiceUrl())
            .bodyValue(request)
            .exchangeToMono(response -> response.bodyToMono(RouteResponse.class));
    }

    public Mono<RouteResponse> getRoute(String id) {
        return webClient.get()
            .uri(properties.getServiceUrl() + "/" + id)
            .exchangeToMono(response -> response.bodyToMono(RouteResponse.class));
    }
}
