package br.com.caelum.agiletickets.models;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Entity
public class Espetaculo {

	@Id
	@GeneratedValue
	private Long id;

	private String nome;

	private String descricao;

	@Enumerated(EnumType.STRING)
	private TipoDeEspetaculo tipo;

	@OneToMany(mappedBy="espetaculo")
	private List<Sessao> sessoes = newArrayList();

	@ManyToOne
	private Estabelecimento estabelecimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoDeEspetaculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeEspetaculo tipo) {
		this.tipo = tipo;
	}

	public List<Sessao> getSessoes() {
		return sessoes;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	/**
      * Esse metodo eh responsavel por criar sessoes para
      * o respectivo espetaculo, dado o intervalo de inicio e fim,
      * mais a periodicidade.
      * 
      * O algoritmo funciona da seguinte forma:
      * - Caso a data de inicio seja 01/01/2010, a data de fim seja 03/01/2010,
      * e a periodicidade seja DIARIA, o algoritmo cria 3 sessoes, uma 
      * para cada dia: 01/01, 02/01 e 03/01.
      * 
      * - Caso a data de inicio seja 01/01/2010, a data fim seja 31/01/2010,
      * e a periodicidade seja SEMANAL, o algoritmo cria 5 sessoes, uma
      * a cada 7 dias: 01/01, 08/01, 15/01, 22/01 e 29/01.
      * 
      * Repare que a data da primeira sessao é sempre a data inicial.
      */
	public List<Sessao> criaSessoes(LocalDate inicio, LocalDate fim, LocalTime horario, Periodicidade periodicidade) {
		
		int intervalo = Days.daysBetween(inicio, fim).getDays();
		
		for (int i = 0; i <= intervalo; i++) {
			
			LocalDate ds = inicio.plusDays(i);
			
			DateTime dataEHorario = ds.toDateTime(horario);
			Sessao sessao = new Sessao();
			sessao.setInicio(dataEHorario);
			
			sessoes.add(sessao);
		}
		
		return sessoes;
		
	}
	
	public boolean temDisponibilidadePara(int quantidadeRequisitada, int minimoPorSessao)
    {
		int totalDisponivel = obtemTotalDeIngressosDisponiveis();

        return (totalDisponivel >= quantidadeRequisitada) && (temMinimoDisponivelPorSessaoPara(minimoPorSessao));
    }
	
	public boolean temMinimoDisponivelPorSessaoPara(int quantidadeMinima) {
    	
    	for (Sessao sessao : sessoes)
        {
    		 if (sessao.getIngressosDisponiveis() < quantidadeMinima) return false;
        }
    	
    	return true;
    	
    }

    public boolean temDisponibilidadePara(int quantidadeRequisitada)
    {
        int totalDisponivel = obtemTotalDeIngressosDisponiveis();

        return totalDisponivel >= quantidadeRequisitada;
        
    }
    
    public int obtemTotalDeIngressosDisponiveis() {
    	
    	int totalDisponivel = 0;
    	
    	for (Sessao sessao : sessoes)
        {
            totalDisponivel += sessao.getIngressosDisponiveis();
        }
    	
    	return totalDisponivel;
    	
    }

}
