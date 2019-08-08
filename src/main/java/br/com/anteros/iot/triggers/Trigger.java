package br.com.anteros.iot.triggers;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import br.com.anteros.core.utils.StringUtils;
import br.com.anteros.iot.Action;
import br.com.anteros.iot.actuators.collectors.CollectResult;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.plant.PlantItem;

public class Trigger {

	private String name;
	private TriggerType type;
	private WhenCondition whenCondition;
	private Action[] targetActions;
	private Action[] exceptionActions;
	private boolean exigePermissao;

	private Trigger(String name, TriggerType type, WhenCondition whenCondition, Action[] targetActions,
			Action[] exceptionActions) {
		this.name = name;
		this.type = type;
		this.whenCondition = whenCondition;
		this.targetActions = targetActions;
		this.exceptionActions = exceptionActions;
	}

	public void fire(CollectResult value) {
		AnterosIOTServiceListener serviceListener = whenCondition.getThing().getDeviceController().getServiceListener();
		if (serviceListener != null) {
			try {
				serviceListener.onFireTrigger(this, value);
			} catch (Exception e) {
				e.printStackTrace();
				if (exceptionActions != null) {		
					for (Action exceptionAction : exceptionActions) {
						if (exceptionAction.getThing() instanceof PlantItem) {
							internalDispatchMessage(value, exceptionAction);
						}					
						whenCondition.getThing().getDeviceController().dispatchAction(exceptionAction, null);
					}
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

	public static Trigger of(String name, TriggerType type, WhenCondition whenCondition, Action[] targetActions,
			Action[] exceptionActions) {
		return new Trigger(name, type, whenCondition, targetActions, exceptionActions);
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

	public Action[] getTargetActions() {
		return targetActions;
	}

	public void setTargetActions(Action[] targetActions) {
		this.targetActions = targetActions;
	}

	public Action[] getExceptionActions() {
		return exceptionActions;
	}

	public void setExceptionAction(Action[] exceptionActions) {
		this.exceptionActions = exceptionActions;
	}

	public WhenCondition getWhenCondition() {
		return whenCondition;
	}

	public void setWhenCondition(WhenCondition whenCondition) {
		this.whenCondition = whenCondition;
	}

	public boolean isExigePermissao() {
		return exigePermissao;
	}

	public void setExigePermissao(boolean exigePermissao) {
		this.exigePermissao = exigePermissao;
	}

}
