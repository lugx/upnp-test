package example.binarylight;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;

public class BinaryLightClient implements Runnable {
	private UpnpService upnpService;

	public void run() {
		
		try {
			upnpService = new UpnpServiceImpl();

			// Add a listener for device registration events
			upnpService.getRegistry().addListener(
					createRegistryListener(upnpService));
			System.out.println("Listening for servers");

			System.out.println("Broadcasting search message");
			// Broadcast a search message for all devices
			upnpService.getControlPoint().search(new STAllHeader());
		} catch (Exception ex) {
			System.err.println("Exception occured: " + ex);
			System.exit(1);
		}
	}

	// DOC: REGISTRYLISTENER
	RegistryListener createRegistryListener(final UpnpService upnpService) {
		return new DefaultRegistryListener() {

			ServiceId serviceId = new UDAServiceId("SwitchPower");

			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

				Service switchPower;
				RemoteService[] services = device.findServices();
				System.out.println("Remote device discovered: " + device);

				/*
				 * for(DeviceType i : services) {
				 * System.out.println("  contains service:" +
				 * i.getServiceId().getId()); }
				 */

				if ((switchPower = device.findService(serviceId)) != null) {

					System.out.println("Service discovered: " + switchPower);
					executeAction(upnpService, switchPower);
				}
			}

			@Override
			public void remoteDeviceRemoved(Registry registry,
					RemoteDevice device) {
				Service switchPower;

				if ((switchPower = device.findService(serviceId)) != null) {
					System.out.println("Service disappeared: " + switchPower);
				}
			}
		};
	}

	// DOC: REGISTRYLISTENER
	// DOC: EXECUTEACTION
	void executeAction(UpnpService upnpService, Service switchPowerService) {

		ActionInvocation setTargetInvocation = new SetTargetActionInvocation(
				switchPowerService);

		// Executes asynchronous in the background
		upnpService.getControlPoint().execute(
				new ActionCallback(setTargetInvocation) {

					@Override
					public void success(ActionInvocation invocation) {
						assert invocation.getOutput().length == 0;
						System.out.println("Successfully called action!");
					}

					@Override
					public void failure(ActionInvocation invocation,
							UpnpResponse operation, String defaultMsg) {
						System.err.println(defaultMsg);
					}
				});
	}

	class SetTargetActionInvocation extends ActionInvocation {

		SetTargetActionInvocation(Service service) {
			super(service.getAction("SetTarget"));
			
			try {
				// Throws InvalidValueException if the value is of wrong type
				setInput("NewTargetValue", true);
			} catch (InvalidValueException ex) {
				System.err.println(ex.getMessage());
				System.exit(1);
			}
		}
	}
	// DOC: EXECUTEACTION
}
