package game.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import game.model.Card;
import javafx.scene.image.Image;
import game.controller.LanguageController;

public class CardImageManager {

    // Value 1->6 : 8 variants each (indices 0..47), Values 7->14 (lamas) : 1 variant each (indices 48..55)
    private static final int[] VALUE_START = {0, 0, 8, 16, 24, 32, 40, 48, 49, 50, 51, 52, 53, 54, 55};
    private static final int[] VALUE_COUNT = {0, 8, 8, 8, 8, 8, 8, 1, 1, 1, 1, 1, 1, 1, 1};

    // Mapping card -> image index assigned (visual stability between refreshes)
    private final Map<Card, Integer> cardImageIndex = new IdentityHashMap<>();

    private final Random random = new Random();

    /**
     * Synchronise the internal mapping with the current hand of the player.
     * Removes cards that are no longer in the hand.
     */
    public void syncWith(List<Card> hand) {
        Map<Card, Boolean> handSet = new IdentityHashMap<>();
        for (Card c : hand) handSet.put(c, Boolean.TRUE);
        cardImageIndex.keySet().retainAll(handSet.keySet());
    }

    /**
     * Returns the image index assigned to a card in the hand.
     * If the card doesn't have one yet, it picks an unused index from the available variants.
     */
    public int getOrAssignIndex(Card card, List<Card> hand) {
        if (!cardImageIndex.containsKey(card)) {
            Set<Integer> usedIndices = new HashSet<>(cardImageIndex.values());
            int idx = pickUniqueIndex(card, usedIndices);
            cardImageIndex.put(card, idx);
        }
        return cardImageIndex.get(card);
    }

    /**
     * Returns a random index for the value of the card (used for the discard pile).
     */
    public int randomIndexFor(Card card) {
        int key = valueKey(card);
        return VALUE_START[key] + random.nextInt(VALUE_COUNT[key]);
    }

    /**
     * Loads the image resources/images/french_cards/{index}.png or the resources/images/english_cards/{index}.png depending on the current locale.
     * Returns a 1x1 transparent image as a fallback if the file is absent.
     */
    public Image loadImageByIndex(int index) {
        String path = LanguageController.getString("card.image.path") + index + ".png";
        var url = getClass().getClassLoader().getResource(path.startsWith("/") ? path.substring(1) : path);
        if (url != null) {
            return new Image(url.toExternalForm());
        }
        // Fallback : transparent 1x1 PNG (should not happen if resources are correctly included)
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
    }

    /**
     * Chooses a unique image index for the card in the hand.
     * If all indices for the value are taken, returns any index for the value.
     */
    private int pickUniqueIndex(Card card, Set<Integer> usedIndices) {
        int key = valueKey(card);
        int start = VALUE_START[key];
        int count = VALUE_COUNT[key];

        List<Integer> available = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            if (!usedIndices.contains(i)) {
                available.add(i);
            }
        }

        if (!available.isEmpty()) {
            return available.get(random.nextInt(available.size()));
        }
        // Fallback : all taken (hand > number of variants), duplicate allowed
        return start + random.nextInt(count);
    }

    /**
     * Converts the value of a card into a table key (1-14).
     */
    private int valueKey(Card card) {
        return card.getValue();
    }
}

