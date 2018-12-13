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
		// We use a sigmoid function for increasing influence
		double r = naivite;
		double K = 0.1;
		double opinion = this.opinion.get(party);
		opinion += r * opinion * (1 - opinion/K);
		this.opinion.put(party, opinion);
	}
	
	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
	}

	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void update() {
	}
}