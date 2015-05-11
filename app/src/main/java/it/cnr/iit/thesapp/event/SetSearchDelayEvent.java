package it.cnr.iit.thesapp.event;

public class SetSearchDelayEvent {
	private final long delay;

	public SetSearchDelayEvent(long searchInterval) {
		this.delay = searchInterval;
	}

	public long getDelay() {
		return delay;
	}
}
