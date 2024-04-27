package cs509.backend.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8030");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Mann Travis");
        myContact.setEmail("tgmann@wpi.edu");


        Info information = new Info()
                .title("World Plane, Inc")
                .version("1.0")
                .description("This API exposes endpoints to manage flight bookings.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}