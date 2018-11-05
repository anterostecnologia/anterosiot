package br.com.anteros.iot.support;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

public class SystemInfoHelper {

	public static JsonObject getAllSystemInfoJsonRaspberryPi() {

		JsonObjectBuilder builderRoot = Json.createObjectBuilder();
		JsonObjectBuilder builderGroup = Json.createObjectBuilder();
		builderGroup.add("platform", getPlatformJsonRaspberryPi());
		builderGroup.add("hardware", getHardwareJsonRaspberryPi());
		builderGroup.add("memory", getMemoryJsonRaspberryPi());
		builderGroup.add("so", getSOJsonRaspberryPi());
		builderGroup.add("java", getJavaJsonRaspberryPi());
		builderGroup.add("network", getNetworkJsonRaspberryPi());
		builderGroup.add("codec", getCodecJsonRaspberryPi());
		builderGroup.add("clock", getClockJsonRaspberryPi());

		builderRoot.add("systemInfo", builderGroup);

		return builderRoot.build();

	}

	public static JsonObject getPlatformJsonRaspberryPi() {
		JsonObjectBuilder builderPlatform = Json.createObjectBuilder();
		try {
			builderPlatform.add("name", PlatformManager.getPlatform().getLabel());
		} catch (Exception ex) {
		}
		try {
			builderPlatform.add("id", PlatformManager.getPlatform().getId());
		} catch (Exception ex) {
		}
		return builderPlatform.build();
	}

	public static JsonObject getHardwareJsonRaspberryPi() {
		JsonObjectBuilder builderHardware = Json.createObjectBuilder();
		try {
			builderHardware.add("serialNumber", SystemInfo.getSerial());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuRevision", SystemInfo.getCpuRevision());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuArchitecture", SystemInfo.getCpuArchitecture());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuPart", SystemInfo.getCpuPart());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuTemperature", SystemInfo.getCpuTemperature());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuCoreVoltage", SystemInfo.getCpuVoltage());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("cpuModelName", SystemInfo.getModelName());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("processor", SystemInfo.getProcessor());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("hardware", SystemInfo.getHardware());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("hardwareRevision", SystemInfo.getRevision());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("isHardFloatABI", SystemInfo.isHardFloatAbi());
		} catch (Exception ex) {
		}
		try {
			builderHardware.add("boardType", SystemInfo.getBoardType().name());
		} catch (Exception ex) {
		}

		return builderHardware.build();
	}
	
	public static JsonObject getTemperatureJsonRaspberryPi() {
		JsonObjectBuilder builderTemperature = Json.createObjectBuilder();

		try {
			builderTemperature.add("cpuTemperature", SystemInfo.getCpuTemperature());
		} catch (Exception ex) {
		}
		try {
			builderTemperature.add("cpuCoreVoltage", SystemInfo.getCpuVoltage());
		} catch (Exception ex) {
		}
		
		return builderTemperature.build();
	}

	public static JsonObject getMemoryJsonRaspberryPi() {
		JsonObjectBuilder builderMemory = Json.createObjectBuilder();

		try {
			builderMemory.add("totalMemory", SystemInfo.getMemoryTotal());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("usedMemory", SystemInfo.getMemoryUsed());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("freeMemory", SystemInfo.getMemoryFree());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("sharedMemory", SystemInfo.getMemoryShared());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("memoryBuffers", SystemInfo.getMemoryBuffers());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("cachedMemory", SystemInfo.getMemoryCached());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("sdram_C_Voltage", SystemInfo.getMemoryVoltageSDRam_C());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("sdram_I_Voltage", SystemInfo.getMemoryVoltageSDRam_I());
		} catch (Exception ex) {
		}
		try {
			builderMemory.add("sdram_P_Voltage", SystemInfo.getMemoryVoltageSDRam_P());
		} catch (Exception ex) {
		}

		return builderMemory.build();
	}

	public static JsonObject getSOJsonRaspberryPi() {
		JsonObjectBuilder builderSO = Json.createObjectBuilder();

		try {
			builderSO.add("name", SystemInfo.getOsName());
		} catch (Exception ex) {
		}
		try {
			builderSO.add("version", SystemInfo.getOsVersion());
		} catch (Exception ex) {
		}
		try {
			builderSO.add("architecture", SystemInfo.getOsArch());
		} catch (Exception ex) {
		}
		try {
			builderSO.add("firmwareBuild", SystemInfo.getOsFirmwareBuild());
		} catch (Exception ex) {
		}
		try {
			builderSO.add("firmwareDate", SystemInfo.getOsFirmwareDate());
		} catch (Exception ex) {
		}

		return builderSO.build();
	}

	public static JsonObject getJavaJsonRaspberryPi() {
		JsonObjectBuilder builderJava = Json.createObjectBuilder();
		builderJava.add("vendor", SystemInfo.getJavaVendor());
		builderJava.add("vendor URL", SystemInfo.getJavaVendorUrl());
		builderJava.add("version", SystemInfo.getJavaVersion());
		builderJava.add("vm", SystemInfo.getJavaVirtualMachine());
		builderJava.add("runtime", SystemInfo.getJavaRuntime());
		return builderJava.build();
	}

	public static JsonObject getNetworkJsonRaspberryPi() {
		JsonObjectBuilder builderNewtwork = Json.createObjectBuilder();
		try {
			builderNewtwork.add("hostname", NetworkInfo.getHostname());
		} catch (Exception e1) {

		}

		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

			JsonArrayBuilder builderNewtworkInterfaces = Json.createArrayBuilder();

			for (NetworkInterface netint : Collections.list(nets)) {
				JsonObjectBuilder builderNewtworkInterfacesItem = Json.createObjectBuilder();

				builderNewtworkInterfacesItem.add("displayName", netint.getDisplayName());
				builderNewtworkInterfacesItem.add("name", netint.getName());
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

				JsonArrayBuilder builderNewtworkInterfacesIp = Json.createArrayBuilder();

				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					JsonObjectBuilder builderNewtworkInterfacesIpItem = Json.createObjectBuilder();
					builderNewtworkInterfacesIpItem.add("inetAddress", inetAddress.toString());
					builderNewtworkInterfacesIp.add(builderNewtworkInterfacesIpItem);
				}
				builderNewtworkInterfacesItem.add("addresses", builderNewtworkInterfacesIp);

				builderNewtworkInterfacesItem.add("isUp", netint.isUp());
				builderNewtworkInterfacesItem.add("loopback", netint.isLoopback());
				builderNewtworkInterfacesItem.add("p2p", netint.isPointToPoint());
				builderNewtworkInterfacesItem.add("supportsMulticast", netint.supportsMulticast());
				builderNewtworkInterfacesItem.add("virtual", netint.isVirtual());

				byte[] mac = netint.getHardwareAddress();

				if (mac != null) {
					StringBuilder sbMac = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sbMac.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
					}
					builderNewtworkInterfacesItem.add("mac", sbMac.toString());
				}
				builderNewtworkInterfacesItem.add("MTU", netint.getMTU());
				builderNewtworkInterfaces.add(builderNewtworkInterfacesItem);
			}
			builderNewtwork.add("interfaces", builderNewtworkInterfaces);

		} catch (Exception e1) {

		}

		try {
			JsonArrayBuilder builderNewtworkNS = Json.createArrayBuilder();
			for (String nameserver : NetworkInfo.getNameservers()) {
				JsonObjectBuilder builderNewtworkInterfacesNsItem = Json.createObjectBuilder();
				builderNewtworkInterfacesNsItem.add("nameServer", nameserver);
				builderNewtworkNS.add(builderNewtworkInterfacesNsItem);
			}
			builderNewtwork.add("nameServers", builderNewtworkNS);
		} catch (Exception e) {
		}

		return builderNewtwork.build();
	}
	
	public static JsonObject getCodecJsonRaspberryPi() {
		JsonObjectBuilder builderCodec = Json.createObjectBuilder();
		try {
			builderCodec.add("H264Enabled:  ", SystemInfo.getCodecH264Enabled());
		} catch (Exception ex) {
		}
		try {
			builderCodec.add("MPG2Enabled:  ", SystemInfo.getCodecMPG2Enabled());
		} catch (Exception ex) {
		}
		try {
			builderCodec.add("WVC1Enabled:  ", SystemInfo.getCodecWVC1Enabled());
		} catch (Exception ex) {
		}
		return builderCodec.build();
	}
	
	public static JsonObject getClockJsonRaspberryPi() {
		JsonObjectBuilder builderClock = Json.createObjectBuilder();
		try {
			builderClock.add("ARMFrequency", SystemInfo.getClockFrequencyArm());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("COREFrequency", SystemInfo.getClockFrequencyCore());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("H264Frequency", SystemInfo.getClockFrequencyH264());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("ISPFrequency", SystemInfo.getClockFrequencyISP());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("V3DFrequency", SystemInfo.getClockFrequencyV3D());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("UARTFrequency", SystemInfo.getClockFrequencyUART());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("PWMFrequency", SystemInfo.getClockFrequencyPWM());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("EMMCFrequency", SystemInfo.getClockFrequencyEMMC());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("PixelFrequency", SystemInfo.getClockFrequencyPixel());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("VECFrequency", SystemInfo.getClockFrequencyVEC());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("HDMIFrequency", SystemInfo.getClockFrequencyHDMI());
		} catch (Exception ex) {
		}
		try {
			builderClock.add("DPIFrequency", SystemInfo.getClockFrequencyDPI());
		} catch (Exception ex) {
		}
		return builderClock.build();
	}

}
