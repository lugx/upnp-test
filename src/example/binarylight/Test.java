package example.binarylight;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {

		try {
			org.teleal.common.logging.LoggingUtil.loadDefaultConfiguration();
		} catch (java.io.IOException ex) {
			System.out.println("Failed to load logging file");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (args.length > 0 && args[0].equals("client")) {
			BinaryLightClient m1 = new BinaryLightClient();
			Thread t1 = new Thread(m1);
			t1.start();

		} else if (args.length > 0 && args[0].equals("server")) {
			BinaryLightServer m1 = new BinaryLightServer();
			Thread t1 = new Thread(m1);
			t1.start();
		} else {
			System.out.println("Usage: java TestApp <mode>");
			System.out.println("Possible modes: \"server\" \"client\"");
			System.exit(1);
		}
	}
}
