package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private List<Actor> vertici;
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	
	private Simulatore sim;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		this.dao.listAllActors(this.idMap);
	}
	
	public void creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>();
		this.vertici = this.dao.getActorsByGenre(genere, this.idMap);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		List<Adiacenza> adiacenze = this.dao.getAdiacenze(genere, this.idMap);
		for(Adiacenza a : adiacenze) {
			Actor a1 = a.getA1();
			Actor a2 = a.getA2();
			if(this.grafo.containsVertex(a1) && this.grafo.containsVertex(a2)) {
				Graphs.addEdge(this.grafo, a1, a2, a.getPeso());
			}
		}
		
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int getNVertici() {
		return this.vertici.size();
	}
	
	public List<String> getGeneri() {
		return this.dao.getGeneri();
	}

	public List<Actor> getVertici() {
		Collections.sort(this.vertici);
		return this.vertici;
	}

	public List<Actor> calcolaRaggiungibili(Actor a) {

		// AMPIEZZA
		BreadthFirstIterator<Actor, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo, a);
			
		List<Actor> result = new ArrayList<>();
		
		while(bfv.hasNext()) {
			 Actor f = bfv.next();
			 result.add(f);
		}
		result.remove(0);
		Collections.sort(result);
		return result;
	
	}

	public void simula(Integer n) {
		
		sim = new Simulatore(n, this.grafo, this.vertici);
		sim.init();
		sim.run();
		
		
	}

	public int getPause() {
		// TODO Auto-generated method stub
		return sim.getNumPause();
	}

	public Collection<Actor> getIntervistati() {
		// TODO Auto-generated method stub
		return sim.getIntervistati();
	}
	
	
	

}
