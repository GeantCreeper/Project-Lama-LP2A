package game.gui;

import game.model.Card;
import javafx.scene.image.Image;

import java.util.*;

public class CardImageManager {

    // Valeur 1->6 : 8 variantes chacune (indices 0..48), Lama (index 7) : 8 variantes (indices 49..56)
    private static final int[] VALUE_START = {0, 0, 8, 16, 24, 32, 40, 48, 56};
    private static final int[] VALUE_COUNT = {0, 8, 8, 8, 8, 8, 8, 8, 8, 8};

    // Mapping carte -> index image assigné (stabilité visuelle entre les refresh)
    private final Map<Card, Integer> cardImageIndex = new IdentityHashMap<>();

    private final Random random = new Random();

    /**
     * Synchronise le mapping interne avec la main courante du joueur.
     * Supprime les cartes qui ne sont plus dans la main.
     */
    public void syncWith(List<Card> hand) {
        Map<Card, Boolean> handSet = new IdentityHashMap<>();
        for (Card c : hand) handSet.put(c, Boolean.TRUE);
        cardImageIndex.keySet().retainAll(handSet.keySet());
    }

    /**
     * Retourne l'index image assigné à une carte de la main.
     * Si la carte n'en a pas encore, en choisit un non utilisé par les autres cartes.
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
     * Retourne un index aléatoire pour la valeur de la carte (utilisé pour la défausse).
     */
    public int randomIndexFor(Card card) {
        int key = valueKey(card);
        return VALUE_START[key] + random.nextInt(VALUE_COUNT[key]);
    }

    /**
     * Charge l'image resources/images/cards/{index}.png depuis le classpath.
     * Retourne une image transparente 1x1 en fallback si le fichier est absent.
     */
    public Image loadImageByIndex(int index) {
        String path = "/images/cards/" + index + ".png";
        var url = getClass().getResource(path);
        if (url != null) {
            return new Image(url.toExternalForm());
        }
        // Fallback : pixel transparent
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
    }

    /**
     * Choisit un index image non encore utilisé dans la main pour cette carte.
     * Si tous les indices de la valeur sont pris, retourne un index quelconque de la valeur.
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
        // Fallback : tous pris (main > nb variantes), doublon accepté
        return start + random.nextInt(count);
    }

    /**
     * Convertit la valeur d'une carte en clé de tableau (1-6, ou 7 pour Lama).
     */
    private int valueKey(Card card) {
        return card.isLama() ? 7 : card.getValue();
    }
}
