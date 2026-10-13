package com.budiluhur.product_catalog;

import com.budiluhur.product_catalog.exception.ProductNotFoundException;
import com.budiluhur.product_catalog.model.Product;
import com.budiluhur.product_catalog.service.ProductService;
import java.util.Locale;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApplication.class, args);
    }

    // Runner sederhana untuk menguji Injection
    @Bean
    public CommandLineRunner run(ProductService productService) {
        return args -> {
            System.out.println("=== SPRING BOOT PRODUCT CATALOG IS RUNNING ===");
            productService.createProduct(
                    new Product("PRD-03", "Monitor Gaming 24 Inch", 2100000.0)
            );
            System.out.println("Daftar Produk di Spring Container:");
            productService.getAllProducts().forEach(System.out::println);

            System.out.println();
            System.out.println("=== INFORMASI HARGA & DISKON ===");
            printDiscountInfo(productService, "PRD-01", 10.0);
            System.out.println();
            printDiscountInfo(productService, "PRD-02", 0);
        };
    }

    private void printDiscountInfo(ProductService productService, String id, double discount)
            throws ProductNotFoundException {
        Product product = productService.getProductById(id);
        System.out.println("[" + product.getId() + "] " + product.getName());
        System.out.println("Harga Normal: " + formatRupiah(product.getPrice()));
        // shortcut: hanya nol yang dicetak tanpa ".0", lihat spesifikasi output
        String discountText = discount == 0 ? "0" : String.valueOf(discount);
        System.out.println("Diskon : " + discountText + "% ("
                + (discount > 0 ? "Ada Diskon" : "Tidak Ada Diskon") + ")");
        System.out.println("Harga Akhir: " + formatRupiah(productService.getDiscountedProductPrice(id, discount)));
    }

    private String formatRupiah(double price) {
        return String.format(Locale.of("id", "ID"), "Rp%,.2f", price);
    }
}
