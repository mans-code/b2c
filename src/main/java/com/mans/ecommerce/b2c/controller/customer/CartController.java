package com.mans.ecommerce.b2c.controller.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/cart/:cartId")
public class CartController
{

    @GetMapping("/")
    public String getItems()
    {

    }

    @PostMapping("/:productId")
    public String addItem()
    {

        return "";
    }


    @DeleteMapping("/:productId")
    public String deleteOne()
    {
        return delete("");
    }

    @DeleteMapping("/deleteAll")
    public String deleteAll()
    {
        return delete(new ArrayList<String>());
    }

    @DeleteMapping("/deleteSome")
    public String deleteSome()
    {
        return "";
    }

    private String delete(String prodcutId)
    {
        return "";
    }

    private String delete(List<String> productIds)
    {
        return "";
    }
}
