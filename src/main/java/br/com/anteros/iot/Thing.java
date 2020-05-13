package br.com.anteros.iot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.domain.PlantItemNode;
import br.com.anteros.iot.triggers.Trigger;
import br.com.anteros.iot.triggers.ShotMoment;
import br.com.anteros.iot.triggers.WhenCondition;

public interface Thing {

	public String getThingID(); // UUID

	public String getStatus();

	public void setStatus(String status);

	public Set<Part> getParts();

	public boolean hasParts();

	public boolean needsPropagation();

	public Thing addPart(Part part);

	public Thing removePart(Part part);

	public Part getPartById(String part);

	public Thing loadConfiguration(PlantItemNode node);

	public void setDeviceController(DeviceController controller);

	public DeviceController getDeviceController();

	public String[] getActions();

	public Thing addTrigger(Trigger trigger);

	public Thing removeTrigger(Trigger trigger);

	public Trigger[] getTriggers();

	default public boolean hasTriggers(ShotMoment type, String actionOrValue) {
		for (Trigger trigger : getTriggers()) {
			if (StringUtils.isEmpty(trigger.getWhenCondition().getActionOrValue())
					|| trigger.getWhenCondition().hasActionOrValueEquals(WhenCondition.ALL_VALUES)
					|| trigger.getWhenCondition().hasActionOrValueEquals(actionOrValue)) {
				return true;
			}
		}
		return false;
	}

	default public boolean hasTriggers(ShotMoment shotMoment) {
		for (Trigger trigger : getTriggers()) {
			if (trigger.getShotMoment().equals(shotMoment)) {
				return true;
			}
		}
		return false;
	}

	default public Trigger[] getTriggersByType(ShotMoment shotMoment, String actionOrValue) {
		List<Trigger> result = new ArrayList<>();
		for (Trigger trigger : getTriggers()) {
			if (trigger.getShotMoment().equals(shotMoment)) {

				if (StringUtils.isEmpty(trigger.getWhenCondition().getActionOrValue())
						|| trigger.getWhenCondition().hasActionOrValueEquals(WhenCondition.ALL_VALUES)
						|| trigger.getWhenCondition().hasActionOrValueEquals(actionOrValue)) {
					result.add(trigger);
				}
			}
		}
		return result.toArray(new Trigger[] {});
	}

	default public Trigger[] getTriggersByType(ShotMoment shotMoment) {
		List<Trigger> result = new ArrayList<>();
		for (Trigger trigger : getTriggers()) {
			if (trigger.getShotMoment().equals(shotMoment)) {
				result.add(trigger);
			}
		}
		return result.toArray(new Trigger[] {});
	}
}
