package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Integer n;
	private List<Actor> daIntervistare;
	
	private Map<Integer, Actor> interviste;
	
	private int numPause;
	
	public Simulatore(Integer n, SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo, List<Actor> vertici) {
		this.grafo = grafo;
		this.n = n;
		this.daIntervistare = new ArrayList<>(vertici);
	}

	public void init() {
		
		this.interviste = new HashMap<Integer, Actor>();
		this.numPause = 0;
		
	}

	public void run() {
		
		
		for (int i = 1; i<=this.n; i++) {
			
			Random ram = new Random();
			
			if(i==1 || !this.interviste.containsKey(i-1)) {
				
				Actor a = this.daIntervistare.get(ram.nextInt(this.daIntervistare.size()));
				this.interviste.put(i, a);
				this.daIntervistare.remove(a);
				continue;
				
			}
				
			if(i>=3 && this.interviste.containsKey(i-2) && this.interviste.containsKey(i-1) &&
			this.interviste.get(i-2).getGender().equals(this.interviste.get(i-1).getGender())) {
				
				if(Math.random()<0.9) {
					this.numPause++;
					continue;
			
				}
			}
			
			if(Math.random()<0.6) {
					
				Actor a = this.daIntervistare.get(ram.nextInt(this.daIntervistare.size()));
				this.interviste.put(i, a);
				this.daIntervistare.remove(a);
					
					
			}else {
				Actor a = this.getSuggerimento(this.interviste.get(i-1));
				
				if(a==null || !this.daIntervistare.contains(a)) {
					
					Actor ac = this.daIntervistare.get(ram.nextInt(this.daIntervistare.size()));
					this.interviste.put(i, ac);
					this.daIntervistare.remove(ac);
					
				} else {
					this.interviste.put(i, a);
					this.daIntervistare.remove(a);
						
				}
					
			}
		}
			

		
	}

	private Actor getSuggerimento(Actor actor) {

		List<Actor> adiacenti = Graphs.successorListOf(this.grafo, actor);
		
		if(adiacenti.size()==0)
			return null;
		
		int peso = 0;
		
		Actor best = null;
		
		for(Actor a : adiacenti) {
			if((int)this.grafo.getEdgeWeight(this.grafo.getEdge(actor, a))>peso) {
				peso = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(actor, a));
				best = a;
			}
			
		}
		return best;
	}

	public int getNumPause() {
		return numPause;
	}

	public Collection<Actor> getIntervistati() {
		// TODO Auto-generated method stub
		return this.interviste.values();
	}

}
