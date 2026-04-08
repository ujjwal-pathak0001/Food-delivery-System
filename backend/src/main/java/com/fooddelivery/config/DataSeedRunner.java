package com.fooddelivery.config;

import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeedRunner implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public DataSeedRunner(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        if (restaurantRepository.count() > 0) {
            return;
        }

        Restaurant r1 = new Restaurant();
        r1.setName("Punjabi Tadka");
        r1.setCuisine("North Indian · Biryani");
        r1.setRating(4.4);
        r1.setDeliveryTimeMinutes(32);
        r1.setArea("Koramangala");
        r1.setDescription("Dum biryani and creamy curries, Swiggy-style favourites.");
        r1.setImageUrl("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&q=80");
        r1 = restaurantRepository.save(r1);

        addItem(r1, "Hyderabadi Chicken Biryani", "Long-grain basmati, saffron, slow-cooked.", new BigDecimal("289"), false);
        addItem(r1, "Paneer Butter Masala", "Rich tomato gravy with fenugreek.", new BigDecimal("249"), true);
        addItem(r1, "Garlic Naan", "Tandoor-baked flatbread.", new BigDecimal("55"), true);

        Restaurant r2 = new Restaurant();
        r2.setName("Wok & Roll");
        r2.setCuisine("Chinese · Asian");
        r2.setRating(4.2);
        r2.setDeliveryTimeMinutes(28);
        r2.setArea("Indiranagar");
        r2.setDescription("Street-style momos and noodle bowls.");
        r2.setImageUrl("https://images.unsplash.com/photo-1563245372-f21724e3856d?w=800&q=80");
        r2 = restaurantRepository.save(r2);

        addItem(r2, "Chilli Paneer Dry", "Indo-Chinese classic.", new BigDecimal("229"), true);
        addItem(r2, "Hakka Noodles", "Wok-tossed vegetables.", new BigDecimal("199"), true);
        addItem(r2, "Chicken Manchurian", "Crispy chicken in Manchurian sauce.", new BigDecimal("269"), false);

        Restaurant r3 = new Restaurant();
        r3.setName("South Rasa");
        r3.setCuisine("South Indian");
        r3.setRating(4.6);
        r3.setDeliveryTimeMinutes(24);
        r3.setArea("HSR Layout");
        r3.setDescription("Crispy dosas, filter coffee, and meals.");
        r3.setImageUrl("https://images.unsplash.com/photo-1630383249896-424e482df921?w=800&q=80");
        r3 = restaurantRepository.save(r3);

        addItem(r3, "Masala Dosa", "Potato masala with chutneys.", new BigDecimal("89"), true);
        addItem(r3, "Mini Idli (10 pcs)", "Soft idlis with sambar.", new BigDecimal("79"), true);
        addItem(r3, "Filter Coffee", "Traditional decoction with milk.", new BigDecimal("45"), true);
    }

    private void addItem(Restaurant r, String name, String desc, BigDecimal price, boolean veg) {
        MenuItem m = new MenuItem();
        m.setRestaurant(r);
        m.setName(name);
        m.setDescription(desc);
        m.setPrice(price);
        m.setVegetarian(veg);
        m.setImageUrl(r.getImageUrl());
        menuItemRepository.save(m);
    }
}
