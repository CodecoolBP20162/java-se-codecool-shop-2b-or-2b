$(document).ready(function () {
    var url = "/cart";
    $.get(url, function (data) {
        displayShoppingCart(data);
    });


    $("#shoppingcart").on('focus', 'input.quantity-input', function () {
        var itemId = $(this).closest(".cart-item").attr('id');
        enableCheckButton(itemId);
    });

    $("#shoppingcart").on('focus', 'button.delete-item', function () {
        var itemId = $(this).closest(".cart-item").attr('id');
        removeCartItem(itemId, cartItems);
        $(this).closest(".cart-item").remove();
    });

    $("#shoppingcart").on('focus', 'button.save-changes', function () {
        var itemId = parseInt($(this).closest(".cart-item").attr('id'));
        var newQuantity = parseInt($(this).closest('.cart-item').find('.quantity-input').val());
        if (newQuantity === 0) {
            removeCartItem(itemId);
            $(this).closest(".cart-item").remove();
            showEmptyCartMessage();
        }
        changeItemQuantity(itemId, newQuantity, cartItems);
        showTotalPrice(cartItems);
        disableCheckButton(itemId);
    });

    $("#paypalModal").on('focus', 'button.submit_payment', function () {
        var id = obtainCustomerIdFromHref();
        var url = "/save-order";
        var message = {"id": id};
        $.post(url, JSON.stringify(message), function (data) {
        })
    });


    $(".submit_payment").click(function(event){
        event.preventDefault();
        var id = obtainCustomerIdFromHref();
        var url = "/save-order";
        var message = {"id": id};
        $.post(url, JSON.stringify(message), function (data) {
        });

    });

    $("#paymentButton").click(function (event) {
        event.preventDefault();
        var name = $("#name-input").val();
        var email = $("#email-input").val();
        var phone = $("#phone-input").val();
        var billingAddress = $("#address-input").val();
        var shippingAddress = $("#shipping-input").val();

        payment(name, email, phone, billingAddress, shippingAddress);
    });


    $(".add-to-cart").click(function (event) {
        event.preventDefault();
        addToCart(this);
    });
});

function displayShoppingCart(data) {
    $("#shoppingcart").empty();
    var cartItems = JSON.parse(data);
    for (var i = 0; i < cartItems.length; i++) {
        renderItemData(cartItems[i]);
    }
    if (cartItems.length > 0) {
        showTotalPrice(cartItems);
    } else {
        showEmptyCartMessage();
    }
    cartCounter();
}

function addToCart(obj) {
    var href = obj.getAttribute("href"); //href: "'/cart/' + ${prod.id}"
    $.ajax({
        type: "PUT",
        url: href,
        success: function(data) {
            displayShoppingCart(data);
        }
    });
}

function obtainCustomerIdFromHref() {
    var href_split = window.location.href.split('/');
    var id = board_split[board_split.length - 1];
    return id;
}

function renderItemData(item) {
    $('#empty-message').remove();
    $("#shoppingcart").append(
        '<tr class="cart-item" id="' + item.product.id + '">' +
        '<td><img class="item-pics" src=/img/product_' + item.product.id + '.jpg /></td>' +
        '<td>' + item.product.name + '</td>' +
        '<td><input id="change-quantity' + item.product.id + '" class="quantity-input" ' +
        'type="text" name="quantity" value="' + item.quantity + '"></td>' +
        '<td> <button type="button" class ="save-changes" id="save-changes' + item.product.id + '" disabled="true" ' +
        'data-toggle="tooltip" data-placement="top" title="Save changes">&#10003;</button></td>' +
        '<td>' + item.price + " " + item.product.defaultCurrency + '</td>' +
        '<td> <button type="button" class="delete-item" data-toggle="tooltip" data-placement="top"' +
        'title="Delete item">X</button> </td></tr>'
    );
}

function cartCounter() {
    var counter = "/counter";
    $.get(counter, function (data) {
        var cartItems = JSON.parse(data);
        $("#cartCounter").empty();
        $("#cartCounter").append(cartItems);
    });

};

function enableCheckButton(productId) {
    $("#save-changes" + productId).prop("disabled", false);
}


function disableCheckButton(productId) {
    $("#save-changes" + productId).prop("disabled", true);
}

function removeCartItem(itemId, cartItems) {
    var url = "/remove/" + itemId;
    $.get(url, function (data) {
    });
    deleteItemFromCartItems(itemId, cartItems);
    showTotalPrice(cartItems);
    cartCounter();
}

function changeItemQuantity(itemId, newQuantity, cartItems) {
    var url = "/change-quantity/" + itemId + "/" + newQuantity;
    for (var i = 0; i < cartItems.length; i++) {
        if (itemId == cartItems[i].product.id) {
            cartItems[i].quantity = newQuantity;
        }
    }
    $.get(url, function (data) {
    });
    cartCounter();
}

function deleteItemFromCartItems(itemId, cartItems){
    for (var i=0; i<cartItems.length; i++){
        if (itemId == cartItems[i].product.id){
            cartItems.pop(cartItems[i]);
        }
    }
}


function showTotalPrice(cartItems) {
    var totalPrice = 0;
    for (var i = 0; i < cartItems.length; i++) {
        var itemPrice = parseInt(cartItems[i].product.defaultPrice);
        var quantity = parseInt(cartItems[i].quantity);
        totalPrice += itemPrice * quantity;
    }
    $("#total-price").html(
        '<p id="total-price">Total price: ' + totalPrice + ' USD</p>'
    );
}

function showEmptyCartMessage() {
    $("#shoppingcart").append("<p id='empty-message'>Your cart is currently empty.</p>");
}


function payment() {
    $("#userData").submit();
}




