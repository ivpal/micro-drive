package io.microdrive.trips;

import io.microdrive.core.dto.pricing.PricingRequest;
import io.microdrive.core.dto.routing.RouteRequest;
import io.microdrive.trips.clients.PricingClient;
import io.microdrive.trips.clients.RoutingClient;
import io.microdrive.trips.domain.Trip;
import io.microdrive.trips.service.TripService;
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

    private final RoutingClient routingClient;
    private final PricingClient pricingClient;
    private final TripService tripService;

    public Mono<ServerResponse> info(ServerRequest request) {
        val requestMono = request.bodyToMono(RouteRequest.class);
        val userId = request.headers().header("x-user-id").get(0);

        val tripMono = requestMono.flatMap(routingClient::getRoute)
                .flatMap(routeInfo -> {
                    val pricingRequest = new PricingRequest(routeInfo.getLengthInMeters());
                    return Mono.zip(Mono.just(routeInfo), pricingClient.calculate(pricingRequest));
                }).flatMap(pair -> {
                    val trip = Trip.builder()
                            .routeInfo(pair.getT1())
                            .price(pair.getT2().getPrice())
                            .userId(userId)
                            .status(Trip.Status.NEW)
                            .build();
                    return tripService.addTrip(trip);

                });

        return ok().body(tripMono, Trip.class);
    }

}
