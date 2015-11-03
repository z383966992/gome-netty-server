package test;

public class Test {
	
	public static void main(String[] args) {
		int maxCapacity = 1048576 * 10;
		int minNewCapacity = 1048576 * 5;
		int threshold = 1048576 * 4;
		if (minNewCapacity > threshold) {
            int newCapacity = minNewCapacity / threshold * threshold;
            System.out.println(minNewCapacity / threshold);
            System.out.println(1048576 * 5);
            System.out.println(newCapacity);
            if (newCapacity > maxCapacity - threshold) {
                newCapacity = maxCapacity;
            } else {
                newCapacity += threshold;
            }
//            return newCapacity;
            System.out.println(newCapacity);
        }
	}

}
