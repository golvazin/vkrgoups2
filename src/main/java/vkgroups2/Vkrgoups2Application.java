package vkgroups2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

@SpringBootApplication
@EntityScan(basePackages={"vkgroups2.domain"})
public class Vkrgoups2Application { 
    


	public static void main(String[] args)  {
		SpringApplication.run(Vkrgoups2Application.class, args);
	}
}
