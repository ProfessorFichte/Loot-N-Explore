package lne.item.relics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RelicsConfig {
    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public static class Entry {
        public List<Bonus> attributes = new ArrayList<>();

        public Entry() {
        }

        public Entry(List<Bonus> attributes) {
            this.attributes = attributes;
        }
    }

    public static class Bonus {
        public String attribute = "";
        public double amount = 0;
        public String operation = "ADD_VALUE";

        public Bonus() {
        }

        public Bonus(String attribute, double amount, String operation) {
            this.attribute = attribute;
            this.amount = amount;
            this.operation = operation;
        }
    }
}
