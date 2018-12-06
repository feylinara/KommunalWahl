package kommunalwahl;

import java.util.HashMap;

import cern.jet.random.Normal;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class NetBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
				"social_network", context, true);
		Network<Object> network = netBuilder.buildNetwork();

		Parameters p = RunEnvironment.getInstance().getParameters();
		int endAt = (Integer) p.getValue("endAt");
		RunEnvironment.getInstance().endAt(endAt);

		int nParties = (Integer) p.getValue("numberOfParties");
		int nVoters = (Integer) p.getValue("numberOfVoters");

		Party parties[] = new Party[nParties];
		for (int i = 0; i < parties.length; i++) {
			parties[i] = new Party();
		}

		Normal normalVerteilung = RandomHelper.createNormal(0.5, 0.2);

		for (int i = 0; i < nVoters; i++) {
			HashMap<Party, Double> opinion = new HashMap<>();
			for (Party party : parties) {
				opinion.put(party, normalVerteilung.nextDouble());
			}
			context.add(new Voter(opinion, normalVerteilung.nextDouble()));
		}

		for (Object obj : context.getObjects(Voter.class)) {
			Voter a = (Voter) obj;

			double outEdges = RandomHelper.nextIntFromTo(1, 10);
			for (int i = 0; i < outEdges; i++) {

				Voter friend = (Voter) context.getRandomObject();
				if (!friend.equals(a)) {
					network.addEdge(a, friend);
				} else {
					i--;
				}
			}
		}
		return context;
	}
}
