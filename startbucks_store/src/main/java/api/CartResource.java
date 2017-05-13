package api;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.IOException;

public class CartResource extends ServerResource {

    @Get
    public Representation get_action (Representation rep) throws IOException {
        Order inCartOrder = StarbucksAPI.getinCartOrder() ;
        return new JacksonRepresentation<Order>(inCartOrder) ;
    }

    @Put
    public Representation put_action (Representation rep) throws IOException {

        JacksonRepresentation<Order> orderRep = new JacksonRepresentation<Order>( rep, Order.class ) ;
        Order updatedOrder = orderRep.getObject() ;

        Order existing_order = StarbucksAPI.updateCart(updatedOrder) ;
        api.Status api = new api.Status() ;
        api.status = "Success" ;
        api.message = "Cart Updated" ;
        return new JacksonRepresentation<Status>(api) ;
        }

    @Post
    public Representation post_action (Representation rep) throws IOException {

        JacksonRepresentation<Order> orderRep = new JacksonRepresentation<Order>( rep, Order.class ) ;
        Order updatedOrder = orderRep.getObject() ;

        Order existing_order = StarbucksAPI.updateCart(updatedOrder) ;
        api.Status api = new api.Status() ;
        api.status = "Success" ;
        api.message = "Cart Updated" ;
        return new JacksonRepresentation<Status>(api) ;
    }

}


