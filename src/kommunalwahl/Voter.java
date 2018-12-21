package kommunalwahl;

import java.util.HashMap;
import java.util.Map.Entry;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Voter {
	private HashMap<Party, Double> opinion;
	private HashMap<Party, Double> _opinion;
	private double naivite;

	Voter(HashMap<Party, Double> opinion, double naivite) {
		this.opinion = opinion;
		this.naivite = naivite;
	}

	/**
	 * Increases the opinion of a party
	 * 
	 * @param party
	 */
	void increaseInfluence(Party party, double influence) {
		_opinion = (HashMap<Party, Double>) opinion.clone();
		double opinion = this.opinion.get(party);
		opinion += naivite * influence;
		this._opinion.put(party, opinion);
	}

	/**
	 * The id of the Party the Voter has the highest opinion of
	 * 
	 * @return
	 */
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
		opinion = _opinion;
	}
}