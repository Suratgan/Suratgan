package com.suratgan.backend.review.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

/**
 * 1. 리뷰 작성시 주문자와 로그인한 사용자가 같은지 체크
 * 2. 리뷰 수정인 경우 리뷰 작성자가 로그인한 사용자와 같은지도 체크
 *    (주문번호는 최초 등록시에만 수정이 되므로 수정일땐 체크 불필요)
 *    관리자(MANAGER, MASTER)는 권한 체크 필요없이 항상 가능(추가, 수정)
 * 3.리뷰는 주문상태가 완료(ORDER_DONE)로 변경이 되면(최종 주문상태) 리뷰를 작성할 수 있다.
 * 4. 리뷰는 주문을 한 사용자만 작성 가능
 * 5. 리뷰의 평점은 필수이며 1~5점 사이 선택
 * 6. 리뷰 작성 또는 수정이 완료되면 평점에대한 평균을 주문에 해당하는 상점의 평점에 업데이트 합니다(이벤트 발행)
 * 7. 하나의 주문, 하나의 리뷰를 작성하는 원칙
 */
@Entity
@Getter
@ToString
@Access(AccessType.FIELD)
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="P_REVIEW", indexes = {
    @Index(name="idx_review_order_id", columnList = "order_id, deleted_at", unique = true)
})
public class Review extends BaseEntity {

    @EmbeddedId
    private ReviewId id;

    @Embedded
    private Reviewer reviewer;

    @Embedded
    private ReviewOrderInfo info;

    @Embedded
    private ReviewContent content;
}
