package com.trinity.cart.infrastructure.persistence.entity;

import com.trinity.cart.domain.model.CartStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Anemic JPA persistence record for the cart aggregate. The rich behavior lives
 * in {@link com.trinity.cart.domain.Cart}; this entity is mapped to/from the
 * domain by the CartPersistenceMapper.
 *
 * <p>Note: this is the first @OneToMany in the codebase. We deliberately use
 * @EqualsAndHashCode(of = "id") (never @Data) so equality never traverses the
 * items collection, and a List (not a Set) to avoid the @Data-on-mutable-member
 * hashCode hazard.
 */
@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(columnNames = "customer_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false, unique = true)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus status;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount", precision = 19, scale = 2, nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, nullable = false))
    })
    private MoneyEmbeddable totalAmount;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();

    @Version
    private Long version;
}
