package noodnik.ellation.vending;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

public class VendingMachine {

    @Test
    public void happyPathWalkthrough() {

        // Backend updates rules for vending machine when needed
        rules.update(Collections.EMPTY_SET);

        // Inventory updates user interface when it changes
        userInterface.updateInventory(Collections.EMPTY_SET);

        // user initiates happy path dialog with vending machine
        List<Item> availableItems = userInterface.getAvailableItems();
        Item selectedItem = availableItems.get(0);
        cashier.authorizePayment(newPmtAuth(selectedItem.getPrice()));
        userInterface.purchaseItem(selectedItem);
        delivery.retrieve(selectedItem);

        // confirm item was delivered by the vending machine
        assertEquals(Collections.singletonList(selectedItem), deliveredItems);
    }

    interface UserInterface {
        void purchaseItem(Item item);
        List<Item> getAvailableItems();
        void updateAvailableCredit(MonetaryAmount amount);
        void updateInventory(Set<Item> itemUpdates);
    }

    interface Delivery {
        void deliver(Item item);
        void retrieve(Item item);
    }

    interface Cashier {
        void authorizePayment(PmtAuth authorization);
        void verifyAndHold(Price price);
        void debit(Price price);
    }

    interface Inventory {
        void verifyAndHold(Item item);
        void decrement(Item item);
    }

    interface Rules {
        void purchase(Item item);
        void validateAuthorization(PmtAuth pmtAuth);
        void acknowledgeDelivery(Item item);
        void update(Set<Rules> updatedRules);
    }

    interface Backend {
        void preAuthorize(PmtAuth pmtAuth);
        void completePayment(Price price);
    }

    interface Item {
        Price getPrice();
    }

    interface MonetaryAmount {
        BigDecimal getAmount();
    }

    interface Price {
        MonetaryAmount getMonetaryAmount();
    }

    interface PmtAuth {
        MonetaryAmount getMonetaryAmount();
    }

    Inventory inventory = new Inventory() {
        public void verifyAndHold(Item item) {
            log("inventory.verifyAndHold(%s)", item);
        }
        public void decrement(Item item) {
            log("inventory.decrement(%s)", item);
        }
    };

    UserInterface userInterface = new UserInterface() {
        public List<Item> getAvailableItems() {
            return Collections.singletonList(newItem("1.23"));
        }
        public void purchaseItem(Item item) {
            rules.purchase(item);
        }
        public void updateAvailableCredit(MonetaryAmount amount) {
            log("userInterface.updateAvailableCredit(%s)", amount);
        }

        @Override
        public void updateInventory(Set<Item> itemUpdates) {
            log("userInterface.updateInventory(%s)", itemUpdates);
        }
    };

    Cashier cashier = new Cashier() {
        public void authorizePayment(PmtAuth authorization) {
            rules.validateAuthorization(authorization);
            userInterface.updateAvailableCredit(authorization.getMonetaryAmount());
        }
        public void verifyAndHold(Price price) {
            log("cashier.verifyAndHold(%s)", price);
        }
        public void debit(Price price) {
            backend.completePayment(price);
        }
    };

    Rules rules = new Rules() {
        public void purchase(Item item) {
            inventory.verifyAndHold(item);
            cashier.verifyAndHold(item.getPrice());
            cashier.debit(item.getPrice());
            delivery.deliver(item);
            inventory.decrement(item);
        }
        public void validateAuthorization(PmtAuth pmtAuth) {
            backend.preAuthorize(pmtAuth);
        }
        public void acknowledgeDelivery(Item item) {
            log("rules.acknowledgeDelivery(%s)", item);
            deliveredItems.add(item);
        }
        public void update(Set<Rules> updatedRules) {
            log("rules.update(%s)", updatedRules);
        }
    };

    Delivery delivery = new Delivery() {
        public void deliver(Item item) {
            rules.acknowledgeDelivery(item);
        }
        public void retrieve(Item item) {
            log("delivery.retrieve(%s)", item);
        }
    };

    Backend backend = new Backend() {
        public void preAuthorize(PmtAuth preAuth) {
            log("backend.preAuthorize(%s)", preAuth);
        }
        public void completePayment(Price price) {
            log("backend.completePayment(%s)", price);
        }
    };

    Item newItem(String priceString) {
        Price price = new Price() {
            public MonetaryAmount getMonetaryAmount() {
                return newMonetaryAmount(priceString);
            }
            public String toString() {
                return "Price " + getMonetaryAmount();
            }
        };
        return new Item() {
            public Price getPrice() {
                return price;
            }
            public String toString() {
                return "Item Of " + price;
            }
        };
    }

    PmtAuth newPmtAuth(Price price) {
        return new PmtAuth() {
            public MonetaryAmount getMonetaryAmount() {
                return newMonetaryAmount(price.getMonetaryAmount().getAmount().toString());
            }
            public String toString() {
                return "PmtAuth " + getMonetaryAmount();
            }
        };
    }

    MonetaryAmount newMonetaryAmount(String priceString) {
        return new MonetaryAmount() {
            public BigDecimal getAmount() {
                return new BigDecimal(priceString);
            }
            public String toString() {
                return "Monetary Amount " + getAmount().toString();
            }
        };
    }

    List<Item> deliveredItems = new ArrayList<>();

}
