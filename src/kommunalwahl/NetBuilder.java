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

	public Context<Object> build(Context<Object> context) {

		Parameters p = RunEnvironment.getInstance().getParameters();
		// The number of steps to run
		int endAt = p.getInteger("endAt");
		// The number of parties and voters
		int nParties = p.getInteger("nParties");
		int nVoters = p.getInteger("nVoters");
		// The number of Party Members
		int nPartyMembers = (int) (nVoters * p.getDouble("percentageOfMembers"));
		// The respective influences of member advertisment and party Advertisement
		double socialInfluence = p.getDouble("socialInfluence");
		double partyInfluence = p.getDouble("partyInfluence");
		int socialReach = p.getInteger("voterReach");
		int partyReach = p.getInteger("partyReach");
		
		Voter.influence = socialInfluence;
		Party.influence = partyInfluence;
		Party.reach = partyReach;

		RunEnvironment.getInstance().endAt(endAt);

		// create a social network
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social_network", context,
				true);
		Network<Object> network = netBuilder.buildNetwork();

		// Create Parties
		Party parties[] = new Party[nParties];
		for (int i = 0; i < parties.length; i++) {
			parties[i] = new Party(i);
			context.add(parties[i]);
		}

		// Create Voters
		Normal voterOpinionDistribution = RandomHelper.createNormal(0.5, 0.2);
		Normal naiviteDistribution = RandomHelper.createNormal(0.5, 0.1);
		for (int i = 0; i < nVoters - nPartyMembers; i++) {
			HashMap<Party, Double> opinion = new HashMap<>();
			for (Party party : parties) {
				opinion.put(party, Util.clamp(voterOpinionDistribution.nextDouble(), 0, 1));
			}
			context.add(new Voter(opinion, Util.clamp(naiviteDistribution.nextDouble(), 0, 1)));
		}

		// Create Party Members
		Normal memberOpinionDistribution = RandomHelper.createNormal(0.8, 0.1);
		for (int i = 0; i < nPartyMembers; i++) {
			Party partyMembership = parties[RandomHelper.nextIntFromTo(0, parties.length - 1)];
			partyMembership.nMembers++;
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

		// Add edges
		for (Object obj : context.getObjects(Voter.class)) {
			Voter self = (Voter) obj;

			int outEdges = RandomHelper.nextIntFromTo(1, socialReach * 5);

			Iterable<Object> friends = context.getRandomObjects(Voter.class, outEdges);
			for (Object friend: friends) {
				network.addEdge(self, friend);
			}
		}
		return context;
	}
}
