package br.com.anteros.iot.triggers;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.pi4j.util.StringUtil;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Action;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.collectors.CollectResult;
import br.com.anteros.iot.plant.PlantItem;

public class Trigger {

	private String name;
	private TriggerType type;
	private Action sourceAction;
	private Action[] targetActions;
	private Action exceptionAction;

	private Trigger(String name, TriggerType type, Action sourceAction, Action[] targetActions,
			Action exceptionAction) {
		this.name = name;
		this.type = type;
		this.sourceAction = sourceAction;
		this.targetActions = targetActions;
		this.exceptionAction = exceptionAction;
	}

	public void fire(CollectResult value) {
		AnterosIOTServiceListener serviceListener = sourceAction.getThing().getDeviceController().getServiceListener();
		if (serviceListener != null) {
			try {
				serviceListener.onFireTrigger(this, value);
			} catch (Exception e) {
				e.printStackTrace();
				if (exceptionAction != null) {					
					if (exceptionAction.getThing() instanceof PlantItem) {
						internalDispatchMessage(value, exceptionAction);
					}					
					sourceAction.getThing().getDeviceController().dispatchAction(exceptionAction, null);
				}
				return;
			}
		}

		if (targetActions != null) {
			for (Action targetAction : targetActions) {
				if (targetAction.getThing() instanceof PlantItem) {
					internalDispatchMessage(value, targetAction);					
				}
			}
		}

	}

	protected void internalDispatchMessage(CollectResult value, Action action) {
		String topic = ((PlantItem) action.getThing()).getPath();
		JsonObjectBuilder builder = Json.createObjectBuilder().add("action", action.getAction());
		if (value != null) {
			value.toJson(builder);
		}
		action.getThing().getDeviceController().dispatchMessage(topic, builder.build().toString());

		if (StringUtils.isNotEmpty(action.getMessage()) && action.getTopics() != null) {
			for (String tpc : action.getTopics()) {
				action.getThing().getDeviceController().dispatchMessage(tpc,
						action.getMessage());
			}
		}
	}

	public static Trigger of(String name, TriggerType type, Action sourceAction, Action[] targetActions,
			Action exceptionAction) {
		return new Trigger(name, type, sourceAction, targetActions, exceptionAction);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TriggerType getType() {
		return type;
	}

	public void setType(TriggerType type) {
		this.type = type;
	}

	public Action getSourceAction() {
		return sourceAction;
	}

	public void setSourceAction(Action sourceAction) {
		this.sourceAction = sourceAction;
	}

	public Action[] getTargetActions() {
		return targetActions;
	}

	public void setTargetActions(Action[] targetActions) {
		this.targetActions = targetActions;
	}

	public Action getExceptionAction() {
		return exceptionAction;
	}

	public void setExceptionAction(Action exceptionAction) {
		this.exceptionAction = exceptionAction;
	}

}
