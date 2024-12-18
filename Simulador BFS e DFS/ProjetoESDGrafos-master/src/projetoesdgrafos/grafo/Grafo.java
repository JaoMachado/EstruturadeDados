package projetoesdgrafos.grafo;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Um grafo implementado usando uma tabela de símbolos.
 *
 * @author Prof. Dr. David Buzatto
 */
public class Grafo {

    public Map<Vertice, List<Aresta>> st;
    public Map<Integer, Vertice> vertices;

    public Grafo() {
        st = new TreeMap<>();
        vertices = new TreeMap<>();
    }

    public Vertice addVertice(double x, double y) {
        Vertice v = new Vertice(vertices.size(), x, y);
        vertices.put(v.id, v);
        return v;
    }

    public void addAresta(int origem, int destino, Color cor) {
        Vertice vo = vertices.get(origem);
        Vertice vd = vertices.get(destino);
        if (!st.containsKey(vo)) {
            st.put(vo, new ArrayList<>());
        }
        if (!st.containsKey(vd)) {
            st.put(vd, new ArrayList<>());
        }
        st.get(vo).add(0, new Aresta(vo, vd, cor));
        st.get(vd).add(0, new Aresta(vd, vo, cor));
    }

    public List<Aresta> adjacentes(int origem) {
        return st.getOrDefault(vertices.get(origem), new ArrayList<>());
    }

    public int getQuantidadeVertices() {
        return vertices.size();
    }

    public List<Aresta> getArestas() {
        List<Aresta> arestas = new ArrayList<>();
        List<String> arestasAdicionadas = new ArrayList<>(); // Para evitar duplicatas

        for (Map.Entry<Vertice, List<Aresta>> entry : st.entrySet()) {
            for (Aresta aresta : entry.getValue()) {
                // Gera uma "chave" única para evitar duplicatas
                String chave = Math.min(aresta.origem.id, aresta.destino.id) + "-"
                        + Math.max(aresta.origem.id, aresta.destino.id);

                if (!arestasAdicionadas.contains(chave)) {
                    arestas.add(aresta);
                    arestasAdicionadas.add(chave);
                }
            }
        }

        return arestas;
    }

    public void draw(EngineFrame e) {
        // Primeiro desenha as arestas
        for (Map.Entry<Vertice, List<Aresta>> entry : st.entrySet()) {
            for (Aresta a : entry.getValue()) {
                a.draw(e);
            }
        }

        // Depois desenha os vértices
        for (Map.Entry<Integer, Vertice> entry : vertices.entrySet()) {
            entry.getValue().draw(e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Vertice, List<Aresta>> entry : st.entrySet()) {
            sb.append(entry.getKey()).append(" -> ");
            boolean primeiro = true;
            for (Aresta a : entry.getValue()) {
                if (primeiro) {
                    primeiro = false;
                } else {
                    sb.append(", ");
                }
                sb.append(a.destino.id);
            }
            sb.append("\n");
        }

        return sb.toString().trim();

    }

}
