//package com.urlshortener.redirect.config;
//
//import io.github.bucket4j.Bucket;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Supplier;
//
//@Configuration
//public class RateLimitingConfig {
//    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
//
//    @Bean
//    public Supplier<Bucket> bucketSupplier() {
//        return () -> Bucket.builder()
//                           .addLimit(limit -> limit.(10).refillIntervally(10, Duration.ofMinutes(1)))
//                           .build();
//    }
//
//    public boolean tryConsume(String key) {
//        Bucket bucket = buckets.computeIfAbsent(key, k -> bucketSupplier().get());
//        return bucket.tryConsume(1);
//    }
//}
