package Bomberman;

//Data om de Keys op te slaan en bij te houden waar wat blijft staan en welke keys waar gebruikt worden
public class BomberKeyQueue
{
    private class Node
    {

        public byte data;
        public Node prev;
        public Node next;

        public Node(byte byte0)
        {
            data = 0;
            prev = null;
            next = null;
            data = byte0;
        }

        public Node(Node node)
        {
            data = 0;
            prev = null;
            next = null;
            data = node.data;
            prev = node.prev;
            next = node.next;
        }
    }


    private Node head;
    private Node tail;
    private int totalItems;

    public BomberKeyQueue()
    {
        head = null;
        tail = null;
        totalItems = 0;
    }

    public void push(byte byte0)
    {
        Node node = new Node(byte0);
        if(head == null)
        {
            head = tail = node;
        } else
        {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        totalItems++;
    }

    public byte pop()
    {
        byte byte0 = 0;
        if(tail != null)
        {
            byte0 = tail.data;
            tail = tail.prev;
            if(tail != null)
            {
                tail.next = null;
            } else
            {
                head = tail;
            }
            totalItems--;
        }
        return byte0;
    }

    public void removeItems(byte byte0)
    {
        for(Node node = head; node != null; node = node.next)
        {
            if(node.data == byte0)
            {
                if(node.prev != null)
                {
                    node.prev.next = node.next;
                }
                if(node.next != null)
                {
                    node.next.prev = node.prev;
                }
                if(node == head)
                {
                    head = node.next;
                }
                if(node == tail)
                {
                    tail = node.prev;
                }
                totalItems--;
            }
        }

    }

    public void removeAll()
    {
        head = tail = null;
    }

    public int size()
    {
        return totalItems;
    }

    public byte getLastItem()
    {
        byte byte0 = 0;
        if(tail != null)
        {
            byte0 = tail.data;
        }
        return byte0;
    }

    public boolean contains(byte byte0)
    {
        boolean flag = false;
        for(Node node = head; node != null; node = node.next)
        {
            if(node.data != byte0)
            {
                continue;
            }
            flag = true;
            break;
        }

        return flag;
    }
}
