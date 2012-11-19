package example.binarylight;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.*;
import org.teleal.cling.binding.annotations.*;
import org.teleal.cling.model.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;

import java.io.IOException;

public class BinaryLightServer implements Runnable {
	private UpnpService upnpService;

	public void run() {

		try {
			upnpService = new UpnpServiceImpl();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			});

			// Add the bound local device to the registry
			upnpService.getRegistry().addDevice(createDevice());

		} catch (Exception ex) {
			System.err.println("Exception occured: " + ex);
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}

	// DOC: CREATEDEVICE
	LocalDevice createDevice() throws ValidationException,
			LocalServiceBindingException, IOException {

		DeviceIdentity identity = new DeviceIdentity(
				UDN.uniqueSystemIdentifier("Demo Binary Light"));

		DeviceType type = new UDADeviceType("BinaryLight", 1);

		DeviceDetails details = new DeviceDetails("Friendly Binary Light",
				new ManufacturerDetails("ACME"), new ModelDetails(
						"BinLight2000", "A demo light with on/off switch.",
						"v1"));

		/*
		 * Icon icon = new Icon( "image/png", 48, 48, 8,
		 * getClass().getResource("icon.png") );
		 */
		Icon icon = null;

		LocalService<SwitchPower> switchPowerService = new AnnotationLocalServiceBinder()
				.read(SwitchPower.class);

		switchPowerService.setManager(new DefaultServiceManager(
				switchPowerService, SwitchPower.class));

		return new LocalDevice(identity, type, details, icon,
				switchPowerService);

		/*
		 * Several services can be bound to the same device: return new
		 * LocalDevice( identity, type, details, icon, new LocalService[]
		 * {switchPowerService, myOtherService} );
		 */
	}
	// DOC: CREATEDEVICE
}