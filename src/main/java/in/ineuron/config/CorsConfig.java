package in.ineuron.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	
	@Bean
	public WebMvcConfigurer congifWebMvc(){
		
		return new WebMvcConfigurer() {
			
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
		              .allowedOrigins("http://localhost:3000") // Replace with the appropriate origin
		              .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE")
		              .allowedHeaders("*")
		              .allowCredentials(true)
		              .maxAge(3600);
			}
		};
	}
}
