package io.microdrive.trip.api.web;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.microdrive.trip.api.domain.Point;
import io.microdrive.trip.api.domain.TripInfo;
import io.microdrive.trip.routing.RouteProvider;
import io.microdrive.trip.pricing.PriceCalculator;
import io.microdrive.trip.api.data.PointRepository;

@RestController
@RequestMapping("/trip")
public class TripController {

    private final RouteProvider routeProvider;
    private final PriceCalculator priceCalculator;
    private final PointRepository pointRepo;

    public TripController(RouteProvider routeProvider,
                          PriceCalculator priceCalculator,
                          PointRepository pointRepo) {
        this.routeProvider = routeProvider;
        this.priceCalculator = priceCalculator;
        this.pointRepo = pointRepo;
    }

    @GetMapping("/{locations}")
    public Mono<TripInfo> index(@PathVariable String locations) {
        return Mono.from(routeProvider.calculateRoute(locations))
                .map(route -> new TripInfo(route, priceCalculator.calculate(route)));
    }

    @GetMapping("/{tripId}/points")
    public Flux<Point> show(@PathVariable String tripId) {
        return pointRepo.findAllByTripId(tripId);
    }

    @PostMapping("/{tripId}/points")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Point> create(@PathVariable String tripId, @RequestBody io.microdrive.trip.routing.Point point) {
        Point p = Point.builder()
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .tripId(tripId)
                .build();

        return pointRepo.save(p);
    }
}
