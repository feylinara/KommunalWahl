package kommunalwahl;

import java.util.HashMap;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Voter {
	private HashMap<Party, Double> opinion;
	private double naivite;

	Voter(HashMap<Party, Double> opinion, double naivite) {
		this.opinion = opinion;
		this.naivite = naivite;
	}

	void increaseInfluence(Party party) {
		// TODO change, maybe?
		double opinion = this.opinion.get(party);
		opinion += naivite;
		this.opinion.put(party, opinion);
	}
	
	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
	}

	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void update() {
	}
}