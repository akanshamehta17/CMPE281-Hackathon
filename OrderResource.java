package api ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.json.* ;
import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;
import org.restlet.ext.jackson.* ;
import org.restlet.data.Tag ;
import org.restlet.data.Form ;
import org.restlet.data.Header ;
import org.restlet.data.Digest ;
import org.restlet.util.Series ;
import org.restlet.ext.crypto.DigestUtils ;
import java.io.IOException ;
//import java.util.Map;

import com.mongodb.MongoClient;
//import org.bson.Document;
//import com.mongodb.client.model.Filters.*;

public class OrderResource extends ServerResource {

    @Get
    public Representation get_action() throws JSONException {

        
        Series<Header> headers = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
        if ( headers != null ) {
            String etag = headers.getFirstValue("If-None-Match") ;
            System.out.println( "HEADERS: " + headers.getNames() ) ;
            System.out.println( "ETAG: " + etag ) ;            
        }
        

        String order_id = getAttribute("order_id") ;
        Order order = StarbucksAPI.getOrder( order_id ) ;

        if ( order_id == null || order_id.equals("") ) {

            setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
            api.Status api = new api.Status() ;
            api.status = "error" ;
            api.message = "Order not found." ;

            return new JacksonRepresentation<api.Status>(api) ;
        }
        else {
            Order existing_order = StarbucksAPI.getOrder( order_id ) ;
            if ( order_id == null || order_id.equals("")  || existing_order == null ) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                api.Status api = new api.Status() ;
                api.status = "error" ;
                api.message = "Order not found." ;
                return new JacksonRepresentation<api.Status>(api) ;
            }                
            else {
                Representation result = new JacksonRepresentation<Order>(order) ;
                try { 
                    System.out.println( "Get Text: " + result.getText() ) ;
                    String  hash = DigestUtils.toMd5 ( result.getText() ) ;
                    System.out.println( "Get Hash: " + hash ) ;
                    result.setTag( new Tag( hash ) ) ;
                    return result ;
                }
                catch ( IOException e ) {
                    setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
                    api.Status api = new api.Status() ;
                    api.status = "error" ;
                    api.message = "Server Error, Try Again Later." ;
                    return new JacksonRepresentation<api.Status>(api) ;
                }
            }
        }
    }


    @Post
    public Representation post_action (Representation rep) throws IOException {

        JacksonRepresentation<Order> orderRep = new JacksonRepresentation<Order> ( rep, Order.class ) ;

        Order order = orderRep.getObject() ;
        StarbucksAPI.setOrderStatus( order, getReference().toString(), StarbucksAPI.OrderStatus.PLACED ) ;
        StarbucksAPI.placeOrder( order.id, order ) ;

        Representation result = new JacksonRepresentation<Order>(order) ;
        try { 
                System.out.println( "Text: " + result.getText() ) ;
                String  hash = DigestUtils.toMd5 ( result.getText() ) ;
                result.setTag( new Tag( hash ) ) ;
                return result ;
        }
        catch ( IOException e ) {
                setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
                api.Status api = new api.Status() ;
                api.status = "error" ;
                api.message = "Server Error, Try Again Later." ;
                return new JacksonRepresentation<api.Status>(api) ;
        }
    }


   @Put
    public Representation put_action (Representation rep) throws IOException {

        JacksonRepresentation<Order> orderRep = new JacksonRepresentation<Order> ( rep, Order.class ) ;
        Order order = orderRep.getObject() ;

        String order_id = getAttribute("order_id") ;
        Order existing_order = StarbucksAPI.getOrder( order_id ) ;

        if ( order_id == null || order_id.equals("")  || existing_order == null ) {

            setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
            api.Status api = new api.Status() ;
            api.status = "error" ;
            api.message = "Order not found." ;

            return new JacksonRepresentation<api.Status>(api) ;

        }                
        else if ( existing_order != null && existing_order.status != StarbucksAPI.OrderStatus.PLACED ) {

            setStatus( org.restlet.data.Status.CLIENT_ERROR_PRECONDITION_FAILED ) ;
            api.Status api = new api.Status() ;
            api.status = "error" ;
            api.message = "Order Update Rejected." ;

            return new JacksonRepresentation<api.Status>(api) ;
        }
        else {

            StarbucksAPI.setOrderStatus( order, getReference().toString(), StarbucksAPI.OrderStatus.PLACED ) ;
            order.id = existing_order.id ;
            StarbucksAPI.updateOrder( order.id, order ) ;  
            Representation result = new JacksonRepresentation<Order>(order) ;
            try { 
                    System.out.println( "Text: " + result.getText() ) ;
                    String  hash = DigestUtils.toMd5 ( result.getText() ) ;
                    result.setTag( new Tag( hash ) ) ;
                    return result ;
            }
            catch ( IOException e ) {
                    setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
                    api.Status api = new api.Status() ;
                    api.status = "error" ;
                    api.message = "Server Error, Try Again Later." ;
                    return new JacksonRepresentation<api.Status>(api) ;
            }
        }
    }

    @Delete
    public Representation delete_action (Representation rep) throws IOException {

        String order_id = getAttribute("order_id") ;
        Order existing_order = StarbucksAPI.getOrder( order_id ) ;
        
        if ( order_id == null || order_id.equals("")  || existing_order == null ) {

            setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
            api.Status api = new api.Status() ;
            api.status = "error" ;
            api.message = "Order not found." ;

            return new JacksonRepresentation<api.Status>(api) ;

        }        
        else if ( existing_order.status != StarbucksAPI.OrderStatus.PLACED ) {

            setStatus( org.restlet.data.Status.CLIENT_ERROR_PRECONDITION_FAILED ) ;
            api.Status api = new api.Status() ;
            api.status = "error" ;
            api.message = "Order Cancelling Rejected." ;

            return new JacksonRepresentation<api.Status>(api) ;
        }
        else {

            StarbucksAPI.removeOrder( order_id ) ;
            return null ;    
        }

    }
    public static void main( String args[] ) {

        try{

            // To connect to mongodb server
            //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            //MongoClientURI uri  = new MongoClientURI("mongodb://admin:admin123@127.0.0.1:27017/admin");
            //MongoClient client = new MongoClient(uri);
            //MongoDatabase db = client.getDatabase(uri.getDatabase());
            MongoClient mongoClient = new MongoClient("127.0.0.1",27017);

                    // Now connect to your databases
            //MongoDatabase db = mongoClient.getDatabase("test");

            DB db1 = mongoClient.getDB("test");
            System.out.println(db1.getName());
         //   MongoCollection<Document> dbCollection = db.getCollection("bios");
         // dbCollection.
         //   System.out.println(db.getName());
            System.out.println("Connect to database successfully");

            System.out.println();
            Jongo jongo = new Jongo(db1);
            //System.out.println(jongo.toString());

            org.jongo.MongoCollection orders = jongo.getCollection("bios");
            Order oo = new Order();
            oo.location = "San Jose";
            oo.message = "Hello";
            orders.insert(oo);
            System.out.println(orders.count());


            OrderItem ot =  new OrderItem();
            ot.name = "Item1";
            ot.milk = "water";
            orders.insert(ot);
            System.out.println(orders.count());
            //org.jongo.MongoCursor<Order> all = orders.find("{location: 'SJSU'}").as(Order.class);
            OrderItem orr = orders.findOne("{name: 'Item1'}").as(OrderItem.class);



            System.out.println("Order fetch -" + orr.milk);

/*

            Order o = new Order();
            o.location = "San Jose";
            o.message = "Hello";

         //   BasicDBObject basicDBObject = new BasicDBObject("Name", o);


            //dbCollection.insertOne(basicDBObject);
            //ObjectMapper mapper = new ObjectMapper();

           // String json = mapper.writeValueAsString(o);
            //System.out.println(json);
            //dbCollection.insertOne(Document.parse(json));

            Document document = dbCollection.find(eq("location", "SJSU")).first();
            String json_read = document.toJson();
            System.out.println(json_read);
            //Map<String,Object> or = mapper.readValue(json_read, Map.class);


            System.out.println("Order fetch -" + or.values());
*/
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }


}