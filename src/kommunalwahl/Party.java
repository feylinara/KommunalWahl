package kommunalwahl;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;

public class Party {
	public int nMembers;
	public final int id;
	public static double influence;
	public static int reach;
	
	public Party(int id) {
		this.id = id;
	}
	
	@ScheduledMethod(start = 1.0, interval = 1.0)
	public void step() {
		Context<Object> context = (Context<Object>) ContextUtils.getContext(this);
		Network<Object> network = (Network<Object>) context.getProjection("social_network");
		Iterable<Object> viewers = context.getRandomObjects(Voter.class, reach);
		for (Object o: viewers) {
			((Voter) o).increaseInfluence(this, influence);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Party other = (Party) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Party [nMembers=" + nMembers + ", id=" + id + "]";
	}
}
