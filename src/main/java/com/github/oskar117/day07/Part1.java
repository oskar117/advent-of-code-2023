package com.github.oskar117.day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

class Part1 {

    private final static String FILE_PATH = "input/day07/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        Hand[] hands = Files.readAllLines(path).stream().map(Hand::new).sorted(Comparator.reverseOrder()).toArray(Hand[]::new);
        int result = 0;
        for (int i = 0; i < hands.length; i++) {
            result += (i + 1) * hands[i].bid;
        }
        System.out.println("Total winnings are " + result);
    }

    static class Hand implements Comparable<Hand> {
        Card[] cards;
        int bid;
        int handPower;

        public Hand(String inputLine) {
            String[] splitInput = inputLine.split(" ");
            this.cards = Arrays.stream(splitInput[0].split("")).map(Card::new).toArray(Card[]::new);
            this.bid = Integer.parseInt(splitInput[1]);
            this.handPower = calculatePower();
        }

        int calculatePower() {
            Map<Card, Long> collect = Arrays.stream(cards).sorted().collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
            Map.Entry<Card, Long> max = Collections.max(collect.entrySet(), Map.Entry.comparingByValue());
            if (collect.size() == 1) return 20;
            else if (collect.size() == 2 && max.getValue() == 4) return 19;
            else if (collect.size() == 2 && max.getValue() == 3) return 18;
            else if (collect.size() == 3 && max.getValue() == 3) return 17;
            else if (collect.size() == 3 && max.getValue() == 2) return 16;
            else if (collect.size() == 4 && max.getValue() == 2) return 15;
            else return max.getValue().intValue();
        }

        @Override
        public int compareTo(Hand hand) {
            if (this.handPower != hand.handPower) return hand.handPower - this.handPower;
            for (int i = 0; i < this.cards.length; i++) {
                int compareValue = cards[i].compareTo(hand.cards[i]);
                if (compareValue != 0) return compareValue;
            }
            return 0;
        }
    }

    static class Card implements Comparable<Card> {
        String symbol;
        int power;

        public Card(String symbol) {
            this.symbol = symbol;
            this.power = switch (symbol) {
                case "A" -> 14;
                case "K" -> 13;
                case "Q" -> 12;
                case "J" -> 11;
                case "T" -> 10;
                default -> Integer.parseInt(symbol);
            };
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Card card = (Card) o;
            return power == card.power && Objects.equals(symbol, card.symbol);
        }

        @Override
        public String toString() {
            return symbol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol, power);
        }

        @Override
        public int compareTo(Card card) {
            return card.power - this.power;
        }
    }
}
