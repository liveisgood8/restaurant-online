package com.restaurantonline.menuservice.controller;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.utils.Cart;
import com.restaurantonline.menuservice.validation.InsertGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
  @GetMapping
  public List<Dish> getCartDishes(HttpServletRequest request) {
    final Cart cart = (Cart)request.getSession().getAttribute("cart");
    if (cart == null) {
      return new ArrayList<Dish>();
    } else {
      return cart.getAll();
    }
  }

  @PostMapping
  public void addCartDish(HttpServletRequest request,
                          @Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    Cart cart = (Cart)request.getSession().getAttribute("cart");
    if (cart == null) {
      cart = new Cart();
    }

    cart.add(dish);

    request.getSession().setAttribute("cart", cart);
  }
}
