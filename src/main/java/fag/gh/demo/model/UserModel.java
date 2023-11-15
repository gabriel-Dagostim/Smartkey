package fag.gh.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "TB_USER")
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID_USER")
    private String id;

    @NotEmpty(message = "O nome é obrigatório!")
    @Size(min = 2, message = "O nome deve, no mínimo, ter 3 caracteres")
    @Column(name = "NOME", length = 50, nullable = false)
    private String nome;

    @Email(message = "Insira um e-mail válido!")
    @NotEmpty(message = "O e-mail é obrigatório!")
    @Column(name = "EMAIL", length = 80, nullable = false)
    private String email;

    @Size(min = 5, message = "A senha deve, no mínimo, ter 5 caracteres")
    @NotEmpty(message = "A senha é obrigatória!")
    @Column(name = "SENHA", length = 100, columnDefinition = "TEXT", nullable = false)
    private String senha;

    @NotEmpty(message = "O numero de telefone é obrigatória!")
    @Column(name = "TELEFONE", length = 14, nullable = false)
    private String telefone;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_ALTERACAO", nullable = true)
    private LocalDateTime dataAlteracao;

    @Column(name = "USER_KEY", nullable = false)
    private String key;

}