package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem(10000, 10, "시골 JPA");
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 상품 종류 수 체크", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격 체크", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("상품 재고 체크", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 주문수량초과() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem(10000, 10, "시골 JPA");
        int orderCount = 11;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 부족 예외 발생해야함");

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem(10000, 10, "시골 JPA");

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 상태 체크", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고 복원 체크", 10, book.getStockQuantity());
    }

    private Item createItem(int price, int stockQuantity, String 시골_jpa) {
        Item book = new Book();
        book.setName(시골_jpa);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address( "서울","흑석로", "123-456"));
        em.persist(member);
        return member;
    }

}
