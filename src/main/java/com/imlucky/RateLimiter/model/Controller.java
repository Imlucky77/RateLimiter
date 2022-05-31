package com.imlucky.RateLimiter.model;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class Controller {
    private final Bucket bucket;

    public Controller() {
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofHours(100)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @PostMapping(value = "api/v1/perimeter/type")
    public ResponseEntity<Perimeter> rectangle(@RequestBody Dimension dimension) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(new Perimeter(dimension.getTitle(), (double) 2 * (dimension.getLength() + dimension.getBreadth())));
    }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
