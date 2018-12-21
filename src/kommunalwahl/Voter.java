package kommunalwahl;

import java.util.HashMap;
import java.util.Map.Entry;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Voter {
	private HashMap<Party, Double> opinion;
	private double naivite;

	Voter(HashMap<Party, Double> opinion, double naivite) {
		this.opinion = opinion;
		this.naivite = naivite;
	}

	/**
	 * Increases the opinion of a party with the sigmoid function r(1-K/X)X.
	 * 
	 * @param party
	 */
	void increaseInfluence(Party party) {
		// We use a sigmoid function for increasing influence
		double r = naivite;
		double K = 1.0;
		double opinion = this.opinion.get(party);
		opinion += naivite;
		this.opinion.put(party, opinion);
	}

	public int wouldVote() {
		double max = Double.NEGATIVE_INFINITY;
		Party maxParty = null;
		for (Entry<Party, Double> entry : opinion.entrySet()) {
			double curr = entry.getValue();
			if (curr > max) {
				max = curr;
				maxParty = entry.getKey();
			}
		}
		return maxParty.id;
	}

	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
	}

	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void update() {
	}
}