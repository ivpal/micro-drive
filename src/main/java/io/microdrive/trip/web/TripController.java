package io.microdrive.trip.web;

import io.microdrive.pricing.Info;
import io.microdrive.pricing.PriceCalculator;
import io.microdrive.trip.domain.TripInfo;
import io.microdrive.trip.errors.NoFreeDriversException;
import io.microdrive.trip.repository.PointRepository;
import io.microdrive.trip.repository.TripInfoRepository;
import io.microdrive.trip.routing.RouteProvider;
import io.microdrive.trip.service.DriverService;
import io.microdrive.user.User;
import io.microdrive.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/trip")
public class TripController {

    private final RouteProvider routeProvider;
    private final PriceCalculator priceCalculator;
    private final PointRepository pointRepo;
    private final TripInfoRepository tripInfoRepo;
    private final DriverService driverService;

    @Autowired
    UserRepository userRepository;


    public TripController(RouteProvider routeProvider,
                          PriceCalculator priceCalculator,
                          PointRepository pointRepo,
                          TripInfoRepository tripInfoRepo,
                          DriverService driverService) {
        this.routeProvider = routeProvider;
        this.priceCalculator = priceCalculator;
        this.pointRepo = pointRepo;
        this.tripInfoRepo = tripInfoRepo;
        this.driverService = driverService;
    }

    @PostMapping("/info/{locations}")
    public Mono<TripInfo> getTripInfoByLocations(@PathVariable String locations, @AuthenticationPrincipal User user) {
        Mono<TripInfo> mono = Mono.from(routeProvider.calculateRoute(locations))
                .flatMap(route -> {
                    TripInfo tripInfo = TripInfo.builder()
                            .routeInfo(route)
                            .price(priceCalculator.calculate(new Info(route.getLengthInMeters())))
                            .userId(user.getId())
                            .status(TripInfo.Status.NEW)
                            .build();
                    return tripInfoRepo.save(tripInfo);
                });

        return Mono.from(mono);
    }

    @PostMapping("/request/{tripId}")
    public Mono<User> requestTrip(@PathVariable String tripId, @AuthenticationPrincipal User user) {
        // TODO: notify driver via WebSocket

        return this.driverService
                .getFreeDriver()
                .flatMap(op -> op.isPresent() ? Mono.just(op.get()) : Mono.error(new NoFreeDriversException()))
                .flatMap(u -> this.tripInfoRepo
                        .updateDriverIdAndStatus(tripId, u.getId(), TripInfo.Status.EXPECTED)
                        .flatMap(r -> r.getModifiedCount() == 1 ? Mono.just(u) : Mono.error(new Exception("Trip not found")))
                );
    }
}
