package bookstore.pojos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NailAccessoryTest {
    @Test
    void testSellItemDecrementsStock() {
        NailAccessory accessoryOne = new NailAccessory("Drill", "Nyx", 5, 5);
        accessoryOne.setStock(5);
        accessoryOne.sellItem();
        assertEquals(4, accessoryOne.getStock());
    }

}