package br.com.anteros.iot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.anteros.iot.actuators.LampOrBulbActuator;
import br.com.anteros.iot.actuators.LedActuator;
import br.com.anteros.iot.actuators.MagneticLockActuator;
import br.com.anteros.iot.actuators.MemoryPlcActuator;
import br.com.anteros.iot.actuators.RingStripLED12Actuator;
import br.com.anteros.iot.actuators.collectors.BarrierSensorReaderCollector;
import br.com.anteros.iot.actuators.collectors.CameraMotionDetectorCollector;
import br.com.anteros.iot.actuators.collectors.CameraQRCodeCollector;
import br.com.anteros.iot.actuators.collectors.DeviceSystemInfoCollector;
import br.com.anteros.iot.actuators.collectors.PlcColletor;
import br.com.anteros.iot.actuators.collectors.PresenceDetectorCollector;
import br.com.anteros.iot.actuators.collectors.RFIDReaderCollector;
import br.com.anteros.iot.actuators.collectors.TemperatureOneWireCollector;
import br.com.anteros.iot.actuators.collectors.VehicleEntranceTriggerCollector;

public class DefaultActuators implements Actuators {

	private Set<Class<? extends Actuable>> actuators = new HashSet<>();
	private Map<Class<? extends Actuable>, Actuable> cacheActuators = new HashMap<>();
	private Map<Thing, Actuable> cacheColletors = new HashMap<>();

	public DefaultActuators() {
		registerActuator(DeviceSystemInfoCollector.class);
		registerActuator(LedActuator.class);
		registerActuator(LampOrBulbActuator.class);
		registerActuator(MagneticLockActuator.class);
		registerActuator(PresenceDetectorCollector.class);
		registerActuator(RFIDReaderCollector.class);
		registerActuator(CameraQRCodeCollector.class);
		registerActuator(TemperatureOneWireCollector.class);
		registerActuator(CameraMotionDetectorCollector.class);
		registerActuator(PlcColletor.class);
		registerActuator(MemoryPlcActuator.class);
		registerActuator(RingStripLED12Actuator.class);
		registerActuator(VehicleEntranceTriggerCollector.class);
		registerActuator(BarrierSensorReaderCollector.class);
	}

	public DefaultActuators registerActuator(Class<? extends Actuable> actuable) {
		actuators.add(actuable);
		return this;
	}

	public DefaultActuators removeActuator(Class<? extends Actuable> actuable) {
		actuators.remove(actuable);
		return this;
	}

	@Override
	public Collector discoverCollectorToThing(Thing thing) {
		try {
			for (Class<? extends Actuable> actuable : actuators) {
				Actuable collector = null;

				collector = actuable.newInstance();

				if (collector.isSupportedThing(thing)) {
					if (collector instanceof Collector) {
						((Collector) collector).setThing(thing);
						return (Collector) collector;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Actuator<?> discoverActuatorToThing(Thing thing) {
		try {
			for (Class<? extends Actuable> actuable : actuators) {
				Actuable actuator = null;
				if (cacheActuators.containsKey(actuable)) {
					actuator = cacheActuators.get(actuable);
				} else {
					actuator = actuable.newInstance();
					cacheActuators.put(actuable, actuator);
				}

				if (actuator.isSupportedThing(thing)) {
					if (actuator instanceof Actuator) {
						return (Actuator<?>) actuator;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DefaultActuators registerActuators(Set<Class<? extends Actuable>> newActuators) {
		for (Class<? extends Actuable> act : newActuators) {
			this.registerActuator(act);
		}
		return this;
	}

}
