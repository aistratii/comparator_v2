package imagesearch.cache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.abs;

public class LRU<K, V>{

    private final static int capacity = 100;

    private int count = 0;
   // private final Node[] nodeList = (Node[])Array.newInstance(Node.class, capacity);
    private Node lastNode, firstNode;
    //private Stack<Integer> slots = new Stack<>();

    public LRU(){
//        for(int i = 0; i < capacity; i++)
//            slots.push(i);
    }


    private class Node{
        private K key;
        private V value;
        private Node previous, succesor;
        //private int hash;
        private int slotId;

        Node(K key, V value, Node that){
            //currentVersion++;
            //slotId = slots.pop();
            count++;
            this.key = key;
            this.value = value;
            //this.hash = hash(key);
            setAfter(that);
        }

        void setAfter(Node that){
            //nodeList[hash] = this;
            this.previous = that;

            if (that != null)
                that.succesor = this;
        }

        void remove(){
            if (previous != null)
                previous.succesor = null;

            if (succesor != null)
                succesor.previous = null;

            if (previous != null && succesor != null){
                previous.succesor = succesor;
                succesor.previous = previous;
            }

            if (succesor != null && succesor.previous == null)
                firstNode = succesor;

            //nodeList[hash] = null;
            this.previous = null;
            this.succesor = null;

            System.gc();
        }

        V getValue(){
            return value;
        }

    }


    public boolean exists(K key){
        //return //nodeList[hash(key)] != null;
        if (firstNode == null)
            return false;
        else {
            Node iterator = firstNode;
            while (iterator.succesor != null) {
                if (iterator.key.equals(key))
                    return true;
                else
                    iterator = iterator.succesor;
            }
            return false;
        }
    }

    public V get(K key){
        //Node node = nodeList[hash(key)];

        Node node = firstNode;
        while(node.succesor != null){
            if (node.key.equals(key))
                break;
            else
                node = node.succesor;
        }

        if (lastNode == node)
            lastNode = node.previous;
        node.remove();
        node.setAfter(lastNode);
        lastNode = node;

        return node.getValue();
    }


    public void push(K key, V value) {
        if (count >= capacity) {
            firstNode.remove();
            count--;
        }

        if(lastNode == null) {
            lastNode = new Node(key, value, lastNode);
            firstNode = lastNode;
        } else {
            lastNode = new Node(key, value, lastNode);
        }
    }

    public List<K> getKeys(){
        List<K> result = new ArrayList<>();

        Node iterator = firstNode;
        while(iterator != null) {
            result.add(iterator.key);
            iterator = iterator.succesor;
        } ;

        return result;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : abs((h = key.hashCode()) ^ (h >>> 16));
    }

}
