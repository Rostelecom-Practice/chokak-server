package com.practice.review.config.metric;

import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.application.dto.ReviewSubmissionResultDto;
import com.practice.review.config.annotation.TrackMetric;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class MetricAspect {

    private static final Logger logger = LoggerFactory.getLogger(MetricAspect.class);

    private final MeterRegistry meterRegistry;

    public MetricAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        logger.info("MetricAspect initialized!");
    }


    @AfterReturning(pointcut = "@annotation(com.practice.review.config.annotation.TrackMetric)", returning = "result")
    public void afterReviewMethod(JoinPoint joinPoint, Object result) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TrackMetric annotation = method.getAnnotation(TrackMetric.class);
        String metricType = annotation.type();

        System.out.println("EUUU + " + metricType + " = " + result);
        logger.info("Metric type: {}", metricType);
        logger.info("Metric method: {}", method);


        if (result instanceof ReviewSubmissionResultDto dto) {
            String sourceId = dto.getSourceId().toString();
            meterRegistry.counter("reviews_submitted_total", "type", metricType, "source", sourceId).increment();
        }

        if (result instanceof List<?> list && !list.isEmpty()) {
            Map<String, Long> sourceCountMap = new HashMap<>();

            for (Object item : list) {
                if (item instanceof ReviewResponseDto responseDto) {
                    String sourceId = responseDto.sourceId().toString();
                    sourceCountMap.merge(sourceId, 1L, Long::sum);
                }
            }

            sourceCountMap.forEach((sourceId, count) -> {
                meterRegistry.counter("reviews_events_total",
                        "type", metricType,
                        "source", sourceId).increment(count);

            });
        }
    }


}