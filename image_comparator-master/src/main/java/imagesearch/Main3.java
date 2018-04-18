package imagesearch;

import imagesearch.cache.LRU;

public class Main3 {
    public static void main(String args[]){
        LRU<String, String> lru = new LRU<>();

        lru.push("asd", "first");

        System.out.println(lru.exists("asd"));

        lru.push("gf", "second");
        System.out.println(lru.exists("asd"));
        System.out.println(lru.exists("gf"));


        lru.push("adfssd", "third");
        System.out.println(lru.getKeys());

        lru.get("asd");
        System.out.println(lru.getKeys());

        lru.get("asd");
        System.out.println(lru.getKeys());

        lru.push("idkfa1", "some");
        System.out.println(lru.getKeys());

        lru.push("idkfa2", "some");
        System.out.println(lru.getKeys());

        lru.push("idkfa3", "some");
        System.out.println(lru.getKeys());
    }
}
