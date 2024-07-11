package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.WishRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishService {
    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Transactional(readOnly = true)
    public List<Wish> getWishesByMemberId(Long memberId) {
        return wishRepository.findAllByMemberId(memberId);
    }

    @Transactional
    public void addWish(Long memberId, Long productId) {
        Wish wish = new Wish.WishBuilder().member(new Member.MemberBuilder()
            .id(memberId).build()).product(new Product.ProductBuilder().id(productId).build()).build();
        wishRepository.save(wish);
    }

    @Transactional
    public void removeWish(Long memberId, Long productId) {
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
