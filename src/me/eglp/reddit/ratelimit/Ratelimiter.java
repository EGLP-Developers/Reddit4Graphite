package me.eglp.reddit.ratelimit;

public class Ratelimiter {
	
	private static long ratelimitMinute;
	private static int count;
	
	public static void addRequest() {
		long currentMinute = System.currentTimeMillis() / 1000 / 60;
		if(currentMinute != ratelimitMinute) {
			ratelimitMinute = currentMinute;
			count = 1;
		}else {
			count++;
		}
	}
	
	public static synchronized void waitForRatelimitIfNeeded() {
		long time = System.currentTimeMillis();
		long currentMinute = time / 1000 / 60;
		if(currentMinute == ratelimitMinute && count == 30) { // 30 requests per minute
			try {
				Thread.sleep(60000 - (time % 60000)); // Wait for the current minute to pass
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count = 0;
		}
	}

}
