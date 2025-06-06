package server;

import static spark.Spark.after;

import APIServer.APIDataSource;
import APIServer.CachingDataSource;
import Broadband.BroadbandHandler;
import Broadband.BroadbandUtilities;
import APIServer.CSVManager;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 * <p>Notice that the OrderHandler takes in a state (menu) that can be shared if we extended the
 * restaurant They need to share state (a menu). This would be a great opportunity to use dependency
 * injection. If we needed more endpoints, more functionality classes, etc. we could make sure they
 * all had the same shared state.
 */
public class Server {

  // TODO 0: Read through this class and determine the shape of this project...
  // What are the endpoints that we can access... What happens if you go to them?
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    CSVManager manager = new CSVManager();

    BroadbandUtilities myBroadband = new BroadbandUtilities();
    // Setting up the handler for the GET /order and /activity endpoints

    APIDataSource datasource = new APIDataSource();
    CachingDataSource cacheBroadbandData = new CachingDataSource
        (datasource, 2, 1);

    //setting up endpoints
    Spark.get("loadcsv", new APIServer.LoadCSVHandler(manager));
    Spark.get("viewcsv", new APIServer.ViewCSVHandler(manager));
    Spark.get("searchcsv", new APIServer.SearchCSVHandler(manager));
    Spark.get("broadband", new BroadbandHandler(myBroadband, cacheBroadbandData));
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}