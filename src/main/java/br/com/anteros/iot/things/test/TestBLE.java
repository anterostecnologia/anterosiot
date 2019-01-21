package br.com.anteros.iot.things.test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import br.com.anteros.iot.protocol.ble.AnterosBluetoothLeService;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeDevice;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattCharacteristic;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeGattService;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeService;

public class TestBLE {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		BluetoothLeService service = AnterosBluetoothLeService.getInstance();

		List<BluetoothLeAdapter> adapters = service.getAdapters();
		for (BluetoothLeAdapter adapter : adapters) {
			System.out.println(adapter);
//			Future<BluetoothLeDevice> result = adapter.findDeviceByAddress(5, "24:0A:C4:9B:F6:16");
			Future<BluetoothLeDevice> result = adapter.findDeviceByAddress(5, "B4:E6:2D:85:D3:1B");

			while (!result.isDone()) {
				System.out.print(".");
				Thread.sleep(1); // sleep for 1 millisecond before checking again
			}
			System.out.println("Tarefa completa!");
			BluetoothLeDevice bluetoothLeDevice = result.get();
			if (bluetoothLeDevice != null) {
				System.out.println(bluetoothLeDevice.getAddress() + " " + bluetoothLeDevice.getName() + " "
						+ bluetoothLeDevice.getRSSI());
				
				bluetoothLeDevice.connect();

				List<BluetoothLeGattService> findServices = bluetoothLeDevice.findServices();
				System.out.println(findServices);
				for (BluetoothLeGattService svr : findServices) {

//				BluetoothLeGattService svr = bluetoothLeDevice.findService(UUID.fromString("a4455088-fb26-4116-bc35-06cfba3dfffb"));
//				if (svr != null) {
					List<BluetoothLeGattCharacteristic> findCharacteristics = svr.findCharacteristics();
					for (BluetoothLeGattCharacteristic c : findCharacteristics) {
						System.out.println(new String(c.getValue()));
					}
				}
				
				bluetoothLeDevice.disconnect();
			}

		}

	}

}
