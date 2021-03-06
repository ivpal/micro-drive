package io.microdrive.pricing.web;

import io.microdrive.core.types.pricing.PricingInfo;
import io.microdrive.core.types.pricing.PricingRequest;
import io.microdrive.pricing.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
class ApiHandler {
    private final PriceService priceService;

    @NonNull
    Mono<ServerResponse> calculatePrice(ServerRequest request) {
        Mono<PricingInfo> info = request.bodyToMono(PricingRequest.class)
                .map(priceService::calculate)
                .map(PricingInfo::new);

        return ok().body(info, PricingInfo.class);
    }
}
