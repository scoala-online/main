package org.scoalaonline.api.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component("random")
public class RandomHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
    double chance = ThreadLocalRandom.current().nextDouble();
    // Health builders
    Health.Builder status = Health.up();
    if (chance > 0.9) {
      status = Health.down(new RuntimeException("Bad Luck"));
    }
    return status
      .withDetail("chance", chance)
      .withDetail("strategy", "thread-local")
      .build();
  }
}
