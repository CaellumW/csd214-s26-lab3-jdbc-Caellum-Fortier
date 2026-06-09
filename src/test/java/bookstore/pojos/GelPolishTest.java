package bookstore.pojos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GelPolishTest {

    @Test
    void testSellItemDecrementsStock(){
        GelPolish polishOne = new GelPolish("Blue", "Matte", "Nyx", 5, 5);
        IO.println(polishOne);
        polishOne.setStock(5);
        polishOne.sellItem();
        assertEquals(4, polishOne.getStock());
    };
}