package curso.api.rest.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "codigos")
public class Codigo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String linguagem;
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String codigo;
    
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    // Construtores
    public Codigo() {}

    public Codigo(String linguagem, String descricao, String codigo,String imgUrl) {
        this.linguagem = linguagem;
        this.descricao = descricao;
        this.codigo = codigo;
        this.imgUrl = imgUrl;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

   
   
    public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Codigo other = (Codigo) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Codigo [id=" + id + ", linguagem=" + linguagem + ", descricao=" + descricao + ", codigo=" + codigo
                + "]";
    }
}
