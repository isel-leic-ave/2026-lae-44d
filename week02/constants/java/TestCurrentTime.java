class TestCurrentTime {
    public static void main(String[] args) throws InterruptedException {
        long c0 = System.currentTimeMillis();
        Thread.sleep(3000);

        CurrentTime c = new CurrentTime();

        System.out.println("final created1 (before sleep) " + (c.created1 - c0));
        Thread.sleep(3000);
        System.out.println("final created1 (after sleep) " + (c.created1 - c0));

        CurrentTime c2 = new CurrentTime();

        System.out.println("final created2 (before sleep) " + (c2.created1 - c0));
        Thread.sleep(3000);
        System.out.println("final created2 (after sleep) " + (c2.created1 - c0));

        System.out.println("final created3 (before sleep) " + (c2.created3 - c0));
        Thread.sleep(3000);
        System.out.println("final created3 (after sleep) " + (c2.created3 - c0));

        System.out.println("static final created 1 (before sleep) " + (CurrentTime.createdStatic1 - c0));
        Thread.sleep(3000);
        System.out.println("static final created 1 (after sleep) " + (CurrentTime.createdStatic1 - c0));
        
	System.out.println("static final created 2 (before sleep) " + (CurrentTime.createdStatic2 - c0));
        Thread.sleep(3000);
        System.out.println("static final created 2 (after sleep) " + (CurrentTime.createdStatic2 - c0));
    }
}
