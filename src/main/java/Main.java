import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.*;
import io.gsonfire.GsonFireBuilder;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.util.List;
import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static void main(String[] args) {

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
           return new ThymeleafTemplateEngine().render( new ProductController().renderProducts(req, res) );
        });

        get("/supplier/:name", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render( new ProductController().renderProductsBySupplier(req, res) );
        });

        get("/category/:name", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render( new ProductController().renderProductsByCategory(req, res) );
        });

        get("/addToCart/:id", (Request req, Response res) -> {
            ShoppingCart.getInstance().handleAddToCart(Integer.parseInt(req.params("id")));
            return new ThymeleafTemplateEngine().render( new ProductController().renderProducts(req, res) );
        });

        post("/saveUserData", (Request req, Response res) -> {
            User newUser = User.createUser(req.queryParams("name"), req.queryParams("email"), req.queryParams("phone"), req.queryParams("billingAddress"), req.queryParams("shippingAddress"));
            ShoppingCart shoppingCart = ShoppingCart.getInstance();
            List<LineItem> shoppingCartContent = shoppingCart.getCartItems();
            OrderDao orderDataStore = OrderDaoMem.getInstance();
            Order newOrder = new Order(shoppingCartContent);
            orderDataStore.add(newOrder);
            ShoppingCart.setInstanceToNull(null);
            ShoppingCart shoppingCart1 = ShoppingCart.getInstance();
            return "Ok";
        });

        get("/payment", (Request req, Response res) -> {
            System.out.println("/payment root");
            return new ThymeleafTemplateEngine().render( new ProductController().renderPayment(req, res) );
        });

        get("/cart", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson( ShoppingCart.getInstance().getCartItems());
        });

        put("/cart", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson( ShoppingCart.getInstance().getCartItems());
        });

        get("/remove/:id", (Request req, Response res) -> {
            String param = req.params("id");
            int itemId = Integer.valueOf(param);
            ShoppingCart.getInstance().deleteProductById(itemId);
            return true;
        });

        get("/change-quantity/:id/:quantity", (Request req, Response res) -> {
            String id = req.params("id");
            String quantity = req.params("quantity");
            int itemId = Integer.valueOf(id);
            int newQuantity = Integer.valueOf(quantity);
            System.out.println(newQuantity);
            ShoppingCart.getInstance().findLineItemById(itemId).setQuantity(newQuantity);
            return true;
        });

        get("/counter", (Request req, Response res) -> {
            return new GsonFireBuilder()
                    .enableExposeMethodResult()
                    .createGsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson( ShoppingCart.getInstance().countItemsInTheCart());
        });

        //Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();


        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);
        Supplier DELL = new Supplier("DELL", "Computers");


        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "A portable computer with little weight and long battery life.");
        productCategoryDataStore.add(laptop);

        //setting up products and printing it
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
        productDataStore.add(new Product("Dell Inspiron 5559-I5G159LE", 594, "USD", "Very gut very strong you should buy it, different colors available.", laptop, DELL));
        productDataStore.add(new Product("msi-apache-pro", 1188, "USD", "Good choice for gaming", laptop, amazon));
    }


}
