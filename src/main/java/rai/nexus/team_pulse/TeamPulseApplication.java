package rai.nexus.team_pulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamPulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamPulseApplication.class, args);
	}

}
