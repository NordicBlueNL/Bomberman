package Bomberman;

//Data om de Keys op te slaan en bij te houden waar wat blijft staan en welke keys waar gebruikt worden
public class BomberKeyQueue
{
	private class Node
	{
		//data die deze laad
		public byte data = 0;
		//vorige knooppunt
		public Node vorige = null;
		//volgende knooppunt
		public Node volgende = null;

		public Node(byte byte0)
		{
			this.data = byte0;
		}
	}

	//knooppunt hoofd
	private Node head;
	//knooppunt lichaam
	private Node tail;
	//knooppunt totaal aantal items
	private int totaalItems;

	public BomberKeyQueue()
	{
		this.head = null;
		this.tail = null;
		this.totaalItems = 0;
	}

	public void push(byte byte0)
	{
		Node node = new Node(byte0);
		if(head == null)
		{
			head = tail = node;
		} else
		{
			tail.volgende = node;
			node.vorige = tail;
			tail = node;
		}
		totaalItems++;
	}

	public byte pop()
	{
		byte byte0 = 0;
		if(tail != null)
		{
			byte0 = tail.data;
			tail = tail.vorige;
			if(tail != null)
			{
				tail.volgende = null;
			} else
			{
				head = tail;
			}
			totaalItems--;
		}
		return byte0;
	}

	public void verwijderItems(byte byte0)
	{
		for(Node node = head; node != null; node = node.volgende)
		{
			if(node.data == byte0)
			{
				if(node.vorige != null)
				{
					node.vorige.volgende = node.volgende;
				}
				if(node.volgende != null)
				{
					node.volgende.vorige = node.vorige;
				}
				if(node == head)
				{
					head = node.volgende;
				}
				if(node == tail)
				{
					tail = node.vorige;
				}
				totaalItems--;
			}
		}
	}

	public void verwijderAlles()
	{
		head = tail = null;
	}

	public int grootte()
	{
		return totaalItems;
	}

	public byte haalLaatsteItemOp()
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
		for(Node node = head; node != null; node = node.volgende)
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