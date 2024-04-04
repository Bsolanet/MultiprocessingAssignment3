import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Presents {
	//list to store unsorted gifts
	static int[] presents;
	static int index;
	static int n;
	
	 //linked list to store sorted list
	static ConcurrentLinkedQueue<Integer> list;
	
	public static void main(String[] args) {
		//get number of presents
		Scanner kb = new Scanner(System.in);
		System.out.print("How many presents: ");
		n = kb.nextInt();
		
		presents = new int[n];
		
		//for loop to get presents
		for(int i = 0; i < n; i++) {
			System.out.print("Enter present tag: ");
			presents[i] = kb.nextInt();
		}
			
		
		list = new ConcurrentLinkedQueue<Integer>();
		
		Semaphore add = new Semaphore(1);
		
		Servant s1 = new Servant(add);
		Servant s2 = new Servant(add);
		Servant s3 = new Servant(add);
		Servant s4 = new Servant(add);
		
		s1.start();
		s2.start();
		s3.start();
		s4.start();
	}
}

class Servant extends Thread {
	Semaphore add;
	
	
	public Servant(Semaphore add) {
		this.add = add;
	}
	
	public void run() {
		while(Presents.index < Presents.n) {
			try {
				add.acquire();
			
				//get present from bag
				if(Presents.index < Presents.n) {
					int present = Presents.presents[Presents.index];
					Presents.index++;
				
					if(!Presents.list.isEmpty()) {
						Iterator x = Presents.list.iterator();
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(present);
						int t = 0;
						if(x.hasNext()) { 
							t = (int) x.next();
						} 
					
						while(x.hasNext() && t > present) { t = (int) x.next(); }
						if(!Presents.list.isEmpty()) {
							temp.add(t);
							x.remove();
						}
						while(x.hasNext()) {
							temp.add((Integer) x.next());
							x.remove();
						}
					
						Presents.list.addAll(temp);
					} else {
						Presents.list.add(present);
					}
				
					//release semaphore
					add.release();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//list is not empty
			if(!Presents.list.isEmpty()) {
				//remove from list and write a thank you note
				int x = Presents.list.poll();
				System.out.println("Thank You " + x);
			}
		}
	}
	
	public boolean search(int x) {
		return Presents.list.contains(x);
	}
}
