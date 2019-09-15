package br.com.anteros.iot.things.test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import br.com.anteros.iot.protocol.ble.AnterosBluetoothLeService;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeDevice;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristic;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattService;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeService;

public class TestBLE implements Consumer<List<BluetoothLeDevice>> {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		BluetoothLeService service = AnterosBluetoothLeService.getInstance();

		List<BluetoothLeAdapter> adapters = service.getAdapters();
		for (BluetoothLeAdapter adapter : adapters) {
			System.out.println(adapter);
//			Future<BluetoothLeDevice> result = adapter.findDeviceByAddress(5, "24:0A:C4:9B:F6:16");
			adapter.findDevices(10,new TestBLE());
//
//			while (!result.isDone()) {
//				System.out.print(".");
//				Thread.sleep(1); // sleep for 1 millisecond before checking again
//			}
//			System.out.println("Tarefa completa!");
//			BluetoothLeDevice bluetoothLeDevice = result.get();
//			if (bluetoothLeDevice != null) {
//				System.out.println(bluetoothLeDevice.getAddress() + " " + bluetoothLeDevice.getName() + " "
//						+ bluetoothLeDevice.getRSSI());
//				
//				bluetoothLeDevice.connect();
//
//				List<BluetoothLeGattService> findServices = bluetoothLeDevice.findServices();
//				System.out.println(findServices);
//				for (BluetoothLeGattService svr : findServices) {
//
////				BluetoothLeGattService svr = bluetoothLeDevice.findService(UUID.fromString("a4455088-fb26-4116-bc35-06cfba3dfffb"));
////				if (svr != null) {
//					List<BluetoothLeGattCharacteristic> findCharacteristics = svr.findCharacteristics();
//					for (BluetoothLeGattCharacteristic c : findCharacteristics) {
//						System.out.println(new String(c.getValue()));
//					}
//				}
//				
//				bluetoothLeDevice.disconnect();
//			}

		}

	}

	@Override
	public void accept(List<BluetoothLeDevice> t) {
		for (BluetoothLeDevice dv : t) {
			System.out.println(dv.getAddress()+" "+dv.getName()+" "+dv.getRSSI());
			if (dv.getAddress().equals("A4:CF:12:75:9C:3A")) {
				dv.connect();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<BluetoothLeGattService> findServices = dv.findServices();
				for (BluetoothLeGattService srv : findServices) {
					if (srv.getUUID().toString().equalsIgnoreCase("504ea456-fcc1-4eaa-ad17-f7e0b5a0645e")) {
						System.out.println(srv.getUUID());
						List<BluetoothLeGattCharacteristic> characteristics = srv.findCharacteristics();
						for (BluetoothLeGattCharacteristic car : characteristics) {
							System.out.println(car.getUUID());
						}
					}
					
				}
			}
		}
		
	}

}
