package kommunalwahl;

import java.util.HashMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;
import repast.simphony.context.Context;

public class PartyMember extends Voter {
	private final Party party;
	public static double influence;

	PartyMember(HashMap<Party, Double> opinion, double naivite, Party party) {
		super(opinion, naivite);
		this.party = party;
	}

	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
		super.step();
		Context<Object> context = (Context<Object>) ContextUtils.getContext(this);
		Network<Object> network = (Network<Object>) context.getProjection("social_network");
		for (RepastEdge<Object> edge : network.getOutEdges(this)) {
			Object obj = edge.getTarget();
			if (obj instanceof Voter) {
				((Voter) obj).increaseInfluence(party, influence);
			}
		}
	}

	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void update() {
		super.update();
	}
}