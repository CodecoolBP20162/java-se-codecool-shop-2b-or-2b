$(document).ready(function () {
    var url = "/cart";
    $.get(url, function (data) {
        var cartItems = JSON.parse(data);
        for (var i=0; i<cartItems.length; i++) {
            renderItemData(cartItems[i])
        }
    });


    $("#paymentButton").click(function(event){
        event.preventDefault();
        var name = $("#name-input").val();
        var email = $("#email-input").val();
        var phone = $("#phone-input").val();
        var billingAddress = $("#address-input").val();
        var shippingAddress = $("#shipping-input").val();

        payment(name, email, phone, billingAddress, shippingAddress);
    });

    console.log(name);
});

function renderItemData(item){
    $("#shoppingcart").append(
        '<tr class="cart_item">' +
        '<td><img class="item-pics" src=/img/product_' + item.product.id + '.jpg /></td>' +
        '<td>' + item.product.name + '</td>' +
        '<td><input class="quantity-input" type="text" name="quantity" value="' + item.quantity+'"></td>' +
        '<td> <button type="button" id="save-changes' + item.product.id + '" disabled="true">&#10003;</button></td>' +
        '<td>' + item.price + " " + item.product.defaultCurrency + '</td>' +
        '<td> <button type="button" class="delete-item">X</button> </td></tr>'
    );
}

function disableCheckButton(item) {
    $(".save-changes" + item.product.id).prop("disabled", false);

    }

$( "#target" ).click(function() {

});


function payment(name, email, phone, billingAdress, shippingAddress){

    $.post("/saveUserData", {"name": name, "email": email, "phone": phone, "billingAddress": billingAdress, "shippingAddress": shippingAddress}, function(data){
        console.log()
    });

}




