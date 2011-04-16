package GameStateChangedEvent;

import java.util.EventObject;

import main.GameStates;

public class GameStateChangedEvent extends EventObject
{
	public GameStates getNewState() {
		return newState;
	}

	private void setNewState(GameStates newState) {
		this.newState = newState;
	}

	public GameStates getPreviousState() {
		return previousState;
	}

	private void setPreviousState(GameStates previousState) {
		this.previousState = previousState;
	}
	
	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	private GameStates newState;
	private GameStates previousState;
	private String message;

	public GameStateChangedEvent(Object source, GameStates previousState, GameStates newState, String message)
	{
		super(source);
		setPreviousState(previousState);
		setNewState(newState);
		setMessage(message);
	}
}
