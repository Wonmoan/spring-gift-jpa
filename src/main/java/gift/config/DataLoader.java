package gift.config;

import gift.domain.Product;
import gift.repository.ProductRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 3; i <= 10; i++) {
            Product product = new Product.ProductBuilder()
                .name("Product" + i)
                .price(BigDecimal.valueOf(10.00 * i))
                .imageUrl("http://example.com/product" + i)
                .description("Description for Product" + i)
                .build();
            productRepository.save(product);
        }
    }
}
