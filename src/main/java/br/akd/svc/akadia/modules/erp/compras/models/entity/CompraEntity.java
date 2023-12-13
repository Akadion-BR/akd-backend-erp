package br.akd.svc.akadia.modules.erp.compras.models.entity;

import br.akd.svc.akadia.modules.erp.compras.models.entity.id.CompraId;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompraId.class)
@Table(name = "TB_AKD_COMPRA")
public class CompraEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - UUID")
    @Column(name = "COD_PRODUTO_PDT", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - Código do cliente sistêmico")
    @Column(name = "COD_CLIENTESISTEMA_PDT", nullable = false, updatable = false)
    private UUID idClienteSistema;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do produto - Código da empresa")
    @Column(name = "COD_EMPRESA_PDT", nullable = false, updatable = false)
    private UUID idEmpresa;
}
