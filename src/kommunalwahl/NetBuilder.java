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

		Parameters p = RunEnvironment.getInstance().getParameters();
		int endAt = p.getInteger("endAt");
		int nParties = p.getInteger("numberOfParties");
		int nVoters = p.getInteger("numberOfVoters");
		int nPartyMembers = (int) (nVoters * p.getFloat("percentageOfMembers"));
		
		double socialInfluence = p.getDouble("socialInfluence");
		double partyInfluence = p.getDouble("partyInfluence");

		RunEnvironment.getInstance().endAt(endAt);

		// create a social network
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social_network", context,
				true);
		Network<Object> network = netBuilder.buildNetwork();
		context.addProjection(network);

		Party parties[] = new Party[nParties];
		for (int i = 0; i < parties.length; i++) {
			parties[i] = new Party(i);
		}

		Normal voterOpinionDistribution = RandomHelper.createNormal(0.5, 0.2);
		Normal naiviteDistribution = RandomHelper.createNormal(0.1, 0.05);
		for (int i = 0; i < nVoters - nPartyMembers; i++) {
			HashMap<Party, Double> opinion = new HashMap<>();
			for (Party party : parties) {
				opinion.put(party, Util.clamp(voterOpinionDistribution.nextDouble(), 0, 1));
			}
			context.add(new Voter(opinion, Util.clamp(naiviteDistribution.nextDouble(), 0, 1)));
		}

		Normal memberOpinionDistribution = RandomHelper.createNormal(0.8, 0.1);
		for (int i = 0; i < nPartyMembers; i++) {
			Party partyMembership = parties[RandomHelper.nextIntFromTo(0, parties.length - 1)];
			HashMap<Party, Double> opinion = new HashMap<>();
			for (Party party : parties) {
				if (party == partyMembership) {
					opinion.put(party, Util.clamp(memberOpinionDistribution.nextDouble(), 0, 1));
				} else {
					opinion.put(party, Util.clamp(voterOpinionDistribution.nextDouble(), 0, 1));
				}
			}
			context.add(new PartyMember(opinion, Util.clamp(naiviteDistribution.nextDouble(), 0, 1),
					partyMembership));
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
