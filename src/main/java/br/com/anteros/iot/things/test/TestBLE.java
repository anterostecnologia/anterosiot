package br.com.anteros.iot.things.test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import br.com.anteros.iot.protocol.ble.AnterosBluetoothLeService;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeAdapter;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeDevice;
import br.com.anteros.iot.protocol.bluetooth.le.BluetoothLeService;

public class TestBLE {
	
	public static void main(String[] args) {
		
		BluetoothLeService service = AnterosBluetoothLeService.getInstance();
		
		List<BluetoothLeAdapter> adapters = service.getAdapters();
		for (BluetoothLeAdapter adapter : adapters) {
			System.out.println(adapter);
			Future<List<BluetoothLeDevice>> result = adapter.findDevices(10);
			try {
				result.wait(5000);
				System.out.println(result.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
