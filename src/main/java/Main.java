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
import com.codecool.shop.transformer.JsonTransformer;
import io.gsonfire.GsonFireBuilder;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

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
            return new ThymeleafTemplateEngine().render(new ProductController().renderProductsBySupplier(req, res));
        });

        get("/category/:name", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(new ProductController().renderProductsByCategory(req, res));
        });

        post("/users", (Request req, Response res) -> {

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
            CustomerDaoWithJdbc customerDataStore = CustomerDaoWithJdbc.getInstance();
            String param = req.params("id");
            int customerId = Integer.valueOf(param);
            Customer customer = customerDataStore.find(customerId);

            return new ThymeleafTemplateEngine().render(new ProductController().renderPayment(req, res));
        });
/*
        // this method originally was: get("/addToCart/:id" -> replaced to put("/cart/:id",
        put("/cart/:id", (Request req, Response res) -> {
            ShoppingCart.getInstance().handleAddToCart(Integer.parseInt(req.params("id")));
            return new ThymeleafTemplateEngine().render(new ProductController().renderProducts(req, res));
        });
*/
        get("/cart", (Request req, Response res) ->
                ShoppingCart.getInstance().getCartItems(), new JsonTransformer());

        put("/cart/:id", (Request req, Response res) -> {
            Integer itemID = Integer.parseInt(req.params("id"));
            ShoppingCart cart = ShoppingCart.getInstance();
            cart.addToCart(itemID);
            return cart.getCartItems();
        }, new JsonTransformer());

        get("/remove/:id", (Request req, Response res) -> {
            String param = req.params("id");
            int itemId = Integer.valueOf(param);
            ShoppingCart.getInstance().deleteProductById(itemId);
            return true;
        });

        post("/save-order", (Request req, Response res) -> {
            System.out.println("ittvagyunk");
            System.out.println(req.params("id"));
/*            CustomerDao customerDataStore = CustomerDaoWithJdbc.getInstance();
            OrderDao orderDataStore = OrderDaoWithJdbc.getInstance();
            String param = req.params("id");
            int customerId = Integer.valueOf(param);
            Customer customer = customerDataStore.find(customerId);
            Order order = new Order(customer);
            orderDataStore.add(order);
            int orderId = order.getId();
            List<LineItem> orderedItems = order.getOrderedItems().getShoppingCartContent();
            for (LineItem orderedItem : orderedItems) {
                orderedItemsDataStore.add(orderId, orderedItem);
            }*/

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
                    .create().toJson(ShoppingCart.getInstance().countItemsInTheCart());
        });

        //Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

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
