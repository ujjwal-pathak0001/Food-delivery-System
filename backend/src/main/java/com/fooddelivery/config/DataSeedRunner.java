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

        // Punjabi Tadka - North Indian
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
        addItem(r1, "dal Makhani", "Creamy black lentils.", new BigDecimal("179"), true);
        addItem(r1, "Tandoori Chicken", "Yogurt-marinated chicken.", new BigDecimal("299"), false);
        addItem(r1, "Chole Bhature", "Spiced chickpeas with fried bread.", new BigDecimal("149"), true);

        // Wok & Roll - Chinese
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
        addItem(r2, "Veg Spring Rolls (4pcs)", "Crispy vegetable rolls.", new BigDecimal("119"), true);
        addItem(r2, "Chicken Fried Rice", "Stir-fried basmati with chicken.", new BigDecimal("229"), false);
        addItem(r2, "Shrimp Garlic Noodles", "Prawns in garlic sauce.", new BigDecimal("289"), false);

        // South Rasa - South Indian
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
        addItem(r3, "Uttapam", "Savory pancake with veggies.", new BigDecimal("99"), true);
        addItem(r3, "Sambhar Rice", "Rice with lentil vegetable stew.", new BigDecimal("85"), true);
        addItem(r3, "Chicken Biryani (South Style)", "Telangana style with raita.", new BigDecimal("249"), false);

        // Burger Barn - Fast Food
        Restaurant r4 = new Restaurant();
        r4.setName("Burger Barn");
        r4.setCuisine("Burgers & Fries");
        r4.setRating(4.1);
        r4.setDeliveryTimeMinutes(16);
        r4.setArea("MG Road");
        r4.setDescription("Juicy burgers, crispy fries, and thick shakes.");
        r4.setImageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80");
        r4 = restaurantRepository.save(r4);

        addItem(r4, "Classic Cheeseburger", "Beef patty with cheddar and sauce.", new BigDecimal("199"), false);
        addItem(r4, "Veggie Burger", "Plant-based patty with lettuce.", new BigDecimal("179"), true);
        addItem(r4, "Sweet Potato Fries", "Golden fried sweet potato.", new BigDecimal("129"), true);
        addItem(r4, "Chicken Burger", "Grilled chicken with mayo.", new BigDecimal("189"), false);
        addItem(r4, "Chocolate Milkshake", "Rich and creamy.", new BigDecimal("109"), true);
        addItem(r4, "Double Bacon Burger", "Two patties, double bacon.", new BigDecimal("259"), false);

        // Pizza Palace - Italian
        Restaurant r5 = new Restaurant();
        r5.setName("Pizza Palace");
        r5.setCuisine("Italian · Pizza");
        r5.setRating(4.5);
        r5.setDeliveryTimeMinutes(30);
        r5.setArea("Whitefield");
        r5.setDescription("Wood-fired pizzas and fresh pasta.");
        r5.setImageUrl("https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=800&q=80");
        r5 = restaurantRepository.save(r5);

        addItem(r5, "Margherita Pizza", "Tomato, mozzarella, basil.", new BigDecimal("299"), true);
        addItem(r5, "Pepperoni Pizza", "Spicy pepperoni and cheese.", new BigDecimal("349"), false);
        addItem(r5, "Veggie Delight", "Mixed vegetables on crispy base.", new BigDecimal("279"), true);
        addItem(r5, "Spaghetti Carbonara", "Creamy pasta with bacon.", new BigDecimal("249"), false);
        addItem(r5, "Garlic Bread", "Crispy with herb butter.", new BigDecimal("89"), true);
        addItem(r5, "Tiramisu", "Classic Italian dessert.", new BigDecimal("129"), true);

        // Cafe Brew - Cafe & Bakery
        Restaurant r6 = new Restaurant();
        r6.setName("Cafe Brew");
        r6.setCuisine("Cafe & Bakery");
        r6.setRating(4.7);
        r6.setDeliveryTimeMinutes(20);
        r6.setArea("Jayanagar");
        r6.setDescription("Freshly baked pastries and specialty coffee.");
        r6.setImageUrl("https://images.unsplash.com/photo-1442512595331-e89e5f9e4b20?w=800&q=80");
        r6 = restaurantRepository.save(r6);

        addItem(r6, "Cappuccino", "Rich espresso with steamed milk.", new BigDecimal("79"), true);
        addItem(r6, "Croissant", "Buttery and flaky.", new BigDecimal("69"), true);
        addItem(r6, "Chocolate Cake", "Moist chocolate layers.", new BigDecimal("99"), true);
        addItem(r6, "Espresso", "Double shot of pure coffee.", new BigDecimal("59"), true);
        addItem(r6, "Almond Croissant", "Almond-topped croissant.", new BigDecimal("89"), true);
        addItem(r6, "Iced Latte", "Cold and refreshing.", new BigDecimal("99"), true);

        // Fresh & Organic Mart - Grocery
        Restaurant r7 = new Restaurant();
        r7.setName("Fresh & Organic Mart");
        r7.setCuisine("Grocery & Organic");
        r7.setRating(4.8);
        r7.setDeliveryTimeMinutes(40);
        r7.setArea("All Areas");
        r7.setDescription("Fresh vegetables, fruits, dairy, and organic products.");
        r7.setImageUrl("https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=800&q=80");
        r7 = restaurantRepository.save(r7);

        // Vegetables
        addItem(r7, "Tomatoes (1kg)", "Fresh red tomatoes.", new BigDecimal("45"), true);
        addItem(r7, "Onions (1kg)", "Golden onions.", new BigDecimal("35"), true);
        addItem(r7, "Carrots (500g)", "Orange and sweet.", new BigDecimal("30"), true);
        addItem(r7, "Lettuce Bundle", "Fresh leafy greens.", new BigDecimal("55"), true);
        addItem(r7, "Broccoli (500g)", "Green florets.", new BigDecimal("65"), true);
        
        // Fruits
        addItem(r7, "Bananas (bunch)", "Yellow bananas.", new BigDecimal("40"), true);
        addItem(r7, "Apples (1kg)", "Red delicious apples.", new BigDecimal("120"), true);
        addItem(r7, "Oranges (1kg)", "Fresh citrus.", new BigDecimal("85"), true);
        addItem(r7, "Grapes (500g)", "Seedless and sweet.", new BigDecimal("95"), true);
        
        // Dairy & Dry Goods
        addItem(r7, "Milk (1L)", "Fresh whole milk.", new BigDecimal("55"), true);
        addItem(r7, "Yogurt (400g)", "Probiotic yogurt.", new BigDecimal("45"), true);
        addItem(r7, "Cheddar Cheese (250g)", "Aged cheddar.", new BigDecimal("189"), true);
        addItem(r7, "Wheat Flour (1kg)", "Premium quality.", new BigDecimal("65"), true);
        addItem(r7, "Rice (1kg)", "Basmati rice.", new BigDecimal("95"), true);
        addItem(r7, "Dal (1kg)", "Split lentils.", new BigDecimal("85"), true);
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
