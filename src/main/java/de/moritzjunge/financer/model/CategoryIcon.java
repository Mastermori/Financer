package de.moritzjunge.financer.model;

public enum CategoryIcon {

    SHOPPING_CART("fa-cart-shopping");

    final String cssClass;
    CategoryIcon(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

}
