$(document).ready(function () {
    var url = "/cart";
    $.get(url, function (data) {
        var cartItems = JSON.parse(data);
        for (var i=0; i<cartItems.length; i++) {
            renderItemData(cartItems[i])
        }
    });

    $("#shoppingcart").on('focus','input.quantity-input',function(){
        itemId = $(this).closest(".cart-item").attr('id');
        enableCheckButton(itemId);
    });

    $("#shoppingcart").on('focus','button.delete-item',function(){
        itemId = $(this).closest(".cart-item").attr('id');
        removeCartItem(itemId);
        $(this).closest(".cart-item").remove();
    });

    $("#shoppingcart").on('focus','button.save-changes',function(){
        itemId = $(this).closest(".cart-item").attr('id');
        newQuantity = $(this).closest('.cart-item').find('.quantity-input').val();
        console.log(newQuantity);
        changeItemQuantity(itemId, newQuantity);
        disableCheckButton(itemId);
    });



});

function renderItemData(item){
    $("#shoppingcart").append(
        '<tr class="cart-item" id="' + item.product.id + '">' +
        '<td><img class="item-pics" src=/img/product_' + item.product.id + '.jpg /></td>' +
        '<td>' + item.product.name + '</td>' +
        '<td><input id="change-quantity' + item.product.id + '" class="quantity-input" ' +
            'type="text" name="quantity" value="' + item.quantity+'"></td>' +
        '<td> <button type="button" class ="save-changes" id="save-changes' + item.product.id + '" disabled="true" ' +
            'data-toggle="tooltip" data-placement="top" title="Save changes">&#10003;</button></td>' +
        '<td>' + item.price + " " + item.product.defaultCurrency + '</td>' +
        '<td> <button type="button" class="delete-item" data-toggle="tooltip" data-placement="top"' +
        'title="Delete item">X</button> </td></tr>'
    );
}

function enableCheckButton(productId) {
    $("#save-changes" + productId).prop("disabled", false);
    }


function disableCheckButton(productId) {
    $("#save-changes" + productId).prop("disabled", true);
}

function removeCartItem(itemId) {
    var url = "/remove/" + itemId;
    $.get(url, function (data) {
    });
}

function changeItemQuantity(itemId, newQuantity) {
    var url = "/change-quantity/" + itemId + "/" +  newQuantity;
    $.get(url, function (data) {
    });
}






