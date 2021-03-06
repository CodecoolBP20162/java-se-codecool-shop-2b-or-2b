import com.codecool.shop.controller.DBController;
import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.CustomerDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.*;
import io.gsonfire.GsonFireBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

/**
  * <h1>Codecool Shop</h1>
  * The main method of the Codecool Shop. It uses thymeleaf for templates and Spark as the web framework.
  * It has two static methods: main and populateData.
  *
  * @author 2B || !2B
  * @version 1.0
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
          * Main method.
          *<p> Creates database, server settings.
          *<p> It calls populateData method for creating example data to the database.
          *<p> Implements spark routes.
          */
    public static void main(String[] args) {

        //create postgres DB
        DBController dbController = new DBController();

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        populateData();

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", new ProductController()::renderProducts, new ThymeleafTemplateEngine());

        get("/index", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(new ProductController().renderProducts(req, res));
        });

        get("/supplier/:name", (Request req, Response res) -> {
            logger.info("Filtering products by supplier: {}", req.params(":name"));
            return new ThymeleafTemplateEngine().render(new ProductController().renderProductsBySupplier(req, res));
        });

        get("/category/:name", (Request req, Response res) -> {
            logger.info("Filtering products by category: {}", req.params(":name"));
            return new ThymeleafTemplateEngine().render(new ProductController().renderProductsByCategory(req, res));
        });

        get("/addToCart/:id", (Request req, Response res) -> {
            logger.info("Adding item to shoppingcart");
            logger.debug("Adding item to cart with id: {}", req.params(":id"));
            ShoppingCart.getInstance().addToCart(Integer.parseInt(req.params("id")));
            return new ThymeleafTemplateEngine().render(new ProductController().renderProducts(req, res));
        });

        post("/saveUserData", (Request req, Response res) -> {
            Customer newCustomer = new Customer(
                    req.queryParams("name"),
                    req.queryParams("email"),
                    Integer.valueOf(req.queryParams("phone")),
                    req.queryParams("billingAddress"),
                    req.queryParams("shippingAddress"));

            CustomerDao customerDataStore = CustomerDaoWithJdbc.getInstance();
            customerDataStore.add(newCustomer);
            int customerId = customerDataStore.findByPhoneNumber(newCustomer.getPhoneNumber());
            res.redirect("/payment/" + customerId);
            return "Ok";
        });

        get("/payment/:id", (Request req, Response res) -> {
            logger.info("Initializing payment");
            CustomerDaoWithJdbc customerDataStore = CustomerDaoWithJdbc.getInstance();
            String param = req.params("id");
            int customerId = Integer.valueOf(param);
            Customer customer = customerDataStore.find(customerId);
            logger.debug("Finished initializing payment");
            return new ThymeleafTemplateEngine().render(new ProductController().renderPayment(req, res));
        });

        get("/cart", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson(ShoppingCart.getInstance().getCartItems());
        });

        put("/cart", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson(ShoppingCart.getInstance().getCartItems());
        });

        get("/remove/:id", (Request req, Response res) -> {
            logger.info("Deleting item from shopping cart");
            String param = req.params("id");
            int itemId = Integer.valueOf(param);
            ShoppingCart.getInstance().deleteProductById(itemId);
            return true;
        });

        post("/save-order", (Request req, Response res) -> {
            return true;
        });

        get("/change-quantity/:id/:quantity", (Request req, Response res) -> {
            logger.info("Changing quantity of product");
            logger.debug("Changing quantity to: {}, of product with id: {}.", req.params(":quantity"), req.params(":id"));
            String id = req.params("id");
            String quantity = req.params("quantity");
            int itemId = Integer.valueOf(id);
            int newQuantity = Integer.valueOf(quantity);
            ShoppingCart.getInstance().findLineItemById(itemId).setQuantity(newQuantity);
            return true;
        });

        get("/counter", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson(ShoppingCart.getInstance().countItemsInTheCart());
        });

        //Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    /**
     * Method creates example data, add product categories and suppliers to the database.
     */
    public static void populateData() {

        ProductDao productDataStore = ProductDaoWithJdbc.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();
        SupplierDao supplierDataStore = SupplierDaoWithJdbc.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);
        Supplier DELL = new Supplier("DELL", "Computers");
        supplierDataStore.add(DELL);


        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware",
                "A tablet computer, commonly shortened to tablet, is a thin," +
                        "flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory laptop = new ProductCategory("Laptop", "Hardware",
                "A portable computer with little weight and long battery life.");
        productCategoryDataStore.add(laptop);


        //setting up products and printing it
        productDataStore.add(new Product("Amazon Fire", 50, "USD",
                "Fantastic price. Large content ecosystem. Good parental controls." +
                        "Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD",
                "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports." +
                        "Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD",
                "Amazons latest Fire HD 8 tablet is a great value for media consumption.",
                tablet, amazon));
        productDataStore.add(new Product("Dell Inspiron 5559-I5G159LE", 594, "USD",
                "Very gut very strong you should buy it, different colors available.", laptop, DELL));
        productDataStore.add(new Product("msi-apache-pro", 1188, "USD",
                "Good choice for gaming", laptop, amazon));

    }
}
