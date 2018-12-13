package kommunalwahl;

import java.util.HashMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;
import repast.simphony.context.Context;

public class PartyMember extends Voter {
	private Party party;

	PartyMember(HashMap<Party, Double> opinion, double naivite, Party party) {
		super(opinion, naivite);
	}

	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
		super.step();
		Context<Object> context = (Context<Object>) ContextUtils.getContext(this);
		Network<Object> network = (Network<Object>) context.getProjection("network");
		for (Object obj : network.getOutEdges(this)) {
			if (obj instanceof Voter) {
				((Voter) obj).increaseInfluence(party);
			}
		}
	}

	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void update() {
		super.update();
	}
}