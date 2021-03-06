package io.microdrive.routing;

import io.microdrive.core.types.routing.Point;
import io.microdrive.core.types.routing.RouteInfo;
import reactor.core.publisher.Mono;

public interface RouteProvider {
    Mono<RouteInfo> calculateRoute(String locations);
    Mono<RouteInfo> calculateRoute(Point from, Point to);
}
