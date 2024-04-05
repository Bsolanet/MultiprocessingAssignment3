import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rover {
	public static void main(String[] args) {
		
		long start = System.nanoTime();
		
		Sensor s1 = new Sensor();
		Sensor s2 = new Sensor();
		Sensor s3 = new Sensor();
		Sensor s4 = new Sensor();
		Sensor s5 = new Sensor();
		Sensor s6 = new Sensor();
		Sensor s7 = new Sensor();
		Sensor s8 = new Sensor();
		
		
		s1.start();
		s2.start();
		s3.start();
		s4.start();
		s5.start();
		s6.start();
		s7.start();
		s8.start();
		
		while(s1.isAlive() || s2.isAlive() || s3.isAlive() || s4.isAlive() || s5.isAlive() || s6.isAlive() || s7.isAlive() || s8.isAlive());
		
		s1.printReport();
		
		long end = System.nanoTime();
		
		double time = (double) ((end - start) / Math.pow(10, 9));
		System.out.println(time);
	}
}

class Sensor extends Thread {
	static List<Integer> temps = Collections.synchronizedList(new ArrayList<Integer>());
	static List<Integer> highs = Collections.synchronizedList(new ArrayList<Integer>());
	static List<Integer> lows = Collections.synchronizedList(new ArrayList<Integer>());
	
	static int min = 0;
	static int l = Integer.MIN_VALUE;
	static int h = Integer.MAX_VALUE;
	static int interval = Integer.MIN_VALUE;
	static int start = 0;
	static int end = 0;
	
	public Sensor() {}
	
	public void run() {
		while(min < 60) {
			int time = min;
			min++;
			
			//get new temp
			int t = getTemp();
			temps.add(t);
			
			System.out.println("Minute: " + time);
			System.out.println("Temperature: " + t);
			
			//check if its a top 5 highest temps
			if(t > l || highs.size() < 5) {
				insert(highs, t);
				if(highs.size() > 5) {
					highs.remove(0);
				}
				l = (int) highs.get(0);
			}
			
			//check if its a top 5 lowest temps
			if(t < h || lows.size() < 5) {
				insert(lows, t);
				if(lows.size() > 5) {
					lows.remove(lows.size() - 1);
				}
				h = (int) lows.get(lows.size() - 1);
			}
			
			//check interval
			if(time >= 10 && interval < Math.abs(t - (int) temps.get(time - 10))) {
				interval = Math.abs(t - (int) temps.get(time - 10));
				start = time - 10;
				end = time;
			}
		}
	}
	
	//method to add to a sorted list
	public void insert(List<Integer> list, int x) {
		
		int i = 0;
		while(i < list.size()) {
			if(x < (int) list.get(i)) {
				list.add(i, x);
				return;
			}
			i++;
		}
		
		list.add(x);
		return;
	}
	
	//method that simulates getting temperature
	public int getTemp() {
		return (int) (Math.random() * 170 - 100);
	}
	
	public void printReport() {
		System.out.println("5 Lowest temps: " + lows);
		System.out.println("5 highest temps: " + highs);
		System.out.println("Greatest interval: " + interval);
		System.out.println("Start: " + start);
		System.out.println("End: " + end);
	}
}
