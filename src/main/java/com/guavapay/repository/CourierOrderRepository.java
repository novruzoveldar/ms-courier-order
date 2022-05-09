package com.guavapay.repository;

import com.guavapay.model.entity.Account;
import com.guavapay.model.entity.CourierOrder;
import com.guavapay.model.entity.Parcel;
import com.guavapay.model.request.CourierOrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface CourierOrderRepository extends JpaRepository<CourierOrder, Long>, JpaSpecificationExecutor<CourierOrder> {

    int DEFAULT_LIMIT = 50;

    @Query("FROM CourierOrder co WHERE co.parcel.id = :parcelId")
    CourierOrder getCourierOrderByParcelId(@Param(value = "parcelId") Long parcelId);

    @Query("FROM CourierOrder co " +
            "JOIN FETCH co.parcel p " +
            "JOIN FETCH p.account a " +
            "JOIN FETCH co.courier c " +
            "where co.parcel.id = :parcelId")
    Optional<CourierOrder> getCourierOrderDetail(@Param(value = "parcelId") Long parcelId);

    default List<CourierOrder> findCourierOrderByCriteria(CourierOrderFilter filter, Account account) {
        int page = filter.getPage();
        int limit = (filter.getLimit() == 0 || filter.getLimit() > DEFAULT_LIMIT) ? DEFAULT_LIMIT : filter.getLimit();

        Pageable pageable = PageRequest.of(page, limit);
        Page<CourierOrder> orderPage = findAll((Specification<CourierOrder>) (root, query, criteriaBuilder) -> {

            if (CourierOrder.class.equals(query.getResultType())) {
                Fetch<CourierOrder, Parcel> parcelFetch = root.fetch("parcel");
                parcelFetch.fetch("account");
                root.fetch("courier");
            }
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(account)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("parcel").get("account"), account)));
            }
            if(Objects.nonNull(filter.getCourierId())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("courier").get("id"), filter.getCourierId())));
            }
            if(Objects.nonNull(filter.getParcelId())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("parcel").get("id"), filter.getParcelId())));
            }
            if (!CollectionUtils.isEmpty(filter.getStates())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("state")).value(filter.getStates())));
            }
            if (Objects.nonNull(filter.getFrom())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryDate"), filter.getFrom())));
            }
            if (Objects.nonNull(filter.getTo())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryDate"), filter.getTo())));
            }
            query.orderBy(criteriaBuilder.desc(root.get("deliveryDate")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

        return orderPage.getContent();
    }
}
