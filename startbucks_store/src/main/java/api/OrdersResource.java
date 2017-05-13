package api;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.Collection;

public class OrdersResource extends ServerResource {

    @Get
    public Representation get_action (Representation rep) throws IOException {
        Collection<Order> orders = StarbucksAPI.getOrders() ;
        return new JacksonRepresentation<Collection<Order>>(orders) ;
    }


}


