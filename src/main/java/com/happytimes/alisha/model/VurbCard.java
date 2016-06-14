package com.happytimes.alisha.model;

import java.util.List;

/**
 * Created by alishaalam on 2/3/16.
 */
public class VurbCard {

    List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "VurbCard{" +
                "cards=" + cards +
                '}';
    }

}
